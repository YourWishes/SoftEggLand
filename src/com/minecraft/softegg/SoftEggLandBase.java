package com.minecraft.softegg;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class SoftEggLandBase {
    /* Defines the Colors for SoftEggLand */
    public static ChatColor ChatDefault = ChatColor.GRAY;
    public static ChatColor ChatImportant = ChatColor.BLUE;
    public static ChatColor ChatError = ChatColor.RED;
    
    public String[] jobsI = {"Adventurer", "Miner", "Constructor", "Forester", "Supplier"};
    public String[] jobsII = {"AdventurerII", "MinerII", "ConstructorII", "ForesterII", "SupplierII"};
    public String[] jobsIII = {"AdventurerIII", "MinerIII", "ConstructorIII", "ForesterIII", "SupplierIII"};
    
    public String[] jobs = (String[]) ArrayUtils.addAll(ArrayUtils.addAll(jobsI, jobsII), jobsIII);
    
    public static String[] SurvivalWorlds = {
        "SoftEggLand",
        "SoftEggLand_nether",
        "SoftEggLand_the_end",
        "Newbland",
        "Newbland_nether",
        "Anemos",
        "Anemos_nether",
        "Anemos_the_end",
        "Spawn"
    };
    
    public static String[] CreativeWorlds = {
        "Creative",
        "Creative_nether",
        "Creative_the_end",
        "sandbox"
    };
    
    /* Used for ForeverFalling Worlds, From and To */
    public static String[] FromWorlds = {
        "SoftEggLand_the_end",
        "SoftEggLand_nether",
        "Newbland_nether",
        "Newbland",
        "SoftEggLand",
        "skylands",
        "Creative_the_end",
        "Creative_nether",
        "Creative",
        "sandbox",
        "Anemos",
        "Anemos_the_end",
        "Anemos_nether",
        "spawn"
    };
    public static String[] ToWorlds = {
        "SoftEggLand",
        "SoftEggLand",
        "SoftEggLand",
        "Newbland",
        "SoftEggLand",
        "SoftEggLand",
        "Creative",
        "Creative",
        "Creative",
        "Creative",
        "Anemos",
        "Anemos",
        "Anemos",
        "Anemos"
    };
    
    public static EntityType[] BroadcastedMobs = {
        EntityType.WITHER,
        EntityType.ENDER_DRAGON
    };
    
    public static World[] EndWorlds = {
        Bukkit.getServer().getWorld("SoftEggLand_the_end"),
        Bukkit.getServer().getWorld("Anemos_the_end")
    };
    
    /* Blocks that hoppers can steal from */
    public static Material[] HopperBlocks = {
        Material.HOPPER,
        Material.CHEST,
        Material.TRAPPED_CHEST,
        Material.DROPPER,
        Material.DISPENSER,
        Material.FURNACE
    };
    
    /*** Event Items ***/
    public static ItemStack BunnyHelmet() {
        ItemStack bunnyHelm = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta bunnyMeta = (LeatherArmorMeta) bunnyHelm.getItemMeta();
        
        bunnyMeta.setDisplayName("§dBunny Helmet");
        
        /* Dye the Helmet */
        Color BunnyColor = Bukkit.getServer().getItemFactory().getDefaultLeatherColor();
        BunnyColor = BunnyColor.setRed(204).setGreen(244).setBlue(255);
        bunnyMeta.setColor(BunnyColor);
                
        List<String> lores = new ArrayList<String>();
        lores.add("§f§oHippity Hoppity");
        lores.add("§5Happy Easter 2013~");
        bunnyMeta.setLore(lores);
        
        bunnyHelm.setItemMeta(bunnyMeta);
        
        bunnyHelm.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        bunnyHelm.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 2);
        
        return bunnyHelm;
    }
    
    public static String[] colorCodes = {
        "§0",
        "§1",
        "§2",
        "§3",
        "§4",
        "§5",
        "§6",
        "§7",
        "§8",
        "§9",
        "§a",
        "§b",
        "§c",
        "§d",
        "§e",
        "§f",
        "§k",
        "§l",
        "§m",
        "§n",
        "§o",
        "§r"
    };
    public static String[] altCodes = {
        "&0",
        "&1",
        "&2",
        "&3",
        "&4",
        "&5",
        "&6",
        "&7",
        "&8",
        "&9",
        "&a",
        "&b",
        "&c",
        "&d",
        "&e",
        "&f",
        "&k",
        "&l",
        "&m",
        "&n",
        "&o",
        "&r"
    };
    
    public static World[] MMOWorlds = {
        Bukkit.getWorld("Anemos"),
        Bukkit.getWorld("Anemos_nether"),
        Bukkit.getWorld("Anemos_the_end"),
        Bukkit.getWorld("Spawn")
    };
}
