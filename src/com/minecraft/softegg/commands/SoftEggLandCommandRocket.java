package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatError;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class SoftEggLandCommandRocket extends SoftEggLandBase implements CommandExecutor {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandRocket(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(cmd.getName().equalsIgnoreCase("rocket")) {
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please enter a player.");
                return false;
            }
            
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage(ChatError + args[0] + " isn't online.");
                return true;
            }
            
            if(sender instanceof Player) {
                if(!SoftEggLandUtils.canSee((Player) sender, target)) {
                    sender.sendMessage(ChatError + args[0] + " isn't online.");
                    return true;
                }
            }
            
            Firework firework = (Firework) target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            Builder fireworkBuilder = FireworkEffect.builder();
            
            FireworkEffect fireworkEffect = fireworkBuilder
                    .trail(true)
                    .flicker(true)
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(Color.WHITE)
                    .withColor(Color.RED)
                    .withFade(Color.BLUE)
                    .withFade(Color.AQUA)
                    .build();
            fireworkMeta.addEffect(fireworkEffect);
            
            fireworkEffect = fireworkBuilder
                    .trail(false)
                    .flicker(false)
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(Color.AQUA)
                    .withColor(Color.BLUE)
                    .withFade(Color.RED)
                    .withFade(Color.WHITE)
                    .build();
            fireworkMeta.addEffect(fireworkEffect);
            
            fireworkMeta.setPower(3);
            firework.setFireworkMeta(fireworkMeta);
            
            firework.setPassenger(target);
            
            target.sendMessage(ChatDefault + "You have been rocketed!");
            sender.sendMessage(ChatDefault + "Rocketed " + ChatImportant + target.getName());
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("explode")) {
            
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please enter a player.");
                return false;
            }
            
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage(ChatError + args[0] + " isn't online.");
                return true;
            }
            
            if(sender instanceof Player) {
                if(!SoftEggLandUtils.canSee((Player) sender, target)) {
                    sender.sendMessage(ChatError + args[0] + " isn't online.");
                    return true;
                }
            }
            
            Firework firework = (Firework) target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK);

            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            Builder fireworkBuilder = FireworkEffect.builder();

            Type[] types = FireworkEffect.Type.values();
            Color[] colors = {
                Color.AQUA,
                Color.BLACK,
                Color.BLUE,
                Color.FUCHSIA,
                Color.GRAY,
                Color.GREEN,
                Color.LIME,
                Color.MAROON,
                Color.NAVY,
                Color.OLIVE,
                Color.ORANGE,
                Color.PURPLE,
                Color.RED,
                Color.SILVER,
                Color.TEAL,
                Color.WHITE,
                Color.YELLOW
            };

            for(Type t : types) {
                for(Color c : colors) {
                    fireworkMeta.addEffect(fireworkBuilder.with(t).withColor(c).withFade(c).build());
                }
            }

            fireworkMeta.setPower(0);
            firework.setFireworkMeta(fireworkMeta);
            target.setPassenger(firework);
            
            sender.sendMessage(ChatDefault + "Exploded " + ChatImportant + target.getName());
            return true;
        }
        return false;
    }
}
