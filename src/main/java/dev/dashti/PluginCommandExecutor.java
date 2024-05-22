package dev.dashti;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public class PluginCommandExecutor implements CommandExecutor {

    private final Plugin plugin;
    private final PluginInfoModel pluginInfoModel;

    public PluginCommandExecutor(Plugin pl){
        plugin = pl;
        pluginInfoModel = new PluginInfoModel(plugin);
        pluginInfoModel.PluginObject = plugin; // Reference the plugin object
    }

    // onCommand listener
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        // If the sender of the command it NOT a player
        if(!(commandSender instanceof Player)) { return false; }

        // Reference the player that invoked the command
        Player player = (Player) commandSender;

        // Command Listener
        if(command.getName().equalsIgnoreCase(pluginInfoModel.PluginCommand))
        {
            // [No permissions]
            if(!player.hasPermission(pluginInfoModel.PermissionAdmin))
            {
                player.sendMessage(MessageFormat.format("[{0}] You do not have permissions to use this plugin",
                        pluginInfoModel.PluginName
                ));
                return true;
            }

            // Command: [Command]
            if(args.length == 0){
                player.sendMessage(MessageFormat.format("{0} {1} by {2}",
                        pluginInfoModel.PluginName,
                        pluginInfoModel.PluginVersion,
                        pluginInfoModel.AuthorName
                ));
                player.sendMessage(MessageFormat.format("/{0} help",
                        pluginInfoModel.PluginCommand
                ));
                return true;
            }

            // Command: [Command] Reload
            if (args[0].equalsIgnoreCase("reload"))
            {
                plugin.reloadConfig();

                player.sendMessage(MessageFormat.format("[{0}] Config was successfully reloaded.",
                        pluginInfoModel.PluginName
                ));

                return true;
            }

            // Command: [Command] settime [integer]
            else if (args[0].equalsIgnoreCase("settime"))
            {
                // If invalid arguments
                if(args.length != 2)
                {
                    player.sendMessage(MessageFormat.format("[{0}] /ArrowTNT settime [integer]",
                            pluginInfoModel.PluginName
                    ));
                }else{
                    int newTime;

                    //If input is double
                    try{
                        newTime = Integer.parseInt(args[1]);

                        // Add new value to config file
                        PluginHelper.getInstance(plugin).SetValueToConfig("config.yml", "SpamSeconds", newTime);

                        player.sendMessage(MessageFormat.format("[{0}] Set SpamTime to {1}",
                                pluginInfoModel.PluginName,
                                newTime
                        ));
                    }catch(NumberFormatException e){
                        player.sendMessage(MessageFormat.format("[{0}] Something went wrong: {1}",
                                pluginInfoModel.PluginName,
                                e.getMessage()
                        ));
                        return true;
                    }
                }

                return true;
            }

            // Command: [Command] setEnable [boolean]
            else if (args[0].equalsIgnoreCase("setEnable"))
            {
                boolean isEnabled;

                // if there was no argument
                if (args.length != 2)
                {
                    player.sendMessage(MessageFormat.format("[{0}] /{1} setenable [boolean]",
                            pluginInfoModel.PluginName,
                            pluginInfoModel.PluginCommand
                    ));
                    return true;
                }
                else
                {
                    isEnabled = Boolean.parseBoolean(args[1]);
                    player.sendMessage(MessageFormat.format("[{0}] Set EnableAntiSpam to {1}",
                            pluginInfoModel.PluginName,
                            isEnabled
                    ));
                }
            }

            // Command: [Command] help
            else if (args[0].equalsIgnoreCase("help"))
            {
                player.sendMessage(MessageFormat.format("{0} {1} by {2}",
                        pluginInfoModel.PluginName,
                        pluginInfoModel.PluginVersion,
                        pluginInfoModel.AuthorName
                ));

                player.sendMessage(MessageFormat.format("/{0} reload : Reload config.yml safely",
                        pluginInfoModel.PluginCommand
                ));

                player.sendMessage(MessageFormat.format("/{0} settime : Sets the SpamTime value",
                        pluginInfoModel.PluginCommand
                ));

                player.sendMessage(MessageFormat.format("/{0} setenable : Enable / disable plugin",
                        pluginInfoModel.PluginCommand
                ));

                return true;
            }
        }

        return true;
    }
}