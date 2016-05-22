package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatError;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandGameUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import com.minecraft.softegg.objects.SoftEggLandGame;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandGame extends SoftEggLandBase implements CommandExecutor {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandGame(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("games")) {
            if(args.length != 0) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            String[] messages = {
                ChatDefault + "Current Running Games:",
                ChatDefault + ""
            };
            
            for(SoftEggLandGame game : SoftEggLandGameUtils.currentGames) {
                messages[1] += ChatImportant + game.getName() + ChatDefault + ", ";
            }
            
            sender.sendMessage(messages);
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("game")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can run this command.");
                return false;
            }
            
            if(args.length != 0) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            SoftEggLandGame game = SoftEggLandGameUtils.getPlayersGame((Player) sender);
            
            if(game == null) {
                sender.sendMessage(ChatError + "You are not in a game.");
                return true;
            }
            
            String[] messages = {
                ChatDefault + "Current Players:",
                ChatDefault + ""
            };
            
            for(OfflinePlayer p : game.getPlayers()) {
                if(game.isPlayerHoster(p)) {
                    messages[1] += "[Hoster] " + ChatImportant + p.getName() + ChatDefault + ", ";
                    continue;
                }
                if(!p.isOnline()) {
                    messages[1] += ChatDefault + "[Offline] ";
                }
                messages[1] += ChatImportant + p.getName();
            }
            
            sender.sendMessage(messages);
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("hostgame")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can run this command.");
                return false;
            }
            
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please enter a game name with no spaces.");
                return false;
            }
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Game name cannot contain spaces.");
                return false;
            }
            
            if(args[0].length() > 8) {
                sender.sendMessage(ChatError + "Game name is too long, keep it under 8 characters.");
                return true;
            }
            
            SoftEggLandGame g = SoftEggLandGameUtils.getPlayersGame((Player) sender);
            if(g != null) {
                sender.sendMessage(ChatError + "You are already in a game, type /leavegame to leave.");
                return true;
            }
            
            //Time to make a game//
            SoftEggLandGame game = new SoftEggLandGame(args[0], (OfflinePlayer) sender);
            SoftEggLandGameUtils.currentGames.add(game);
            
            Bukkit.broadcastMessage(ChatImportant + ((Player) sender).getDisplayName() + ChatDefault + " started a new game called " + ChatImportant + args[0] + ChatDefault + ".");
            Bukkit.broadcastMessage(ChatDefault + "Join the game with " + ChatImportant + "/joingame " + args[0] + ChatDefault + ".");
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("joingame")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can run this command.");
                return false;
            }
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please enter a game name.");
                return false;
            }
            
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            if(SoftEggLandGameUtils.isPlayerInGame((Player) sender)) {
                sender.sendMessage(ChatError + "You are already in a game.");
                return true;
            }
            
            if(!SoftEggLandGameUtils.doesGameExist(args[0])) {
                sender.sendMessage(ChatError + "Game does not exist, type /games for a list.");
                return true;
            }
            
            SoftEggLandGame game = SoftEggLandGameUtils.getGame(args[0]);
            if(game == null) {
                sender.sendMessage(ChatError + "Game does not exist, type /games for a list.");
                return true;
            }
            
            game.addPlayer(((Player) sender));
            game.broadcastToPlayers(ChatImportant + ((Player) sender).getDisplayName() + ChatDefault + " joined the current game!");
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("leavegame")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can run this command.");
                return false;
            }
            
            if(args.length > 0) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            if(!SoftEggLandGameUtils.isPlayerInGame((Player) sender)) {
                sender.sendMessage(ChatError + "You are aren't in a game.");
                return true;
            }
            
            SoftEggLandGame game = SoftEggLandGameUtils.getPlayersGame((Player) sender);
            
            if(game.isPlayerHoster((Player) sender)) {
                sender.sendMessage(ChatError + "You are the host and cannot leave.");
                return true;
            }
            
            game.broadcastToPlayers(ChatImportant + ((Player) sender).getName() + ChatDefault + " left the current game.");
            game.removePlayer(((Player) sender));
            ((Player) sender).teleport(game.getPlayerBackLocation((Player) sender));
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("closegame")) {            
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please enter a game name or winner name.");
                return false;
            }
            
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            if(sender.hasPermission("SoftEggLand.closegame.others") && SoftEggLandGameUtils.getGame(args[0]) != null) {
                SoftEggLandGame game = SoftEggLandGameUtils.getGame(args[0]);
                sender.sendMessage(ChatDefault + "Closed the game " + ChatImportant + game.getName() + ChatDefault + ".");
                game.close();
                return true;
            }
            
            if(!SoftEggLandGameUtils.isPlayerInGame((Player) sender)) {
                sender.sendMessage(ChatError + "You are aren't in a game.");
                return true;
            }
            
            SoftEggLandGame game = SoftEggLandGameUtils.getPlayersGame((Player) sender);
            
            if(!game.isPlayerHoster((Player) sender)) {
                sender.sendMessage(ChatError + "Only the hoster can close the game.");
                return true;
            }
            
            OfflinePlayer p = Bukkit.getPlayer(args[0]);
            if(p == null) {
                p = Bukkit.getOfflinePlayer(args[0]);
            } else if (p.isOnline() && !SoftEggLandUtils.canSee(p.getPlayer(), ((Player) sender))) {
                sender.sendMessage(ChatError + "Winner not in arena");
                return false;
            }
            
            
            if(!game.isPlayerInGame(p)) {
                sender.sendMessage(ChatError + "Winner not in arena");
                return false;
            }
            
            game.close(p);
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("gamemsg")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can run this command.");
                return false;
            }
            
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please enter a message.");
                return false;
            }
            
            SoftEggLandGame game = SoftEggLandGameUtils.getPlayersGame((Player) sender);
            if(game == null) {
                sender.sendMessage(ChatError + "You are not in a game.");
                return false;
            }
            
            String message = "§c[§e" + game.getName() + "§c] §3" + ((Player) sender).getDisplayName() + "§d: §r§f" + ChatDefault + "";
            message += SoftEggLandUtils.arrayToString(args);
            game.broadcastToPlayers(message);
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("countdown")) {
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            int seconds = 10;
            
            SoftEggLandGame game = SoftEggLandGameUtils.getPlayersGame((Player) sender);
            if(game == null) {
                sender.sendMessage(ChatError + "You are not in a game.");
                return true;
            }
            
            if(!game.isPlayerHoster((Player) sender)) {
                sender.sendMessage(ChatError + "Only the hoster can start a countdown.");
                return true;
            }
            
            if(game.isCountdownRunning()) {
                sender.sendMessage(ChatError + "Stopped countdown.");
                game.setCountdown(-1);
                return true;
            }
            
            if(args.length == 1) {
                try {
                    seconds = Integer.parseInt(args[0]);
                } catch(Exception ex) {
                    sender.sendMessage(ChatError + "Seconds must be a number!");
                    return false;
                }
            }
            
            game.setCountdown(seconds+1);
            sender.sendMessage(ChatDefault + "Started a " + ChatImportant + seconds + ChatDefault + " second counter. Type /countdown to stop it.");
            return true;
        }
        return false;
    }
}
