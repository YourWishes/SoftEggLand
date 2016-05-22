package com.minecraft.softegg;

import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SoftEggLandMMOUtils extends SoftEggLandBase {
    
    private static File mmoDir;
    
    public static String[] skills = {
        "Mining",
        "Farming",
        "Woodcutting",
        "Fishing",
        "Smithing",
        "Strength",
        "Attack",
        "Defence",
        "Adventuring",
        "Archery",
        "Enchanting",
        "Cooking"
    };
    
    public static void SetupDirs() {
        mmoDir = new File(SoftEggLandUtils.getDataFolder() + "/SELMMO");
    }
    
    public static void CreatePlayerFile(OfflinePlayer player) {
        File playerFile = new File(mmoDir + "/" + player.getName() + ".yml");
        if(!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException ex) {
                Bukkit.getLogger().info("File Error Occured! " + ex.getLocalizedMessage());
            }
        }
        checkPlayerFile(player);
    }
    
    public static void checkPlayerFiles() {
        File[] playerFiles = mmoDir.listFiles();
        for(File f : playerFiles) {
            checkPlayerFile(Bukkit.getPlayer(f.getName().replaceAll(".yml", "")));
        }
    }
    
    public static void checkPlayerFile(OfflinePlayer player) {
        try {
            File pFile = new File(mmoDir + "/" + player.getName() + ".yml");
            YamlConfiguration pYML = new YamlConfiguration();
            pYML.load(pFile);
            for(String s : skills) {
                s = s.toLowerCase();
                if(!pYML.contains(s)) {
                    pYML.set(s, 0);
                }
            }
            
            pYML.save(pFile);
        } catch (Exception ex) {
            Bukkit.getLogger().info("File Error Occured! " + ex.getLocalizedMessage());
        }
    }
    
    public static boolean isMMOWorld(Location loc) {
        return isMMOWorld(loc.getWorld());
    }
    
    public static boolean isMMOWorld(World world) {
        for(World w : SoftEggLandBase.MMOWorlds) {
            if(world == w) {
                return true;
            }
        }
        return false;
    }
    
    public static long getLevel(OfflinePlayer player, String skill) {
        long level = 0;
        
        long xp = getExp(player, skill);
        
        int pass1 = 0;
        
        for(int i = 1; i <= xp; i++) {
            if(i >= pass1) {
                level += 1;
                pass1 += (100*level);
            }
        }
        
        return level;
    }
    
    public static long getTotalLevel(OfflinePlayer player) {
        long amount = 0;
        for(String s : skills) {
            amount += getLevel(player, s);
        }
        return amount / skills.length;
    }
    
    public static void addExp(OfflinePlayer player, String skill, long amount) {
        long level = getLevel(player, skill);
        long ovLevel = getTotalLevel(player);
        setExp(player, skill, getExp(player, skill) + amount);
        long newlevel = getLevel(player, skill);
        long newOvLevel = getTotalLevel(player);
        
        if(level < newlevel) {
            if(player.isOnline()) {
                Player p = (Player) player;
                p.sendMessage(ChatDefault + "You leveled up in " + ChatImportant + skill + ChatDefault + ", you are now level " + ChatImportant + newlevel + ChatDefault + ".");
                if(!p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    PotionEffect pe = new PotionEffect(PotionEffectType.NIGHT_VISION, 20*2, 1);
                    p.addPotionEffect(pe);
                }
                p.playEffect(p.getLocation().getBlock().getRelative(0, 1, 0).getLocation(), Effect.ENDER_SIGNAL, 100);
            }
        }
        
        if(newOvLevel > ovLevel && player.isOnline()) {
            Player plyr = player.getPlayer();
            plyr.sendMessage(ChatDefault + "You leveled up to level " + ChatImportant + newOvLevel + ChatDefault + ".");
            for(Player p : plyr.getWorld().getPlayers()) {
                if(!SoftEggLandUtils.playerInProximity(plyr, p)) {
                    continue;
                }
                if(!p.canSee(plyr)) {
                    continue;
                }

                p.sendMessage(ChatImportant + plyr.getDisplayName() + ChatDefault + " leveled up to level " + ChatImportant + newOvLevel + ChatDefault + ".");
            }
        }
        
    }
    
    public static long getExp(OfflinePlayer player, String skill) {
        skill = skill.toLowerCase();
        try {
            File pFile = new File(mmoDir + "/" + player.getName() + ".yml");
            YamlConfiguration pYML = new YamlConfiguration();
            pYML.load(pFile);
            return pYML.getLong(skill)+1;
        } catch (Exception ex) {
            Bukkit.getLogger().info("File Error Occured! " + ex.getLocalizedMessage());
        }
        return 0;
    }
    
    public static void setExp(OfflinePlayer player, String skill, long amount) {
        skill = skill.toLowerCase();
        try {
            File pFile = new File(mmoDir + "/" + player.getName() + ".yml");
            YamlConfiguration pYML = new YamlConfiguration();
            pYML.load(pFile);
            pYML.set(skill, amount);
            pYML.save(pFile);
        } catch (Exception ex) {
            Bukkit.getLogger().info("File Error Occured! " + ex.getLocalizedMessage());
        }
    }
    
    public static boolean isMMOPlayer(OfflinePlayer player) {
        List<OfflinePlayer> ops = getMMOPlayers();
        
        for(OfflinePlayer op : ops) {
            if(op == player) {
                return true;
            }
        }
        
        return false;
    }
    
    public static List<OfflinePlayer> getMMOPlayers() {
        List<OfflinePlayer> ops = new ArrayList<OfflinePlayer>();
        
        File[] files = mmoDir.listFiles();
        for(File f : files) {
            ops.add(Bukkit.getOfflinePlayer(f.getName().replaceAll(".yml", "")));
        }
        
        return ops;
    }

    public static void checkSkills() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!SoftEggLandMMOUtils.isMMOWorld(player.getLocation())) {
                continue;
            }

            if(player.isFlying()) {
                continue;
            }

            if(player.getGameMode() == GameMode.CREATIVE) {
                continue;
            }
            
            if(player.getItemInHand() == null) {
                continue;
            }
            
            if(player.getItemInHand().getType() == null) {
                continue;
            }
            
            Material m = player.getItemInHand().getType();
            
            for(String skill : skills) {
                long level = SoftEggLandMMOUtils.getLevel(player, skill) / 5;
                
                if(level >= 15) {
                    level = 15;
                }
                
                if(skill.equalsIgnoreCase("mining") && SoftEggLandUtils.isPickaxe(m)) {
                    PotionEffect pe = new PotionEffect(PotionEffectType.FAST_DIGGING, 120, ((int) level)/2);
                    player.addPotionEffect(pe);
                }
                
                if(skill.equalsIgnoreCase("woodcutting") && SoftEggLandUtils.isAxe(m)) {
                    PotionEffect pe = new PotionEffect(PotionEffectType.JUMP, 120, ((int) level)/2);
                    player.addPotionEffect(pe);
                }
                
                if(skill.equalsIgnoreCase("fishing") && m == Material.FISHING_ROD) {
                    PotionEffect pe = new PotionEffect(PotionEffectType.WATER_BREATHING, 120, ((int) level)/2);
                    player.addPotionEffect(pe);
                }
                
                if(skill.equalsIgnoreCase("smithing") && m == Material.COAL) {
                    PotionEffect pe = new PotionEffect(PotionEffectType.NIGHT_VISION, 420, ((int) level)/2);
                    player.addPotionEffect(pe);
                }
                
                if(skill.equalsIgnoreCase("strength") && player.getHealth() < 2) {
                    PotionEffect pe = new PotionEffect(PotionEffectType.REGENERATION, 120, ((int) level)/2);
                    player.addPotionEffect(pe);
                }
                
                if(skill.equalsIgnoreCase("attack") && SoftEggLandUtils.isSword(m)) {
                    PotionEffect pe = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, ((int) level)/4);
                    player.addPotionEffect(pe);
                }
                
                if(skill.equalsIgnoreCase("defence") && SoftEggLandUtils.isPlayerWearingArmour(player)) {
                    PotionEffect pe = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, ((int) level)/4);
                    player.addPotionEffect(pe);
                }
                
                if(skill.equalsIgnoreCase("adventuring") && player.isSprinting()) {
                    PotionEffect pe = new PotionEffect(PotionEffectType.SPEED, 120, ((int) level)/4);
                    player.addPotionEffect(pe);
                }
            }
            
        }
    }
}
