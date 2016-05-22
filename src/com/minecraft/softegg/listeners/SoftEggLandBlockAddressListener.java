package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SoftEggLandBlockAddressListener extends SoftEggLandBase implements Listener {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandBlockAddressListener(SoftEggLand base) {
        plugin = base;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if(!e.getPlayer().hasPermission("SoftEggLand.color")) {
            e.setMessage(SoftEggLandUtils.removeColors(e.getMessage()));
        }
        
        if(e.getPlayer().hasPermission("SoftEggLand.ip.bypass")) {
            return;
        }
        
        String message = SoftEggLandUtils.PlainString(e.getMessage());
        String[] parts = message.split(" ");
        for ( String s : parts ) {
            s = s.replaceAll(" ", "");
            if(SoftEggLandUtils.isStringAnIP(s)) {
                e.getPlayer().sendMessage(ChatError + "You cannot say IP addresses in chat!");
                Bukkit.getLogger().info(e.getPlayer().getName() + " tried to say the IP " + s + " in chat.");
                e.setCancelled(true);
            }
        }
    }
}
