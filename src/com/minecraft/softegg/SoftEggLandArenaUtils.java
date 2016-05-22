package com.minecraft.softegg;

import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.objects.SoftEggLandArena;
import com.minecraft.softegg.objects.SoftEggLandArenas;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SoftEggLandArenaUtils extends SoftEggLandBase {
    public static File ArenaFile = new File(SoftEggLandUtils.getDataFolder() + "/arenas.yml");
    public static YamlConfiguration ArenaYML = YamlConfiguration.loadConfiguration(ArenaFile);
    
    public static void ReloadConfig() {
        try {
            ArenaFile = new File(SoftEggLandUtils.getDataFolder() + "/arenas.yml");
            ArenaYML.load(ArenaFile);
        } catch (Exception ex) {
        }
    }
    
    public static void SaveConfig() {
        try {
            ArenaYML.save(ArenaFile);
        } catch (Exception ex) {
            
        }
    }
    
    public static boolean isPlayerInAnArena(Player player) {
        for(SoftEggLandArena arena : SoftEggLandArenas.arenas) {
            if(arena.isPlayerInArena(player)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPlayerInAnAreaPhysically(Player player) {
        for(SoftEggLandArena arena : SoftEggLandArenas.arenas) {
            if(SoftEggLandUtils.isLocationInArea(arena.getLocation1(), arena.getLocation2(), player.getLocation())) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPlayerInAreaPhysically(Player player, SoftEggLandArena arena) {
        if(!SoftEggLandUtils.isLocationInArea(arena.getLocation1(), arena.getLocation2(), player.getLocation())) {
            return false;
        }
        return true;
    }
    
    public static boolean isLocationInArena(Location location, SoftEggLandArena arena) {
         if(!SoftEggLandUtils.isLocationInArea(arena.getLocation1(), arena.getLocation2(), location)) {
            return false;
        }
        return true;
    }
    
    public static void joinArena(Player player, SoftEggLandArena arena) {
        arena.addPlayer(player);
        if(SoftEggLandUtils.CanPlayerTalk(player)) {
            Bukkit.getServer().broadcastMessage(ChatImportant + player.getName() + ChatDefault + " joined Arena " + (arena.getNumber()+1) + ".");
        }
        player.sendMessage(ChatError + "Type /leave to leave the arena at any time.");
        player.teleport(arena.getSpawnLocations().get(0));
    }
    
    public static void quitArena(Player player, boolean showMessage) {
        SoftEggLandArena arena = getPlayerArena(player);
        if(showMessage) {
            if(SoftEggLandUtils.CanPlayerTalk(player)) {
                Bukkit.getServer().broadcastMessage(ChatImportant + player.getName() + ChatDefault + " left Arena " + (arena.getNumber()+1) + ", after killing " + arena.getKillCount(player) + " mobs.");
            }
        }
        arena.removePlayer(player);
        player.teleport(arena.getLocation1().getWorld().getSpawnLocation());
    }
    public static void quitArena(Player player) {
        quitArena(player, true);
    }

    public static SoftEggLandArena getPlayerArena(Player player) {
        for(SoftEggLandArena a : SoftEggLandArenas.arenas) {
            if(a.isPlayerInArena(player)) {
                return a;
            }
        }
        return null;
    }

    public static void checkMobs() {
        for(SoftEggLandArena arena : SoftEggLandArenas.arenas) {
            if(arena.getPlayerCount() < 1) {
                continue;
            }
            
            List<Entity> ls = new ArrayList<Entity>();
            
            for(Entity e : arena.mobs) {
                ls.add(e);
            }
            
            for(Entity e : ls) {
                if(e == null || e.isDead() || !SoftEggLandArenaUtils.isLocationInAnArena(e.getLocation())) {
                    arena.mobs.remove(e);
                }
            }
            
            if(arena.mobs.size() > 0) {
                continue;
            }
            
            int spawned = arena.getDifficulty() * arena.getPlayerCount();
            while(spawned > 0) {
                EntityType spawn = EntityType.ZOMBIE;
                ItemStack item = new ItemStack(Material.AIR);
                if(spawned > 5) {
                    spawn = EntityType.ZOMBIE;
                    item = new ItemStack(Material.IRON_SWORD);
                    spawned -= 5;
                } else if(spawned > 3) {
                    spawn = EntityType.SPIDER;
                    spawned -= 3;
                } else {
                    spawn = EntityType.ZOMBIE;
                    spawned -= 1;
                }
                LivingEntity mob = (LivingEntity) arena.getRandomSpawnLocation().getWorld().spawnEntity(arena.getRandomSpawnLocation(), spawn);
                if(item.getType() != Material.AIR) {
                    mob.getEquipment().setItemInHand(item);
                }
                arena.mobs.add(mob);
            }
            arena.newWave();
            arena.sendMessageToPlayers(ChatDefault + "New wave. Previous wave lasted " + (arena.getWaveTime() / 1000) + " seconds.");
            arena.setLastTime(SoftEggLandUtils.getNow());
        }
    }

    public static void checkStats() {
        for(SoftEggLandArena arena : SoftEggLandArenas.arenas) {
            List<String> msg = new ArrayList<String>();
            msg.add(ChatDefault + "Current wave time is "  + (arena.getWaveTime() / 1000) + " seconds. Scores:");
            for(Player p : arena.getPlayers()) {
                msg.add(ChatImportant + p.getName() + ChatDefault + " has slain " + ChatImportant + arena.getKillCount(p) + ChatDefault + " mobs, and survived " + ChatImportant + arena.getWaveCount(p) + ChatDefault + " waves.");
            }
            
            String[] message = SoftEggLandUtils.ListToArray(msg);
            arena.sendMessageToPlayers(message);
        }
    }

    public static SoftEggLandArena getArenaAtLocation(Location loc) {
        for(SoftEggLandArena a : SoftEggLandArenas.arenas) {
            if(SoftEggLandArenaUtils.isLocationInArena(loc, a)) {
                return a;
            }
        }
        return null;
    }

    public static boolean isLocationInAnArena(Location to) {
        if(getArenaAtLocation(to) == null) {
            return false;
        }
        return true;
    }
    
    public static SoftEggLandArena getEntityArena(Entity ent) {
        for(SoftEggLandArena a : SoftEggLandArenas.arenas) {
            if(!a.isEntityInArenaList(ent)) {
            }
            return a;
        }
        return null;
    }
}
