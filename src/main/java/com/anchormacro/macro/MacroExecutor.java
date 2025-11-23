package com.anchormacro.macro;

import com.anchormacro.AnchorMacroAdvanced;
import com.anchormacro.util.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;

public class MacroExecutor {

    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static MacroState state = MacroState.IDLE;
    private static int delayTicks = 0;
    private static int executionCount = 0;

    private enum MacroState {
        IDLE,
        PLACE_ANCHOR,
        WAIT_AFTER_PLACE,
        CHARGE_ANCHOR,
        WAIT_AFTER_CHARGE,
        SWITCH_TO_TOTEM,
        WAIT_BEFORE_DETONATE,
        DETONATE_ANCHOR,
        COMPLETE
    }

    public static void tick() {
        if (state == MacroState.IDLE) return;

        if (delayTicks > 0) {
            delayTicks--;
            return;
        }

        switch (state) {
            case PLACE_ANCHOR -> {
                int anchorSlot = ItemUtils.getAnchorSlot();
                if (anchorSlot == -1) {
                    mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No anchor found!"), false);
                    reset();
                    return;
                }
                mc.player.getInventory().selectedSlot = anchorSlot;
                delayTicks = 1;
                state = MacroState.WAIT_AFTER_PLACE;
            }
            case WAIT_AFTER_PLACE -> {
                ItemUtils.placeBlockAtCursor(Items.RESPAWN_ANCHOR);
                delayTicks = getDelay();
                state = MacroState.CHARGE_ANCHOR;
            }
            case CHARGE_ANCHOR -> {
                int glowSlot = ItemUtils.getGlowstoneSlot();
                if (glowSlot == -1) {
                    mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No glowstone found!"), false);
                    reset();
                    return;
                }
                mc.player.getInventory().selectedSlot = glowSlot;
                delayTicks = 1;
                state = MacroState.WAIT_AFTER_CHARGE;
            }
            case WAIT_AFTER_CHARGE -> {
                ItemUtils.useItemOnBlock(Items.GLOWSTONE);
                
                if (AnchorMacroAdvanced.config.safeAnchor) {
                    delayTicks = getDelay();
                    state = MacroState.COMPLETE;
                } else {
                    delayTicks = getDelay();
                    state = MacroState.SWITCH_TO_TOTEM;
                }
            }
            case SWITCH_TO_TOTEM -> {
                int totemSlot = ItemUtils.getTotemSlot();
                if (totemSlot == -1) {
                    mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No totem found!"), false);
                    reset();
                    return;
                }
                mc.player.getInventory().selectedSlot = totemSlot;
                delayTicks = 1;
                state = MacroState.WAIT_BEFORE_DETONATE;
            }
            case WAIT_BEFORE_DETONATE -> {
                delayTicks = getDelay();
                state = MacroState.DETONATE_ANCHOR;
            }
            case DETONATE_ANCHOR -> {
                ItemUtils.useItemOnBlock(Items.RESPAWN_ANCHOR);
                state = MacroState.COMPLETE;
            }
            case COMPLETE -> {
                // Place glowstone block protection in safe anchor mode
                if (AnchorMacroAdvanced.config.safeAnchor && executionCount == 0) {
                    ItemUtils.placeGlowstoneInFrontOfAnchor();
                    executionCount++;
                }
                
                if ("double".equals(AnchorMacroAdvanced.config.mode) && executionCount < 2) {
                    executionCount++;
                    delayTicks = 2;
                    state = MacroState.PLACE_ANCHOR;
                } else {
                    reset();
                }
            }
        }
    }

    public static void runMacro() {
        if (mc.player == null || mc.world == null) return;
        
        if (state != MacroState.IDLE) {
            mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] Macro already running!"), false);
            return;
        }

        executionCount = 0;
        state = MacroState.PLACE_ANCHOR;
    }

    private static int getDelay() {
        int delay = AnchorMacroAdvanced.config.actionDelay;
        
        if ("human".equals(AnchorMacroAdvanced.config.mode) && AnchorMacroAdvanced.config.legitMode) {
            delay += (int)(Math.random() * 3); // Add 0-2 ticks random delay
        }
        
        return delay;
    }

    private static void reset() {
        state = MacroState.IDLE;
        delayTicks = 0;
        executionCount = 0;
    }
}
