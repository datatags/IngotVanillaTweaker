package com.budderman18.IngotVanillaTweaker.Bukkit;

import com.budderman18.IngotVanillaTweaker.Data.FileManager;
import com.budderman18.IngotVanillaTweaker.Data.OutputType;
import com.budderman18.IngotVanillaTweaker.Main;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles all events for the plugin.
 * 
 */
public class Events implements Listener {
    //plugin
    private static final Plugin plugin = Main.getInstance();
    //files
    private static final String ROOT = "";
    private static FileConfiguration config = FileManager.getCustomData(plugin, "config", ROOT);
    //output type
    private OutputType type = null;
    /**
     * 
     * This method reloads the files in this class.
     * 
     */
    public static void reload() {
        config = FileManager.getCustomData(plugin, "config", ROOT);
    }
    /**
     *
     * This method handles everything involving fixing the xp
     * It'll set the dropped xp to be half of the player's xp
     * like the vanilla game does, with no dumbass
     * limit.(Why is this not fixed in vanilla?)
     *
     * @param event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        //set new value
        int xp = (event.getEntity().getTotalExperience()/2);
        //check if player has permission and if its enabled
        if (event.getEntity().hasPermission("ingotvt.xpdeathdropfix.allow") && config.getBoolean("enable-xpdeathdropfix") == true && event.getEntity().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY) == false) {
            //set xp
            event.setDroppedExp(xp);
        }
    }
    /**
     *
     * This method handles everything involving fixing the limit
     * It simply always sets the maximum to the level limit
     * It also sets renamed items to 1 level
     *
     */
    @EventHandler
    public void onAnvilUse(PrepareAnvilEvent event) {
        //local vars
        Player player = (Player) event.getViewers().get(0);
        String costString = ChatColor.translateAlternateColorCodes('&', "&cEnchantment Cost: " + event.getInventory().getRepairCost());
        //check if player can afford the cost
        if (player.getExpToLevel() >= event.getInventory().getRepairCost()) {
            //set afford string
            costString = ChatColor.translateAlternateColorCodes('&', "&aEnchantment Cost: " + event.getInventory().getRepairCost());
        }
        //check if player has permission and if its enabled
        if (player.hasPermission("ingotvt.tooexpensivefix.allow") && config.getBoolean("enable-tooexpensivefix") == true) {
            //set type from config
            type = OutputType.getFromString(config.getString("tooexpensivefix-outputtype"));
            //set max cost
            event.getInventory().setMaximumRepairCost(21862);
            //check if item is onoly being renamed
            if (event.getInventory().getItem(2) != null) {
                if (event.getInventory().getItem(0).getItemMeta().getDisplayName() != event.getInventory().getItem(2).getItemMeta().getDisplayName() && event.getInventory().getItem(1) == null) {
                    //set rename cost
                    event.getInventory().setRepairCost(1);
                }
            }
            //check if "too expensive!" would be shown
            try {
                if (event.getInventory().getRepairCost() >= 40 && event.getInventory().getItem(0).getItemMeta().getDisplayName() == event.getInventory().getItem(2).getItemMeta().getDisplayName()) {
                    //check if type is chat
                    if (type == OutputType.CHAT) {
                        //send chat
                        player.sendMessage(costString);
                    }
                    //check if type is actionbar
                    else if (type == OutputType.ACTION_BAR) {
                        //send actionbar
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(costString));
                    }
                    //check if typer is title
                    else if (type == OutputType.TITLE) {
                        //send title
                        player.sendTitle(costString, null, 10, 80, 10);
                    }
                }
            }
            catch (NullPointerException ex) {}
        }
    } 
    /**
     * 
     * This method handles both silkspawners obtaining and silkamethyst.
     * if they are enabled and the player has permission, they can
     * collect the desired block when using a silk touch pickaxe.
     * 
     * @param event 
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        //local vars
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        Material heldMaterial = heldItem.getType();
        ItemStack droppedItem = new ItemStack(event.getBlock().getType());
        //silkspawners
        if (block.getType() == Material.SPAWNER) {
            //slikspawner vars
            List<String> lore = new ArrayList<>();
            CreatureSpawner spawnerData = (CreatureSpawner) block.getState();
            ItemMeta obtainedSpawner = droppedItem.getItemMeta();
            //check if player isnt in creative, has permission, and is enabled
            if (player.getGameMode() != GameMode.CREATIVE && player.hasPermission("ingotvt.silkspawners.allow") && config.getBoolean("enable-silkspawners") == true) {
                //check if item is a silk pickaxe
                if (heldItem.containsEnchantment(Enchantment.SILK_TOUCH) && (heldMaterial == Material.WOODEN_PICKAXE || heldMaterial == Material.STONE_PICKAXE || heldMaterial == Material.IRON_PICKAXE || heldMaterial == Material.GOLDEN_PICKAXE || heldMaterial == Material.DIAMOND_PICKAXE || heldMaterial == Material.NETHERITE_PICKAXE) && event.getBlock().getType() == Material.SPAWNER) {
                    //set lore and spawn item
                    lore.add(spawnerData.getSpawnedType().toString() + " spawner.");
                    obtainedSpawner.setLore(lore);
                    droppedItem.setItemMeta(obtainedSpawner);
                    player.getWorld().dropItem(block.getLocation(), droppedItem);
                }
            }
        }
        //silkamethyst
        //check if player isnt in creative, has permission, and is enable
        else if (player.getGameMode() != GameMode.CREATIVE && player.hasPermission("ingotvt.silkamethyst.allow") && config.getBoolean("enable-silkamethyst") == true) {
            //check if item is a silk pickaxe
            if (heldItem.containsEnchantment(Enchantment.SILK_TOUCH) && (heldMaterial == Material.WOODEN_PICKAXE || heldMaterial == Material.STONE_PICKAXE || heldMaterial == Material.IRON_PICKAXE || heldMaterial == Material.GOLDEN_PICKAXE || heldMaterial == Material.DIAMOND_PICKAXE || heldMaterial == Material.NETHERITE_PICKAXE) && event.getBlock().getType() == Material.BUDDING_AMETHYST) {
                //drop item
                player.getWorld().dropItem(block.getLocation(), droppedItem);
            }
        }
    }
    /**
     * 
     * This method handles block placing. 
     * Used for silkspawners to determine what spawner to make.
     * 
     * @param event 
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        //local vars
        Block block = event.getBlock();
        Location blockLoc = event.getBlockAgainst().getLocation();
        //check if block is a spawner
        if (block.getType() == Material.SPAWNER) {
            //spawner vars
            CreatureSpawner spawnerData = (CreatureSpawner) block.getState();
            ItemMeta placedSpawner = event.getItemInHand().getItemMeta();
            List<String> lore = placedSpawner.getLore();
            //set drop location
            blockLoc.setY(blockLoc.getY()+1);
            //check if player has permission, and is enable
            if (event.getPlayer().hasPermission("ingotvt.silkspawners.allow") && config.getBoolean("enable-silkspawners") == true) {
                //set spawner
                spawnerData.setSpawnedType(EntityType.valueOf(lore.get(0).replace(" spawner.", "")));
                event.getPlayer().getWorld().setBlockData(blockLoc, spawnerData.getBlockData());
            }
        }
    }
}
