package com.budderman18.IngotVanillaTweaker.Data;

import com.budderman18.IngotVanillaTweaker.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
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
    /**
     * 
     * This method updates outdated config files
     * 
     */
    public static void updateConfig() {
        //local vars
        FileConfiguration config = FileManager.getCustomData(Main.getInstance(), "config", "");
        File configf = new File(Main.getInstance().getDataFolder(), "config.yml");
        List<String> comments = new ArrayList<>();
        //check for older config
        if (config.getString("version").contains("1.0")) {
            //attach coments
            comments.add("Change how enchant cost will be displayed");
            comments.add("CHAT = inside chat");
            comments.add("ACTION_BAR = on the action bar");
            comments.add("TITLE = as a title");
            //set data
            config.createSection("tooexpensivefix-outputtype");
            config.setComments("tooexpensivefix-outputtype", comments);
            config.set("tooexpensivefix-outputtype", "CHAT");
            config.set("version", "1.1");
            //save file
            try {
                config.save(configf);
            } 
            catch (IOException ex) {
                if (config.getBoolean("enable-debug-mode") == true) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "COULD NOT SAVE CONFIG.YML!!!");
                }
            }
        }
    }
}