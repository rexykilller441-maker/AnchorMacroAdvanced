package com.anchormacro.util;

import com.anchormacro.AnchorMacroAdvanced;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

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
            if (mc.player.inventory.getStack(i).getItem() == item) return i;
        }
        return -1;
    }

    public static void placeBlockAtCursor(Item item) {
        // Simplified, normally send right-click packet / use client interaction
        mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND,
                new BlockHitResult(mc.player.getPos(), mc.player.getHorizontalFacing(), new BlockPos(mc.player.getPos()), false));
    }

    public static void useItemOnBlock(Item item) {
        placeBlockAtCursor(item);
    }

    public static void placeGlowstoneInFrontOfAnchor() {
        // TODO: Implement raycast to front block, place glowstone there
        useItemOnBlock(Items.GLOWSTONE);
    }
}
