package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SoftEggLandCommandHoliday extends SoftEggLandBase implements CommandExecutor {    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandHoliday(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(cmd.getName().equalsIgnoreCase("bunny")){
            if(!(sender instanceof Player)) {
                sender.sendMessage(ChatError + "Only humans can wear bunny hats.");
                return true;
            }
            
            if(args.length != 0) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return true;
            }
            
            Player player = (Player) sender;
            player.getInventory().addItem(BunnyHelmet());
            sender.sendMessage(ChatDefault + "Hippity Hoppity.");
            
            LivingEntity le = player;
            le.setCustomName("§dBunny " + player.getDisplayName());
            le.setCustomNameVisible(true);
            
            return true;
        }
        return false;
    }
}
