package me.notjoshx.dhoomarenas.models;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Arena {
    private final String name;
    private final File configFile;
    private FileConfiguration config;
    private Location pos1;
    private Location pos2;
    private int maxPlayers;
    private Location center;
    private final Map<Integer, Location> spawnPoints;
    private int waitTimeSeconds;

    /**
     * Create a new arena
     * @param name Arena name
     * @param configFile Configuration file
     */
    public Arena(String name, File configFile) {
        this.name = name;
        this.configFile = configFile;
        this.spawnPoints = new HashMap<>();
        loadConfig();
    }

    /**
     * Load arena configuration
     */
    private void loadConfig() {
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.maxPlayers = config.getInt("maxPlayers", 0);
        this.waitTimeSeconds = config.getInt("waitTimeSeconds", 10);

        // Load locations
        if (config.contains("pos1")) {
            this.pos1 = (Location) config.get("pos1");
        }
        if (config.contains("pos2")) {
            this.pos2 = (Location) config.get("pos2");
        }
        if (config.contains("center")) {
            this.center = (Location) config.get("center");
        }

        // Load spawn points
        if (config.contains("spawnPoints")) {
            for (String key : config.getConfigurationSection("spawnPoints").getKeys(false)) {
                int pointNumber = Integer.parseInt(key);
                spawnPoints.put(pointNumber, (Location) config.get("spawnPoints." + key));
            }
        }
    }

    /**
     * Save arena configuration
     */
    public void saveConfig() {
        config.set("name", name);
        config.set("maxPlayers", maxPlayers);
        config.set("waitTimeSeconds", waitTimeSeconds);

        // Save locations
        if (pos1 != null) {
            config.set("pos1", pos1);
        }
        if (pos2 != null) {
            config.set("pos2", pos2);
        }
        if (center != null) {
            config.set("center", center);
        }

        // Save spawn points
        for (Map.Entry<Integer, Location> entry : spawnPoints.entrySet()) {
            config.set("spawnPoints." + entry.getKey(), entry.getValue());
        }

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters and setters

    /**
     * Get arena name
     * @return Arena name
     */
    public String getName() {
        return name;
    }

    /**
     * Get position 1
     * @return Position 1
     */
    public Location getPos1() {
        return pos1;
    }

    /**
     * Set position 1
     * @param pos1 Position 1
     */
    public void setPos1(Location pos1) {
        this.pos1 = pos1;
        saveConfig();
    }

    /**
     * Get position 2
     * @return Position 2
     */
    public Location getPos2() {
        return pos2;
    }

    /**
     * Set position 2
     * @param pos2 Position 2
     */
    public void setPos2(Location pos2) {
        this.pos2 = pos2;
        saveConfig();
    }

    /**
     * Get max players
     * @return Max players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Set max players
     * @param maxPlayers Max players
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        saveConfig();
    }

    /**
     * Get center location
     * @return Center location
     */
    public Location getCenter() {
        return center;
    }

    /**
     * Set center location
     * @param center Center location
     */
    public void setCenter(Location center) {
        this.center = center;
        saveConfig();
    }

    /**
     * Get spawn points
     * @return Spawn points
     */
    public Map<Integer, Location> getSpawnPoints() {
        return spawnPoints;
    }

    /**
     * Add a spawn point
     * @param number Spawn point number
     * @param location Spawn point location
     */
    public void setSpawnPoint(int number, Location location) {
        spawnPoints.put(number, location);
        saveConfig();
    }

    /**
     * Get wait time in seconds
     * @return Wait time in seconds
     */
    public int getWaitTimeSeconds() {
        return waitTimeSeconds;
    }

    /**
     * Set wait time in seconds
     * @param waitTimeSeconds Wait time in seconds
     */
    public void setWaitTimeSeconds(int waitTimeSeconds) {
        this.waitTimeSeconds = waitTimeSeconds;
        saveConfig();
    }
}
