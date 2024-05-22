package dev.dashti;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.MessageFormat;

public class PluginListener implements Listener {

    private final Plugin plugin;
    private final PluginInfoModel pluginInfoModel;

    public PluginListener(Plugin pl){
        plugin = pl;
        pluginInfoModel = new PluginInfoModel(plugin);
        pluginInfoModel.PluginObject = plugin; // Reference the plugin object
    }

    /**
     * Handles what happens when player types message in chat
     * @param event AsyncPlayerChatEvent
     */
    @EventHandler
    public void playerChat(AsyncChatEvent event)
    {
        Player player = event.getPlayer();

        if(!PluginHelper.getInstance(plugin).isEnabled)
            return;

        //If player is permitted to use plugin
        if(player.hasPermission(pluginInfoModel.PermissionUse) && !player.hasPermission(pluginInfoModel.PermissionExempt))
        {
            //Get data from config
            int spamSeconds = Integer.parseInt(
                    PluginHelper.getInstance(plugin).GetValueFromConfigFile("config.yml","SpamSeconds").toString()
            );

            // If player isn't already in the list, add them to hashmap with current time
            if (!PluginHelper.getInstance(plugin).delay.containsKey(player)) {
                PluginHelper.getInstance(plugin).delay.put(player, System.currentTimeMillis());
            }
            else
            {
                //If player isn't spamming
                if (System.currentTimeMillis() - PluginHelper.getInstance(plugin).delay.get(player) >= spamSeconds * 1000L) {
                    PluginHelper.getInstance(plugin).delay.remove(player);
                } else { //If player spams
                    player.sendMessage(MessageFormat.format("[{0}] Please wait {1} second before sending a new message",
                            pluginInfoModel.PluginName,
                            spamSeconds
                    ));

                    // Send message to staff about spamming
                    for (Player p : plugin.getServer().getOnlinePlayers()) //Search all players online...
                    {
                        if (p.hasPermission(pluginInfoModel.PermissionStaff))
                            p.sendMessage(MessageFormat.format("[{0}] ATTENTION STAFF: Player {1} is spamming",
                                    pluginInfoModel.PluginName,
                                    player.getName()
                            ));
                    }
                    event.setCancelled(true); //Delete chat message
                }
            }
        }
    }
}