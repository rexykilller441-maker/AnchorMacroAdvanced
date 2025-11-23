package com.anchormacro.util;

import com.anchormacro.AnchorMacroAdvanced;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class ItemUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static int getAnchorSlot() {
        if (AnchorMacroAdvanced.config.autoSearch) return findHotbarItem(Items.RESPAWN_ANCHOR);
        return AnchorMacroAdvanced.config.anchorSlot - 1;
    }

    public static int getGlowstoneSlot() {
        if (AnchorMacroAdvanced.config.autoSearch) return findHotbarItem(Items.GLOWSTONE);
        return AnchorMacroAdvanced.config.glowstoneSlot - 1;
    }

    public static int getTotemSlot() {
        if (AnchorMacroAdvanced.config.autoSearch) return findHotbarItem(Items.TOTEM_OF_UNDYING);
        return AnchorMacroAdvanced.config.totemSlot - 1;
    }

    public static int findHotbarItem(Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) return i;
        }
        return -1;
    }

    public static void placeBlockAtCursor(Item item) {
        // Use the player's crosshair target (what they're looking at)
        HitResult hitResult = mc.crosshairTarget;
        
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            
            // Right-click/interact with the block where player is looking
            mc.interactionManager.interactBlock(
                mc.player, 
                Hand.MAIN_HAND,
                blockHitResult
            );
        } else {
            // If not looking at a block, try to place in front of player
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        }
    }

    public static void useItemOnBlock(Item item) {
        // Use the player's crosshair target (what they're looking at)
        HitResult hitResult = mc.crosshairTarget;
        
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            
            // Right-click/interact with the block (charge or detonate anchor)
            mc.interactionManager.interactBlock(
                mc.player, 
                Hand.MAIN_HAND,
                blockHitResult
            );
        }
    }

    public static void placeGlowstoneInFrontOfAnchor() {
        // Place glowstone block in front of the anchor (acts as protection)
        useItemOnBlock(Items.GLOWSTONE);
    }
}
