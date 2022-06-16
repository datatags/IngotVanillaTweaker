package com.budderman18.IngotVanillaTweaker.Data;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * 
 * This class manages anything file related.
 * It can also auto-update files with new variables 
 * when the methods are configured.
 * 
 */
public class FileManager {
    /**
     *
     * This method is used to read and write to a given file. Also handles YML
     * loading if its a yml file
     *
     * @param plugin
     * @param filename
     * @param path
     * @return FileConfiguration
     *
     */
    public static YamlConfiguration getCustomData(Plugin plugin, String filename, String path) {
        //creat file object
        File file = null;
        //check if folder is a thing
        if (!plugin.getDataFolder().exists()) {
            //create plugin folder
            plugin.getDataFolder().mkdir();
        }
        //check if file is a yml
        if (!filename.contains(".")) {
            file = new File(plugin.getDataFolder() + "/" + path, filename + ".yml");
        }
        //if file isn't yml, run this
        else {
            file = new File(plugin.getDataFolder() + "/" + path, filename);
        }
        //load
        return YamlConfiguration.loadConfiguration(file);
    }
}
