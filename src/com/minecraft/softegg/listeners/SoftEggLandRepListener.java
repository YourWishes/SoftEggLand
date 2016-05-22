package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandRepUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitTask;

public class SoftEggLandRepListener extends SoftEggLandBase implements Listener {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public List<OfflinePlayer> notify = new ArrayList<OfflinePlayer>();
    
    public BukkitTask checkRep;
    
    /* Basic Constructor */
    public SoftEggLandRepListener(SoftEggLand base) {
        plugin = base;
        
        /* Task to check players */
        checkRep = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
            public void run() {
                List<OfflinePlayer> ops = SoftEggLandRepUtils.getRepPlayers();
                for (OfflinePlayer op : ops) {
                    Date lastRep = new Date(SoftEggLandRepUtils.getRepTime(op));
                    if (SoftEggLandRepUtils.getRepTime(op) > 0) {
                        if (SoftEggLandRepUtils.areSameDay(lastRep, new Date())){
                            continue;
                        }
                    }
                    Date lastJoin = new Date(SoftEggLandRepUtils.getLastJoin(op));
                    if (!SoftEggLandRepUtils.areSameDay(lastJoin, new Date())) {
                        continue;
                    }
                    SoftEggLandRepUtils.addRep(op, 1);
                    SoftEggLandRepUtils.setRepTime(op);
                    if (op.isOnline()) {
                        Player p = op.getPlayer();
                        p.sendMessage(ChatImportant + "You have recieved a rep point for being online today.");
                    } else {
                        notify.add(op);
                    }
                }
                
                //Save Rep
                SoftEggLandRepUtils.SavePlayersConfig();
            }
        }, 200L, 12000L);
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        SoftEggLandRepUtils.RecordLastJoin(e.getPlayer());
        if(notify.contains((OfflinePlayer) e.getPlayer())) {
            e.getPlayer().sendMessage(ChatImportant + "You have recieved a rep point for being online today.");
            notify.remove((OfflinePlayer) e.getPlayer());
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        SoftEggLandRepUtils.RecordLastJoin(e.getPlayer());
    }
}
