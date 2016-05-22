package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandArenaUtils;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatError;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.objects.SoftEggLandArena;
import com.minecraft.softegg.objects.SoftEggLandArenas;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandArena extends SoftEggLandBase implements CommandExecutor {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandArena(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(cmd.getName().equalsIgnoreCase("joinarena")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can compete.");
                return true;
            }
            
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please enter an arena number to join.");
                return false;
            }
            
            Player p = (Player) sender;
            
            if(SoftEggLandArenaUtils.isPlayerInAnArena(p)) {
                sender.sendMessage(ChatError + "You are already in an arena.");
                return true;
            }
            
            int arenaCount = SoftEggLandArenas.arenas.size();
            int joinArena = 0;
            try {
                joinArena = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(ChatError + "Arena must be a number between 1 and " + arenaCount);
                return true;
            }
            
            if(joinArena > arenaCount || joinArena < 1) {
                sender.sendMessage(ChatError + "Arena must be a number between 1 and " + arenaCount);
                return true;
            }
            
            SoftEggLandArena arena = SoftEggLandArenas.arenas.get(joinArena - 1);
            if(arena.isArenaFull()) {
                sender.sendMessage(ChatError + "This arena is currently full.");
                return true;
            }
            
            SoftEggLandArenaUtils.joinArena(p, arena);
            if(arena.highScoreSet()) {
                sender.sendMessage(ChatDefault + "Highest score is owned by " + ChatImportant + arena.getHighestScorer() + ChatDefault + " with " + ChatImportant + arena.getHighestScore() + ChatDefault + " kills.");
            }
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("leavearena")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can compete.");
                return true;
            }
            
            if(args.length > 0) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            Player p = (Player) sender;
            
            if(!SoftEggLandArenaUtils.isPlayerInAnArena(p)) {
                sender.sendMessage(ChatError + "You aren't in an Arena.");
                return true;
            }
            SoftEggLandArenaUtils.quitArena(p);
            return true;
        }
        return false;
    }
}
