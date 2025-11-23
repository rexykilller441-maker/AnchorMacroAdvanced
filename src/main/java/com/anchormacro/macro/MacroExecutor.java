package com.anchormacro.macro;

import com.anchormacro.AnchorMacroAdvanced;
import com.anchormacro.util.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;

public class MacroExecutor {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public enum Mode { OLD, NEW }
    private static Mode macroMode = Mode.NEW; // default

    public static void setMode(Mode mode) {
        macroMode = mode;
        // reset states when mode changes
        resetAll();
    }

    public static Mode getMode() {
        return macroMode;
    }

    // NEW-mode state machine
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

    private static MacroState state = MacroState.IDLE;
    private static int delayTicks = 0;
    private static int executionCount = 0;

    // OLD-mode internal runner
    private static boolean oldRunning = false;
    private static int oldStep = 0;
    private static int oldTickCounter = 0;

    public static void runMacro() {
        // Choose based on current mode
        if (macroMode == Mode.OLD) {
            runOldMacro();
        } else {
            runNewMacro();
        }
    }

    public static void runOldMacro() {
        if (mc.player == null || mc.world == null) return;
        if (oldRunning) {
            mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] OLD macro already running!"), false);
            return;
        }
        oldRunning = true;
        oldStep = 1;
        oldTickCounter = 0;
    }

    public static void runNewMacro() {
        if (mc.player == null || mc.world == null) return;
        if (state != MacroState.IDLE) {
            mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] NEW macro already running!"), false);
            return;
        }
        executionCount = 0;
        state = MacroState.PLACE_ANCHOR;
        delayTicks = 0;
    }

    public static void tick() {
        // Always tick old runner if active (this matches old behavior)
        if (oldRunning) {
            tickOldMacro();
        }
        // Tick new runner if active
        if (state != MacroState.IDLE) {
            if (delayTicks > 0) {
                delayTicks--;
            } else {
                tickNewMacro();
            }
        }
    }

    // --- OLD ultra-fast logic (keeps hand swings etc) ---
    private static void tickOldMacro() {
        oldTickCounter++;
        try {
            switch (oldStep) {
                case 1 -> { // place anchor immediately
                    int anchorSlot = ItemUtils.getAnchorSlot();
                    if (anchorSlot == -1) {
                        if (mc.player != null) mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No anchor found!"), false);
                        oldRunning = false;
                        oldStep = 0;
                        return;
                    }
                    mc.player.getInventory().selectedSlot = anchorSlot;
                    ItemUtils.placeBlockAtCursor(Items.RESPAWN_ANCHOR);
                    oldStep = 2;
                    oldTickCounter = 0;
                }
                case 2 -> { // switch to glowstone slot
                    int glowSlot = ItemUtils.getGlowstoneSlot();
                    if (glowSlot == -1) {
                        if (mc.player != null) mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No glowstone found!"), false);
                        oldRunning = false;
                        oldStep = 0;
                        return;
                    }
                    mc.player.getInventory().selectedSlot = glowSlot;
                    oldStep = 3;
                    oldTickCounter = 0;
                }
                case 3 -> { // charge anchor
                    ItemUtils.useItemOnBlock(Items.GLOWSTONE);
                    if (!AnchorMacroAdvanced.config.safeAnchor) {
                        int totemSlot = ItemUtils.getTotemSlot();
                        if (totemSlot == -1) {
                            if (mc.player != null) mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No totem found!"), false);
                            oldRunning = false;
                            oldStep = 0;
                            return;
                        }
                        mc.player.getInventory().selectedSlot = totemSlot;
                    }
                    oldStep = 4;
                    oldTickCounter = 0;
                }
                case 4 -> { // detonate
                    ItemUtils.useItemOnBlock(Items.RESPAWN_ANCHOR);
                    if (AnchorMacroAdvanced.config.safeAnchor) {
                        ItemUtils.placeGlowstoneInFrontOfAnchor();
                    }
                    // double mode support in old runner: run twice quickly if configured
                    if ("double".equals(AnchorMacroAdvanced.config.mode)) {
                        if (executionCount < 1) {
                            executionCount++;
                            oldStep = 1; // repeat
                            oldTickCounter = 0;
                            return;
                        }
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

    // --- NEW structured logic (configurable delays, human mode) ---
    private static void tickNewMacro() {
        switch (state) {
            case PLACE_ANCHOR -> {
                int anchorSlot = ItemUtils.getAnchorSlot();
                if (anchorSlot == -1) {
                    if (mc.player != null) mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No anchor found!"), false);
                    resetNew();
                    return;
                }
                mc.player.getInventory().selectedSlot = anchorSlot;
                delayTicks = 1;
                state = MacroState.WAIT_AFTER_PLACE;
            }
            case WAIT_AFTER_PLACE -> {
                ItemUtils.placeBlockAtCursor(Items.RESPAWN_ANCHOR);
                delayTicks = getPlaceDelay();
                state = MacroState.CHARGE_ANCHOR;
            }
            case CHARGE_ANCHOR -> {
                int glowSlot = ItemUtils.getGlowstoneSlot();
                if (glowSlot == -1) {
                    if (mc.player != null) mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No glowstone found!"), false);
                    resetNew();
                    return;
                }
                mc.player.getInventory().selectedSlot = glowSlot;
                delayTicks = 1;
                state = MacroState.WAIT_AFTER_CHARGE;
            }
            case WAIT_AFTER_CHARGE -> {
                ItemUtils.useItemOnBlock(Items.GLOWSTONE);

                if (AnchorMacroAdvanced.config.safeAnchor) {
                    delayTicks = getChargeDelay();
                    state = MacroState.COMPLETE;
                } else {
                    delayTicks = getChargeDelay();
                    state = MacroState.SWITCH_TO_TOTEM;
                }
            }
            case SWITCH_TO_TOTEM -> {
                int totemSlot = ItemUtils.getTotemSlot();
                if (totemSlot == -1) {
                    if (mc.player != null) mc.player.sendMessage(net.minecraft.text.Text.literal("[AnchorMacro] No totem found!"), false);
                    resetNew();
                    return;
                }
                mc.player.getInventory().selectedSlot = totemSlot;
                delayTicks = 1;
                state = MacroState.WAIT_BEFORE_DETONATE;
            }
            case WAIT_BEFORE_DETONATE -> {
                delayTicks = getDetonateDelay();
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
                    resetNew();
                }
            }
            default -> resetNew();
        }
    }

    private static int getPlaceDelay() {
        int delay = AnchorMacroAdvanced.config.placeDelay;
        if ("human".equals(AnchorMacroAdvanced.config.mode) && AnchorMacroAdvanced.config.legitMode) {
            delay += (int) (Math.random() * 3);
        }
        return delay;
    }

    private static int getChargeDelay() {
        int delay = AnchorMacroAdvanced.config.chargeDelay;
        if ("human".equals(AnchorMacroAdvanced.config.mode) && AnchorMacroAdvanced.config.legitMode) {
            delay += (int) (Math.random() * 3);
        }
        return delay;
    }

    private static int getDetonateDelay() {
        int delay = AnchorMacroAdvanced.config.detonateDelay;
        if ("human".equals(AnchorMacroAdvanced.config.mode) && AnchorMacroAdvanced.config.legitMode) {
            delay += (int) (Math.random() * 3);
        }
        return delay;
    }

    private static void resetNew() {
        state = MacroState.IDLE;
        delayTicks = 0;
        executionCount = 0;
    }

    private static void resetAll() {
        resetNew();
        oldRunning = false;
        oldStep = 0;
        oldTickCounter = 0;
        executionCount = 0;
    }

    private static void reset() {
        resetAll();
    }
    }
