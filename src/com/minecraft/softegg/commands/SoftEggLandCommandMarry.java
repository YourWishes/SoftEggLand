package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandMarriageUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SoftEggLandCommandMarry extends SoftEggLandBase implements CommandExecutor {
    
    private SoftEggLand plugin;
    public SoftEggLandCommandMarry(SoftEggLand plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("marry")) {
            if(!(sender instanceof Player)) {
                
            }
            
            Player cs = (Player) sender;
            
            if(args.length > 0 && SoftEggLandMarriageUtils.isMarried(cs)) {
                OfflinePlayer married = SoftEggLandMarriageUtils.getMarried(cs);
                String arg = args[0].toLowerCase();
                if(arg.equalsIgnoreCase("divorce")) {
                    SoftEggLandMarriageUtils.DivorcePlayers(cs, married);
                    return true;
                } 
            }
            
            if(args.length < 1) {
                if(!SoftEggLandMarriageUtils.isMarried(cs)) {
                    sender.sendMessage(ChatDefault + "You are not married.");
                    return true;
                }
                
                OfflinePlayer married = SoftEggLandMarriageUtils.getMarried(cs);
                sender.sendMessage(ChatDefault + "You are married to " + ChatImportant + married.getName() + ChatDefault + ".");
                return true;
            }
            
            Player p = Bukkit.getPlayer(args[0]);
            if(p == null) {
                sender.sendMessage(ChatError + args[0] + " isn't online.");
                return true;
            }
            
            if(!SoftEggLandUtils.canSee(cs, p)) {
                sender.sendMessage(ChatError + args[0] + " isn't online.");
                return true;
            }
            
            if(SoftEggLandMarriageUtils.isMarried(p)) {
                sender.sendMessage(ChatError + p.getDisplayName() + " is already married. :(");
                return true;
            }
            
            if(p == cs) {
                sender.sendMessage(ChatError + "You cannot marry yourself :(");
                return true;
            }
            
            //Offer to marry the player.//
            SoftEggLandMarriageUtils.offers.remove(cs);
            
            SoftEggLandMarriageUtils.offers.put(cs, p);
            cs.sendMessage(ChatDefault + "Sent offer to " + ChatImportant + p.getDisplayName());
            p.sendMessage(ChatImportant + cs.getDisplayName() + ChatDefault + " has proposed!");
            p.sendMessage(ChatDefault + "Type " + ChatImportant + "/accept" + ChatDefault + " to accept.");
            p.sendMessage(ChatDefault + "Or " + ChatImportant + "/deny" + ChatDefault + " to deny.");
            
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("formatusers")) {
            try {
                File usersFile = new File(SoftEggLandUtils.getDataFolder() + "/users.yml");
                File output = new File(SoftEggLandUtils.getDataFolder() + "/output.yml");
                
                if(!usersFile.exists()) {
                    sender.sendMessage(ChatError + "Users.yml missing.");
                    return true;
                }
                
                
                if(output.exists()) {
                    output.delete();
                    output.createNewFile();
                }
                
                YamlConfiguration users = YamlConfiguration.loadConfiguration(usersFile);
                YamlConfiguration outpt = YamlConfiguration.loadConfiguration(output);
                
                MemorySection us = (MemorySection) users.get("users");
                for(String n : us.getKeys(false)) {
                    String name = n;
                    if(!users.contains("users." + n + ".group")) {
                        Bukkit.getLogger().info("Failed to parse user " + n);
                        continue;
                    }
                    String group = users.getString("users." + n + ".group").toLowerCase();
                    
                    List<String> eP = new ArrayList<String>();
                    if(outpt.contains(group)) {
                        eP = outpt.getStringList(group);
                    }
                    
                    eP.add(n.replaceAll("'", ""));
                    outpt.set(group, eP);
                }
                
                outpt.save(output);
                sender.sendMessage(ChatDefault + "Done!");
                return true;
            } catch (IOException ex) {
                sender.sendMessage(ChatError + "File error. Error: " + ex.getLocalizedMessage());
                return false;
            }
        }
        
        return false;
    }
}
