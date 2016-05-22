package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import com.minecraft.softegg.SoftEggLandMarriageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class SoftEggLandMarriageListener extends SoftEggLandBase implements Listener {
    
    private SoftEggLand plugin;
    public SoftEggLandMarriageListener(SoftEggLand plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if(!SoftEggLandMarriageUtils.offers.containsValue(e.getPlayer())) {
            return;
        }
        
        if(e.getMessage().equalsIgnoreCase("/accept")) {
            OfflinePlayer m = null;
            
            for(OfflinePlayer p : SoftEggLandMarriageUtils.offers.keySet()) {
                if(SoftEggLandMarriageUtils.offers.get(p) != e.getPlayer()) {
                    continue;
                }
                
                SoftEggLandMarriageUtils.MarryPlayers(p, e.getPlayer());
                m = p;
            }
            if(m != null) {
                SoftEggLandMarriageUtils.offers.remove(m);
            }
            e.setCancelled(true);
        }
        if(e.getMessage().equalsIgnoreCase("/deny")) {
            OfflinePlayer m = null;
            for(OfflinePlayer p : SoftEggLandMarriageUtils.offers.keySet()) {
                if(SoftEggLandMarriageUtils.offers.get(p) != e.getPlayer()) {
                    continue;
                }
                
                m = p;
                if(p.isOnline()) {
                    p.getPlayer().sendMessage(ChatDefault + "Your proposal was denied.");
                }
                e.getPlayer().sendMessage(ChatDefault + "Denied the propsal.");
            }
            if(m != null) {
                SoftEggLandMarriageUtils.offers.remove(m);
            }
            e.setCancelled(true);
        }
    }
}
