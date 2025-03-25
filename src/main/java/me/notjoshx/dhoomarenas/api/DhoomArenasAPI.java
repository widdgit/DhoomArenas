package me.notjoshx.dhoomarenas.api;

import me.notjoshx.dhoomarenas.DhoomArenas;
import me.notjoshx.dhoomarenas.models.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * API for DhoomArenas
 */
public class DhoomArenasAPI {
    private static DhoomArenasAPI instance;
    private final DhoomArenas plugin;

    /**
     * Create a new API instance
     * @param plugin Plugin instance
     */
    private DhoomArenasAPI(DhoomArenas plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the API instance
     * @return API instance
     */
    public static DhoomArenasAPI getInstance() {
        if (instance == null) {
            instance = new DhoomArenasAPI(DhoomArenas.getInstance());
        }
        return instance;
    }

    /**
     * Get all arena names
     * @return Set of arena names
     */
    public Set<String> getArenaNames() {
        return Collections.unmodifiableSet(plugin.getArenaManager().getArenas().keySet());
    }

    /**
     * Check if an arena exists
     * @param arenaName Arena name
     * @return True if exists
     */
    public boolean arenaExists(String arenaName) {
        return plugin.getArenaManager().arenaExists(arenaName);
    }

    /**
     * Get the center location of an arena
     * @param arenaName Arena name
     * @return Center location, or null if not found or not set
     */
    public Location getArenaCenter(String arenaName) {
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (arena == null) {
            return null;
        }
        return arena.getCenter();
    }

    /**
     * Get the spawn points of an arena
     * @param arenaName Arena name
     * @return Map of spawn point numbers to locations, or empty map if not found
     */
    public Map<Integer, Location> getArenaSpawnPoints(String arenaName) {
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (arena == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(arena.getSpawnPoints());
    }

    /**
     * Get a specific spawn point of an arena
     * @param arenaName Arena name
     * @param spawnPoint Spawn point number
     * @return Spawn point location, or null if not found or not set
     */
    public Location getArenaSpawnPoint(String arenaName, int spawnPoint) {
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (arena == null) {
            return null;
        }
        return arena.getSpawnPoints().get(spawnPoint);
    }

    /**
     * Get the maximum number of players for an arena
     * @param arenaName Arena name
     * @return Maximum number of players, or 0 if not found or not set
     */
    public int getArenaMaxPlayers(String arenaName) {
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (arena == null) {
            return 0;
        }
        return arena.getMaxPlayers();
    }

    /**
     * Get the wait time for an arena in seconds
     * @param arenaName Arena name
     * @return Wait time in seconds, or 0 if not found or not set
     */
    public int getArenaWaitTime(String arenaName) {
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (arena == null) {
            return 0;
        }
        return arena.getWaitTimeSeconds();
    }

    /**
     * Teleport a player to an arena's center
     * @param player Player to teleport
     * @param arenaName Arena name
     * @return True if teleported successfully
     */
    public boolean teleportPlayerToArenaCenter(Player player, String arenaName) {
        return plugin.getArenaManager().teleportToArena(player, arenaName, -1);
    }

    /**
     * Teleport a player to an arena's spawn point
     * @param player Player to teleport
     * @param arenaName Arena name
     * @param spawnPoint Spawn point number
     * @return True if teleported successfully
     */
    public boolean teleportPlayerToArenaSpawnPoint(Player player, String arenaName, int spawnPoint) {
        return plugin.getArenaManager().teleportToArena(player, arenaName, spawnPoint);
    }
}
