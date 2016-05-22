package com.minecraft.softegg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class SoftEggLandRepUtils {
    public static File PlayersFile = new File(SoftEggLandUtils.getDataFolder() + "/players.yml");
    public static YamlConfiguration PlayersYML = YamlConfiguration.loadConfiguration(PlayersFile);
    
    public static File KitsFile = new File(SoftEggLandUtils.getDataFolder() + "/kits.yml");
    public static YamlConfiguration KitsYML = YamlConfiguration.loadConfiguration(KitsFile);
    
    public static File RepsFile = new File(SoftEggLandUtils.getDataFolder() + "/reps.yml");
    public static YamlConfiguration RepsYML = YamlConfiguration.loadConfiguration(KitsFile);
    
    public static void ReloadConfig() {
        PlayersYML = YamlConfiguration.loadConfiguration(PlayersFile);
        KitsYML = YamlConfiguration.loadConfiguration(KitsFile);
        RepsYML = YamlConfiguration.loadConfiguration(RepsFile);
    }
    
    public static void SavePlayersConfig() {
        try {
            PlayersYML.save(PlayersFile);
            RepsYML.save(RepsFile);
        } catch (IOException ex) {
            Bukkit.getLogger().info("Failed to save Rep players file. Exception: " + ex.getLocalizedMessage());
        }
    }
    
    public static int getRep(OfflinePlayer player) {
        int amount = 0;
        
        if(!RepsYML.contains(player.getName().toLowerCase())) {
            return amount;
        }
        
        amount = RepsYML.getInt(player.getName().toLowerCase());
        
        return amount;
    }
    
    public static void addRep(OfflinePlayer player, int rep) {
        int oldRep = getRep(player);
        
        int newRep = oldRep + rep;
        
        setRep(player, newRep);
    }
    
    public static boolean spendRep(OfflinePlayer player, int rep) {
        int oldRep = getRep(player);
        
        if(oldRep < rep) {
            return false;
        }
        
        int newRep = oldRep - rep;
        
        int oldSpentRep = getSpentRep(player);
        int newSpentRep = oldSpentRep + rep;
        
        setRep(player, newRep);
        setSpentRep(player, newSpentRep);
        
        return true;
    }
    
    public static void setRep(OfflinePlayer player, int rep) {
        RepsYML.set(player.getName().toLowerCase(), rep);
    }

    public static int getSpentRep(OfflinePlayer player) {
        int amount = 0;
        
        if(!PlayersYML.contains(player.getName().toLowerCase() + ".pointsspent")) {
            return amount;
        }
        
        amount = PlayersYML.getInt(player.getName().toLowerCase() + ".pointsspent");
        
        return amount;
    }
    
    public static void setSpentRep(OfflinePlayer player, int spentRep) {
        PlayersYML.set(player.getName().toLowerCase() + ".pointsspent", spentRep);
    }
    
    public static boolean doesKitExist(String kit) {
        if(!KitsYML.contains(kit)) {
            return false;
        }
        return true;
    }
    
    public static int getKitCost(String kit) {
        return KitsYML.getInt(kit + ".cost");
    }
    
    public static String[] getKits() {
        return SoftEggLandUtils.ObjToString(KitsYML.getKeys(false).toArray());
    }
    
    public static List<String> getKitCommands(String kit) {
        return KitsYML.getStringList(kit + ".commands");
    }
    
    public static void executeCommands(List<String> commands, CommandSender cs) {
        for (String command : commands) {
            command = command.replace("{player}", cs.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
        }
    }

    public static boolean hasKitPermission(String k, CommandSender cs) {
        if(!cs.hasPermission("reputation.kit." + k)) {
            return false;
        }
        return true;
    }
    
    public static void RecordLastJoin(OfflinePlayer player) {
        long time = new Date().getTime();
        PlayersYML.set(player.getName().toLowerCase() + ".jointimestamp",  time);
    }
    
    public static long getLastJoin(OfflinePlayer player) {
        if(!PlayersYML.contains(player.getName().toLowerCase() + ".jointimestamp")) {
            return 0;
        }
        long time = PlayersYML.getLong(player.getName().toLowerCase() + ".jointimestamp");
        return time;
    }
    
    public static void setRepTime(OfflinePlayer player) {
        long time = new Date().getTime();
        PlayersYML.set(player.getName().toLowerCase() + ".reptime",  time);
    }
    
    public static long getRepTime(OfflinePlayer player) {
        if(!PlayersYML.contains(player.getName().toLowerCase() + ".reptime")) {
            return 0;
        }
        long time = PlayersYML.getLong(player.getName().toLowerCase() + ".reptime");
        return time;
    }
    
    public static void setVoteTime(OfflinePlayer player) {
        long time = new Date().getTime();
        PlayersYML.set(player.getName().toLowerCase() + ".votetime",  time);
    }
    
    public static long getVoteTime(OfflinePlayer player) {
        if(!PlayersYML.contains(player.getName().toLowerCase() + ".votetime")) {
            return 0;
        }
        long time = PlayersYML.getLong(player.getName().toLowerCase() + ".votetime");
        return time;
    }
    
    public static boolean areSameDay(Date a, Date b) {
        Calendar ca = Calendar.getInstance();
        Calendar cb = Calendar.getInstance();
        ca.setTime(a);
        cb.setTime(b);
        return ca.get(Calendar.DAY_OF_MONTH) == cb.get(Calendar.DAY_OF_MONTH) && ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR) && ca.get(Calendar.MONTH) == cb.get(Calendar.MONTH);
    }
    
    public static List<OfflinePlayer> getRepPlayers() {
        List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
        
        Set<String> player = PlayersYML.getKeys(false);
        for(String p : player) {
            players.add(Bukkit.getServer().getOfflinePlayer(p));
        }
        
        return players;
    }
}