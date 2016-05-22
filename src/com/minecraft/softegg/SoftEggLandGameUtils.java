package com.minecraft.softegg;

import com.minecraft.softegg.objects.SoftEggLandGame;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class SoftEggLandGameUtils extends SoftEggLandBase {
    public static List<SoftEggLandGame> currentGames;
    
    public static void Initialize() {
        currentGames = new ArrayList<SoftEggLandGame>();
    }

    public static boolean doesGameExist(String string) {
        for(SoftEggLandGame g : currentGames) {
            if(g.getName().equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    public static SoftEggLandGame getGame(String string) {
        for(SoftEggLandGame g : currentGames) {
            if(g.getName().equalsIgnoreCase(string)) {
                return g;
            }
        }
        return null;
    }

    public static boolean isPlayerInGame(Player player) {
        for(SoftEggLandGame g : currentGames) {
            if(g.isPlayerInGame(player)) {
                return true;
            }
        }
        return false;
    }

    public static SoftEggLandGame getPlayersGame(Player player) {
        for(SoftEggLandGame g : currentGames) {
            if(!g.isPlayerInGame(player)) {
                continue;
            }
            return g;
        }
        return null;
    }

    public static void destroyGame(SoftEggLandGame g) {
        currentGames.remove(g);
        g = null;
    }
}
