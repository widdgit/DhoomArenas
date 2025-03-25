package me.notjoshx.dhoomarenas.commands;

import me.notjoshx.dhoomarenas.DhoomArenas;
import me.notjoshx.dhoomarenas.managers.ArenaManager;
import me.notjoshx.dhoomarenas.models.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArenaCommand implements CommandExecutor, TabCompleter {
    private final DhoomArenas plugin;
    private final ArenaManager arenaManager;

    /**
     * Create a new arena command
     * @param plugin Plugin instance
     */
    public ArenaCommand(DhoomArenas plugin) {
        this.plugin = plugin;
        this.arenaManager = plugin.getArenaManager();
        plugin.getCommand("dhoomarenas").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("dhoomarenas.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "create":
                handleCreateCommand(player, args);
                break;
            case "delete":
                handleDeleteCommand(player, args);
                break;
            case "setup":
                handleSetupCommand(player, args);
                break;
            case "tp":
                handleTeleportCommand(player, args);
                break;
            case "list":
                handleListCommand(player);
                break;
            case "info":
                handleInfoCommand(player, args);
                break;
            default:
                sendHelpMessage(player);
                break;
        }

        return true;
    }

    /**
     * Send help message to a player
     * @param player Player to send message to
     */
    private void sendHelpMessage(Player player) {
        player.sendMessage(ChatColor.GREEN + "===== DhoomArenas Commands =====");
        player.sendMessage(ChatColor.YELLOW + "/dhoomarenas create <name> " + ChatColor.WHITE + "- Create an arena");
        player.sendMessage(ChatColor.YELLOW + "/dhoomarenas delete <arenaName> " + ChatColor.WHITE + "- Delete an arena");
        player.sendMessage(ChatColor.YELLOW + "/dhoomarenas setup <arenaName> pos 1|2 " + ChatColor.WHITE + "- Set arena position");
        player.sendMessage(ChatColor.YELLOW + "/dhoomarenas setup <arenaName> maxPlayers <number> " + ChatColor.WHITE + "- Set max players");
        player.sendMessage(ChatColor.YELLOW + "/dhoomarenas setup <arenaName> spawnpoint <number> " + ChatColor.WHITE + "- Set spawn point");
        player.sendMessage(ChatColor.YELLOW + "/dhoomarenas setup <arenaName> center " + ChatColor.WHITE + "- Set arena center");
        player.sendMessage(ChatColor.YELLOW + "/dhoomarenas setup <arenaName> waitTime <seconds>s " + ChatColor.WHITE + "- Set wait time");
        player.sendMessage(ChatColor.YELLOW + "/dhoomarenas tp <arenaName> [spawnPoint] " + ChatColor.WHITE + "- Teleport to arena");
        player.sendMessage(ChatColor.YELLOW + "/dhoomarenas list " + ChatColor.WHITE + "- List all arenas");
        player.sendMessage(ChatColor.YELLOW + "/dhoomarenas info <arenaName> " + ChatColor.WHITE + "- Show arena info");
    }

    /**
     * Handle create command
     * @param player Player who executed the command
     * @param args Command arguments
     */
    private void handleCreateCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /dhoomarenas create <name>");
            return;
        }

        String arenaName = args[1];
        boolean created = arenaManager.createArena(arenaName);

        if (created) {
            player.sendMessage(ChatColor.GREEN + "Arena " + arenaName + " created successfully!");
        } else {
            player.sendMessage(ChatColor.RED + "Arena " + arenaName + " already exists!");
        }
    }

    /**
     * Handle delete command
     * @param player Player who executed the command
     * @param args Command arguments
     */
    private void handleDeleteCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /dhoomarenas delete <arenaName>");
            return;
        }

        String arenaName = args[1];
        boolean deleted = arenaManager.deleteArena(arenaName);

        if (deleted) {
            player.sendMessage(ChatColor.GREEN + "Arena " + arenaName + " deleted successfully!");
        } else {
            player.sendMessage(ChatColor.RED + "Arena " + arenaName + " does not exist!");
        }
    }

    /**
     * Handle setup command
     * @param player Player who executed the command
     * @param args Command arguments
     */
    private void handleSetupCommand(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /dhoomarenas setup <arenaName> <option> [value]");
            return;
        }

        String arenaName = args[1];
        String option = args[2].toLowerCase();

        if (!arenaManager.arenaExists(arenaName)) {
            player.sendMessage(ChatColor.RED + "Arena " + arenaName + " does not exist!");
            return;
        }

        switch (option) {
            case "pos":
                if (args.length < 4) {
                    player.sendMessage(ChatColor.RED + "Usage: /dhoomarenas setup <arenaName> pos 1|2");
                    return;
                }

                if (args[3].equals("1")) {
                    arenaManager.setPosition1(arenaName, player.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Position 1 for arena " + arenaName + " set to your location!");
                } else if (args[3].equals("2")) {
                    arenaManager.setPosition2(arenaName, player.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Position 2 for arena " + arenaName + " set to your location!");
                } else {
                    player.sendMessage(ChatColor.RED + "Position must be 1 or 2!");
                }
                break;

            case "maxplayers":
                if (args.length < 4) {
                    player.sendMessage(ChatColor.RED + "Usage: /dhoomarenas setup <arenaName> maxPlayers <number>");
                    return;
                }

                try {
                    int maxPlayers = Integer.parseInt(args[3]);
                    if (maxPlayers < 1 || maxPlayers > 10) {
                        player.sendMessage(ChatColor.RED + "Max players must be between 1 and 10!");
                        return;
                    }

                    arenaManager.setMaxPlayers(arenaName, maxPlayers);
                    player.sendMessage(ChatColor.GREEN + "Max players for arena " + arenaName + " set to " + maxPlayers + "!");
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Max players must be a number!");
                }
                break;

            case "spawnpoint":
                if (args.length < 4) {
                    player.sendMessage(ChatColor.RED + "Usage: /dhoomarenas setup <arenaName> spawnpoint <number>");
                    return;
                }

                try {
                    int spawnPoint = Integer.parseInt(args[3]);
                    if (spawnPoint < 1 || spawnPoint > 10) {
                        player.sendMessage(ChatColor.RED + "Spawn point must be between 1 and 10!");
                        return;
                    }

                    arenaManager.setSpawnPoint(arenaName, spawnPoint, player.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Spawn point " + spawnPoint + " for arena " + arenaName + " set to your location!");
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Spawn point must be a number!");
                }
                break;

            case "center":
                arenaManager.setCenter(arenaName, player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Center for arena " + arenaName + " set to your location!");
                break;

            case "waittime":
                if (args.length < 4) {
                    player.sendMessage(ChatColor.RED + "Usage: /dhoomarenas setup <arenaName> waitTime <seconds>s");
                    return;
                }

                String timeValue = args[3].toLowerCase();
                if (!timeValue.endsWith("s")) {
                    player.sendMessage(ChatColor.RED + "Wait time must be in seconds (e.g. 10s)!");
                    return;
                }

                try {
                    int seconds = Integer.parseInt(timeValue.substring(0, timeValue.length() - 1));
                    if (seconds < 1 || seconds > 30) {
                        player.sendMessage(ChatColor.RED + "Wait time must be between 1 and 30 seconds!");
                        return;
                    }

                    arenaManager.setWaitTime(arenaName, seconds);
                    player.sendMessage(ChatColor.GREEN + "Wait time for arena " + arenaName + " set to " + seconds + " seconds!");
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Wait time must be a number followed by 's'!");
                }
                break;

            default:
                player.sendMessage(ChatColor.RED + "Unknown option: " + option);
                player.sendMessage(ChatColor.RED + "Valid options: pos, maxPlayers, spawnpoint, center, waitTime");
                break;
        }
    }

    /**
     * Handle teleport command
     * @param player Player who executed the command
     * @param args Command arguments
     */
    private void handleTeleportCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /dhoomarenas tp <arenaName> [spawnPoint]");
            return;
        }

        String arenaName = args[1];

        if (!arenaManager.arenaExists(arenaName)) {
            player.sendMessage(ChatColor.RED + "Arena " + arenaName + " does not exist!");
            return;
        }

        int spawnPoint = -1; // Default to center
        if (args.length >= 3) {
            try {
                spawnPoint = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Spawn point must be a number!");
                return;
            }
        }

        boolean teleported = arenaManager.teleportToArena(player, arenaName, spawnPoint);

        if (teleported) {
            if (spawnPoint == -1) {
                player.sendMessage(ChatColor.GREEN + "Teleported to center of arena " + arenaName + "!");
            } else {
                player.sendMessage(ChatColor.GREEN + "Teleported to spawn point " + spawnPoint + " of arena " + arenaName + "!");
            }
        } else {
            if (spawnPoint == -1) {
                player.sendMessage(ChatColor.RED + "Center of arena " + arenaName + " is not set!");
            } else {
                player.sendMessage(ChatColor.RED + "Spawn point " + spawnPoint + " of arena " + arenaName + " is not set!");
            }
        }
    }

    /**
     * Handle list command
     * @param player Player who executed the command
     */
    private void handleListCommand(Player player) {
        if (arenaManager.getArenas().isEmpty()) {
            player.sendMessage(ChatColor.RED + "There are no arenas!");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "===== Arenas =====");
        for (String arenaName : arenaManager.getArenas().keySet()) {
            player.sendMessage(ChatColor.YELLOW + "- " + arenaName);
        }
    }

    /**
     * Handle info command
     * @param player Player who executed the command
     * @param args Command arguments
     */
    private void handleInfoCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /dhoomarenas info <arenaName>");
            return;
        }

        String arenaName = args[1];

        if (!arenaManager.arenaExists(arenaName)) {
            player.sendMessage(ChatColor.RED + "Arena " + arenaName + " does not exist!");
            return;
        }

        Arena arena = arenaManager.getArena(arenaName);

        player.sendMessage(ChatColor.GREEN + "===== Arena: " + arenaName + " =====");
        player.sendMessage(ChatColor.YELLOW + "Max Players: " + ChatColor.WHITE + arena.getMaxPlayers());
        player.sendMessage(ChatColor.YELLOW + "Wait Time: " + ChatColor.WHITE + arena.getWaitTimeSeconds() + " seconds");
        player.sendMessage(ChatColor.YELLOW + "Position 1: " + ChatColor.WHITE + (arena.getPos1() != null ? formatLocation(arena.getPos1()) : "Not set"));
        player.sendMessage(ChatColor.YELLOW + "Position 2: " + ChatColor.WHITE + (arena.getPos2() != null ? formatLocation(arena.getPos2()) : "Not set"));
        player.sendMessage(ChatColor.YELLOW + "Center: " + ChatColor.WHITE + (arena.getCenter() != null ? formatLocation(arena.getCenter()) : "Not set"));

        player.sendMessage(ChatColor.YELLOW + "Spawn Points:");
        if (arena.getSpawnPoints().isEmpty()) {
            player.sendMessage(ChatColor.RED + "  No spawn points set!");
        } else {
            for (Integer pointNumber : arena.getSpawnPoints().keySet()) {
                player.sendMessage(ChatColor.YELLOW + "  " + pointNumber + ": " + ChatColor.WHITE + formatLocation(arena.getSpawnPoints().get(pointNumber)));
            }
        }
    }

    /**
     * Format location to string
     * @param location Location to format
     * @return Formatted string
     */
    private String formatLocation(Location location) {
        return location.getWorld().getName() + ", " +
                String.format("%.2f", location.getX()) + ", " +
                String.format("%.2f", location.getY()) + ", " +
                String.format("%.2f", location.getZ()) + " (" +
                String.format("%.2f", location.getYaw()) + ", " +
                String.format("%.2f", location.getPitch()) + ")";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String[] subCommands = {"create", "delete", "setup", "tp", "list", "info"};
            return filterCompletions(args[0], subCommands);
        } else if (args.length >= 2) {
            String subCommand = args[0].toLowerCase();

            switch (subCommand) {
                case "delete":
                case "tp":
                case "info":
                    if (args.length == 2) {
                        return filterCompletions(args[1], arenaManager.getArenas().keySet().toArray(new String[0]));
                    }
                    break;

                case "setup":
                    if (args.length == 2) {
                        return filterCompletions(args[1], arenaManager.getArenas().keySet().toArray(new String[0]));
                    } else if (args.length == 3) {
                        String[] setupOptions = {"pos", "maxPlayers", "spawnpoint", "center", "waitTime"};
                        return filterCompletions(args[2], setupOptions);
                    } else if (args.length == 4) {
                        String setupOption = args[2].toLowerCase();
                        if (setupOption.equals("pos")) {
                            return filterCompletions(args[3], new String[]{"1", "2"});
                        } else if (setupOption.equals("maxplayers")) {
                            return filterCompletions(args[3], IntStream.rangeClosed(1, 10).mapToObj(String::valueOf).toArray(String[]::new));
                        } else if (setupOption.equals("spawnpoint")) {
                            return filterCompletions(args[3], IntStream.rangeClosed(1, 10).mapToObj(String::valueOf).toArray(String[]::new));
                        } else if (setupOption.equals("waittime")) {
                            return filterCompletions(args[3], Arrays.stream(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}).mapToObj(i -> i + "s").toArray(String[]::new));
                        }
                    }
                    break;
            }
        }

        return completions;
    }

    /**
     * Filter completions based on partial input
     * @param partial Partial input
     * @param options Completion options
     * @return Filtered completions
     */
    private List<String> filterCompletions(String partial, String[] options) {
        return Arrays.stream(options)
                .filter(option -> option.toLowerCase().startsWith(partial.toLowerCase()))
                .collect(Collectors.toList());
    }
}
