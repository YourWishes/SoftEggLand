package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SoftEggLandForeverFallingListener extends SoftEggLandBase implements Listener {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandForeverFallingListener(SoftEggLand base) {
        plugin = base;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerMoveEvent e) {
        for(int i = 0; i < FromWorlds.length; i++) {
            String fromWorld = FromWorlds[i];
            String toWorld = ToWorlds[i];
            if(e.getPlayer().getWorld().getName().equalsIgnoreCase(fromWorld)) {
                if(e.getPlayer().getLocation().getY() <= -32) {
                    double oldX = e.getPlayer().getLocation().getX();
                    double oldZ = e.getPlayer().getLocation().getZ();
                    Location target = new Location(Bukkit.getServer().getWorld(toWorld), oldX, 356, oldZ);
                    e.getPlayer().teleport(target);
                }
            }
        }
    }
}
