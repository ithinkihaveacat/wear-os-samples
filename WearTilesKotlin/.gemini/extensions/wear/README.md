# Gemini Extension Server for Wear OS Tiles Development

This server provides a set of tools via the Model-Context Protocol (MCP) to
assist with Wear OS Tiles development. It allows an AI agent, like Gemini in
Android Studio, to interact with your development environment to perform tasks
such as building, installing, and managing Wear OS Tiles.

## Prerequisites

- [Node.js](https://nodejs.org/) (version 18 or higher)

## Installation

1. Clone the repository or obtain the project files.
2. Install the necessary dependencies from the directory containing
   `package.json`:

   ```sh
   npm install
   ```

## Usage

This server is designed to be run by an MCP client, such as Gemini in Android
Studio.

### With Gemini

Configure your agent's `mcpServer` settings to launch this server. The command
should execute the `server.ts` script using `tsx`.

**Example Configuration:**

```json
{
  "mcpServers": {
    "adb": {
      "command": "npx",
      "args": [
        "-y",
        "tsx",
        "server.ts"
      ]
    }
  }
}
```

### With Android Studio

For integration with Android Studio, please refer to the official documentation,
[Add an MCP
server](https://developer.android.com/studio/preview/gemini/agent-mode#add-mcp).

## Development

### Inspecting the Server

You can inspect the tools and capabilities provided by the server using the MCP
Inspector. This is useful for debugging or understanding the server's
functionality without connecting a full agent.

```sh
npx @modelcontextprotocol/inspector tsx server.ts
```

### Updating Dependencies

To keep the project's dependencies up to date:

1. **Check for outdated packages:**

   ```sh
   npx npm-check-updates
   ```

2. **Update `package.json` with the latest versions:**

   ```sh
   npx npm-check-updates -u
   ```

   After updating, run `npm install` to install the new package versions.
