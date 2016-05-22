package com.minecraft.softegg.objects;

import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import com.minecraft.softegg.SoftEggLandGameUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SoftEggLandGame extends SoftEggLandBase {
    private List<OfflinePlayer> players;
    private OfflinePlayer hoster;
    private String name;
    private Location location;
    private int gameCounter;
    private HashMap<OfflinePlayer, Location> locs;
    
    public SoftEggLandGame(String name, OfflinePlayer hoster) {
        this.name = name;
        this.hoster = hoster;
        this.players = new ArrayList<OfflinePlayer>();
        this.players.add(hoster);
        this.location = hoster.getPlayer().getLocation();
        locs = new HashMap<OfflinePlayer, Location>();
        gameCounter = -1;
    }
    
    public SoftEggLandGame addPlayer(OfflinePlayer player) {
        players.add(player);
        locs.put(player, player.getPlayer().getLocation());
        player.getPlayer().teleport(this.location);
        return this;
    }
    
    public SoftEggLandGame removePlayer(OfflinePlayer player) {
        if(!isPlayerInGame(player)) {
            return this;
        }
        players.remove(player);
        return this;
    }
    
    public List<OfflinePlayer> getPlayers() {
        return this.players;
    }
    
    public boolean isPlayerInGame(OfflinePlayer player) {
        for(OfflinePlayer p : this.getPlayers()) {
            if(p == player) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isPlayerHoster(OfflinePlayer player) {
        if(this.hoster != player) {
            return false;
        }
        return true;
    }
    
    public String getName() {
        return this.name;
    }
    
    public SoftEggLandGame setName(String name) {
        this.name = name;
        return this;
    }
    
    public Location getLocation() {
        return this.location;
    }

    public void broadcastToPlayers(String string) {
        for(OfflinePlayer p : this.getPlayers()) {
            if(!p.isOnline()) {
                continue;
            }
            
            p.getPlayer().sendMessage(string);
        }
    }

    public void close() {
        broadcastToPlayers(ChatDefault + "The Game was closed, no winner was declared.");
        for(OfflinePlayer p : this.getPlayers()) {
            if(!p.isOnline()) {
                continue;
            }
            
            Location loc = this.getPlayerBackLocation(p.getPlayer());
            p.getPlayer().teleport(loc);
        }
        SoftEggLandGameUtils.destroyGame(this);
    }

    public void close(OfflinePlayer winner) {
        broadcastToPlayers(ChatDefault + "The Game was closed, The Winner is " + ChatImportant + winner.getName() + ChatDefault + ".");
        for(OfflinePlayer p : this.getPlayers()) {
            if(!p.isOnline()) {
                continue;
            }
            p.getPlayer().teleport(this.getPlayerBackLocation(p.getPlayer()));
        }
        SoftEggLandGameUtils.destroyGame(this);
    }
    
    public void tick() {
        if(this.gameCounter <= 0) {
            return;
        }
        gameCounter -= 1;
        String message = "§c[§e" + this.getName() + "§c] §3Countdown§d: §r§f" + ChatDefault + gameCounter;
        
        this.broadcastToPlayers(message);
    }
    
    public boolean isCountdownRunning() {
        if(this.gameCounter <= 0) {
            return false;
        }
        return true;
    }

    public void setCountdown(int seconds) {
        this.gameCounter = seconds;
    }

    public Location getPlayerBackLocation(Player player) {
        Location loc = this.locs.get(player);
        if(loc == null) {
            loc = player.getWorld().getSpawnLocation();
        }
        return loc;
    }
}
