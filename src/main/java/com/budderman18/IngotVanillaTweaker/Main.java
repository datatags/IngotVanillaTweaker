package com.budderman18.IngotVanillaTweaker;

import com.budderman18.IngotVanillaTweaker.Bukkit.Events;
import com.budderman18.IngotVanillaTweaker.Bukkit.IngotVanillaTweaker;
import com.budderman18.IngotVanillaTweaker.Data.FileManager;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class enables and disables the plugin
 * It also imports commands and handles events
 */
public class Main extends JavaPlugin implements Listener {
    //plugin
    private static Main plugin = null;
    //global vars
    private static final String ROOT = "";
    private final ConsoleCommandSender sender = getServer().getConsoleSender();
    /**
    * 
    * This method obtains the plugin info
    * 
    * @return 
    */
    public static Main getInstance() {
        return plugin;
    }
    /**
    *
    * This method creates files if needed. 
    * Only needed if file is missing (first usage). 
    *
    */
    private void createFiles() {
        //config file
        File configf = new File(getDataFolder(), "config.yml");
        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
         }
        //config object
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configf);
        } 
        catch (IOException | InvalidConfigurationException e) {
            if (config.getBoolean("enable-debug-mode") == true) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "COULD NOT LOAD CONFIG.YML!!!");
            }
        }
        //langauge file
        File languagef = new File(getDataFolder(), "language.yml");
        if (!languagef.exists()) {
            languagef.getParentFile().mkdirs();
            saveResource("language.yml", false);
         }
        //langauge object
        FileConfiguration language = new YamlConfiguration();
        try {
            language.load(languagef);
        } 
        catch (IOException | InvalidConfigurationException e) {
            if (config.getBoolean("enable-debug-mode") == true) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "COULD NOT LOAD LANGUAGE.YML!!!");
            }
        }
    } 
    /*
    *
    * Enables the plugin.
    * Checks if MC version isn't the latest.
    * If its not, warn the player about lacking support
    * Checks if server is running offline mode
    * If it is, disable the plugin
    * Also loads death event
    *
    */
    @Override
    public void onEnable() {
        //set plugin
        plugin = this;
        //files
        createFiles();
        FileConfiguration language = FileManager.getCustomData(plugin, "language", ROOT);
        //language variables
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message")); 
        String unsupportedVersionAMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-VersionA-Message")); 
        String unsupportedVersionBMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-VersionB-Message")); 
        String unsupportedVersionCMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-VersionC-Message")); 
        String unsecureServerAMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerA-Message")); 
        String unsecureServerBMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerB-Message")); 
        String unsecureServerCMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerC-Message")); 
        String pluginEnabledMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Plugin-Enabled-Message")); //check for correct version
        if (!(Bukkit.getVersion().contains("1.19"))) {
            sender.sendMessage(prefixMessage + unsupportedVersionAMessage);
            sender.sendMessage(prefixMessage + unsupportedVersionBMessage);
            sender.sendMessage(prefixMessage + unsupportedVersionCMessage);  
        }
        //check for online mode
        if (!(getServer().getOnlineMode())) {
            sender.sendMessage(prefixMessage + unsecureServerAMessage);
            sender.sendMessage(prefixMessage + unsecureServerBMessage);
            sender.sendMessage(prefixMessage + unsecureServerCMessage);
            getServer().getPluginManager().disablePlugin(this);
        }
        //commands
        this.getCommand("IngotVanillaTweaker").setExecutor(new IngotVanillaTweaker());
        //events
        getServer().getPluginManager().registerEvents(new Events(),this);
        //enable plugin
        getServer().getPluginManager().enablePlugin(this);
        sender.sendMessage(prefixMessage + pluginEnabledMessage);
    }
    /*
    *
    * This method disables the plugin
    *
    */
    @Override
    public void onDisable() {
        //files
        FileConfiguration language = FileManager.getCustomData(plugin, "language", ROOT);
        //messages
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message")); 
        String pluginDisabledMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Plugin-Disabled-Message"));
        //disables plugin
        getServer().getPluginManager().disablePlugin(this);
        sender.sendMessage(prefixMessage + pluginDisabledMessage);
    }
}
