package dev.dashti;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginInfoModel {
    JavaPlugin PluginObject;

    String PluginName = "AntiSpam"; // Plugin name
    String PluginCommand = "AntiSpam"; // Command you enter to interact with plugin
    String PluginVersion = "v1.4";
    String AuthorName = "Ali Dashti";
    //String AuthorEmail = "contact@dashti.dev";
    //String AuthorWebsite = "https://dashti.dev";
    String PermissionUse = "AntiSpam.Use";
    String PermissionStaff = "AntiSpam.Staff";
    String PermissionExempt = "AntiSpam.Exempt";
    String PermissionAdmin = "AntiSpam.Admin";

    //String[] PluginDependency = new String[]{""}; // Other plugins this plugin relies on

    PluginInfoModel(Plugin pl){
        PluginObject = pl;
    }
}