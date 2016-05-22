package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandMailBoxUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SoftEggLandCommandSendItem extends SoftEggLandBase implements CommandExecutor {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandSendItem(SoftEggLand base) {
        plugin = base;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){        
        if(cmd.getName().equalsIgnoreCase("senditem")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can have Mailboxes.");
                return true;
            }
            Player player = (Player) sender;
            
            String worldname = player.getWorld().getName();
            boolean acceptWorld = false;
            for(int i = 0; i < SurvivalWorlds.length; i++) {
                if(worldname.equalsIgnoreCase(SurvivalWorlds[i])) {
                    acceptWorld = true;
                }
            }
            if(!acceptWorld) {
                sender.sendMessage(ChatError + "You can't do that in this world.");
                return true;
            }
            
            if(args.length != 1) {
                sender.sendMessage(ChatError + "Invalid number of arguments.");
                return false;
            }
            
            OfflinePlayer target = Bukkit.getServer().getPlayer(args[0]);
            if(target == null) {
                target = Bukkit.getServer().getOfflinePlayer(args[0]);
            }
            
            if(!SoftEggLandMailBoxUtils.hasMailbox(target)) {
                sender.sendMessage(ChatError + target.getName() + " doesn't have a Mailbox");
                return true;
            }
            
            if(target.getName() == player.getName()) {
                sender.sendMessage(ChatError + "You cannot send items to yourself.");
                return true;
            }
            
            ItemStack is = player.getItemInHand();
            
            if(is == null || is.getType() == Material.AIR) {
                sender.sendMessage(ChatError + "You must be holding an item.");
                return true;
            }
            
            Chest chest = SoftEggLandMailBoxUtils.getPlayerMailbox(target);
            if(!SoftEggLandUtils.doesChestHaveSpace(chest)) {
                sender.sendMessage(ChatError + target.getName() + "'s Mailbox is full!");
                return true;
            }
            
            player.getInventory().remove(is);
            // Add the "FROM" tag //
            List<String> itemLore = new ArrayList<String>();
            itemLore.add("§7A gift from §9" + player.getName());
            ItemMeta im = is.getItemMeta();
            im.setLore(itemLore);
            is.setItemMeta(im);
            chest.getBlockInventory().addItem(is);
            
            if(target.isOnline()) {
                target.getPlayer().sendMessage(ChatImportant + "You have items in your Mailbox!");
            }
            
            sender.sendMessage(ChatImportant + "Sent " + is.getAmount() + " " + is.getType().name() + " to " + target.getName());
            return true;
        }
        return false;
    }
}