package me.notjoshx.dhoomarenas;

import me.notjoshx.dhoomarenas.api.DhoomArenasAPI;
import me.notjoshx.dhoomarenas.commands.ArenaCommand;
import me.notjoshx.dhoomarenas.managers.ArenaManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class DhoomArenas extends JavaPlugin {

    private static DhoomArenas instance;
    private ArenaManager arenaManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        // Create data folder
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        // Create arenas folder
        File arenasFolder = new File(getDataFolder(), "arenas");
        if (!arenasFolder.exists()) {
            arenasFolder.mkdir();
        }

        // Initialize managers
        arenaManager = new ArenaManager(this);

        // Register commands
        getCommand("dhoomarenas").setExecutor(new ArenaCommand(this));

        // Initialize API (this will be lazy-loaded when requested)
        DhoomArenasAPI.getInstance();

        getLogger().info("DhoomArenas has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("DhoomArenas has been disabled!");
    }

    /**
     * Get the instance of the plugin
     * @return Plugin instance
     */
    public static DhoomArenas getInstance() {
        return instance;
    }

    /**
     * Get the arena manager
     * @return Arena manager
     */
    public ArenaManager getArenaManager() {
        return arenaManager;
    }
}
