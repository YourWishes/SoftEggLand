package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandDeathChestUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class SoftEggLandDeathChestListener extends SoftEggLandBase implements Listener {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public BukkitTask checkChests;
    
    /* Basic Constructor */
    public SoftEggLandDeathChestListener(SoftEggLand base) {
        plugin = base;
        
        checkChests = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                World[] worlds = (World[])Bukkit.getServer().getWorlds().toArray(new World[0]);
                for (World worlda : worlds) {
                    SoftEggLandDeathChestUtils.checkChests(worlda);
                }
            }
        }, 120L, 20L);
    }
    
    @EventHandler (ignoreCancelled=true)
    public void checkForDeathChest(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block b = e.getClickedBlock();
        if(b != null) {
            if(SoftEggLandDeathChestUtils.isDeathChest(b)) {
                player.sendMessage(ChatImportant + "You have claimed a death chest.");
                SoftEggLandDeathChestUtils.breakDeathChest(b, true);
                e.setCancelled(true);
            }
            if(SoftEggLandDeathChestUtils.isDeathSign(b)) {
                player.sendMessage(ChatImportant + "You have claimed a death chest.");
                SoftEggLandDeathChestUtils.breakDeathChest(b, true);
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled=true)
    public void createDeathChest(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Location deathpoint = player.getLocation();
        
        for(String world : SoftEggLandUtils.CreativeWorlds) {
            if(deathpoint.getWorld().getName().equalsIgnoreCase(world)) {
                return;
            }
        }
        
        if(e.getDrops().size() > 0) {
            Block chestBlock = deathpoint.getBlock();
            
            boolean worldguardOk = true;
            
            if(!SoftEggLandUtils.getWorldGuard().canBuild(player, chestBlock)) {
                worldguardOk = false;
            }
            
            /* Ensure Worlds Are OK */
            for(int i = 0; i < SoftEggLandUtils.CreativeWorlds.length; i++) {
                String world = SoftEggLandUtils.CreativeWorlds[i];
                if(chestBlock.getWorld().getName() == world) {
                    worldguardOk = false;
                }
            }
            
            if(worldguardOk) {
                Block signBlock = chestBlock.getRelative(0, 0, 1);
                if(SoftEggLandDeathChestUtils.isChestTypeSafe(chestBlock.getType())) {
                    chestBlock.setType(Material.CHEST);
                    if(SoftEggLandDeathChestUtils.isChestTypeSafe(signBlock.getType())) {
                        signBlock.setType(Material.WALL_SIGN);
                        byte[] b = new byte[1];
                        b[0] = 3;
                        signBlock.setData(b[0]);
                    }
                }
                BlockState chestBlockState = chestBlock.getState();
                if(chestBlockState instanceof Chest) {
                    Chest deathChest = (Chest) chestBlockState;
                    
                    player.sendMessage(ChatImportant + "You must claim your death chest within " + (SoftEggLandDeathChestUtils.chestTimeout / 1000) + " seconds.");

                    /* Change the list of dropped items into a single stack */
                    for(int i = 0; i < e.getDrops().size(); i++) {
                        deathChest.getBlockInventory().addItem(e.getDrops().get(i));
                    }
                    deathChest.update();
                    ItemStack[] storedItems = deathChest.getBlockInventory().getContents();
                    for(int i = 0; i < storedItems.length; i++) {
                        e.getDrops().remove(storedItems[i]);
                    }

                    SoftEggLandDeathChestUtils.logDeathChest(deathChest.getLocation(), player.getName());

                    if(e.getDrops().size() >= 1) {
                        Block largeChestBlock = chestBlock.getRelative(1, 0, 0);
                        if(largeChestBlock.getType() == Material.AIR) {
                            largeChestBlock.setType(Material.CHEST);
                        }
                        BlockState chestBlockLargeState = largeChestBlock.getState();
                        if(chestBlockLargeState instanceof Chest) {
                            Chest deathChestLarge = (Chest) chestBlockLargeState;
                            for(int i = 0; i < e.getDrops().size(); i++) {
                                deathChestLarge.getBlockInventory().addItem(e.getDrops().get(i));
                            }
                            deathChestLarge.update();
                            ItemStack[] storedItemsLarge = deathChestLarge.getBlockInventory().getContents();
                            for(int i = 0; i < storedItemsLarge.length; i++) {
                                e.getDrops().remove(storedItemsLarge[i]);
                            }
                            e.getDrops().clear();
                            SoftEggLandDeathChestUtils.logDeathChest(deathChestLarge.getLocation(), player.getName());
                        }
                    }

                    BlockState signBlockState = signBlock.getState();
                    if(signBlockState instanceof Sign) {
                        Sign sign = (Sign) signBlockState;
                        sign.setLine(0, "§4Gravemarker");
                        sign.setLine(1, player.getName());
                        sign.setLine(2, "died here.");
                        sign.update();
                        SoftEggLandDeathChestUtils.logDeathSign(sign.getLocation(), player.getName());
                    }
                }
            }
        }
    }
}
