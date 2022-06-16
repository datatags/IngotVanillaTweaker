/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.budderman18.IngotVanillaTweaker.Bukkit;

import com.budderman18.IngotVanillaTweaker.Data.FileManager;
import com.budderman18.IngotVanillaTweaker.Main;
import java.util.ArrayList;
import java.util.List;import org.bukkit.ChatColor;
;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

/**
 *
 * @author Kyle Collins
 */
public class IngotVanillaTweaker implements TabExecutor {
    //plugin
    private static Plugin plugin = Main.getInstance();
    //files
    private static final String ROOT = "";
    private static FileConfiguration language = FileManager.getCustomData(plugin, "language", ROOT);
    //messages
    private static String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message"));
    private static String noPermissiondMessage = ChatColor.translateAlternateColorCodes('&', language.getString("No-Permission-Message"));
    private static String commandReloadedMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Command-Reloaded-Message"));
    /**
     * 
     * This method reloads messages and sounds in the command
     * 
     */
    private static void reload() {
        language = FileManager.getCustomData(plugin, "language", ROOT);
        prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message"));
        noPermissiondMessage = ChatColor.translateAlternateColorCodes('&', language.getString("No-Permission-Message"));
        commandReloadedMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Command-Reloaded-Message"));
    }
    /**
    *
    * This method handles the ingotvanilatweaker command. 
    *
    * @param sender
    * @param cmd
    * @param label
    * @param args
    * @return 
    * command
    */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("IngotVanillaTweaker")) {
            if (args.length == 0 && sender.hasPermission("ingotvt.ivt")) {
                return false;
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("ingotvt.ivt") && sender.hasPermission("ingotvt.ivt.reload")) {
                Events.reload();
                reload();
                sender.sendMessage(prefixMessage + commandReloadedMessage);
                return true;
            }
            return false;
        }
        return false;
    }
    /**
    *
    * This method handles tabcompletion when required. 
    *
    * @param sender
    * @param command
    * @param alias
    * @param args
    * @return 
    * tab-completion
    */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 1) {
            arguments.add("reload");
        }
        return arguments;
    }
}
