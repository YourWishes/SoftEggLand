package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.kitteh.tag.TagAPI;

public class SoftEggLandHolidayListener extends SoftEggLandBase implements Listener {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public BukkitTask checkBunnies;
    
    /* Basic Constructor */
    public SoftEggLandHolidayListener(SoftEggLand base) {
        plugin = base;
        
        /* Task to check players */
        checkBunnies = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                Player[] players = Bukkit.getServer().getOnlinePlayers();
                for(int i = 0; i < players.length; i++) {
                    Player player = players[i];
                    bunnyHelmet(player);
                }
            }
        }, 60L, 120L);
    }
    
    public void bunnyHelmet(Player player) {
        ItemStack[] PlayerArmour = player.getInventory().getArmorContents();
        for(int i = 0; i < PlayerArmour.length; i++) {
            ItemStack is = PlayerArmour[i];
            if(is == null) {
                continue;
            }
            
            if(is.getType() != Material.LEATHER_HELMET) {
                continue;
            }
            
            if(is.getItemMeta() == null) {
                continue;
            }
            if(!is.getItemMeta().hasDisplayName()) {
                continue;
            }
            
            if(!is.getItemMeta().getDisplayName().equalsIgnoreCase("§dBunny Helmet")) {
                continue;
            }
            
            if(!player.hasPotionEffect(PotionEffectType.JUMP)) {
                player.sendMessage(ChatDefault + "Hippity Hoppity.");
                TagAPI.refreshPlayer(player);
            }
            
            /* Player is wearing the bunny helm! */
            player.addPotionEffect(PotionEffectType.JUMP.createEffect(200, 1), true);
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    }
}
