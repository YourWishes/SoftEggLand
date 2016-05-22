package com.minecraft.softegg;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SoftEggLandDeathChestUtils extends SoftEggLandBase {
    public static long chestTimeout = 300000;
    
    public static void checkChests(World world) {
        String[] chests = getDeathChestList();
        boolean process = true;
        for(int i = 0; i < chests.length; i++) {
            if(process) {
                process = checkDeathChest(chests[i], world);
            }
        }
    }
    
    public static boolean checkDeathChest(String chest, World world) {
        Location loc = SoftEggLandUtils.getLocationString(chest, world);
        Block checkBlock = world.getBlockAt(loc);
        
        boolean UnloadChunk = false;
        
        if(!world.getChunkAt(loc).isLoaded()) {
            world.getChunkAt(loc).load();
            UnloadChunk = true;
        }
        if(checkBlock != null) {
            if(!isDeathChestValid(checkBlock)) {
                breakDeathChest(checkBlock, false);
                if(UnloadChunk) {
                    world.getChunkAt(loc).unload();
                }
                return false;
            }
            Block signBlock = checkBlock.getRelative(0, 0, 1);
            if(isDeathSign(signBlock)) {
                BlockState signBlockState = signBlock.getState();
                if(signBlockState instanceof Sign) {
                    Sign sign = (Sign) signBlockState;
                    String eq = String.valueOf(getDeathChestTimeLeft(checkBlock)/1000);
                    int left = Integer.parseInt(eq) + 1;
                    sign.setLine(3, left + " sec left.");
                    sign.update();
                }
            }
        }
        if(UnloadChunk) {
            world.getChunkAt(loc).unload();
        }
        return true;
    }
    
    public static void deleteDeathSign(Block signTarget) {
        try {
            File deathSign = new File(SoftEggLandUtils.getDataFolder() + "/deathsign.yml");
            YamlConfiguration deathSignYML = YamlConfiguration.loadConfiguration(deathSign);
            YamlConfiguration deathSignYMLFinal = new YamlConfiguration();
            Location location = signTarget.getLocation();
            String l = SoftEggLandUtils.getStringLocation(location);
            
            Set<String> keys = deathSignYML.getKeys(true);
            keys.remove(l);
            keys.remove(l+".time");
            keys.remove(l+".player");
            keys.remove(l+".chest");
            keys.remove(l+".location");
            keys.remove(l+".world");
            
            for(int x = 0; x < keys.size(); x++) {
                Object[] k = keys.toArray();
                String m = k[x].toString();
                deathSignYMLFinal.set(m, deathSignYML.get(m));
            }
            
            deathSignYMLFinal.save(deathSign);
        } catch(IOException e) {
            
        }
    }
    
    public static void deleteDeathChest(Block chest) {
        Block signTarget = chest.getRelative(0, 0, 1);
        if(isDeathSign(signTarget)) {
            deleteDeathSign(signTarget);
        }
        
        try {
            File deathChest = new File(SoftEggLandUtils.getDataFolder() + "/deathchest.yml");
            YamlConfiguration deathChestYML = YamlConfiguration.loadConfiguration(deathChest);
            YamlConfiguration deathChestYMLFinal = new YamlConfiguration();
            Location location = chest.getLocation();
            String l = SoftEggLandUtils.getStringLocation(location);
            
            Set<String> keys = deathChestYML.getKeys(true);
            keys.remove(l);
            keys.remove(l+".time");
            keys.remove(l+".player");
            keys.remove(l+".location");
            keys.remove(l+".world");
            
            for(int x = 0; x < keys.size(); x++) {
                Object[] k = keys.toArray();
                String m = k[x].toString();
                deathChestYMLFinal.set(m, deathChestYML.get(m));
            }
            
            deathChestYMLFinal.save(deathChest);
        } catch(IOException e) {
            
        }
        
        //getDeathChests();
    }
    
    public static Player getDeathChestOwner(Chest chest) {
        File deathChest = new File(SoftEggLandUtils.getDataFolder() + "/deathchest.yml");
        YamlConfiguration deathChestYML = YamlConfiguration.loadConfiguration(deathChest);
        Location location = chest.getLocation();
        String l = SoftEggLandUtils.getStringLocation(location);
        
        Object ymlChest = deathChestYML.get(l + ".player");
        
        String name = ymlChest.toString();
        
        if(Bukkit.getServer().getOfflinePlayer(name).isOnline()) {
            return Bukkit.getServer().getPlayerExact(name);
        }
        
        return null;
    }
    
    public static void breakDeathChest(Block b, boolean dropItems) {
        Block finalChest = b;
        if(b.getType() == Material.WALL_SIGN) {
            finalChest = getDeathChestFromSign(b);
        }
        if(finalChest.getType() == Material.CHEST) {
            BlockState finalChestState = finalChest.getState();
            Chest chest = (Chest) finalChestState;
            Inventory droppedItems = chest.getBlockInventory();
            
            Player chestOwner = getDeathChestOwner(chest);
            if(chestOwner != null) {
                chestOwner.sendMessage(ChatImportant + "Your death chest is gone!");
            }
            
            Location blockLoc = chest.getLocation();
            
            ItemStack[] contents = droppedItems.getContents();
            
            if(dropItems) {
                for(int is = 0; is < contents.length; is++) {
                    if(contents[is] != null) {
                        blockLoc.getWorld().dropItemNaturally(blockLoc, contents[is]);
                    }
                }
            }
            
            chest.getBlockInventory().clear();
            
            boolean deleteSign = false;
            
            Block signBlock = finalChest.getRelative(0, 0, 1);
            if(signBlock.getType() == Material.WALL_SIGN) {
                if(isDeathSign(signBlock)) {
                    deleteSign = true;
                }
            }
            deleteDeathChest(finalChest);
            if(deleteSign) {
                signBlock.setType(Material.AIR);
            }
            finalChest.setType(Material.AIR);
        }
    }
    
    public static String[] getDeathChestList() {
        File deathChest = new File(SoftEggLandUtils.getDataFolder() + "/deathchest.yml");
        YamlConfiguration deathChestYML = YamlConfiguration.loadConfiguration(deathChest);
        Set<String> keys = deathChestYML.getKeys(false);
        
        Object[] k = keys.toArray();
        String[] rv = new String[k.length];
        for(int x = 0; x < keys.size(); x++) {
            String m = k[x].toString();
            rv[x] = m;
        }
        return rv;
    }
    
    public static boolean isDeathChestValid(Block chest) {
        long diff = getDeathChestAliveTime(chest);
        if(diff >= chestTimeout) {
            return false;
        }
        return true;
    }
    
    public static long getDeathChestSpawnTime(Block block) {
        File deathChest = new File(SoftEggLandUtils.getDataFolder() + "/deathchest.yml");
        YamlConfiguration deathChestYML = YamlConfiguration.loadConfiguration(deathChest);
        Location location = block.getLocation();
        String l = SoftEggLandUtils.getStringLocation(location);
        
        String ymlChest = deathChestYML.getString(l + ".time");
        
        long time = Long.parseLong(ymlChest);
        return time;
    }
    
    public static long getDeathChestAliveTime(Block chest) {
        long now = SoftEggLandUtils.getNow();
        long time = getDeathChestSpawnTime(chest);
        long diff = now - time;
        return diff;
    }
    
    public static long getDeathChestTimeLeft(Block chest) {
        long diff = getDeathChestAliveTime(chest);
        return chestTimeout - diff;
    }
    
    public static boolean isDeathChest(Block block) {
        File deathChest = new File(SoftEggLandUtils.getDataFolder() + "/deathchest.yml");
        YamlConfiguration deathChestYML = YamlConfiguration.loadConfiguration(deathChest);
        Location location = block.getLocation();
        String l = SoftEggLandUtils.getStringLocation(location);
        
        Object ymlChest = deathChestYML.get(l);
        
        if(block.getType() != Material.CHEST) {
            return false;
        }
        
        if(ymlChest != null) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean isDeathSign(Block block) {
        File deathSign = new File(SoftEggLandUtils.getDataFolder() + "/deathsign.yml");
        YamlConfiguration deathSignYML = YamlConfiguration.loadConfiguration(deathSign);
        Location location = block.getLocation();
        String l = SoftEggLandUtils.getStringLocation(location);
        
        if(block.getType() != Material.WALL_SIGN) {
            return false;
        }
        
        Object ymlSign = deathSignYML.get(l);
        if(ymlSign != null) {
            return true;
        } else {
            return false;
        }
    }
    
    public static Block getDeathChestFromSign(Block b) {
        if(b.getType() == Material.CHEST) {
            return b;
        } else {
            File deathSign = new File(SoftEggLandUtils.getDataFolder() + "/deathsign.yml");
            YamlConfiguration deathSignYML = YamlConfiguration.loadConfiguration(deathSign);
            Location location = b.getLocation();
            String l = SoftEggLandUtils.getStringLocation(location);

            if(b == null) {
                return b;
            }
            
            if(b.getType() != Material.WALL_SIGN) {
                return b;
            }
            
            String ymlSign = deathSignYML.getString(l + ".chest");
            World world = Bukkit.getServer().getWorld(deathSignYML.getString(l + ".world"));
            Location loc = SoftEggLandUtils.getLocationString(ymlSign, world);
            
            Block t = world.getBlockAt(loc);
            if(t.getType() == Material.CHEST) {
                return t;
            }
        }
        return b;
    }
    
    public static void logDeathChest(Location location, String player) {
        try {
            String l = SoftEggLandUtils.getStringLocation(location);
            File deathChest = new File(SoftEggLandUtils.getDataFolder() + "/deathchest.yml");
            YamlConfiguration deathChestYML = YamlConfiguration.loadConfiguration(deathChest);
            HashMap<String, String> map;
            map = new HashMap<String, String>();
            
            map.put("player", player);
            map.put("location", l);
            map.put("time", Long.toString(SoftEggLandUtils.getNow()));
            map.put("world", location.getWorld().getName());
            
            deathChestYML.set(l, map);
            deathChestYML.save(deathChest);
            
        } catch (IOException ex) {
        }
    }
    
    public static void logDeathSign(Location location, String player) {
        try {
            String l = SoftEggLandUtils.getStringLocation(location);
            File deathSign = new File(SoftEggLandUtils.getDataFolder() + "/deathsign.yml");
            YamlConfiguration deathSignYML = YamlConfiguration.loadConfiguration(deathSign);
            HashMap<String, String> map;
            map = new HashMap<String, String>();
            
            String sb = SoftEggLandUtils.getStringLocation(location.getBlock().getRelative(0, 0, -1).getLocation());
            
            map.put("player", player);
            map.put("location", l);
            map.put("chest", sb);
            map.put("time", Long.toString(SoftEggLandUtils.getNow()));
            map.put("world", location.getWorld().getName());
            
            deathSignYML.set(l, map);
            deathSignYML.save(deathSign);
            
        } catch (IOException ex) {
        }
    }
    
    public static boolean isChestTypeSafe(Material type) {
        Material[] AcceptedTypes = {
            Material.AIR,
            Material.SNOW,
            Material.WATER,
            Material.WATER_LILY,
            Material.LAVA,
            Material.LONG_GRASS,
            Material.RED_ROSE,
            Material.YELLOW_FLOWER,
            Material.CROPS,
            Material.RED_MUSHROOM,
            Material.BROWN_MUSHROOM,
            Material.VINE,
            Material.POTATO,
            Material.CARROT,
            Material.SUGAR_CANE_BLOCK
        };
        
        for(int i = 0; i < AcceptedTypes.length; i++) {
            if(type == AcceptedTypes[i]) {
                return true;
            }
        }
        
        return false;
    }
}
