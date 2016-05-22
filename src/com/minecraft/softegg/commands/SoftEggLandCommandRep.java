package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatError;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandRepUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandRep extends SoftEggLandBase implements CommandExecutor {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandRep(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("getrep")) {
            
            OfflinePlayer target = null;
            
            if(args.length == 0) {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(ChatError + "Enter the name of the player.");
                    return false;
                }
                target = (Player) sender;
            }
            
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            if(target == null) {
                target = Bukkit.getServer().getPlayer(args[0]);
                if(target != null) {
                    if(sender instanceof Player) {
                        if(!SoftEggLandUtils.canSee((Player) sender, target.getPlayer())) {
                            if(!args[0].equalsIgnoreCase(target.getName())) {
                                sender.sendMessage(ChatError + args[0] + " isn't online.");
                                return true;
                            }
                        }
                    }
                } else {
                    target = Bukkit.getServer().getOfflinePlayer(args[0]);
                    if(!target.hasPlayedBefore()) {
                        sender.sendMessage(ChatError + args[0] + " isn't online.");
                        return true;
                    }
                }
            }
            
            int rep = SoftEggLandRepUtils.getRep(target);
            int spentRep = SoftEggLandRepUtils.getSpentRep(target);
            
            sender.sendMessage(
                ChatImportant + 
                target.getName() + 
                ChatDefault + 
                " has " + 
                ChatImportant + 
                rep + 
                ChatDefault + 
                " rep, and has spent " +  
                ChatImportant + 
                spentRep + 
                ChatDefault + 
                " rep."
            );
            
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("giverep")) {
            if(args.length > 2) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            if(args.length < 2) {
                sender.sendMessage(ChatError + "Not enough arguments.");
                return false;
            }
            
            OfflinePlayer target = Bukkit.getServer().getPlayer(args[0]);
            if(target == null) {
                target = Bukkit.getServer().getOfflinePlayer(args[0]);
                if(!target.hasPlayedBefore()) {
                    sender.sendMessage(ChatError + args[0] + " has never played before.");
                    return true;
                }
            } else {
                if(sender instanceof Player) {
                    if(!SoftEggLandUtils.canSee((Player) sender, target.getPlayer())) {
                        if(!args[0].equalsIgnoreCase(target.getName())) {
                            sender.sendMessage(ChatError + args[0] + " has never played before.");
                            return true;
                        }
                    }
                }
            }
            
            if(!SoftEggLandUtils.isInt(args[1])) {
                sender.sendMessage(ChatError + args[1] + " isn't a number.");
                return true;
            }
            
            int amount = Integer.parseInt(args[1]);
            SoftEggLandRepUtils.addRep(target, amount);
            sender.sendMessage(ChatDefault + "Gave " + ChatImportant + amount + ChatDefault + " rep to " + ChatImportant + target.getName() + ChatDefault + ".");
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("sendrep")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can use this command.");
                return true;
            }
            
            if(args.length > 2) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            if(args.length < 2) {
                sender.sendMessage(ChatError + "Not enough arguments.");
                return false;
            }
            
            OfflinePlayer target = Bukkit.getServer().getPlayer(args[0]);
            if(target == null) {
                target = Bukkit.getServer().getOfflinePlayer(args[0]);
                if(!target.hasPlayedBefore()) {
                    sender.sendMessage(ChatError + args[0] + " has never played before.");
                    return true;
                }
            } else {
                if(!SoftEggLandUtils.canSee((Player) sender, target.getPlayer())) {
                    if(!args[0].equalsIgnoreCase(target.getName())) {
                        sender.sendMessage(ChatError + args[0] + " has never played before.");
                        return true;
                    }
                }
            }
            
            if(target.getName().equalsIgnoreCase(((Player) sender).getName())) {
                sender.sendMessage(ChatError + "You cant send rep to yourself.");
                return true;
            }
            
            if(!SoftEggLandUtils.isInt(args[1])) {
                sender.sendMessage(ChatError + args[1] + " isn't a number.");
                return true;
            }
            
            int amount = Integer.parseInt(args[1]);
            if(amount < 1) {
                sender.sendMessage(ChatError + "Amount is too small, please enter 1 or more");
                return true;
            }
            
            if(!SoftEggLandRepUtils.spendRep((Player) sender, amount)) {
                sender.sendMessage(ChatError + "You don't have enough rep!");
                return true;
            }
            
            SoftEggLandRepUtils.addRep(target, amount);
            sender.sendMessage(ChatDefault + "Sent " + ChatImportant + amount + ChatDefault + " rep to " + ChatImportant + target.getName() + ChatDefault + ".");
            if(target.isOnline()) {
                target.getPlayer().sendMessage(ChatDefault + "Recieved " + ChatImportant + amount + ChatDefault + " rep from " + ChatImportant + ((Player) sender).getName() + ChatDefault + ".");
            }
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("setrep")) {
            if(args.length > 2) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            if(args.length < 2) {
                sender.sendMessage(ChatError + "Not enough arguments.");
                return false;
            }
            
            OfflinePlayer target = Bukkit.getServer().getPlayer(args[0]);
            if(target == null) {
                target = Bukkit.getServer().getOfflinePlayer(args[0]);
                if(!target.hasPlayedBefore()) {
                    sender.sendMessage(ChatError + args[0] + " has never played before.");
                    return true;
                }
            } else {
                if(!SoftEggLandUtils.canSee((Player) sender, target.getPlayer())) {
                    if(!args[0].equalsIgnoreCase(target.getName())) {
                        sender.sendMessage(ChatError + args[0] + " has never played before.");
                        return true;
                    }
                }
            }
            if(!SoftEggLandUtils.isInt(args[1])) {
                sender.sendMessage(ChatError + args[1] + " isn't a number.");
                return true;
            }
            
            int amount = Integer.parseInt(args[1]);
            
            SoftEggLandRepUtils.setRep(target, amount);
            sender.sendMessage(ChatDefault + "Set " + ChatImportant + target.getName() + ChatDefault + "'s rep to " + ChatImportant + amount + ChatDefault + ".");
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("buykit")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only players can use this command.");
                return false;
            }
            
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please enter a kit name. Type /" + label + " list for a list.");
                return false;
            }
            
            String kit = args[0].toLowerCase().replaceAll(" ", "");
            if(kit.equalsIgnoreCase("list")) {
                String message = ChatDefault + "Kits you can purchase: ";
                String[] kits = SoftEggLandRepUtils.getKits();
                for(String k : kits) {
                    if(SoftEggLandRepUtils.hasKitPermission(k, sender)) {
                        message += ChatImportant + k + ChatDefault + ", ";
                    }
                }
                sender.sendMessage(message);
                return true;
            }
            
            if(!SoftEggLandRepUtils.doesKitExist(kit)) {
                sender.sendMessage(ChatError + "No kit by that name exists.");
                return true;
            }
            
            if(!SoftEggLandRepUtils.hasKitPermission(kit, sender)) {
                sender.sendMessage(ChatError + "You don't have permission for this kit.");
                return true;
            }
            
            Player player = (Player) sender;
            
            int cost = SoftEggLandRepUtils.getKitCost(kit);
            if(!SoftEggLandRepUtils.spendRep(player, cost)) {
                sender.sendMessage(ChatError + "You don't have enough rep to buy this. This costs " + cost);
                return true;
            }
            
            sender.sendMessage(ChatDefault + "Purchased " + ChatImportant + kit + ChatDefault + " for " + ChatImportant + cost + ChatDefault + " rep.");
            
            /* Run commands */
            SoftEggLandRepUtils.executeCommands(SoftEggLandRepUtils.getKitCommands(kit), sender);
            return true;
        }
        
        return false;
    }
}
