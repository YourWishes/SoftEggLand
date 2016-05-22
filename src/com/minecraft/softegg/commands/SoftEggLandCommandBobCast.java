package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SoftEggLandCommandBobCast extends SoftEggLandBase implements CommandExecutor {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandBobCast(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("bobcast")){
            if(args.length < 1) {
                sender.sendMessage("§cPlease enter a message.");
                return true;
            }
            
            String message = "";
            for(int i = 0; i < args.length; i++) {
                message += args[i];
                if(i < (args.length - 1)) {
                    message += " ";
                }
            }
            
            message = SoftEggLandUtils.FormatString(message);
            
            Bukkit.getServer().broadcastMessage("§8[§4BobCast§8] §e" + message);
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("setmotd")) {
            if(args.length < 1) {
                sender.sendMessage(ChatImportant + "Current MOTD is: " + ChatDefault + SoftEggLandUtils.motd);
                return false;
            }
            
            String msg = SoftEggLandUtils.arrayToString(args);
            msg = SoftEggLandUtils.FormatString(msg);
            
            SoftEggLandUtils.motd = msg;
            
            SoftEggLandUtils.broadcastWithPerm("SoftEggLand.*", ChatDefault + "Servers MOTD is now: " + msg);
            return true;
        }
        
        return false;
    }
}
