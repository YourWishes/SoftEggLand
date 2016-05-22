package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatError;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandUtils;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandActivated extends SoftEggLandBase implements CommandExecutor {

    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandActivated(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
       if(cmd.getName().equalsIgnoreCase("activated")) {
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }

            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please enter a player name.");
                return false;
            }

            OfflinePlayer p = Bukkit.getPlayer(args[0]);
            if(p == null) {
                p = Bukkit.getServer().getOfflinePlayer(args[0]);
            }

            if(p.isOnline() && (sender instanceof Player)) {
                Player pl = p.getPlayer();
                if(!SoftEggLandUtils.canSee(pl,(Player) sender)) {
                    p = Bukkit.getServer().getOfflinePlayer(args[0]);
                    return true;
                }
            }

            if(!p.hasPlayedBefore()) {
                sender.sendMessage(ChatError + p.getName() + " hasn't played before.");
                return true;
            }
            
            if(!SoftEggLandUtils.isPlayerRegistered(p)) {
                sender.sendMessage(ChatError + p.getName() + " is not registered on the forums.");
                return true;
            }
            
            if(!SoftEggLandUtils.isPlayerActivated(p)) {
                sender.sendMessage(ChatImportant + p.getName() + ChatDefault + " is registered, but not activated.");
                return true;
            }
            
            sender.sendMessage(ChatImportant + p.getName() + ChatDefault + " is registered and activated.");
            return true;
        }
        return false;
    }    
}
