package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandMailBoxUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandMailbox extends SoftEggLandBase implements CommandExecutor {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandMailbox(SoftEggLand base) {
        plugin = base;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){        
        if(cmd.getName().equalsIgnoreCase("mailbox")) {
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
            
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            if(args.length == 0) {
                if(!SoftEggLandMailBoxUtils.hasMailbox(player)) {
                    sender.sendMessage(ChatError + "You have not set a Mailbox.");
                    return true;
                }
                
                Chest chest = SoftEggLandMailBoxUtils.getPlayerMailbox(player);
                if(SoftEggLandUtils.doesChestHaveItems(chest)) {
                    sender.sendMessage(ChatImportant + "There's items in your Mailbox!");
                    return true;
                } else {
                    sender.sendMessage(ChatDefault + "Your Mailbox is empty.");
                    return true;
                }
            }
            
            if(!args[0].equalsIgnoreCase("set")) {
                sender.sendMessage(ChatError + "Invalid arguments.");
                return false;
            }
            
            Block targetBlock = player.getTargetBlock(null, 50);
            if(targetBlock != null && targetBlock.getType() == Material.CHEST) {
                Chest chest = (Chest) targetBlock.getState();
                if(SoftEggLandUtils.canClaimChest(chest, player)) {
                    SoftEggLandMailBoxUtils.setPlayerMailbox(player, chest);
                    sender.sendMessage(ChatDefault + "Set your Mailbox!");
                    return true;
                } else {
                    sender.sendMessage(ChatError + "You can't set this chest as your Mailbox!");
                }
            } else {
                sender.sendMessage(ChatError + "You need to look at a chest.");
                return true;
            }
        }
        return false;
    }
}