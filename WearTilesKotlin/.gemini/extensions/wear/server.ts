import { McpServer, McpTool } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { exec } from "child_process";
import { promises as fs } from "fs";
import { promisify } from "util";
import { z } from "zod";
import { homedir } from "os";

const server = new McpServer({
  name: "adb-mcp",
  version: "0.0.1",
});

const APP_PATH = `${homedir()}/workspace/wear-os-samples/WearTilesKotlin`;
const APK_PATH = `${APP_PATH}/app/build/outputs/apk/debug/app-debug.apk`;
const PACKAGE_NAME = "com.example.wear.tiles";

class CommandError extends Error {
  constructor(
    message: string,
    public command: string,
    public stdout: string,
    public stderr: string
  ) {
    super(message);
  }
}

// Helper function to execute ADB commands
async function executeCommand(
  command: string,
  options?: { validatePath?: string }
): Promise<string> {
  const execAsync = promisify(exec);
  let cmdResult;
  try {
    cmdResult = await execAsync(command);
  } catch (error: any) {
    throw new CommandError(
      `Command failed: ${command}`,
      command,
      error.stdout,
      error.stderr
    );
  }

  if (options?.validatePath) {
    try {
      await fs.access(options.validatePath);
    } catch {
      // The command succeeded, but validation failed.
      throw new CommandError(
        `Command succeeded, but output file was not found at ${options.validatePath}.`,
        command,
        cmdResult.stdout, // stdout from the successful command
        "" // stderr is empty on success
      );
    }
  }

  return cmdResult.stdout;
}

type ToolLogic = (
  input: any
) => Promise<{ isError?: boolean; content: McpTool.Output["content"] }>;

function createTool(
  name: string,
  spec: Omit<McpTool.Spec, "name">,
  logic: ToolLogic
): McpTool.Tool {
  return server.registerTool(name, spec, async (input) => {
    try {
      return await logic(input);
    } catch (error: any) {
      const content: McpTool.Output["content"] = [
        {
          type: "text",
          text: `Error: ${error.message}`,
        },
      ];
      if (error instanceof CommandError) {
        content.push(
          {
            type: "text",
            text: `Command: ${error.command}`,
          },
          {
            type: "text",
            text: `Output: ${error.stdout}
${error.stderr}`,
          }
        );
      }
      return {
        isError: true,
        content,
      };
    }
  });
}

createTool(
  "build-apk",
  {
    title: "Builds the debug APK.",
    description:
      "Builds the debug APK from source. Use this to check for compilation errors after a code change. Exact command: `./gradlew :app:assembleDebug`.",
  },
  async () => {
    await executeCommand(
      `cd ${APP_PATH} && ./gradlew :app:assembleDebug`,
      {
        validatePath: APK_PATH,
      }
    );
    return {
      content: [
        {
          type: "text",
          text: APK_PATH,
        },
      ],
    };
  }
);

createTool(
  "install-apk",
  {
    title: "Installs the debug APK.",
    description:
      "Installs the debug APK on the connected device. Exact command: `./gradlew :app:installDebug`.",
  },
  async ({ apk }) => {
    const stdout = await executeCommand(
      `cd ${APP_PATH} && ./gradlew :app:installDebug`
    );
    return {
      content: [
        {
          type: "text",
          text: stdout,
        },
      ],
    };
  }
);

createTool(
  "add-tile",
  {
    title: "Adds a tile to the carousel.",
    description:
      "Adds a tile to the carousel. If the tile already exists, it is removed and re-added. If the carousel is full, the last tile is removed to make space. Exact command: `adb shell am broadcast -a com.google.android.wearable.app.DEBUG_SURFACE --es operation add-tile --ecn component [COMPONENT_NAME]`.",
    inputSchema: { componentName: z.string() },
  },
  async ({ componentName }) => {
    const stdout = await executeCommand(
      `adb shell am broadcast -a com.google.android.wearable.app.DEBUG_SURFACE --es operation add-tile --ecn component ${componentName}`
    );
    return {
      content: [
        {
          type: "text",
          text: stdout,
        },
      ],
    };
  }
);

createTool(
  "show-tile",
  {
    title: "Shows a tile.",
    description:
      "Activates and displays the tile at a specific index in the carousel. Exact command: `adb shell am broadcast -a com.google.android.wearable.app.DEBUG_SYSUI --es operation show-tile --ei index [TILE_INDEX]`.",
    inputSchema: { tileIndex: z.number() },
  },
  async ({ tileIndex }) => {
    const stdout = await executeCommand(
      `adb shell am broadcast -a com.google.android.wearable.app.DEBUG_SYSUI --es operation show-tile --ei index ${tileIndex}`
    );
    return {
      content: [
        {
          type: "text",
          text: stdout,
        },
      ],
    };
  }
);

createTool(
  "remove-tile",
  {
    title: "Removes a tile.",
    description:
      "Removes all instances of a tile from the carousel. Exact command: `adb shell am broadcast -a com.google.android.wearable.app.DEBUG_SURFACE --es operation remove-tile --ecn component [COMPONENT_NAME]`.",
    inputSchema: { componentName: z.string() },
  },
  async ({ componentName }) => {
    const stdout = await executeCommand(
      `adb shell am broadcast -a com.google.android.wearable.app.DEBUG_SURFACE --es operation remove-tile --ecn component ${componentName}`
    );
    return {
      content: [
        {
          type: "text",
          text: stdout,
        },
      ],
    };
  }
);

createTool(
  "list-tiles",
  {
    title: "Lists all tiles for a package.",
    description:
      "Lists all available tiles for a given package. Exact command: `adb shell cmd package query-services -a androidx.wear.tiles.action.BIND_TILE_PROVIDER --brief | grep [PACKAGE_NAME]`.",
    inputSchema: { packageName: z.string() },
  },
  async ({ packageName }) => {
    const stdout = await executeCommand(
      `adb shell cmd package query-services -a androidx.wear.tiles.action.BIND_TILE_PROVIDER --brief | grep -E "\\s+${packageName}" | sed 's/^[[:space:]]*//' | sort`
    );
    return {
      content: [
        {
          type: "text",
          text: stdout,
        },
      ],
    };
  }
);

createTool(
  "get-package-name",
  {
    title: "Gets the app's package name.",
    description:
      "Returns the package name for the application being tested, for use with other commands.",
  },
  async () => {
    return {
      content: [
        {
          type: "text",
          text: PACKAGE_NAME,
        },
      ],
    };
  }
);

async function takeScreenshot(filename: string) {
  const wakeupCommand = "adb exec-out input keyevent KEYCODE_WAKEUP";
  const screencapCommand = `adb exec-out "screencap -p 2>/dev/null"`;

  const magickArgs = [
    "magick -",
    "-alpha set -background none -fill white",
    '\\( +clone -channel A -evaluate set 0 +channel -draw "circle %[fx:w/2],%[fx:h/2] %[fx:w/2],0" \\)',
    "-compose dstin -composite",
    `png:"${filename}"`,
  ];
  const magickCommand = magickArgs.join(" ");

  await executeCommand(
    `${wakeupCommand} && ${screencapCommand} | ${magickCommand}`,
    {
      validatePath: filename,
    }
  );
}

createTool(
  "screenshot-to-stdout",
  {
    title: "Takes a screenshot and returns it as PNG data.",
    description:
      "Takes a screenshot of the connected device and returns the image data as a base64-encoded PNG.",
  },
  async () => {
    const filename = `/tmp/screenshot-${Date.now()}.png`;
    await takeScreenshot(filename);
    const fileContent = await fs.readFile(filename);
    const base64Content = fileContent.toString("base64");
    return {
      content: [
        {
          type: "image",
          mimeType: "image/png",
          data: base64Content,
        },
      ],
    };
  }
);

createTool(
  "screenshot-to-file",
  {
    title: "Takes a screenshot and saves it to a file.",
    description:
      "Takes a screenshot of the connected device and saves it to a temporary file, returning the absolute path to the PNG file.",
  },
  async () => {
    const filename = `/tmp/screenshot-${Date.now()}.png`;
    await takeScreenshot(filename);
    return {
      content: [
        {
          type: "text",
          text: filename,
        },
      ],
    };
  }
);

const transport = new StdioServerTransport();

async function main() {
  await server.connect(transport);
}

main().catch(console.error);
