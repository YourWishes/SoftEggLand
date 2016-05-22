package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBansUtils;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandUtils;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandMute extends SoftEggLandBase implements CommandExecutor {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandMute(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(cmd.getName().equalsIgnoreCase("mute")) {
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please supply a player!");
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
            
            /* Is target already muted? */
            if(SoftEggLandBansUtils.hasActiveBans(target, "mute")) {
                if(args.length != 1) {
                    sender.sendMessage(ChatError + "Player is muted, type /mute [player] to unmute them.");
                    return true;
                }
                
                String name = "CONSOLE";
                if(!SoftEggLandUtils.isConsole(sender)) {
                    name = ((Player) sender).getName();
                }
                
                SoftEggLandBansUtils.PardonPlayer(target, "mute");
                SoftEggLandUtils.broadcastWithPerm("SoftEggLand.mute.notify", 
                        ChatImportant + name + 
                        ChatDefault + 
                        " unmuted " + 
                        ChatImportant + 
                        target.getName() + 
                        ChatDefault + 
                        "."
                        );
                return true;
            }
            
            
            if(args.length < 2) {
                sender.sendMessage(ChatError + "Please supply a reason!");
                return false;
            }
            
            /* Checks to see if arg[1] is a valid time */
            boolean useTime = SoftEggLandUtils.isValidTime(args[1]);
            
            if(useTime && args.length < 3) {
                sender.sendMessage(ChatError + "Enter a reason!");
                return false;
            }
            
            Date unbanDate = new Date();
            String message = "";
            
            for(int i = 1; i < args.length; i++) {
                message += args[i];
                if(i < (args.length - 1)) {
                    message += " ";
                }
            }
            
            if(useTime) {
                unbanDate = SoftEggLandUtils.nowAndString(args[1]);
                message = "";
                for(int i = 2; i < args.length; i++) {
                    message += args[i];
                    if(i < (args.length - 1)) {
                        message += " ";
                    }
                }
            }
            
            SoftEggLandBansUtils.MutePlayer(target, message, sender, unbanDate, useTime);
            return true;
        }
        return false;
    }
}
