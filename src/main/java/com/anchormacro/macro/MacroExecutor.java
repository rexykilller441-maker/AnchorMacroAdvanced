package com.anchormacro.macro;

import com.anchormacro.AnchorMacroAdvanced;
import com.anchormacro.util.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;

public class MacroExecutor {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public enum Mode { OLD, NEW }
    private static Mode macroMode = Mode.NEW;

    private static MacroState state = MacroState.IDLE;
    private static int delayTicks = 0;
    private static int executionCount = 0;

    // Old ultra-fast executor state
    private static boolean oldRunning = false;
    private static int oldStep = 0;
    private static int oldTickCounter = 0;

    public enum MacroState {
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

    public static void setMode(Mode mode) {
        macroMode = mode;
        reset();
    }

    public static void runMacro() {
        if (mc.player == null || mc.world == null) return;

        if (macroMode == Mode.OLD) {
            if (oldRunning) {
                mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] OLD macro already running!"), false);
                return;
            }
            oldRunning = true;
            oldStep = 1;
            oldTickCounter = 0;
        } else {
            if (state != MacroState.IDLE) {
                mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] NEW macro already running!"), false);
                return;
            }
            executionCount = 0;
            state = MacroState.PLACE_ANCHOR;
        }
    }

    public static void tick() {
        if (macroMode == Mode.OLD) {
            tickOldMacro();
        } else {
            tickNewMacro();
        }
    }

    private static void tickNewMacro() {
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
                delayTicks = AnchorMacroAdvanced.config.placeDelay;
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
                    delayTicks = AnchorMacroAdvanced.config.chargeDelay;
                    state = MacroState.COMPLETE;
                } else {
                    delayTicks = AnchorMacroAdvanced.config.chargeDelay;
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
                delayTicks = AnchorMacroAdvanced.config.detonateDelay;
                state = MacroState.DETONATE_ANCHOR;
            }
            case DETONATE_ANCHOR -> {
                ItemUtils.useItemOnBlock(Items.RESPAWN_ANCHOR);
                state = MacroState.COMPLETE;
            }
            case COMPLETE -> {
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

    private static void tickOldMacro() {
        if (!oldRunning) return;
        oldTickCounter++;

        try {
            switch (oldStep) {
                case 1 -> {
                    int anchorSlot = ItemUtils.getAnchorSlot();
                    mc.player.getInventory().selectedSlot = anchorSlot;
                    ItemUtils.placeBlockAtCursor(Items.RESPAWN_ANCHOR);
                    oldStep = 2;
                    oldTickCounter = 0;
                }
                case 2 -> {
                    int glowSlot = ItemUtils.getGlowstoneSlot();
                    mc.player.getInventory().selectedSlot = glowSlot;
                    oldStep = 3;
                    oldTickCounter = 0;
                }
                case 3 -> {
                    ItemUtils.useItemOnBlock(Items.GLOWSTONE);
                    if (!AnchorMacroAdvanced.config.safeAnchor) {
                        int totemSlot = ItemUtils.getTotemSlot();
                        mc.player.getInventory().selectedSlot = totemSlot;
                    }
                    oldStep = 4;
                    oldTickCounter = 0;
                }
                case 4 -> {
                    ItemUtils.useItemOnBlock(Items.RESPAWN_ANCHOR);
                    if (AnchorMacroAdvanced.config.safeAnchor) {
                        ItemUtils.placeGlowstoneInFrontOfAnchor();
                    }
                    oldRunning = false;
                    oldStep = 0;
                }
            }
        } catch (Exception e) {
            oldRunning = false;
            oldStep = 0;
        }
    }

    private static void reset() {
        state = MacroState.IDLE;
        delayTicks = 0;
        executionCount = 0;
        oldRunning = false;
        oldStep = 0;
        oldTickCounter = 0;
    }
                        }
