package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandPlayerMessage extends SoftEggLandBase implements CommandExecutor {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandPlayerMessage(SoftEggLand base) {
        plugin = base;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("playermessage")){
            if(args.length < 1) {
                sender.sendMessage("§cPlease enter a name.");
                return true;
            } else if (args.length < 2) {
                sender.sendMessage("§cPlease enter a message.");
                return true;
            }
            
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage("§cPlayer not online.");
                return true;
            }
            
            String message = "";
            for(int i = 1; i < args.length; i++) {
                message += args[i];
                if(i < (args.length - 1)) {
                    message += " ";
                }
            }
            
            message = SoftEggLandUtils.FormatString(message);
            
            sender.sendMessage("->" + target.getName() + ": " + message);
            target.sendMessage(message);
            return true;
        }
        return false;
    }

}
