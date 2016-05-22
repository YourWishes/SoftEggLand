package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandArenaUtils;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandRepUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitTask;

public class SoftEggLandCombatListener extends SoftEggLandBase implements Listener {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public BukkitTask checkEnderdragon;
    
    Map<Player, Long> LastDamageTimer = new HashMap<Player, Long>();
    
    /* File Variables */
    private static File ConfigYML = new File("SoftEggLand/dragon.yml");
    private static YamlConfiguration configFile = YamlConfiguration.loadConfiguration(ConfigYML);
    
    
    /* Basic Constructor */
    public SoftEggLandCombatListener(SoftEggLand base) {
        plugin = base;
        
        ConfigYML = new File("plugins/SoftEggLand/dragon.yml");
        configFile = YamlConfiguration.loadConfiguration(ConfigYML);
        
        checkEnderdragon = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                /* End Worlds */
                
                //SoftEggLandUtils.checkScoreboards();
                
                outer_loop:
                for(int i = 0; i < EndWorlds.length; i++) {
                    World checkWorld = EndWorlds[i];
                    
                    if(checkWorld == null) {
                        continue;
                    }
                    
                    if(checkWorld.getPlayers() == null) {
                        continue;
                    }
                    
                    List<Player> players = checkWorld.getPlayers();
                    if(players.size() < 1) {
                        continue;
                    }
                    Player p = players.get(0);
                    boolean SpawnDragon = true;
                    
                    if(p == null) {
                        SpawnDragon = false;
                        continue;
                    }
                    
                    if(configFile.getString(checkWorld.getName()) == null) {
                        SpawnDragon = false;
                        saveEnderDragon(checkWorld);
                        continue outer_loop;
                    }
                    
                    ConfigYML = new File("plugins/SoftEggLand/dragon.yml");
                    configFile = YamlConfiguration.loadConfiguration(ConfigYML);
                    
                    long lastDragon = Long.parseLong(configFile.get(checkWorld.getName()).toString());
                    
                    if(((System.currentTimeMillis() / 1000) - lastDragon) < 43200) {
                        SpawnDragon = false;
                        continue outer_loop;
                    }
                    
                    List<Entity> Entities = p.getNearbyEntities(10000, 10000, 10000);
                    
                    if(Entities.size() < 1) {
                        SpawnDragon = false;
                        continue;
                    }
                    
                    inner_loop:
                    for(int e = 0; e < Entities.size(); e++) {
                        Entity ent = Entities.get(e);
                        if(ent == null) {
                            continue;
                        }
                        
                        if(ent.getType() == null) {
                            continue;
                        }
                        
                        if(ent.getType() == EntityType.ENDER_DRAGON) {
                            SpawnDragon = false;
                            break inner_loop;
                        }
                    }
                    
                    if(SpawnDragon) {
                        EnderDragon ed = (EnderDragon) checkWorld.spawnEntity(new Location(checkWorld, 0, 0, 0), EntityType.ENDER_DRAGON);
                        ed.setRemoveWhenFarAway(false);
                        Bukkit.getServer().broadcastMessage(ChatDefault + "The new " + ChatImportant + "EnderDragon" + ChatDefault + " was born!");
                        saveEnderDragon(checkWorld);
                    }
                }
            }
        }, 60L, 80L);
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = (Player) e.getEntity();
        if(p.getKiller() != null) {
            Player killer = p.getKiller();
            e.setDeathMessage(ChatImportant + p.getDisplayName() + ChatDefault + " was slain by " + ChatImportant + killer.getDisplayName() + ChatDefault + "!");
            return;
        }
        //Fix Death Messsage
        
        if(e.getDeathMessage().contains("tried to swim in lava")) {
            int random = SoftEggLandUtils.getRandomNumberBetween(0, 4);
            if(random == 1) {
                e.setDeathMessage(ChatImportant + p.getPlayer().getDisplayName() + ChatDefault + " had a bright idea to try to swim in lava. FEELING SMART?");
            } else if(random == 2) {
                e.setDeathMessage(ChatDefault + "Seriously " + ChatImportant + p.getDisplayName() + ChatDefault + "? Trying to swim.. in molten lava.. smart move sherlock.");
            } else if(random == 3) {
                e.setDeathMessage(ChatDefault + "Sherlock " + ChatImportant + p.getDisplayName() + ChatDefault + " swam in boiling hot lava. GENIUS!");
            } else {
                e.setDeathMessage(ChatDefault + "Captain Jack " + ChatImportant + p.getDisplayName() + ChatDefault + " Sparrow... Swimming lava to test your hottness again!?");
            }
            return;
        }
        
        String dm = ChatDefault + e.getDeathMessage().replaceAll(p.getName(), ChatImportant + p.getDisplayName() + ChatDefault) + ".";
        dm = dm.replaceAll("Zombie", ChatImportant + "Zombie" + ChatDefault)
                .replaceAll("Ender Dragon", ChatImportant + "The Ender Dragon" + ChatDefault)
                .replaceAll("Spider", ChatImportant + "Spider" + ChatDefault)
                .replaceAll("Cave", ChatImportant + "Cave" + ChatDefault)
                .replaceAll("Pigman", ChatImportant + "Pigman" + ChatDefault)
                .replaceAll("Silverfish", ChatImportant + "Silverfish" + ChatDefault)
                .replaceAll("flames", ChatImportant + "flames" + ChatDefault)
                .replaceAll("Creeper", ChatImportant + "Creeper" + ChatDefault)
                .replaceAll("Skeleton", ChatImportant + "Skeleton" + ChatDefault)
                .replaceAll("Giant", ChatImportant + "Giant" + ChatDefault)
                .replaceAll("Slime", ChatImportant + "Slime" + ChatDefault)
                .replaceAll("Ghast", ChatImportant + "Ghast" + ChatDefault)
                .replaceAll("fireballed", ChatImportant + "fireballed" + ChatDefault)
                .replaceAll("Enderman", ChatImportant + "an Enderman" + ChatDefault)
                .replaceAll("Blaze", ChatImportant + "Blaze" + ChatDefault)
                .replaceAll("Magma Cube", ChatImportant + "Magma Cube" + ChatDefault)
                .replaceAll("Wither", ChatImportant + "Wither" + ChatDefault)
                .replaceAll("Witch", ChatImportant + "Witch" + ChatDefault)
                .replaceAll("magic", ChatImportant + "magic" + ChatDefault)
                .replaceAll("Wolf", ChatImportant + "Wolf" + ChatDefault)
                .replaceAll("Iron Golem", ChatImportant + "Iron Golem" + ChatDefault)
                ;
        e.setDeathMessage(dm);
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onDamage(EntityDamageEvent e) {
        Entity entityDamaged = e.getEntity();
        if(entityDamaged.getType() == EntityType.PLAYER) {
            Player player = (Player) e.getEntity();
            EntityDamageEvent.DamageCause cause = e.getCause();
            if(cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                return;
            }
            
            if(SoftEggLandArenaUtils.isPlayerInAnArena(player)) {
                return;
            }
            
            Player[] playerList = Bukkit.getServer().getOnlinePlayers();
            if(SoftEggLandUtils.getClosestMob(player).size() < 1) {
                return;
            }
            if(SoftEggLandUtils.getClosestMob(player).get(0).getType().getName() == null) {
                return;
            }
            
            if(!LastDamageTimer.containsKey(player)) {
                LastDamageTimer.put(player, System.currentTimeMillis() / 1000 - 20);
            }
            
            long lastMessage = (long) LastDamageTimer.get(player);
            long now = System.currentTimeMillis() / 1000;
            
            if((now - lastMessage) < 20) {
                return;
            }
            
            if(SoftEggLandUtils.getClosestAttacker(player) == null) {
                return;
            }
            
            LastDamageTimer.put(player, System.currentTimeMillis() / 1000);
            String MessageFinal = ChatImportant + player.getName() + 
                    ChatDefault + " is being attacked by a " + ChatImportant + 
                    SoftEggLandUtils.getClosestAttacker(player).getType().getName() +
                    ChatDefault + "!";

            for(int i = 0; i < playerList.length; i++) {
                if(SoftEggLandUtils.playerInProximity(player, playerList[i])) {
                    if(!playerList[i].canSee(player)) {
                        continue;
                    }
                    
                    if(SoftEggLandArenaUtils.isPlayerInAnArena(playerList[i])) {
                        continue;
                    }
                    
                    playerList[i].sendMessage(MessageFinal);
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if(e.getEntity() == null) {
            return;
        }
        
        Entity ent = e.getEntity();
        
        if(ent.getType() == null) {
            return;
        }
        
        boolean found = false;
        for(int i = 0; i < BroadcastedMobs.length; i++) {
            if(ent.getType() == BroadcastedMobs[i]) {
                found = true;
            }
        }
        
        if(!found) {
            return;
        }
        
        double searchradius = 500;
        if(ent.getType() == EntityType.WITHER) {
            searchradius = 100;
        }
        
        if(ent.getType() == EntityType.ENDER_DRAGON) {
            World checkWorld = e.getEntity().getWorld();
            saveEnderDragon(checkWorld);
        }
        
        List<Entity> entities = ent.getNearbyEntities(searchradius, searchradius, searchradius);
        List<String> players = new ArrayList();
        
        for(int p = 0; p < entities.size(); p++) {
            Entity entit = entities.get(p);
            if(entit.getType() == EntityType.PLAYER) {
                Player player = (Player) entit;
                players.add(player.getName());
            }
        }

        if(players.size() < 0) {
            return;
        }
        String playersList = players.get(0);
        if(players.size() > 1) {
            playersList = "";
            for(int p = 0; p < players.size(); p++) {
                playersList = playersList + ChatImportant + players.get(p);
                if(p < (players.size() - 2)) {
                    playersList += ChatDefault + ", ";
                } else if(p < (players.size() - 1)) {
                    playersList += ChatDefault + " and ";
                }
                Player pl = Bukkit.getPlayerExact(players.get(p));
                if(pl != null) {
                    SoftEggLandRepUtils.addRep(pl, 1);
                    pl.sendMessage(ChatDefault + "Here's a rep for killing the " + ent.getType().getName() + ".");
                }
            }
        }
        Bukkit.getServer().broadcastMessage(ChatImportant + playersList + ChatDefault + " killed the " + ChatImportant + ent.getType().getName() + ChatDefault + ".");
    }
    
    @EventHandler
    public void onEnderPortalCreated(EntityCreatePortalEvent e) {
        if(e.getPortalType() == null) {
            return;
        }
        
        if(e.getPortalType() != PortalType.ENDER) {
            return;
        }
        
        e.getEntity().getWorld().getBlockAt(0, 64, 0).setType(Material.DRAGON_EGG);
        
        e.setCancelled(true);
    }
    
    public static void saveEnderDragon(World world) {
        try {
            configFile = YamlConfiguration.loadConfiguration(ConfigYML);
            configFile.set(world.getName(), Long.toString(System.currentTimeMillis() / 1000));
            configFile.save(ConfigYML);
            Bukkit.getLogger().info("Updated Enderdragon timer to " + System.currentTimeMillis() + " for " + world.getName());
        } catch (IOException ex) {
            Logger.getLogger(SoftEggLandCombatListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
