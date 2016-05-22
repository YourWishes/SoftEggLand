package com.minecraft.softegg.objects;

import com.minecraft.softegg.SoftEggLandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class SoftEggLandCraftItemEvent extends CraftItemEvent {
    
    private Player player;
    
    public SoftEggLandCraftItemEvent(Player player, Recipe recipe, InventoryView what, SlotType type, int slot, boolean right, boolean shift) {
        super(recipe, what, type, slot, right, shift);
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public boolean isValidRecipe() {
        if(this.getCurrentItem() == null || this.getCurrentItem().getType() == null || this.getCurrentItem().getType() == Material.AIR) {
            return false;
        }
        return true;
    }
    
    public ItemStack[] getCraftedItem() {
        ItemStack[] is = new ItemStack[1];
        is[0] = this.getCurrentItem();
        return is;
    }
    
    public ItemStack[] getCraftableItems() {
        if(!this.isValidRecipe()) {
            return null;
        }
        
        ItemStack[] contents = this.getInventory().getMatrix();
        
        int lowest = 0;
        
        for(ItemStack is : contents) {
            if(is == null || is.getType() == null) {
                continue;
            }

            if(is.getType() == Material.AIR) {
                continue;
            }
            if(is.getAmount() > lowest) {
                lowest = is.getAmount();
            }
        }
        
        for(ItemStack is : contents) {
            if(is == null || is.getType() == null) {
                continue;
            }
            
            if(is.getType() == Material.AIR) {
                continue;
            }
            
            if(is.getAmount() <= lowest) {
                lowest = is.getAmount();
            }
        }
        
        
        ItemStack[] items = new ItemStack[lowest];
        for(int i = 0; i < lowest; i++) {
            items[i] = this.getCurrentItem();
        }
        
        return items;
    }
    
    public ItemStack[] getCraftedItems() {
        
        ItemStack[] start = this.getCraftedItem();
        
        if(this.isShiftClick()) {
            start = this.getCraftableItems();
        }
        
        return start;
    }
    
}
