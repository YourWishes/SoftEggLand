package com.minecraft.softegg.objects;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SoftEggLandArenas {
    public static List<SoftEggLandArena> arenas = new ArrayList<SoftEggLandArena>();
    
    public static void SetupArenas() {
        /* Test Arena */
        List<Location> spawns = new ArrayList<Location>();
        spawns.add(new Location(Bukkit.getWorld("SoftEggLand"), 508, 64, -623));
        spawns.add(new Location(Bukkit.getWorld("SoftEggLand"), 508, 64, -642));
        spawns.add(new Location(Bukkit.getWorld("SoftEggLand"), 489, 64, -642));
        spawns.add(new Location(Bukkit.getWorld("SoftEggLand"), 489, 64, -623));
        
        /* Arena 1 */
        arenas.add(
            new SoftEggLandArena(
                new Location(Bukkit.getWorld("SoftEggLand"), 484, 74, -646),
                new Location(Bukkit.getWorld("SoftEggLand"), 513, 63, -619),
                spawns,
                10,
                0,
                7
            )
        );
        
        /* Arena 2 */
        spawns = new ArrayList<Location>();
        spawns.add(new Location(Bukkit.getWorld("SoftEggLand"), -228, 65, 7348));
        spawns.add(new Location(Bukkit.getWorld("SoftEggLand"), -228, 65, 7379));
        spawns.add(new Location(Bukkit.getWorld("SoftEggLand"), -185, 65, 7379));
        spawns.add(new Location(Bukkit.getWorld("SoftEggLand"), -185, 65, 7348));
        
        arenas.add(
            new SoftEggLandArena(
                new Location(Bukkit.getWorld("SoftEggLand"), -183, 64, 7381),
                new Location(Bukkit.getWorld("SoftEggLand"), -230, 73, 7346),
                spawns,
                20,
                1,
                14
            )
        );
    }
}
