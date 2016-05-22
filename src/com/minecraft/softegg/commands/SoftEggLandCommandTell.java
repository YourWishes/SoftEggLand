package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatError;
import com.minecraft.softegg.SoftEggLandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandTell extends SoftEggLandBase implements CommandExecutor {

    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandTell(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
       if(cmd.getName().equalsIgnoreCase("tell")){
            if(sender instanceof Player) {
                Player p = (Player) sender;
                
                if(!SoftEggLandUtils.CanPlayerTalk(p)) {
                    sender.sendMessage(ChatError + "You can't message, you're muted.");
                    return true;
                }
            }
            
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Enter a player to message.");
                return false;
            }
            
            if(args.length < 2) {
                sender.sendMessage(ChatError + "Enter a message!");
                return false;
            }
            
            CommandSender targetPlayer = Bukkit.getServer().getPlayer(args[0]);
            if(targetPlayer == null) {
                if(args[0].equalsIgnoreCase("console") || args[0].equalsIgnoreCase("server")) {
                    targetPlayer = Bukkit.getServer().getConsoleSender();
                } else {
                    sender.sendMessage(ChatError + args[0] + " isn't online.");
                    return true;
                }
            }
            
            String message = "";
            
            for(int m = 1; m < args.length; m++) {
                message += args[m];
                if(m < (args.length - 1)) {
                    message += " ";
                }
            }
            
            if(targetPlayer instanceof Player) {
                if(sender instanceof Player) {
                    if(!SoftEggLandUtils.canSee((Player) sender, (Player) targetPlayer)) {
                        sender.sendMessage(ChatError + "You have no one to reply to.");
                        return true;
                    }
                }
            }
            
            SoftEggLandUtils.SendPrivateMessage(sender, targetPlayer, message);
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("reply")){           
            if(sender instanceof Player) {
                Player p = (Player) sender;
                if(!SoftEggLandUtils.CanPlayerTalk(p)) {
                    sender.sendMessage(ChatError + "You can't message, you're muted.");
                    return true;
                }
            }
            
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Enter a message!");
                return false;
            }
            
            CommandSender last = SoftEggLandUtils.GetLastMessager(sender);
            if(last == null) {
                sender.sendMessage(ChatError + "You have no one to reply to.");
                return true;
            }
            
            String message = "";
            
            for(int m = 0; m < args.length; m++) {
                message += args[m];
                if(m < (args.length - 1)) {
                    message += " ";
                }
            }
            
            if(last instanceof Player) {
                if(sender instanceof Player) {
                    if(!SoftEggLandUtils.canSee((Player) sender, (Player) last)) {
                        sender.sendMessage(ChatError + "You have no one to reply to.");
                        return true;
                    }
                }
            }
            
            SoftEggLandUtils.SendPrivateMessage(sender, last, message);
            return true;
        }
        return false;
    }    
}
