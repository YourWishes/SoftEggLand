package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandMMOUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandMMO extends SoftEggLandBase implements CommandExecutor {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandMMO(SoftEggLand base) {
        plugin = base;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){        
        if(cmd.getName().equalsIgnoreCase("stats")) {
            
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return true;
            }
            
            OfflinePlayer op = null;
            
            if(!(sender instanceof Player)) {
                if(args.length < 1) {
                    sender.sendMessage(ChatError + "Please enter a player name.");
                    return true;
                }
            }
            
            if(args.length == 1) {
                op = Bukkit.getServer().getPlayer(args[0]);
                if(op != null && (sender instanceof Player)) {
                    if(!SoftEggLandUtils.canSee((Player) sender, op.getPlayer())) {
                        op = null;
                    }
                }
                
                if(op == null) {
                    op = Bukkit.getOfflinePlayer(args[0]);
                    if(!op.hasPlayedBefore()) {
                        sender.sendMessage(ChatError + args[0] + " isn't online.");
                        return true;
                    }
                }
            }
            
            if(op == null) {
                op = (Player) sender;
            }
            
            if(!SoftEggLandMMOUtils.isMMOPlayer(op)) {
                sender.sendMessage(ChatError + op.getName() + " hasn't played in Anemos before.");
                return true;
            }
            
            List<String> msgs = new ArrayList<String>();
            
            msgs.add(ChatDefault + "MMO Stats for " + ChatImportant + op.getName() + ChatDefault + " - Current Level " + ChatImportant + String.valueOf(SoftEggLandMMOUtils.getTotalLevel(op)) + ChatDefault + ".");
            
            for(String s : SoftEggLandMMOUtils.skills) {
                msgs.add(ChatImportant + s + ChatDefault + ": Level " + SoftEggLandMMOUtils.getLevel(op, s) + ChatImportant + " - " + ChatDefault + SoftEggLandMMOUtils.getExp(op, s) + "xp");
            }
            
            sender.sendMessage(SoftEggLandUtils.ListToArray(msgs));
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("mmotop")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can run this command.");
                return true;
            }
            
            if(args.length != 0) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            Player player = (Player) sender;
            List<OfflinePlayer> players = SoftEggLandMMOUtils.getMMOPlayers();
            
            OfflinePlayer topPlayer = null;
            long highestPlayer = 0;
            
            for(OfflinePlayer p : players) {
                if(SoftEggLandMMOUtils.getTotalLevel(p) >= highestPlayer) {
                    topPlayer = p;
                    highestPlayer = SoftEggLandMMOUtils.getTotalLevel(p);
                }
            }
            
            if(topPlayer == null) {
                sender.sendMessage(ChatError + "No recorded MMO Players");
                return true;
            }
        }
        return false;
    }
}