package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandArenaUtils;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandUtils;
import com.minecraft.softegg.objects.SoftEggLandArena;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;

public class SoftEggLandArenaListener extends SoftEggLandBase implements Listener {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public BukkitTask checkArenas;
    private long lastMessage;
    
    /* Basic Constructor */
    public SoftEggLandArenaListener(SoftEggLand base) {
        plugin = base;
        lastMessage = 0;
        
        checkArenas = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                SoftEggLandArenaUtils.checkMobs();
                if(SoftEggLandUtils.getNow() - lastMessage >= 40000) {
                    SoftEggLandArenaUtils.checkStats();
                    lastMessage = SoftEggLandUtils.getNow();
                }
            }
        }, 60L, 120L);
    }
    
    @EventHandler
    public void onPlayerKicked(PlayerKickEvent e) {
        if(SoftEggLandArenaUtils.isPlayerInAnArena(e.getPlayer())) {
            SoftEggLandArenaUtils.quitArena(e.getPlayer());
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(SoftEggLandArenaUtils.isPlayerInAnArena(e.getPlayer())) {
            SoftEggLandArenaUtils.quitArena(e.getPlayer());
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(SoftEggLandArenaUtils.isPlayerInAnArena(e.getEntity())) {
            e.setDeathMessage(
                ChatImportant + 
                e.getEntity().getName() + 
                ChatDefault + 
                " died in an arena, after slaying " + 
                ChatImportant + 
                SoftEggLandArenaUtils.getPlayerArena(e.getEntity()).getKillCount(e.getEntity()) + 
                ChatDefault + 
                " mobs."
            );
            SoftEggLandArenaUtils.quitArena(e.getEntity(), false);
        }
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if(!SoftEggLandArenaUtils.isPlayerInAnArena(e.getPlayer())) {
            if(SoftEggLandArenaUtils.isLocationInAnArena(e.getTo())) {
                
                SoftEggLandArena arena = SoftEggLandArenaUtils.getArenaAtLocation(e.getTo());
                if(arena.isArenaFull()) {
                    e.getPlayer().sendMessage(ChatError + "Can't teleport to this arena, it's full.");
                    e.setCancelled(true);
                    return;
                }
                
                SoftEggLandArenaUtils.joinArena(e.getPlayer(), arena);
            }
            return;
        }
        
        if(!SoftEggLandArenaUtils.isLocationInArena(e.getTo(), SoftEggLandArenaUtils.getPlayerArena(e.getPlayer()))) {
            SoftEggLandArenaUtils.quitArena(e.getPlayer());
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if(!SoftEggLandUtils.isTypeHostile(e.getEntityType())) {
            return;
        }
        
        if(e.getEntity().getKiller() == null) {
            return;
        }
        
        LivingEntity Mob = e.getEntity();
        
        if(SoftEggLandArenaUtils.getEntityArena(Mob) != null) {
            SoftEggLandArena a = SoftEggLandArenaUtils.getEntityArena(Mob);
            a.mobs.remove(Mob);
        } else {
            return;
        }
        
        Player Killer = Mob.getKiller();
        
        if(!SoftEggLandArenaUtils.isPlayerInAnArena(Killer)) {
            return;
        }
        
        SoftEggLandArena arena = SoftEggLandArenaUtils.getPlayerArena(Killer);
        if(arena == null) {
            return;
        }
        
        arena.addKill(Killer);
    }
}
