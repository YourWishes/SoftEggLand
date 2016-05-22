package com.minecraft.softegg;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;

public class SoftEggLandMailBoxUtils {
    public static void setPlayerMailbox(OfflinePlayer player, Chest chest) {
        try {
            File ChestYML = new File(SoftEggLandUtils.getDataFolder(), "/mailbox.yml");
            YamlConfiguration configFile = YamlConfiguration.loadConfiguration(ChestYML);

            String top =  player.getName();

            configFile.set(top + ".chest", SoftEggLandUtils.getStringLocation(chest.getLocation()));
            configFile.set(top + ".world", chest.getWorld().getName());

            configFile.save(ChestYML);
        } catch(IOException ex) {
            
        }
    }
    
    public static Chest getPlayerMailbox(OfflinePlayer player) {
        Chest chest = null;
        File ChestYML = new File(SoftEggLandUtils.getDataFolder(), "/mailbox.yml");
        YamlConfiguration configFile = YamlConfiguration.loadConfiguration(ChestYML);
        
        if(!configFile.contains( player.getName())) {
            return null;
        }
        
        Location loc = SoftEggLandUtils.getLocationString(configFile.getString( player.getName() + ".chest"), configFile.getString( player.getName() + ".world"));
        chest = (Chest) loc.getBlock().getState();
        if(chest.getType() != Material.CHEST) {
            return null;
        }
        
        return chest;
    }
    
    public static OfflinePlayer getMailboxPlayer(Chest chest) {
        File ChestYML = new File(SoftEggLandUtils.getDataFolder(), "/mailbox.yml");
        YamlConfiguration configFile = YamlConfiguration.loadConfiguration(ChestYML);
        Set<String> keys = configFile.getKeys(false);
        Object[] keysArray = keys.toArray();
        for(int i = 0; i < keys.size(); i++) {
            String currentKey = (String) keysArray[i];
            String location = configFile.getString( currentKey + ".chest");
            String world = configFile.getString( currentKey + ".world");
            if(world.equalsIgnoreCase(chest.getWorld().getName()) && location.equalsIgnoreCase(SoftEggLandUtils.getStringLocation(chest.getLocation()))) {
                return Bukkit.getServer().getOfflinePlayer(currentKey);
            }
        }
        
        return null;
    }
    
    public static boolean hasMailbox(OfflinePlayer player) {
        if(getPlayerMailbox(player) != null) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void deletePlayerMailbox(OfflinePlayer player) {
        try {
            if(hasMailbox(player)) {
                File ChestYML = new File(SoftEggLandUtils.getDataFolder(), "/mailbox.yml");
                YamlConfiguration configFile = YamlConfiguration.loadConfiguration(ChestYML);
                YamlConfiguration newConfigFile = new YamlConfiguration();
                
                Set<String> keys = configFile.getKeys(true);
                keys.remove(player.getName());
                keys.remove(player.getName() + ".chest");
                keys.remove(player.getName() + ".world");
                Object[] k = keys.toArray();
                    
                for(int x = 0; x < k.length; x++) {
                    String m = k[x].toString();
                    newConfigFile.set(m, configFile.get(m));
                }

                newConfigFile.save(ChestYML);
            }
        } catch (IOException ex) {
            Bukkit.getLogger().info("ERROR " + ex.getMessage());
        }
    }
}