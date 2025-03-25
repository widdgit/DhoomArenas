package me.notjoshx.dhoomarenas.managers;

import me.notjoshx.dhoomarenas.DhoomArenas;
import me.notjoshx.dhoomarenas.models.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ArenaManager {
    private final DhoomArenas plugin;
    private final Map<String, Arena> arenas;
    private final File arenasFolder;

    /**
     * Create a new arena manager
     * @param plugin Plugin instance
     */
    public ArenaManager(DhoomArenas plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
        this.arenasFolder = new File(plugin.getDataFolder(), "arenas");

        // Load all arenas
        loadArenas();
    }

    /**
     * Load all arenas from config files
     */
    private void loadArenas() {
        if (!arenasFolder.exists()) {
            arenasFolder.mkdir();
            return;
        }

        File[] files = arenasFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return;
        }

        for (File file : files) {
            String arenaName = file.getName().replace(".yml", "");
            Arena arena = new Arena(arenaName, file);
            arenas.put(arenaName, arena);
            plugin.getLogger().info("Loaded arena: " + arenaName);
        }
    }

    /**
     * Create a new arena
     * @param name Arena name
     * @return Success status
     */
    public boolean createArena(String name) {
        if (arenas.containsKey(name)) {
            return false;
        }

        File arenaFile = new File(arenasFolder, name + ".yml");
        if (!arenaFile.exists()) {
            try {
                arenaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        Arena arena = new Arena(name, arenaFile);
        arenas.put(name, arena);
        return true;
    }

    /**
     * Delete an arena
     * @param name Arena name
     * @return Success status
     */
    public boolean deleteArena(String name) {
        if (!arenas.containsKey(name)) {
            return false;
        }

        File arenaFile = new File(arenasFolder, name + ".yml");
        boolean deleted = arenaFile.delete();
        if (deleted) {
            arenas.remove(name);
        }
        return deleted;
    }

    /**
     * Get an arena by name
     * @param name Arena name
     * @return Arena instance, or null if not found
     */
    public Arena getArena(String name) {
        return arenas.get(name);
    }

    /**
     * Check if an arena exists
     * @param name Arena name
     * @return True if exists
     */
    public boolean arenaExists(String name) {
        return arenas.containsKey(name);
    }

    /**
     * Get all arenas
     * @return Map of arenas
     */
    public Map<String, Arena> getArenas() {
        return arenas;
    }

    /**
     * Set position 1 for an arena
     * @param name Arena name
     * @param location Location
     * @return Success status
     */
    public boolean setPosition1(String name, Location location) {
        Arena arena = getArena(name);
        if (arena == null) {
            return false;
        }

        arena.setPos1(location);
        return true;
    }

    /**
     * Set position 2 for an arena
     * @param name Arena name
     * @param location Location
     * @return Success status
     */
    public boolean setPosition2(String name, Location location) {
        Arena arena = getArena(name);
        if (arena == null) {
            return false;
        }

        arena.setPos2(location);
        return true;
    }

    /**
     * Set max players for an arena
     * @param name Arena name
     * @param maxPlayers Max players
     * @return Success status
     */
    public boolean setMaxPlayers(String name, int maxPlayers) {
        Arena arena = getArena(name);
        if (arena == null) {
            return false;
        }

        arena.setMaxPlayers(maxPlayers);
        return true;
    }

    /**
     * Set center for an arena
     * @param name Arena name
     * @param location Location
     * @return Success status
     */
    public boolean setCenter(String name, Location location) {
        Arena arena = getArena(name);
        if (arena == null) {
            return false;
        }

        arena.setCenter(location);
        return true;
    }

    /**
     * Set wait time for an arena
     * @param name Arena name
     * @param seconds Wait time in seconds
     * @return Success status
     */
    public boolean setWaitTime(String name, int seconds) {
        Arena arena = getArena(name);
        if (arena == null) {
            return false;
        }

        arena.setWaitTimeSeconds(seconds);
        return true;
    }

    /**
     * Set spawn point for an arena
     * @param name Arena name
     * @param number Spawn point number
     * @param location Location
     * @return Success status
     */
    public boolean setSpawnPoint(String name, int number, Location location) {
        Arena arena = getArena(name);
        if (arena == null) {
            return false;
        }

        arena.setSpawnPoint(number, location);
        return true;
    }

    /**
     * Teleport a player to an arena location
     * @param player Player to teleport
     * @param name Arena name
     * @param spawnPointNumber Spawn point number, -1 for center
     * @return Success status
     */
    public boolean teleportToArena(Player player, String name, int spawnPointNumber) {
        Arena arena = getArena(name);
        if (arena == null) {
            return false;
        }

        if (spawnPointNumber == -1) {
            // Teleport to center
            if (arena.getCenter() == null) {
                return false;
            }
            player.teleport(arena.getCenter());
            return true;
        } else {
            // Teleport to spawn point
            Map<Integer, Location> spawnPoints = arena.getSpawnPoints();
            if (!spawnPoints.containsKey(spawnPointNumber)) {
                return false;
            }

            player.teleport(spawnPoints.get(spawnPointNumber));
            return true;
        }
    }
}
