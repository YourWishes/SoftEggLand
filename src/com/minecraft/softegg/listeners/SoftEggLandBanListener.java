package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBansUtils;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatError;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandUtils;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public class SoftEggLandBanListener extends SoftEggLandBase implements Listener {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public BukkitTask checkBans;
    public BukkitTask checkVanish;
    
    public HashMap<Player, Integer> vanishTimes;
    private Date dp;
    
    /* Basic Constructor */
    public SoftEggLandBanListener(SoftEggLand base) {
        plugin = base;
        
        vanishTimes = new HashMap<Player, Integer>();
        
        checkBans = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                SoftEggLandBansUtils.checkBans();
            }
        }, 60L, 200L);
        
        
        checkVanish = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(SoftEggLandUtils.isVisible(p)) {
                        if(!vanishTimes.containsKey(p)) {
                            continue;
                        }
                        vanishTimes.remove(p);
                        continue;
                    }
                    
                    if(!vanishTimes.containsKey(p)) {
                        vanishTimes.put(p, 1);
                        continue;
                    }
                    
                    int oldAmount = vanishTimes.get(p);
                    vanishTimes.remove(p);
                    vanishTimes.put(p, oldAmount + 1);
                    
                    if(oldAmount < 4) {
                        continue;
                    }
                    
                    SoftEggLandBansUtils.WarnPlayer(p, "Overuse of Vanish! Don't abuse the privilage! This is your warning!", Bukkit.getConsoleSender());
                }
            }
        }, 60L, 3000L);
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void playerLogin(PlayerLoginEvent event) {
        SoftEggLandBansUtils.checkBans();
        if(!SoftEggLandBansUtils.isPlayerBanned(event.getPlayer())){
            return;
        }
        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        
        Map<String, String> reason = SoftEggLandBansUtils.getBanReason(event.getPlayer(), "ban");
        if(!"Uknown Reason".equals(reason.get("reason"))) {
            if(reason.get("date").equals(reason.get("unbandate"))) {
                String KickMessage = 
                    ChatDefault + "You have been banned for " + 
                    ChatImportant + reason.get("reason") + 
                    ChatDefault + ". Visit " + 
                    ChatImportant + SoftEggLandUtils.website + 
                    ChatDefault + " to appeal.";
                event.setKickMessage(KickMessage);
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                return;
            }
            
            Date d = new Date();
            Date u = SoftEggLandUtils.getSQLDate(reason.get("unbandate"));
            String diff = SoftEggLandUtils.TimeDiff(d, u);
            
            String KickMessage = 
                ChatDefault + "You have been banned for " + 
                ChatImportant + reason.get("reason") + 
                ChatDefault + " for " + ChatImportant + diff +
                ChatDefault + ". Visit " + 
                ChatImportant + SoftEggLandUtils.website + 
                ChatDefault + " to appeal.";
            event.setKickMessage(KickMessage);
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void blockColorless(PlayerCommandPreprocessEvent e) {
        if(e.getPlayer().hasPermission("SoftEggLand.color")) return;
        String m = e.getMessage();
        for(String s : colorCodes) {
            m = m.replaceAll(s, "");
        }
        for(String s : altCodes) {
            m = m.replaceAll(s, "");
        }
        
        if(!m.replaceAll(" ", "").equalsIgnoreCase("")) return;
        SoftEggLandBansUtils.KickPlayer(e.getPlayer(), "Null Message", Bukkit.getConsoleSender());
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onCmd(PlayerCommandPreprocessEvent e) {
        if(e.getMessage().equalsIgnoreCase("/seed")) {
            e.setCancelled(true);
            return;
        }
        
        if(e.getMessage().equalsIgnoreCase("/Jenae")) {
            if(e.getPlayer().getName().equalsIgnoreCase("Lady_Penny") || e.getPlayer().getName().equalsIgnoreCase("DOMIN8TRIX25")) {
                e.getPlayer().sendMessage(
                        ChatDefault + "..Sorry.. that I haven't always been there for you.. "
                        + ChatColor.LIGHT_PURPLE + "~Dom"
                );
                e.setCancelled(true);
                return;
            } else {
                return;
            }
        }
        
        String[] tellCommands = {
            "tell", "msg", "pm", "message", "whisper", "w", "send"
        };
        
        
        for(String tc : tellCommands) {
            if(!e.getMessage().toLowerCase().startsWith("/" + tc)){
                continue;
            }
            if(SoftEggLandUtils.CanPlayerTalk(e.getPlayer())) {
                continue;
            }
            e.getPlayer().sendMessage(ChatError + "You can't message, you're muted.");
            e.setCancelled(true);
            return;
        }
        
        for(int i = 0; i < 1; i++) {
            if(!(e.getMessage().toLowerCase().startsWith("slap") || e.getMessage().toLowerCase().startsWith("/slap"))) continue;
            String[] split = e.getMessage().split(" ");
            if(split.length < 2) continue;
            
            Player p = Bukkit.getPlayer(split[1]);
            if(p == null) continue;
            if(!p.getName().equalsIgnoreCase("Lady_Penny")) continue;
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatError + "NOPE!");
        }
        
        for(String tc : tellCommands) {
            if(!e.getMessage().toLowerCase().startsWith("/" + tc)){
                continue;
            }
            /* Hide the vanilla "Invisble Player Messaging" */
            String[] split = e.getMessage().split("\\s+");
            
            if(split.length < 2) {
                return;
            }
            
            Player target = Bukkit.getServer().getPlayer(split[1]);
            if(target == null) {
                return;
            }

            if(!SoftEggLandUtils.canSee(e.getPlayer(), target)) {
                e.getPlayer().sendMessage(ChatError + split[1] + " isn't online.");
                e.setCancelled(true);
                return;
            }
        }
        
        
        String[] homesCommands = {
            "listhome", "lshome", "listhomes", "lshomes", "homes", "rlisthomes"
        };
        for(String hc : homesCommands) {
            if(!e.getMessage().toLowerCase().startsWith("/" + hc)){
                continue;
            }
            String[] split = e.getMessage().split("\\s+");
            
            if(split.length < 2) {
                return;
            }
            
            Player target = Bukkit.getServer().getPlayer(split[1]);
            if(target == null) {
                return;
            }

            if(!SoftEggLandUtils.canSee(e.getPlayer(), target)) {
                e.getPlayer().sendMessage(ChatError + "No such player!");
                e.setCancelled(true);
                return;
            }
        }
        
        String[] pardonCommands = {
            "pardon", "unban"
        };
        for(String tc : pardonCommands) {
            if(!e.getMessage().toLowerCase().startsWith("/" + tc)){
                continue;
            }
            /* Hide the vanilla "Invisble Player Messaging" */
            String[] s = e.getMessage().split("\\s+");
            String[] split = Arrays.copyOfRange(s, 1, s.length);
            
            plugin.CommandPardon.onCommand(e.getPlayer(), plugin.getCommand("pardon"), tc, split);
            e.setCancelled(true);
            return;
        }
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent e) {
        onPlayerGone(e.getPlayer());
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerKicked(PlayerKickEvent e) {
        onPlayerGone(e.getPlayer());
        
        Bukkit.getLogger().info("Kick reason: " + e.getReason());
    }
    
    public void onPlayerGone(Player player) {
        //Remove MSG replies
        Map<CommandSender, CommandSender> css = SoftEggLandUtils.PlayerMessageReplies;
        Map<CommandSender, CommandSender> cs2 = new HashMap<CommandSender, CommandSender>(css);
        
        for(CommandSender cs : cs2.keySet()) {
            if(css.get(cs) == player) {
                css.remove(cs);
            }
            
            if(cs == player) {
                css.remove(cs2.get(cs));
            }
        }
        
        if(!vanishTimes.containsKey(player)) {
            return;
        }
        vanishTimes.remove(player);
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent e) {
        if(e.getEntity() == null) {
            return;
        }
        
        if(e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        
        Player player = (Player) e.getEntity();
        
        if(!SoftEggLandUtils.CanPlayerTalk(player)) {
            e.setDeathMessage(null);
            return;
        }
    }
}
