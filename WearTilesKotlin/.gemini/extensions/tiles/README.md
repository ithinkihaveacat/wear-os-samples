# Wear OS MCP Server

Install dependencies:

```sh
npm install
```

Check for updates (ignoring version specifiers):

```sh
npx npm-check-updates    # reports outdated versions
npx npm-check-updates -u # updates package.json
```

To run directly:

```sh
npx tsx server.ts
```

To run using the MCP inspector (no install needed):

```sh
npx @modelcontextprotocol/inspector tsx server.ts
```

To use with Gemini:

```xml
{
  "mcpServers": {
    "adb": {
      "command": "npx",
      "args": [
        "tsx",
        "/Users/mjs/workspace/mcp/adb.ts"
      ]
    }
  }
}
```
