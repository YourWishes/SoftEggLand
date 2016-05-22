package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandMMOUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import com.minecraft.softegg.objects.SoftEggLandCraftItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class SoftEggLandMMOListener extends SoftEggLandBase implements Listener {
    
    public static Map<Material, Integer> ores = new HashMap<Material, Integer>();
    public static Map<Material, Integer> crops = new HashMap<Material, Integer>();
    public static Map<Material, Integer> logs = new HashMap<Material, Integer>();
    public static Map<Material, Integer> food = new HashMap<Material, Integer>();
    public static Map<Material, Integer> smithingOres = new HashMap<Material, Integer>();
    public static List<Material> swords = new ArrayList<Material>();
    public static List<Material> axes = new ArrayList<Material>();
    
    public static Map<OfflinePlayer, Integer> adventurerMoveExp = new HashMap<OfflinePlayer, Integer>();
    public static Map<OfflinePlayer, Integer> fishingMoveExp = new HashMap<OfflinePlayer, Integer>();
    public static Map<OfflinePlayer, Integer> miningMoveExp = new HashMap<OfflinePlayer, Integer>();
    public static HashMap<Player, Location> moveRadius = new HashMap<Player, Location>();
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public BukkitTask savePlayers;
    public BukkitTask checkSkills;
    
    /* Basic Constructor */
    public SoftEggLandMMOListener(SoftEggLand base) {
        plugin = base;
        
        adventurerMoveExp.clear();
        fishingMoveExp.clear();
        miningMoveExp.clear();
        for(Player p : Bukkit.getOnlinePlayers()) {
            SoftEggLandMMOUtils.CreatePlayerFile(p);
            adventurerMoveExp.put(p, 0);
            fishingMoveExp.put(p, 0);
            miningMoveExp.put(p, 0);
        }
        
        ores.put(Material.COAL_ORE, 1);
        ores.put(Material.REDSTONE_ORE, 2);
        ores.put(Material.GLOWING_REDSTONE_ORE, 2);
        ores.put(Material.LAPIS_ORE, 3);
        ores.put(Material.IRON_ORE, 5);
        ores.put(Material.GOLD_ORE, 10);
        ores.put(Material.DIAMOND_ORE, 15);
        
        crops.put(Material.CROPS, 1);
        crops.put(Material.POTATO, 2);
        crops.put(Material.CARROT, 3);
        crops.put(Material.MELON_BLOCK, 5);
        crops.put(Material.PUMPKIN, 5);
        crops.put(Material.SUGAR_CANE_BLOCK, 2);
        crops.put(Material.CACTUS, 1);
        crops.put(Material.NETHER_WARTS, 3);
        crops.put(Material.COCOA, 4);
        
        logs.put(Material.LOG, 5);
        logs.put(Material.LEAVES, 1);
        logs.put(Material.VINE, 1);
        
        food.put(Material.CAKE, 10);
        food.put(Material.BREAD, 3);
        food.put(Material.COOKED_BEEF, 2);
        food.put(Material.COOKED_CHICKEN, 2);
        food.put(Material.COOKED_FISH, 2);
        food.put(Material.PORK, 2);
        food.put(Material.RAW_BEEF, 1);
        food.put(Material.RAW_CHICKEN, 1);
        food.put(Material.RAW_FISH, 1);
        food.put(Material.CAKE_BLOCK, 1);
        food.put(Material.POTATO, 2);
        food.put(Material.CARROT, 3);
        food.put(Material.MELON, 5);
        food.put(Material.BAKED_POTATO, 3);
        food.put(Material.COOKIE, 1);
        food.put(Material.GOLDEN_CARROT, 4);
        food.put(Material.GOLDEN_APPLE, 5);
        food.put(Material.APPLE, 5);
        food.put(Material.PUMPKIN_PIE, 5);
        food.put(Material.SUGAR, 1);
        food.put(Material.MUSHROOM_SOUP, 3);
        food.put(Material.GRILLED_PORK, 3);
        
        smithingOres.put(Material.DIAMOND, 5);
        smithingOres.put(Material.GOLD_INGOT, 2);
        smithingOres.put(Material.IRON_INGOT, 2);
        smithingOres.put(Material.COAL, 1);
        smithingOres.put(Material.EMERALD, 5);
        smithingOres.put(Material.GLASS, 1);
        smithingOres.put(Material.STONE, 1);
        smithingOres.put(Material.CLAY_BRICK, 1);
        smithingOres.put(Material.NETHER_BRICK_ITEM, 1);
        smithingOres.put(Material.REDSTONE, 1);
        smithingOres.put(Material.QUARTZ, 1);
        
        smithingOres.put(Material.DIAMOND_AXE, 20);
        smithingOres.put(Material.DIAMOND_BOOTS, 15);
        smithingOres.put(Material.DIAMOND_CHESTPLATE, 30);
        smithingOres.put(Material.DIAMOND_HELMET, 15);
        smithingOres.put(Material.DIAMOND_HOE, 12);
        smithingOres.put(Material.DIAMOND_LEGGINGS, 25);
        smithingOres.put(Material.DIAMOND_PICKAXE, 20);
        smithingOres.put(Material.DIAMOND_SPADE, 10);
        smithingOres.put(Material.DIAMOND_SWORD, 12);
        
        smithingOres.put(Material.GOLD_AXE, 10);
        smithingOres.put(Material.GOLD_BOOTS, 7);
        smithingOres.put(Material.GOLD_CHESTPLATE, 15);
        smithingOres.put(Material.GOLD_HELMET, 7);
        smithingOres.put(Material.GOLD_HOE, 6);
        smithingOres.put(Material.GOLD_LEGGINGS, 12);
        smithingOres.put(Material.GOLD_PICKAXE, 10);
        smithingOres.put(Material.GOLD_SPADE, 5);
        smithingOres.put(Material.GOLD_SWORD, 6);
        
        smithingOres.put(Material.IRON_AXE, 10);
        smithingOres.put(Material.IRON_BOOTS, 7);
        smithingOres.put(Material.IRON_CHESTPLATE, 15);
        smithingOres.put(Material.IRON_HELMET, 7);
        smithingOres.put(Material.IRON_HOE, 6);
        smithingOres.put(Material.IRON_LEGGINGS, 12);
        smithingOres.put(Material.IRON_PICKAXE, 10);
        smithingOres.put(Material.IRON_SPADE, 5);
        smithingOres.put(Material.IRON_SWORD, 6);
        
        smithingOres.put(Material.STONE_AXE, 5);
        smithingOres.put(Material.CHAINMAIL_BOOTS, 4);
        smithingOres.put(Material.CHAINMAIL_CHESTPLATE, 7);
        smithingOres.put(Material.CHAINMAIL_HELMET, 4);
        smithingOres.put(Material.STONE_HOE, 3);
        smithingOres.put(Material.CHAINMAIL_LEGGINGS, 6);
        smithingOres.put(Material.STONE_PICKAXE, 5);
        smithingOres.put(Material.STONE_SPADE, 2);
        smithingOres.put(Material.STONE_SWORD, 3);
        
        smithingOres.put(Material.LEATHER_BOOTS, 2);
        smithingOres.put(Material.LEATHER_CHESTPLATE, 4);
        smithingOres.put(Material.LEATHER_HELMET, 2);
        smithingOres.put(Material.LEATHER_LEGGINGS, 3);
        
        axes.add(Material.DIAMOND_AXE);
        axes.add(Material.GOLD_AXE);
        axes.add(Material.IRON_AXE);
        axes.add(Material.STONE_AXE);
        axes.add(Material.WOOD_AXE);
        
        savePlayers = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                for(OfflinePlayer op : adventurerMoveExp.keySet()) {
                    if(adventurerMoveExp.get(op) != 0) {
                        SoftEggLandMMOUtils.addExp(op, "Adventuring", adventurerMoveExp.get(op));
                    }
                }
                for(OfflinePlayer op : fishingMoveExp.keySet()) {
                    if(fishingMoveExp.get(op) != 0) {
                        SoftEggLandMMOUtils.addExp(op, "Fishing", fishingMoveExp.get(op));
                    }
                }
                for(OfflinePlayer op : miningMoveExp.keySet()) {
                    if(miningMoveExp.get(op) != 0) {
                        SoftEggLandMMOUtils.addExp(op, "Mining", miningMoveExp.get(op));
                    }
                }
                
                adventurerMoveExp.clear();
                fishingMoveExp.clear();
                miningMoveExp.clear();
                for(Player p : Bukkit.getOnlinePlayers()) {
                    adventurerMoveExp.put(p, 0);
                    fishingMoveExp.put(p, 0);
                    miningMoveExp.put(p, 0);
                }
            }
        }, 60L, 120L);
        
        checkSkills = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                SoftEggLandMMOUtils.checkSkills();
            }
        }, 60L, 30L);
        
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        SoftEggLandMMOUtils.CreatePlayerFile(e.getPlayer());
        if(!adventurerMoveExp.containsKey(e.getPlayer())) {
            adventurerMoveExp.put(e.getPlayer(), 0);
        }
        if(!fishingMoveExp.containsKey(e.getPlayer())) {
            fishingMoveExp.put(e.getPlayer(), 0);
        }
        if(!miningMoveExp.containsKey(e.getPlayer())) {
            miningMoveExp.put(e.getPlayer(), 0);
        }
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent e) {
        if(!SoftEggLandMMOUtils.isMMOWorld(e.getPlayer().getLocation())) {
            return;
        }
        
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        if(e.getBlockPlaced() == null) {
            return;
        }
        
        if(e.getBlockPlaced().getType() == null) {
            return;
        }
        
        Block b = e.getBlockPlaced();
        
        Set<Material> crop = crops.keySet();
        for(Material c : crop) {
            if(b.getType() != c) {
                continue;
            }
            
            boolean found = false;
            for(int x = -1; x <= 1; x++) {
                for(int z = -1; z <= 1; z++) {
                    if(b.getRelative(x, 0, z).getType() == c) {
                        found = true;
                    }
                }
            }
            if(found) {
                int amount = crops.get(c);
                SoftEggLandMMOUtils.addExp(e.getPlayer(), "Farming", amount);
            }
        }
        
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent e) {
        if(!SoftEggLandMMOUtils.isMMOWorld(e.getPlayer().getLocation())) {
            return;
        }
        
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        if(e.getBlock() == null) {
            return;
        }
        
        if(e.getBlock().getType() == null) {
            return;
        }
        
        /* Mining Skill Check */
        Set<Material> ore = ores.keySet();
        for(Material m : ore) {
            Material bm = e.getBlock().getType();
            
            ItemStack is = e.getPlayer().getItemInHand();
            if(is == null) {
                continue;
            }

            if(is.getType() == null) {
                return;
            }
            
            if(is.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                break;
            }
            
            if(bm != m) {
                continue;
            }
            
            SoftEggLandMMOUtils.addExp(e.getPlayer(), "Mining", ores.get(m));
            return;
        }
        
        /* Farming Skill Check */
        Set<Material> crop = crops.keySet();
        for(Material c : crop) {
            Material bm = e.getBlock().getType();
            ItemStack is = e.getPlayer().getItemInHand();
            if(bm != c) {
                continue;
            }
            
            if(c == Material.CROPS || c == Material.POTATO || c == Material.CARROT) {
                if(e.getBlock().getData() != 7) {
                    return;
                }
            }
            
            if(c == Material.CACTUS || c == Material.SUGAR_CANE_BLOCK) {
                boolean hasNeighbour = false;
                
                if(c == Material.SUGAR_CANE_BLOCK) {
                    if(e.getBlock().getRelative(0, 1, 0).getType() == Material.SUGAR_CANE_BLOCK) {
                        hasNeighbour = true;
                    } else if(e.getBlock().getRelative(0, -1, 0).getType() == Material.SUGAR_CANE_BLOCK) {
                        hasNeighbour = true;
                    }
                } else if(c == Material.CACTUS) {
                    if(e.getBlock().getRelative(0, 1, 0).getType() == Material.CACTUS) {
                        hasNeighbour = true;
                    } else if(e.getBlock().getRelative(0, -1, 0).getType() == Material.CACTUS) {
                        hasNeighbour = true;
                    }
                }
                
                if(!hasNeighbour) {
                    return;
                }
                
            }
            
            if(c == Material.PUMPKIN || c == Material.MELON_BLOCK) {
                boolean hasNeighbour = false;
                if(c == Material.MELON_BLOCK) {
                    if(e.getBlock().getRelative(1, 0, 0).getType() == Material.MELON_STEM) {
                        if(e.getBlock().getRelative(1, 0, 0).getData() == 7) {
                            hasNeighbour = true;
                        }
                    } else if(e.getBlock().getRelative(-1, 0, 0).getType() == Material.MELON_STEM) {
                        if(e.getBlock().getRelative(-1, 0, 0).getData() == 7) {
                            hasNeighbour = true;
                        }
                    } else if(e.getBlock().getRelative(0, 0, 1).getType() == Material.MELON_STEM) {
                        if(e.getBlock().getRelative(0, 0, 1).getData() == 7) {
                            hasNeighbour = true;
                        }
                    } else if(e.getBlock().getRelative(0, 0, -1).getType() == Material.MELON_STEM) {
                        if(e.getBlock().getRelative(0, 0, -1).getData() == 7) {
                            hasNeighbour = true;
                        }
                    }
                } else if(c == Material.PUMPKIN) {
                    if(e.getBlock().getRelative(1, 0, 0).getType() == Material.PUMPKIN_STEM) {
                        if(e.getBlock().getRelative(1, 0, 0).getData() == 7) {
                            hasNeighbour = true;
                        }
                    } else if(e.getBlock().getRelative(-1, 0, 0).getType() == Material.PUMPKIN_STEM) {
                        if(e.getBlock().getRelative(-1, 0, 0).getData() == 7) {
                            hasNeighbour = true;
                        }
                    } else if(e.getBlock().getRelative(0, 0, 1).getType() == Material.PUMPKIN_STEM) {
                        if(e.getBlock().getRelative(0, 0, 1).getData() == 7) {
                            hasNeighbour = true;
                        }
                    } else if(e.getBlock().getRelative(0, 0, -1).getType() == Material.PUMPKIN_STEM) {
                        if(e.getBlock().getRelative(1, 0, -1).getData() == 7) {
                            hasNeighbour = true;
                        }
                    }
                }
                
                if(!hasNeighbour) {
                    return;
                }
            }
            SoftEggLandMMOUtils.addExp(e.getPlayer(), "Farming", crops.get(c));
            return;
        }
        
        Set<Material> log = logs.keySet();
        for(Material m : log) {
            if(m != e.getBlock().getType()) {
                continue;
            }
            
            ItemStack is = e.getPlayer().getItemInHand();
            if(is == null) {
                continue;
            }

            if(is.getType() == null) {
                return;
            }
            
            for(Material axe : axes) {
                if(is.getType() != axe) {
                    continue;
                }
                SoftEggLandMMOUtils.addExp(e.getPlayer(), "Woodcutting", logs.get(m));
                return;
            }
        }
        
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPlayerMove(PlayerMoveEvent e) {
        if(!SoftEggLandMMOUtils.isMMOWorld(e.getPlayer().getLocation())) {
            return;
        }
        
        if(e.getPlayer().isFlying()) {
            return;
        }
        
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        Location l = e.getPlayer().getLocation();
        int radius1 = 2;
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Location loc2 = null;
        int x2 = 0;
        int y2 = 0;
        int z2 = 0;
        try{
            loc2 = moveRadius.get(e.getPlayer());
            x2 = loc2.getBlockX() + radius1;
            y2 = loc2.getBlockY() + radius1;
            z2 = loc2.getBlockZ() + radius1;
        }catch(Exception er) {
            moveRadius.put(e.getPlayer(), l);
        }
        if(loc2 != null){
            if(x >= x2 || y >= y2 || z >= z2) {
                
                /* Add Exp */
                if(e.getPlayer().getVehicle() == null || e.getPlayer().getVehicle().getType() == null) {
                    if(e.getPlayer().getLocation().getY() > 20) {
                        int oldExp = adventurerMoveExp.get(e.getPlayer());
                        adventurerMoveExp.remove(e.getPlayer());
                        adventurerMoveExp.put(e.getPlayer(), oldExp + 1);
                    } else {
                        int oldExp = miningMoveExp.get(e.getPlayer());
                        miningMoveExp.remove(e.getPlayer());
                        miningMoveExp.put(e.getPlayer(), oldExp + 1);
                    }
                } else if(e.getPlayer().getVehicle().getType() == EntityType.BOAT) {
                    int oldExp = fishingMoveExp.get(e.getPlayer());
                    fishingMoveExp.remove(e.getPlayer());
                    fishingMoveExp.put(e.getPlayer(), oldExp + 1);
                } else if(SoftEggLandUtils.isTypeMinecart(e.getPlayer().getVehicle().getType())) {
                    int oldExp = miningMoveExp.get(e.getPlayer());
                    miningMoveExp.remove(e.getPlayer());
                    miningMoveExp.put(e.getPlayer(), oldExp + 1);
                }
                
                /* Reset Move Radius */
                moveRadius.put(e.getPlayer(), l);
            }
        }
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onFish(PlayerFishEvent e) {
        if(e.getCaught() == null) {
            return;
        }
        
        if(!SoftEggLandMMOUtils.isMMOWorld(e.getPlayer().getLocation())) {
            return;
        }
        
        if(e.getPlayer().isFlying()) {
            return;
        }
        
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        Entity caught = e.getCaught();
        if(caught.getType() == EntityType.DROPPED_ITEM) {
            SoftEggLandMMOUtils.addExp(e.getPlayer(), "Fishing", 3);
        } else {
            SoftEggLandMMOUtils.addExp(e.getPlayer(), "Fishing", 1);
        }
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onCraft(SoftEggLandCraftItemEvent e) {
        if(e.getPlayer() == null) {
            return;
        }
        
        if(!SoftEggLandMMOUtils.isMMOWorld(e.getPlayer().getLocation())) {
            return;
        }
        if(e.getPlayer().isFlying()) {
            return;
        }
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        ItemStack[] craftedItems = e.getCraftedItems();
        if(craftedItems == null) {
            return;
        }
        if(craftedItems.length < 1) {
            return;
        }
        
        Material itemType = craftedItems[0].getType();
        if(e.getCursor() != null && e.getCursor().getType() != null  && e.getCursor().getType() != itemType && e.getCursor().getType() != Material.AIR) {
            return;
        }
        
        for(Material m : food.keySet()) {
            if(itemType != m) {
                continue;
            }
            
            int amount = food.get(m);
            amount = amount * craftedItems.length;
            amount = amount * craftedItems[0].getAmount();
            
            SoftEggLandMMOUtils.addExp(e.getPlayer(), "Cooking", amount);
            return;
        }
        
        for(Material m : smithingOres.keySet()) {
            if(itemType != m) {
                continue;
            }
            
            int amount = smithingOres.get(m);
            amount = amount * craftedItems.length;
            amount = amount * craftedItems[0].getAmount();
            
            SoftEggLandMMOUtils.addExp(e.getPlayer(), "Smithing", amount);
            return;
        }
        
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onFurnaceExtract(FurnaceExtractEvent e) {
        if(!SoftEggLandMMOUtils.isMMOWorld(e.getPlayer().getLocation())) {
            return;
        }
        if(e.getPlayer().isFlying()) {
            return;
        }
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        if(e.getItemType() == Material.AIR) {
            return;
        }
        
        int actualAmount = e.getItemAmount();
        
        for(Material m : food.keySet()) {
            if(e.getItemType() != m) {
                continue;
            }
            
            int amount = food.get(m);
            amount = amount * actualAmount;
            
            SoftEggLandMMOUtils.addExp(e.getPlayer(), "Cooking", amount);
            return;
        }
        
        for(Material m : smithingOres.keySet()) {
            if(e.getItemType() != m) {
                continue;
            }
            
            int amount = smithingOres.get(m);
            amount = amount * actualAmount;
            
            SoftEggLandMMOUtils.addExp(e.getPlayer(), "Smithing", amount);
            return;
        }
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onBowShoot(EntityShootBowEvent e) {
        if(e.getEntity() == null) {
            return;
        }
        
        if(e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        
        Player player = (Player) e.getEntity();
        
        if(!SoftEggLandMMOUtils.isMMOWorld(player.getLocation())) {
            return;
        }
        
        if(player.isFlying()) {
            return;
        }
        
        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        float force = e.getForce();
        
        float xp = force * 10f;
        
        SoftEggLandMMOUtils.addExp(player, "Archery", (int) Math.round(xp));
        return;
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPlayerDamaged(EntityDamageEvent e) {
        if(e.getEntity() == null) {
            return;
        }
        
        if(e.getEntityType() == null) {
            return;
        }
        
        if(e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        
        Player player = (Player) e.getEntity();
        
        if(!SoftEggLandMMOUtils.isMMOWorld(player.getLocation())) {
            return;
        }
        
        if(player.isFlying()) {
            return;
        }
        
        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        int amount = (int) e.getDamage();
        
        SoftEggLandMMOUtils.addExp(player, "Strength", amount);
        
        if(player.isBlocking()) {
            SoftEggLandMMOUtils.addExp(player, "Defence", amount);
        }
        
        return;
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() == null) {
            return;
        }
        
        if(e.getDamager().getType() == null) {
            return;
        }
        
        if(e.getDamager().getType() != EntityType.PLAYER) {
            return;
        }
        
        Player player = (Player) e.getDamager();
        
        if(!SoftEggLandMMOUtils.isMMOWorld(player.getLocation())) {
            return;
        }
        
        if(player.isFlying()) {
            return;
        }
        
        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        int amount = (int) (e.getDamage() / 2);
        
        SoftEggLandMMOUtils.addExp(player, "Attack", amount);
        return;
    }
    
    
    @EventHandler(ignoreCancelled=true)
    public void onEnchant(EnchantItemEvent e) {
        if(e.getEnchanter() == null) {
            return;
        }
        
        Player player = e.getEnchanter();
        
        if(!SoftEggLandMMOUtils.isMMOWorld(player.getLocation())) {
            return;
        }
        
        if(player.isFlying()) {
            return;
        }
        
        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        int multiplier = e.getExpLevelCost();
        
        SoftEggLandMMOUtils.addExp(player, "Enchanting", multiplier);
        return;
    }
}
