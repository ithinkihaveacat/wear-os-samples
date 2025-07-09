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
    title: "Build Debug APK",
    description:
      "Builds the debug APK variant from the Android source code in the current directory and returns the path to the APK file. Use this if you don't need or want the APK to be installed (for example, if you want to check that the app can still be compiled after making a change to the source code). Exact command is ./gradlew :app:assembleDebug",
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
    title: "Install Debug APK",
    description:
      "Installs an APK file on the attached device. The exact command used is: ./gradlew :app:installDebug.",
    },
  async ({ apk }) => {
    const stdout = await executeCommand(`cd ${APP_PATH} && ./gradlew :app:installDebug`);
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
    title: "Add Tile",
    description:
      "Adds a Wear OS tile to the tiles carousel of the attached device. The app must already be installed on the device. After adding the tile, it will not be visible, it has only been added to the carousel. If a tile already exists in the carousel, it's removed and reinserted in the same location. Otherwise, it's inserted at Index[0] (i.e. TileIndex 0). Also, if the carousel is at its maximum capacity, the last tile is removed to make room for the new tile. The exact command used is: adb shell am broadcast -a com.google.android.wearable.app.DEBUG_SURFACE --es operation add-tile --ecn component <ComponentName>. ComponentName is a string that incorporates a package name, and the class name of the tile service in the standard Android format of <package_name>/<class_name>. For example com.example.wear.tiles/com.example.wear.tiles.PreviewTileService. Example output: Broadcast completed: result=1, data=\"Index=[0]\"",
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
    title: "Show Tile",
    description:
      "Activate (that is raise and display to the user) the tile at index TILE_INDEX. The tile must already be added to the carousel. The exact command used is: adb shell am broadcast -a com.google.android.wearable.app.DEBUG_SURFACE --es operation show-tile --ei tile-index <TileIndex>. TileIndex is the index of the tile in the carousel. Example output: Broadcast completed: result=1",
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
    title: "Remove Tile",
    description:
      'Removes all tile instances on the carousel associated with COMPONENT_NAME. The exact command used is: adb shell am broadcast -a com.google.android.wearable.app.DEBUG_SURFACE --es operation remove-tile --ecn component <ComponentName>. ComponentName is a string that incorporates a package name, and the class name of the tile service in the standard Android format of <package_name>/<class_name>. For example com.example.wear.tiles/com.example.wear.tiles.PreviewTileService. Example output: result=1, data="Tile(s) removed."',
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
    title: "List Tiles",
    description:
      "Lists all tiles on the device provided by a given package name. (The package needs to be installed on the device for this command to work.) It returns the full component name, which is useful input for other tile commands, such as show tile and remove tile. The exact command used is: adb shell cmd package query-services -a androidx.wear.tiles.action.BIND_TILE_PROVIDER --brief | grep -E '\\s+<package_name>' | sed 's/^[[:space:]]*//'",
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
    title: "Get Package Name",
    description:
      "Returns the package name of the app we're interacting with. If you need a package name as input for some other command, use this to get the package name.",
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
    title: "Screenshot to PNG",
    description:
      "Takes a screenshot of the attached device, returning the screenshot as image/png encoded as base64.",
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
    title: "Screenshot to file",
    description:
      "Takes a screenshot of the attached device, returning the filename of the screenshot. The file type is image/png. Use the `cp` shell command to create the file in another location.",
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
