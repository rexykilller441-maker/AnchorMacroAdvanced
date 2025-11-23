package com.anchormacro.util;

import com.anchormacro.AnchorMacroAdvanced;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class ItemUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static int getAnchorSlot() {
        if (AnchorMacroAdvanced.config.autoSearch) {
            return findHotbarItem(Items.RESPAWN_ANCHOR);
        }
        return AnchorMacroAdvanced.config.anchorSlot - 1;
    }

    public static int getGlowstoneSlot() {
        if (AnchorMacroAdvanced.config.autoSearch) {
            return findHotbarItem(Items.GLOWSTONE);
        }
        return AnchorMacroAdvanced.config.glowstoneSlot - 1;
    }

    public static int getTotemSlot() {
        if (AnchorMacroAdvanced.config.autoSearch) {
            return findHotbarItem(Items.TOTEM_OF_UNDYING);
        }
        return AnchorMacroAdvanced.config.totemSlot - 1;
    }

    public static int findHotbarItem(Item item) {
        if (mc.player == null) return -1;
        
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static void placeBlockAtCursor(Item item) {
        if (mc.player == null || mc.world == null) return;
        
        // Use the player's crosshair target (what they're looking at)
        HitResult hitResult = mc.crosshairTarget;
        
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            
            // Send the interaction to the server so others can see it
            ActionResult result = mc.interactionManager.interactBlock(
                mc.player, 
                Hand.MAIN_HAND,
                blockHitResult
            );
            
            // Swing arm animation so others can see the action
            if (result.isAccepted()) {
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        } else {
            // If not looking at a block, try to place in front of player
            ActionResult result = mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            
            if (result.isAccepted()) {
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    public static void useItemOnBlock(Item item) {
        if (mc.player == null || mc.world == null) return;
        
        // Use the player's crosshair target (what they're looking at)
        HitResult hitResult = mc.crosshairTarget;
        
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            
            // Send the interaction to the server (charge or detonate anchor)
            ActionResult result = mc.interactionManager.interactBlock(
                mc.player, 
                Hand.MAIN_HAND,
                blockHitResult
            );
            
            // Swing arm animation so others can see the action
            if (result.isAccepted()) {
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    public static void placeGlowstoneInFrontOfAnchor() {
        if (mc.player == null || mc.world == null) return;
        
        // Get the block we're looking at (should be the anchor)
        HitResult hitResult = mc.crosshairTarget;
        
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            
            // Get the position of the clicked block (anchor)
            net.minecraft.util.math.BlockPos anchorPos = blockHitResult.getBlockPos();
            
            // Get the side we clicked on - place glowstone on that side
            net.minecraft.util.math.Direction side = blockHitResult.getSide();
            
            // Calculate where to place the glowstone block (on the clicked side of anchor)
            net.minecraft.util.math.BlockPos glowstonePos = anchorPos.offset(side);
            
            // Create a new hit result for the glowstone placement position
            BlockHitResult newHitResult = new BlockHitResult(
                blockHitResult.getPos(),
                side.getOpposite(), // Place against the anchor
                glowstonePos,
                false
            );
            
            // Place the glowstone block
            ActionResult result = mc.interactionManager.interactBlock(
                mc.player, 
                Hand.MAIN_HAND,
                newHitResult
            );
            
            if (result.isAccepted()) {
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }
}
