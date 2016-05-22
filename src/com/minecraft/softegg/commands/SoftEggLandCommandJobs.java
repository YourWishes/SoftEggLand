package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SoftEggLandCommandJobs extends SoftEggLandBase implements CommandExecutor {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandJobs(SoftEggLand base) {
        plugin = base;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("jobs")) {
            if(args.length != 0) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            String[] message = new String[] {ChatImportant + "Jobs" + ChatDefault + ": ", ChatDefault + ""};
            List<String> groups = Arrays.asList(jobs);
            Collections.shuffle(groups);
            for(int i = 0; i < groups.size(); i++) {
                message[1] += ChatDefault + groups.get(i);
                if(i < (groups.size() - 1)) {
                    message[1] += ChatDefault + ", ";
                }
            }
            
            sender.sendMessage(message);
            return true;
        }
        return false;
    }
}
