package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.objects.SoftEggLandCraftItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class SoftEggLandCustomEventListenerListener  extends SoftEggLandBase implements Listener {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public SoftEggLandCustomEventListenerListener(SoftEggLand base) {
        plugin = base;
    }
    
    @EventHandler
    private void onCraftItem(CraftItemEvent e) {
        if(e instanceof SoftEggLandCraftItemEvent) {
            return;
        }
        
        Player player = Bukkit.getPlayerExact(e.getWhoClicked().getName());
        SoftEggLandCraftItemEvent event = new SoftEggLandCraftItemEvent(player, e.getRecipe(), e.getView(), e.getSlotType(), e.getSlot(), e.isRightClick(), e.isShiftClick());
        
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
}
