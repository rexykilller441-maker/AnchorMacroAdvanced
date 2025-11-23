package com.anchormacro.macro;

import com.anchormacro.AnchorMacroAdvanced;
import com.anchormacro.util.DelayUtils;
import com.anchormacro.util.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;

public class MacroExecutor {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void runMacro() {
        if (!AnchorMacroAdvanced.config.enabled || mc.player == null || mc.world == null) return;

        switch (AnchorMacroAdvanced.config.mode) {
            case "normal" -> executeNormal();
            case "double" -> { executeNormal(); DelayUtils.sleepTicks(1); executeNormal(); }
            case "human" -> executeHuman();
        }
    }

    private static void executeNormal() {
        // Step 1: Anchor
        int anchorSlot = ItemUtils.getAnchorSlot();
        if (anchorSlot == -1) return;
        mc.player.getInventory().selectedSlot = anchorSlot;
        DelayUtils.sleepTicks(AnchorMacroAdvanced.config.actionDelay);
        ItemUtils.placeBlockAtCursor(Items.RESPAWN_ANCHOR);

        // Step 2: Charge
        int glowSlot = ItemUtils.getGlowstoneSlot();
        if (glowSlot == -1) return;
        mc.player.getInventory().selectedSlot = glowSlot;
        DelayUtils.sleepTicks(AnchorMacroAdvanced.config.actionDelay);
        ItemUtils.useItemOnBlock(Items.GLOWSTONE);

        // Step 3: Safe Anchor?
        if (AnchorMacroAdvanced.config.safeAnchor) {
            ItemUtils.placeGlowstoneInFrontOfAnchor();
            return;
        }

        // Step 4: Totem slot
        int totemSlot = ItemUtils.getTotemSlot();
        if (totemSlot == -1) return;
        mc.player.getInventory().selectedSlot = totemSlot;
        DelayUtils.sleepTicks(AnchorMacroAdvanced.config.actionDelay);

        // Step 5: Detonate
        ItemUtils.useItemOnBlock(Items.RESPAWN_ANCHOR);
    }

    private static void executeHuman() {
        // Humanized delays, gradual look movement, small randomization
        executeNormal(); // Simplified, DelayUtils handles humanized ticks
    }
}
