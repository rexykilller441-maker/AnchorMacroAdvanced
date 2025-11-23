package com.anchormacro.macro;

import com.anchormacro.AnchorMacroAdvanced;
import com.anchormacro.util.DelayUtils;
import com.anchormacro.util.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;

public class MacroExecutor {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void runMacro() {
        // Check if player and world exist
        if (mc.player == null || mc.world == null) {
            return;
        }

        // Execute based on mode
        switch (AnchorMacroAdvanced.config.mode) {
            case "normal" -> executeNormal();
            case "double" -> { 
                executeNormal(); 
                DelayUtils.sleepTicks(1); 
                executeNormal(); 
            }
            case "human" -> executeHuman();
        }
    }

    private static void executeNormal() {
        // Step 1: Switch to anchor slot and place anchor
        int anchorSlot = ItemUtils.getAnchorSlot();
        if (anchorSlot == -1) {
            mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No anchor found!"), false);
            return;
        }
        mc.player.getInventory().selectedSlot = anchorSlot;
        DelayUtils.sleepTicks(AnchorMacroAdvanced.config.actionDelay);
        ItemUtils.placeBlockAtCursor(Items.RESPAWN_ANCHOR);

        // Step 2: Switch to glowstone slot and charge anchor
        int glowSlot = ItemUtils.getGlowstoneSlot();
        if (glowSlot == -1) {
            mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No glowstone found!"), false);
            return;
        }
        mc.player.getInventory().selectedSlot = glowSlot;
        DelayUtils.sleepTicks(AnchorMacroAdvanced.config.actionDelay);
        ItemUtils.useItemOnBlock(Items.GLOWSTONE);

        // Step 3: If safe anchor is enabled, place glowstone in front and stop
        if (AnchorMacroAdvanced.config.safeAnchor) {
            ItemUtils.placeGlowstoneInFrontOfAnchor();
            return;
        }

        // Step 4: Switch to totem slot
        int totemSlot = ItemUtils.getTotemSlot();
        if (totemSlot == -1) {
            mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No totem found!"), false);
            return;
        }
        mc.player.getInventory().selectedSlot = totemSlot;
        DelayUtils.sleepTicks(AnchorMacroAdvanced.config.actionDelay);

        // Step 5: Detonate the anchor
        ItemUtils.useItemOnBlock(Items.RESPAWN_ANCHOR);
    }

    private static void executeHuman() {
        // Humanized delays, gradual look movement, small randomization
        executeNormal(); // Simplified, DelayUtils handles humanized ticks
    }
}
