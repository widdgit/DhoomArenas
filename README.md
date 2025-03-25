# DhoomArenas

A Minecraft plugin for managing arenas with teleportation and setup capabilities.

## 📋 Features

- Create and manage multiple arenas
- Setup arena positions, spawn points, and center
- Set maximum players and wait times for arenas
- Teleport to arenas and specific spawn points
- API for other plugins to integrate with

## 🔧 Commands

| Command | Description |
|---------|-------------|
| `/dhoomarenas create {name}` | Creates a new arena |
| `/dhoomarenas delete {arenaName}` | Deletes an existing arena |
| `/dhoomarenas setup {arenaName} pos 1\|2` | Sets arena position boundaries |
| `/dhoomarenas setup {arenaName} maxPlayers 1-10` | Sets maximum players for arena |
| `/dhoomarenas setup {arenaName} spawnpoint 1-10` | Sets a specific spawn point at your location |
| `/dhoomarenas setup {arenaName} center` | Sets the center of the arena at your location |
| `/dhoomarenas setup {arenaName} waitTime 1s-10s` | Sets wait time for the arena |
| `/dhoomarenas tp {arenaName} [spawnPoint]` | Teleports to arena center or specific spawn point |
| `/dhoomarenas list` | Lists all arenas |
| `/dhoomarenas info {arenaName}` | Shows detailed information about an arena |

## 🏗️ Installation

1. Download the plugin JAR file
2. Place it in your server's `plugins` folder
3. Restart your server
4. Use the commands to start creating and managing arenas

## 🔌 API Usage

For developers who want to use DhoomArenas in their plugins:

```java
// Get the API instance
DhoomArenasAPI api = DhoomArenasAPI.getInstance();

// Check if an arena exists
boolean exists = api.arenaExists("myArena");

// Get all arena names
Set<String> arenaNames = api.getArenaNames();

// Get arena center location
Location center = api.getArenaCenter("myArena");

// Get arena spawn points
Map<Integer, Location> spawnPoints = api.getArenaSpawnPoints("myArena");
Location spawnPoint1 = api.getArenaSpawnPoint("myArena", 1);

// Get arena properties
int maxPlayers = api.getArenaMaxPlayers("myArena");
int waitTime = api.getArenaWaitTime("myArena");

// Teleport a player
boolean teleported = api.teleportPlayerToArenaCenter(player, "myArena");
boolean teleported = api.teleportPlayerToArenaSpawnPoint(player, "myArena", 1);
```

## 📁 Configuration

Each arena has its own configuration file in the `plugins/DhoomArenas/arenas/` directory.

## 🔒 Permissions

- `dhoomarenas.admin` - Access to all commands

## 👨‍💻 Author

- SoloWiper

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
