package com.minecraft.softegg;

import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import java.io.File;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class SoftEggLandMarriageUtils extends SoftEggLandBase {
    
    public static HashMap<OfflinePlayer, OfflinePlayer> offers;
    
    public static void MarryPlayers(OfflinePlayer player, OfflinePlayer player2) {
        try {
            File playerFile = new File(SoftEggLandUtils.getDataFolder() + "/marriage/" + player.getName() + ".yml");
            if(playerFile.exists()) {
                playerFile.delete();
            }
            
            if(!playerFile.exists()) {
                playerFile.createNewFile();
            }
            
            YamlConfiguration pYml = new YamlConfiguration();
            pYml.set("married", player2.getName());
            pYml.save(playerFile);
            
            
            playerFile = new File(SoftEggLandUtils.getDataFolder() + "/marriage/" + player2.getName() + ".yml");
            if(playerFile.exists()) {
                playerFile.delete();
            }
            
            if(!playerFile.exists()) {
                playerFile.createNewFile();
            }
            
            pYml = new YamlConfiguration();
            pYml.set("married", player.getName());
            pYml.save(playerFile);
            
            if(player.isOnline()) {
                player.getPlayer().sendMessage(ChatDefault + "You are now married to " + ChatImportant + player2.getName() + ChatDefault + "!");
            }
            if(player2.isOnline()) {
                player2.getPlayer().sendMessage(ChatDefault + "You are now married to " + ChatImportant + player.getName() + ChatDefault + "!");
            }
        } catch (Exception ex) {
            
        }
    }
    
    public static void DivorcePlayers(OfflinePlayer player, OfflinePlayer player2) {
        File playerFile = new File(SoftEggLandUtils.getDataFolder() + "/marriage/" + player.getName() + ".yml");
        if(playerFile.exists()) {
            playerFile.delete();
        }
        
        playerFile = new File(SoftEggLandUtils.getDataFolder() + "/marriage/" + player2.getName() + ".yml");
        if(playerFile.exists()) {
            playerFile.delete();
        }
        
        if(player.isOnline()) {
            player.getPlayer().sendMessage(ChatDefault + "You are now divorced with " + ChatImportant + player2.getName() + ChatDefault + "!");
        }
        if(player2.isOnline()) {
            player2.getPlayer().sendMessage(ChatDefault + "You are now divorced with " + ChatImportant + player.getName() + ChatDefault + "!");
        }
    }
    
    public static void DivorcePlayers(OfflinePlayer player) {
        File playerFile = new File(SoftEggLandUtils.getDataFolder() + "/marriage/" + player.getName() + ".yml");
        
        if(!playerFile.exists()) {
            return;
        }
        
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(playerFile);
        
        OfflinePlayer p = Bukkit.getOfflinePlayer(yml.getString("married"));
        
        DivorcePlayers(player, p);
    }
    
    public static OfflinePlayer getMarried(OfflinePlayer player) {
        File playerFile = new File(SoftEggLandUtils.getDataFolder() + "/marriage/" + player.getName() + ".yml");
        
        if(!playerFile.exists()) {
            return null;
        }
        
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(playerFile);
        
        OfflinePlayer p = Bukkit.getOfflinePlayer(yml.getString("married"));
        return p;
    }
    
    public static boolean isMarried(OfflinePlayer player) {
        File playerFile = new File(SoftEggLandUtils.getDataFolder() + "/marriage/" + player.getName() + ".yml");
        
        if(!playerFile.exists()) {
            return false;
        }
        
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(playerFile);
        
        OfflinePlayer p = Bukkit.getOfflinePlayer(yml.getString("married"));
        return (p != null);
    }
    
    public static boolean isMarried(OfflinePlayer player, OfflinePlayer player2) {
        
        File playerFile = new File(SoftEggLandUtils.getDataFolder() + "/marriage/" + player.getName() + ".yml");
        
        if(!playerFile.exists()) {
            return false;
        }
        
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(playerFile);
        
        OfflinePlayer p = Bukkit.getOfflinePlayer(yml.getString("married"));
        
        if(!p.getName().equalsIgnoreCase(player2.getName())) {
            return false;
        }
        
        return true;
    }
    
}
