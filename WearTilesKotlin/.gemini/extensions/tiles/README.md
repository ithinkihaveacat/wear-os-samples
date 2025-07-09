# Wear OS MCP Server

## Getting Started

For all options, first install node (version 18+).

Then install install dependencies:

```sh
# from the directory containing package.json
npm install
```

Final step depends on the agent using the MCP.

For Gemini and other agents that are configured via a `mcpServer` value in a
JSON configuration file:

```xml
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

For Android Studio see [Add an MCP
server](https://developer.android.com/studio/preview/gemini/agent-mode#add-mcp).

## Development

Check for dependency updates (i.e. ignoring version specifiers):

```sh
npx npm-check-updates    # reports outdated versions
npx npm-check-updates -u # updates package.json
```

Inspect the server using the [MCP
inspector](https://modelcontextprotocol.io/docs/tools/inspector) (no need to
install the inspector):

```sh
npx @modelcontextprotocol/inspector tsx server.ts
```
