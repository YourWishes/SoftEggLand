package com.minecraft.softegg.objects;

import com.minecraft.softegg.SoftEggLandArenaUtils;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandRepUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SoftEggLandArena extends SoftEggLandBase {
    
    private Location location1;
    private Location location2;
    private List<Location> spawns;
    private List<Player> players;
    private int mPlayers;
    private int myNumber;
    private int difficulty;
    private HashMap<Player, Integer> KillCount;
    private HashMap<Player, Integer> WaveCount;
    private long lastWaveStart;
    public List<Entity> mobs;
    
    public SoftEggLandArena(Location loc1, Location loc2, List<Location> spawnLocations, int MaxPlayers, int number, int difficult) {
        location1 = loc1;
        location2 = loc2;
        spawns = spawnLocations;
        players = new ArrayList<Player>();
        mPlayers = MaxPlayers;
        myNumber = number;
        difficulty = difficult;
        KillCount = new HashMap<Player, Integer>();
        WaveCount = new HashMap<Player, Integer>();
        lastWaveStart = SoftEggLandUtils.getNow();
        mobs = new ArrayList<Entity>();
    }
    
    public Location getLocation1() {
        return location1;
    }
    
    public Location getLocation2() {
        return location2;
    }
    
    public List<Location> getSpawnLocations() {
        return spawns;
    }
    
    public SoftEggLandArena addPlayer(Player player) {
        if(getPlayerCount() < 1) {
            lastWaveStart = SoftEggLandUtils.getNow();
        }
        
        if(players.size() < 1) {
            for(Entity mob : mobs) {
                mob.remove();
            }
            mobs = new ArrayList<Entity>();
        }
        
        players.add(player);
        KillCount.put(player, 0);
        WaveCount.put(player, 0);
        return this;
    }
    
    public SoftEggLandArena removePlayer(Player player) {
        SoftEggLandArenaUtils.ReloadConfig();
        if(!SoftEggLandArenaUtils.ArenaYML.contains("Arena" + this.getNumber())) {
            SoftEggLandArenaUtils.ArenaYML.set("Arena" + this.getNumber() + ".highestscorer", player.getName());
            SoftEggLandArenaUtils.ArenaYML.set("Arena" + this.getNumber() + ".highestscore", 0);
        }
        
        int highestScore = SoftEggLandArenaUtils.ArenaYML.getInt("Arena" + this.getNumber() + ".highestscore");
        if(KillCount.get(player) > highestScore) {
            SoftEggLandArenaUtils.ArenaYML.set("Arena" + this.getNumber() + ".highestscorer", player.getName());
            SoftEggLandArenaUtils.ArenaYML.set("Arena" + this.getNumber() + ".highestscore", KillCount.get(player));
            Bukkit.getServer().broadcastMessage(ChatImportant + player.getName() + ChatDefault + " set a new record in " + ChatImportant + "Arena " + (this.getNumber()+1) + ChatDefault + " with " + ChatImportant + KillCount.get(player) + ChatDefault + " kills!");
        }
        SoftEggLandArenaUtils.SaveConfig();
        
        players.remove(player);
        KillCount.remove(player);
        WaveCount.remove(player);
        return this;
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public int getMaxPlayers() {
        return mPlayers;
    }
    
    public int getPlayerCount() {
        return getPlayers().size();
    }
    
    public boolean isPlayerInArena(Player player) {
        if(!players.contains(player)) {
            return false;
        }
        return true;
    }
    
    public boolean isArenaFull() {
        if(getPlayerCount() >= getMaxPlayers()) {
            return true;
        }
        return false;
    }

    public int getNumber() {
        return myNumber;
    }
    
    public int getDifficulty() {
        return difficulty;
    }

    public void sendMessageToPlayers(String string) {
        for(Player p : getPlayers()) {
            p.sendMessage(string);
        }
    }
    
    public void sendMessageToPlayers(String[] string) {
        for(Player p : getPlayers()) {
            p.sendMessage(string);
        }
    }
    
    public int getKillCount(Player player) {
        return KillCount.get(player);
    }

    public void addKill(Player Killer) {
        KillCount.put(Killer, KillCount.get(Killer) + 1);
    }
    
    public int getWaveCount(Player player) {
        return WaveCount.get(player);
    }
    
    public SoftEggLandArena addWave(Player player) {
        WaveCount.put(player, WaveCount.get(player) + 1);
        player.sendMessage(ChatDefault + "You are now on wave " + ChatImportant + this.getWaveCount(player) + ChatDefault + ".");
        return this;
    }

    public void setLastTime(long now) {
        lastWaveStart = now;
    }

    public long getWaveTime() {
        return SoftEggLandUtils.getNow() - lastWaveStart;
    }

    public boolean highScoreSet() {
        if(SoftEggLandArenaUtils.ArenaYML.contains("Arena" + this.getNumber())) {
           return true; 
        }
        return false;
    }

    public String getHighestScorer() {
        return SoftEggLandArenaUtils.ArenaYML.getString("Arena" + this.getNumber() + ".highestscorer");
    }

    public int getHighestScore() {
        return SoftEggLandArenaUtils.ArenaYML.getInt("Arena" + this.getNumber() + ".highestscore");
    }

    public Location getRandomSpawnLocation() {
        return this.getSpawnLocations().get(SoftEggLandUtils.getRandomNumberBetween(0, this.getSpawnLocations().size()));
    }
    
    public boolean isEntityInArenaList(Entity ent) {
        if(!mobs.contains(ent)) {
            return false;
        }
        return true;
    }

    public void newWave() {
        for(Player p : players) {
            this.addWave(p);
            double waveR = ((double) this.getWaveCount(p)) / 50d;
            double rwaver = Math.ceil(waveR);
            if(waveR == rwaver) {
                p.sendMessage(ChatDefault + "§dAnother 50 waves complete! Have a rep!");
                SoftEggLandRepUtils.addRep(p, 1);
            }
        }
    }
}
