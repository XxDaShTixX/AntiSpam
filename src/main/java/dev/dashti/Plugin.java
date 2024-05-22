package dev.dashti;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Plugin extends JavaPlugin implements Listener {

    // Variables
    private PluginInfoModel pluginInfoModel;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    /**
     * Handles what heppens when plugin is enabled
     */
    @Override
    public void onEnable() {

        // Create all required config files
        createFiles("config.yml", "plugin.yml");

        pluginInfoModel = new PluginInfoModel(this); // Initialize PluginInfo
        pluginInfoModel.PluginObject = this; // Reference the plugin object

        // Register this class as listener
        getServer().getPluginManager().registerEvents(this, this);

        // Register CommandExecutors
        registerCommands();

        // Register Listeners
        registerEvents(this, new PluginListener(this)); //Register events

        // Refresh cached config/variables
        PluginHelper.getInstance(this).UpdateLocalVariables();

        // Log that plugin is enabled in console
        getLogger().info(pluginInfoModel.PluginName + " Enabled!");
    }

    /**
     * Handles what happens when the plugin is disabled
     */
    @Override
    public void onDisable() {
        getLogger().info(pluginInfoModel.PluginName + " Disabled!");
    }

    /**
     * Handles event registrations for the plugin
     */
    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners)
    {
        for (Listener listener : listeners)
        {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void registerCommands() {
        // Register Command Executors
        PluginCommand command = this.getCommand(pluginInfoModel.PluginCommand);
        if(command != null){ command.setExecutor(new PluginCommandExecutor(this)); }
    }

    private void createFiles(String... fileNames) {
        for (String fileName : fileNames) {
            File file = new File(getDataFolder(), fileName);
            if (!file.exists()) {
                saveResource(fileName, false);
            }
        }
    }
}