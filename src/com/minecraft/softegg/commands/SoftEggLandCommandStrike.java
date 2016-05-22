package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBansUtils;
import com.minecraft.softegg.SoftEggLandBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SoftEggLandCommandStrike extends SoftEggLandBase implements CommandExecutor {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandStrike(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(cmd.getName().equalsIgnoreCase("strike")) {
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please supply a player name.");
                return false;
            }
            
            if(args.length < 2) {
                sender.sendMessage(ChatError + "You need to supply a reason!");
                return false;
            }
            
            /* Is target Valid? */
            OfflinePlayer target = Bukkit.getServer().getPlayer(args[0]);
            if(target == null) {
                target = Bukkit.getServer().getOfflinePlayer(args[0]);
                if(!target.hasPlayedBefore()) {
                    sender.sendMessage(ChatError + args[0] + " hasn't played before!");
                    return true;
                }
            }
            
            /* Determine Reason */
            String message = "";
            
            for(int i = 1; i < args.length; i++) {
                message += args[i];
                if(i < (args.length - 1)) {
                    message += " ";
                }
            }
            
            SoftEggLandBansUtils.StrikePlayer(target, message, sender);
            return true;
        }
        return false;
    }
}
