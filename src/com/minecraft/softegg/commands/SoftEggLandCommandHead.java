package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SoftEggLandCommandHead extends SoftEggLandBase implements CommandExecutor {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandHead(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){        
        if(cmd.getName().equalsIgnoreCase("head")) {
            if(args.length > 1) {              
                sender.sendMessage(ChatError + "Type /head while holding a skull to get your head!");
                return false;
            }
            
            String target;
            if(args.length == 1) {
                target = args[0];
                OfflinePlayer t = Bukkit.getServer().getOfflinePlayer(target);
                if(t == null) {
                    sender.sendMessage(ChatError + args[0] + " hasn't played before.");
                    return true;
                }
                target = t.getName();
            } else {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(ChatError + "Only players have heads.");
                    return true;
                }
                Player player = (Player) sender;
                target = player.getName();
            }
            
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players have heads.");
                return true;
            }
            
            Player player = (Player) sender;
            
            if(player.getItemInHand() != null) {
                ItemStack i = player.getItemInHand();
                if(i.getType() == Material.SKULL_ITEM) {
                    ItemStack is= new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                    SkullMeta sm = (SkullMeta) is.getItemMeta();
                    sm.setOwner(target);
                    if(target.equalsIgnoreCase("wither")) {
                        sm.setOwner(null);
                        is= new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
                    } else {
                        is.setItemMeta(sm);
                    }
                    ItemStack[] isl = player.getInventory().getContents();
                    boolean safe = false;
                    if(i.getAmount() >= 2) {
                        i.setAmount(i.getAmount() - 1);
                    } else {
                        player.getInventory().remove(i);
                    }
                    
                    for(int e = 0; e < isl.length; e++) {
                        ItemStack ms = isl[e];
                        if(ms == null) {
                            safe = true;
                        } else {
                            if(ms.getType() == Material.AIR || ms.getAmount() <= 0) {
                                safe = true;
                            }
                        }
                    }
                    
                    if(!safe) {
                        sender.sendMessage(ChatError + "You don't have any inventory space");
                        return true;
                    }
                    
                    player.getInventory().addItem(is);
                    sender.sendMessage(ChatDefault + "Giving 1 head.");
                    return true;
                }
            }
            sender.sendMessage(ChatError + "You must have a skull to change for a head.");
            return true;
        }
        return false;
    }
}
