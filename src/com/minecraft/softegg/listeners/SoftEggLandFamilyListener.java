package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SoftEggLandFamilyListener extends SoftEggLandBase implements Listener {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandFamilyListener(SoftEggLand base) {
        plugin = base;
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        
    }
}
