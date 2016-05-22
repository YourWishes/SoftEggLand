package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandMailBoxUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SoftEggLandMailboxListener extends SoftEggLandBase implements Listener {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandMailboxListener(SoftEggLand base) {
        plugin = base;
    }
    
    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(SoftEggLandMailBoxUtils.hasMailbox(player)) {
            Chest chest = SoftEggLandMailBoxUtils.getPlayerMailbox(player);
            if(SoftEggLandUtils.doesChestHaveItems(chest)) {
                player.sendMessage(ChatColor.DARK_RED + "There's items in your Mailbox!");
            }
        }
    }
    
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Block targetBlock = e.getClickedBlock();
        if(targetBlock != null) {
            if(targetBlock.getType() == Material.CHEST) {
                Chest chest = (Chest) targetBlock.getState();
                OfflinePlayer player = SoftEggLandMailBoxUtils.getMailboxPlayer(chest);
                
                if(player != null) {
                    if(!e.getPlayer().getName().equals(player.getName()) && !e.getPlayer().hasPermission("SoftEggLand.mailbox.break")) {
                        e.getPlayer().sendMessage(ChatDefault + "This is " + ChatImportant +  player.getName() + ChatDefault + "\'s MailBox.");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler (priority = EventPriority.LOWEST)
    public void onBlockBreak (BlockBreakEvent e) {
        if(e.getBlock() != null && e.getBlock().getType() == Material.CHEST) {
            Chest chest = (Chest) e.getBlock().getState();
            OfflinePlayer player = SoftEggLandMailBoxUtils.getMailboxPlayer(chest);
            if(player != null && e.getPlayer().hasPermission("EggMailBoxes.break")) {
                SoftEggLandMailBoxUtils.deletePlayerMailbox(player);
                e.getPlayer().sendMessage(ChatDefault + "Unregistered " + ChatImportant +  player.getName() + ChatDefault + "'s MailBox.");
            } else if(player != null && player.getName() != e.getPlayer().getName()) {
                e.getPlayer().sendMessage(ChatError + "You can't break someone elses Mailbox.");
                e.setCancelled(true);
            } else if(player != null) {
                SoftEggLandMailBoxUtils.deletePlayerMailbox(player);
                e.getPlayer().sendMessage(ChatDefault + "Unregistered " + ChatImportant +  player.getName() + ChatDefault + "'s MailBox.");
            }
        }
    }
    
    /*
     * Remove names on items
     */
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) {
            return;
        }
        
        if(e.getCurrentItem().getItemMeta() == null) {
            return;
        }
        
        if(e.getCurrentItem().getItemMeta().getLore() == null) {
            return;
        }
        
        ItemStack is = e.getCurrentItem();
        ItemMeta im = is.getItemMeta();
        if(im.getLore().get(0).contains("A gift from")) {
            im.setLore(new ArrayList<String>());
            is.setItemMeta(im);
            e.setCurrentItem(is);
        }
    }
}
