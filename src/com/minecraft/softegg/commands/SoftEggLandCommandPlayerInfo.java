package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBansUtils;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandPlayerInfo extends SoftEggLandBase implements CommandExecutor {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandPlayerInfo(SoftEggLand base) {
        plugin = base;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(cmd.getName().equalsIgnoreCase("playerinfo")) {
            /* Handles what alias is used */
            String command = alias.toLowerCase();
            
            if(args.length < 1) {
                sender.sendMessage(ChatError + "Please enter a player name.");
                return false;
            }
            
            /* Is target Valid? */
            OfflinePlayer target = Bukkit.getServer().getPlayer(args[0]);
            if(target == null) {
                target = Bukkit.getServer().getOfflinePlayer(args[0]);
                if(!target.hasPlayedBefore()) {
                    sender.sendMessage(ChatError + args[0] + " hasn't played before!");
                    return true;
                }
            }
            
            int page = 1;
            if(args.length > 1) {
                try {
                    page = Integer.parseInt(args[1]);
                    if(page < 1) {
                        sender.sendMessage(ChatError + "Page must be 1 or more.");
                        return true;
                    }
                } catch(Exception ex) {
                    sender.sendMessage(ChatError + "Page must be a number!");
                    return true;
                }
            }
            
            List<Integer> banIDs = SoftEggLandBansUtils.getBans(target, "ban");
            
            /** Handle Different lookups **/
            if(command.equalsIgnoreCase("warns")) {
                banIDs = SoftEggLandBansUtils.getBans(target, "warn");
            }
            if(command.equalsIgnoreCase("bans")) {
                banIDs = SoftEggLandBansUtils.getBans(target, "ban");
            }
            if(command.equalsIgnoreCase("kicks")) {
                banIDs = SoftEggLandBansUtils.getBans(target, "kick");
            }
            if(command.equalsIgnoreCase("mutes")) {
                banIDs = SoftEggLandBansUtils.getBans(target, "mute");
            }
            if(command.equalsIgnoreCase("strikes")) {
                banIDs = SoftEggLandBansUtils.getBans(target, "strike");
            }
            
            
            if(banIDs.size() < 1) {
                sender.sendMessage(ChatError + "No results.");
                return true;
            }
            
            List<String> messages = new ArrayList<String>();
            
            for(int i = 0; i < banIDs.size(); i++) {
                int banID = banIDs.get(i);
                Map<String, String> banData = SoftEggLandBansUtils.getBanData(banID);
                String type = banData.get("type");
                type = SoftEggLandBansUtils.getPastTenseStringName(type);
                String Message = ""
                        + ChatImportant + banData.get("player") + " "
                        + ChatDefault + type + " by "
                        + ChatImportant + banData.get("playerby")
                        + ChatDefault + ", " + SoftEggLandUtils.TimeAgo(SoftEggLandUtils.getSQLDate(banData.get("date"))) + " ago"
                        + ChatDefault + " for " + ChatImportant + banData.get("reason");
                if(!banData.get("unbandate").equalsIgnoreCase(banData.get("date"))) {
                    Date start = SoftEggLandUtils.getSQLDate(banData.get("date"));
                    Date end = SoftEggLandUtils.getSQLDate(banData.get("unbandate"));
                    
                    Message += ChatDefault + ", for " + SoftEggLandUtils.TimeDiff(start, end);
                }
                if(!(sender instanceof Player)) {
                    Message += ChatDefault + ", At: " + ChatImportant + banData.get("pos");
                }
                if(banData.get("active").equalsIgnoreCase("true")) {
                    Message += ChatDefault + ", " + ChatError + "Active";
                }
                messages.add(Message);
            }
            
            List<String[]> messageList = new ArrayList<String[]>();
            
            for(int i = 0; i < messages.size(); i+=3) {
                
                int maxSize = messages.size() - i;
                if(maxSize > 3) {
                    maxSize = 3;
                }
                
                String[] message = new String[maxSize + 1];
                
                message[0] = "TITLEGOESHERE";
                
                for(int e = 1; e < (maxSize+1); e++) {
                    message[e] = messages.get(i+e-1);
                }
                
                messageList.add(message);
            }
            
            for(int i = 0; i < messageList.size(); i++) {
                
                messageList.get(i)[0] = ChatImportant + target.getName() + ChatDefault + ", Page " + ChatImportant + (i+1) + ChatDefault + " of " + ChatImportant + messageList.size() + ChatDefault + ".";
            }
            
            if(page > messageList.size()) {
                sender.sendMessage(ChatError + "Please enter a number between 1 and " + messageList.size() + ".");
                return true;
            }
            
            sender.sendMessage(messageList.get(page - 1));
            return true;
        }
        return false;
    }
}
