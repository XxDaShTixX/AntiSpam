package dev.dashti;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class PluginHelper {

    // Variables
    private final Plugin plugin;
    private final PluginInfoModel pluginInfoModel;

    public boolean isEnabled;
    public int spamSeconds;

    // Creating the HashMap which stores the player and the time delay
    public HashMap<Player, Long> delay = new HashMap<>();

    private static PluginHelper instance;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private PluginHelper(Plugin pl){
        plugin = pl;
        pluginInfoModel = new PluginInfoModel(plugin);
        pluginInfoModel.PluginObject = plugin; // Reference the plugin object
    }

    // Static method to create instance of Singleton class
    public static synchronized PluginHelper getInstance(Plugin pl)
    {
        if (instance == null)
            instance = new PluginHelper(pl);

        return instance;
    }

    public Object GetValueFromConfigFile(String configFileName, String pathToValue) {
        File file = new File(plugin.getDataFolder(), configFileName);

        if (!file.exists()) {
            plugin.getLogger().warning("File '" + configFileName + "' not found");
            return null;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        return config.get(pathToValue);
    }

    public List<String> GetListFromConfigFile(String configFileName, String pathToValue) {
        File file = new File(plugin.getDataFolder(), configFileName);

        if (!file.exists()) {
            plugin.getLogger().warning("File '" + configFileName + "' not found");
            return null;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(config.isList(pathToValue)){
            return config.getStringList(pathToValue);
        }else{
            return null;
        }
    }

    public void SetValueToConfig(String configFileName, String pathToValue, Object value) {
        File file = new File(plugin.getDataFolder(), configFileName);
        if (!file.exists()) {
            plugin.getLogger().warning("File '" + configFileName + "' not found");
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Check if the value is a list
        if (value instanceof List<?>) {
            List<?> valueList = (List<?>) value;

            // Retrieve the existing list from the config
            List<String> existingList = config.getStringList(pathToValue);

            // Add the new values to the list, avoiding duplicates
            for (Object item : valueList) {
                if (item instanceof String && !existingList.contains(item)) {
                    existingList.add((String) item);
                }
            }

            // Set the updated list to the config
            config.set(pathToValue, existingList);
        } else {
            config.set(pathToValue, value);
        }

        try {
            config.save(file);
            UpdateLocalVariables();
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFileName, e);
        }
    }

    public void SetListToConfig(String configFileName, String pathToValue, Object value) {
        File file = new File(plugin.getDataFolder(), configFileName);
        if (!file.exists()) {
            plugin.getLogger().warning("File '" + configFileName + "' not found");
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Check if the value is a list
        if (value instanceof List<?>) {
            List<?> valueList = (List<?>) value;

            // Set the new list to the config
            config.set(pathToValue, valueList);
        } else {
            config.set(pathToValue, value);
        }

        try {
            config.save(file);
            UpdateLocalVariables();
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFileName, e);
        }
    }

    public void UpdateLocalVariables(){
        isEnabled = Boolean.parseBoolean(GetValueFromConfigFile("config.yml", "EnableAntiSpam").toString());
        spamSeconds = Integer.parseInt(GetValueFromConfigFile("config.yml", "SpamSeconds").toString());
    }
}