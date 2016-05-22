package com.minecraft.softegg.listeners;

import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import com.griefcraft.scripting.event.LWCProtectionRegisterEvent;
import com.griefcraft.scripting.event.LWCProtectionRegistrationPostEvent;
import com.griefcraft.util.matchers.DoubleChestMatcher;
import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandMMOUtils;
import com.minecraft.softegg.SoftEggLandUtils;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class SoftEggLandAntiBuildListener extends SoftEggLandBase implements Listener {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public BukkitTask checkChests;
    
    /* Basic Constructor */
    public SoftEggLandAntiBuildListener(SoftEggLand base) {
        plugin = base;
    }
    
    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent e) {
        if(e.getVehicle() != null && e.getVehicle().getType() != null) {
            if(e.getVehicle().getType() == EntityType.MINECART_HOPPER) {
                Location loc = e.getVehicle().getLocation();
                ItemStack is = new ItemStack(Material.HOPPER_MINECART, 1);
                loc.getWorld().dropItem(loc, is);
                e.getVehicle().remove();
            }
            if(e.getVehicle().getType() == EntityType.MINECART_TNT) {
                Location loc = e.getVehicle().getLocation();
                e.getVehicle().remove();
            }
        }
    }
    
    @EventHandler
    public void onLavaPlace(PlayerInteractEvent e) {
        /* Handles Anti-Lava'ing Methods */
        if(e.getMaterial() == null) {
            return;
        }
        Material type = e.getMaterial();
        if(type == null) {
            return;
        }
        if(SoftEggLandMMOUtils.isMMOWorld(e.getPlayer().getWorld())) {
            return;
        }
        
        if(type.equals(Material.LAVA) || type.equals(Material.LAVA_BUCKET) || type.equals(Material.FLINT_AND_STEEL) || type.equals(Material.FIRE)) {
            List<Entity> entities = e.getPlayer().getNearbyEntities(15, 10, 10);
            boolean foundPlayers = false;
            for(int i = 0; i < entities.size(); i++) {
                Entity ent = entities.get(i);
                if(ent != null && ent.getType() != null) {
                    if(ent.getType() == EntityType.PLAYER) {
                        foundPlayers = true;
                        break;
                    }
                }
            }
            if(foundPlayers && !e.getPlayer().hasPermission("SoftEggLand.uselava")) {
                e.getPlayer().sendMessage(ChatError + "You cannot use this while players are nearby!");
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onHopperPlace(BlockPlaceEvent event) {
        
        /* Handles AntiHopper Cheating */
        if(event.getBlock() == null) {
            return;
        }
        
        if(SoftEggLandMMOUtils.isMMOWorld(event.getPlayer().getWorld())) {
            return;
        }
        
        if(event.getBlock().getType() == null) {
            return;
        }
        
        boolean isChecked = false;
        for(int x = 0; x < HopperBlocks.length; x++) {
            if(event.getBlock().getType() == HopperBlocks[x]) {
                isChecked = true;
                break;
            }
        }
        
        if(!isChecked) {
            return;
        }
        
        Block target = event.getBlock();
        
        /* Search nearby blocks for Chests, Hoppers */
        
        int startX = target.getLocation().getBlockX() - 1;
        int startY = target.getLocation().getBlockY() - 1;
        int startZ = target.getLocation().getBlockZ() - 1;
        
        int endX = target.getLocation().getBlockX() + 1;
        int endY = target.getLocation().getBlockY() + 1;
        int endZ = target.getLocation().getBlockZ() + 1;
        
        LWC lwc = SoftEggLandUtils.getLWC().getLWC();
        
        for(int x = startX; x <= endX; x++) {
            for(int y = startY; y <= endY; y++) {
                for(int z = startZ; z <= endZ; z++) {
                    Block checkBlock = target.getWorld().getBlockAt(x, y, z);
                    
                    if(checkBlock == null) {
                        continue;
                    }
                    
                    /* Checks if block is a disabled block */
                    for(int b = 0; b < HopperBlocks.length; b++) {
                        if(checkBlock.getType() == null) {
                            continue;
                        }
                        if(checkBlock.getType() == HopperBlocks[b]) {
                            /* Block is "Hopper Blocked" */
                            
                            if(lwc.findProtection(checkBlock) == null) {
                                continue;
                            }
                            
                            String Owner = lwc.findProtection(checkBlock).getOwner();
                            
                            if(!Owner.equalsIgnoreCase(event.getPlayer().getName())) {
                                event.getPlayer().sendMessage(
                                        ChatError + 
                                        "You cannot place a " + 
                                        ChatImportant + 
                                        event.getBlock().getType().name() + 
                                        ChatDefault + 
                                        " here, a locked block is nearby."
                                );
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }                    
                }
            }
        }
        
        /* Register Hopper */
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        // Update the cache if a protection is matched here
        Protection current = lwc.findProtection(block);
        if (current != null && current.getProtectionFinder() != null) {
            current.getProtectionFinder().fullMatchBlocks();
            lwc.getProtectionCache().add(current);
        }

        String autoRegisterType = "private";

        if (!lwc.hasPermission(player, "lwc.create." + autoRegisterType, "lwc.create", "lwc.protect")) {
            return;
        }

        // Parse the type
        Protection.Type type;

        type = Protection.Type.valueOf(autoRegisterType.toUpperCase());

        // If it's a chest, make sure they aren't placing it beside an already registered chest
        if (DoubleChestMatcher.PROTECTABLES_CHESTS.contains(block.getType())) {
            BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

            for (BlockFace blockFace : faces) {
                Block face = block.getRelative(blockFace);

                //They're placing it beside a chest, check if it's already protected
                if (face.getType() == block.getType()) {
                    if (lwc.findProtection(face) != null) {
                        return;
                    }
                }
            }
        }
        
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        try {
            LWCProtectionRegisterEvent evt = new LWCProtectionRegisterEvent(player, block);
            lwc.getModuleLoader().dispatchEvent(evt);
            
            Protection protection = lwc.getPhysicalDatabase().registerProtection(block.getTypeId(), type, block.getWorld().getName(), player.getName(), "", block.getX(), block.getY(), block.getZ());

            if (protection != null) {
                lwc.getModuleLoader().dispatchEvent(new LWCProtectionRegistrationPostEvent(protection));
            }
        } catch (Exception e) {
            lwc.sendLocale(player, "protection.internalerror", "id", "PLAYER_INTERACT");
            e.printStackTrace();
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType() != Material.AIR) {
            /* Checks to see if they're unhappy blocks */
            ItemStack handItem = e.getPlayer().getItemInHand();
            
            /* if(handItem.getType() == Material.HOPPER) {
                e.getPlayer().sendMessage(ChatError + "Hoppers are disabled temporarily.");
                e.setCancelled(true);
            } else */
                
            if(handItem.getType() == Material.HOPPER_MINECART) {
                e.getPlayer().sendMessage(ChatError + "Hopper carts are disabled temporarily.");
                e.setCancelled(true);
            }
            
            if(handItem.getType() == Material.BED && SoftEggLandUtils.isEndWorld(e.getPlayer().getWorld())) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onSignPlace(SignChangeEvent e) {
        String[] lines = e.getLines();
        for(int i = 0; i < lines.length; i++) {
            String l = SoftEggLandUtils.FormatString(lines[i]);
            e.setLine(i, l);
        }
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onTeleport(PlayerTeleportEvent e) {
        if(!e.getPlayer().isInsideVehicle()) {
            return;
        }
        
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEnterVehicle(VehicleEnterEvent e) {
        if(e.getEntered() == null) {
            return;
        }
        
        if(e.getVehicle() == null) {
            return;
        }
        
        Vehicle horse = e.getVehicle();
        Entity player = e.getEntered();
        
        if(player.getType() != EntityType.PLAYER) {
            return;
        }
        
        Player p = (Player) player;
        if(p.getPassenger() != null) {
            e.setCancelled(true);
            p.sendMessage(ChatError + "Cannot ride this, you have someone riding you.");
        }
    }
}
