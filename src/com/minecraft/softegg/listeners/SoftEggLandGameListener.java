package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandGameUtils;
import com.minecraft.softegg.objects.SoftEggLandGame;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public class SoftEggLandGameListener extends SoftEggLandBase implements Listener {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public BukkitTask checkGames;
    private long lastMessage;
    
    /* Basic Constructor */
    public SoftEggLandGameListener(SoftEggLand base) {
        plugin = base;
        
        checkGames = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                List<SoftEggLandGame> games = SoftEggLandGameUtils.currentGames;
                for(SoftEggLandGame game : games) {
                    game.tick();
                }
            }
        }, 60L, 20L);
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        onPlayerGone(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerKicked(PlayerKickEvent e) {
        onPlayerGone(e.getPlayer());
    }
    
    public void onPlayerGone(Player player) {
        SoftEggLandGame game = SoftEggLandGameUtils.getPlayersGame(player);
        if(game == null) {
            return;
        }
        
        if(!game.isPlayerHoster(player)) {
            game.broadcastToPlayers(ChatImportant + player.getName() + ChatDefault + " left the current game.");
            game.removePlayer(player);
            return;
        }
        
        game.broadcastToPlayers(ChatImportant + player.getName() + ChatDefault + " left the current game, since they were the host the game has ended.");
        game.close();
    }
}
