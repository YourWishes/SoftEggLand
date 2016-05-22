package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBansUtils;
import com.minecraft.softegg.SoftEggLandBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SoftEggLandCommandPardon extends SoftEggLandBase implements CommandExecutor {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandPardon(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(cmd.getName().equalsIgnoreCase("pardon")) {
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please supply a player.");
                return false;
            }
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments!");
                return false;
            }
            
            OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
            if(target == null) {
                sender.sendMessage(ChatError + args[0] + " has never played before!");
                return true;
            }
            
            if(!SoftEggLandBansUtils.isPlayerBanned(target)) {
                sender.sendMessage(ChatError + args[0] + " isn't banned!");
                return true;
            }
            
            SoftEggLandBansUtils.PardonPlayer(target, "ban");
            target.setBanned(false);
            
            sender.sendMessage(ChatDefault + "Unbanned " + ChatImportant + target.getName() + ChatDefault + ".");
            return true;
        }
        return false;
    }
}
