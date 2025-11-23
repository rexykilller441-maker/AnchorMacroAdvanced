package com.anchormacro.command;

import com.anchormacro.AnchorMacroAdvanced;
import com.anchormacro.macro.MacroExecutor;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class GGCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("gg")
            .then(ClientCommandManager.literal("gui")
                .executes(ctx -> {
                    com.anchormacro.gui.AnchorMacroConfigScreen.open();
                    return 1;
                }))
            .then(ClientCommandManager.literal("enable")
                .executes(ctx -> {
                    AnchorMacroAdvanced.config.enabled = true;
                    AnchorMacroAdvanced.config.save();
                    sendFeedback("Macro enabled");
                    return 1;
                }))
            .then(ClientCommandManager.literal("disable")
                .executes(ctx -> {
                    AnchorMacroAdvanced.config.enabled = false;
                    AnchorMacroAdvanced.config.save();
                    sendFeedback("Macro disabled");
                    return 1;
                }))
            .then(ClientCommandManager.literal("old")
                .executes(ctx -> {
                    MacroExecutor.setMode(MacroExecutor.Mode.OLD);
                    sendFeedback("Switched to OLD macro mode (slow)");
                    return 1;
                }))
            .then(ClientCommandManager.literal("new")
                .executes(ctx -> {
                    MacroExecutor.setMode(MacroExecutor.Mode.NEW);
                    sendFeedback("Switched to NEW macro mode (fast)");
                    return 1;
                }))
            .then(ClientCommandManager.literal("airplace")
                .executes(ctx -> {
                    AnchorMacroAdvanced.config.airPlace = !AnchorMacroAdvanced.config.airPlace;
                    AnchorMacroAdvanced.config.save();
                    sendFeedback("Air Place: " + (AnchorMacroAdvanced.config.airPlace ? "ON" : "OFF"));
                    return 1;
                }))
            .then(ClientCommandManager.literal("reload")
                .executes(ctx -> {
                    AnchorMacroAdvanced.config = AnchorMacroAdvanced.config.load();
                    sendFeedback("Config reloaded");
                    return 1;
                }))
            .then(ClientCommandManager.literal("info")
                .executes(ctx -> {
                    var cfg = AnchorMacroAdvanced.config;
                    sendFeedback("AnchorMacroAdvanced v1.0");
                    sendFeedback("Enabled: " + cfg.enabled);
                    sendFeedback("Mode: " + cfg.mode);
                    sendFeedback("Safe Anchor: " + cfg.safeAnchor);
                    sendFeedback("AutoSearch: " + cfg.autoSearch);
                    sendFeedback("Air Place: " + cfg.airPlace);
                    sendFeedback("Place Delay: " + cfg.placeDelay + " ticks");
                    sendFeedback("Charge Delay: " + cfg.chargeDelay + " ticks");
                    sendFeedback("Detonate Delay: " + cfg.detonateDelay + " ticks");
                    sendFeedback("Macro Executor Mode: " + MacroExecutor.getMode().name());
                    return 1;
                }))
            // Delays
            .then(ClientCommandManager.literal("place")
                .then(ClientCommandManager.literal("delay")
                    .then(ClientCommandManager.argument("ticks", IntegerArgumentType.integer(0, 20))
                        .executes(ctx -> {
                            int ticks = IntegerArgumentType.getInteger(ctx, "ticks");
                            AnchorMacroAdvanced.config.placeDelay = ticks;
                            AnchorMacroAdvanced.config.save();
                            sendFeedback("Place delay set to " + ticks + " ticks");
                            return 1;
                        }))))
            .then(ClientCommandManager.literal("charge")
                .then(ClientCommandManager.literal("delay")
                    .then(ClientCommandManager.argument("ticks", IntegerArgumentType.integer(0, 20))
                        .executes(ctx -> {
                            int ticks = IntegerArgumentType.getInteger(ctx, "ticks");
                            AnchorMacroAdvanced.config.chargeDelay = ticks;
                            AnchorMacroAdvanced.config.save();
                            sendFeedback("Charge delay set to " + ticks + " ticks");
                            return 1;
                        }))))
            .then(ClientCommandManager.literal("detonate")
                .then(ClientCommandManager.literal("delay")
                    .then(ClientCommandManager.argument("ticks", IntegerArgumentType.integer(0, 20))
                        .executes(ctx -> {
                            int ticks = IntegerArgumentType.getInteger(ctx, "ticks");
                            AnchorMacroAdvanced.config.detonateDelay = ticks;
                            AnchorMacroAdvanced.config.save();
                            sendFeedback("Detonate delay set to " + ticks + " ticks");
                            return 1;
                        }))))
            // Slots
            .then(ClientCommandManager.literal("anchorslot")
                .then(ClientCommandManager.argument("slot", IntegerArgumentType.integer(1,9))
                    .executes(ctx -> {
                        AnchorMacroAdvanced.config.anchorSlot = IntegerArgumentType.getInteger(ctx,"slot");
                        AnchorMacroAdvanced.config.save();
                        sendFeedback("Anchor slot set to " + AnchorMacroAdvanced.config.anchorSlot);
                        return 1;
                    })))
            .then(ClientCommandManager.literal("glowstoneslot")
                .then(ClientCommandManager.argument("slot", IntegerArgumentType.integer(1,9))
                    .executes(ctx -> {
                        AnchorMacroAdvanced.config.glowstoneSlot = IntegerArgumentType.getInteger(ctx,"slot");
                        AnchorMacroAdvanced.config.save();
                        sendFeedback("Glowstone slot set to " + AnchorMacroAdvanced.config.glowstoneSlot);
                        return 1;
                    })))
            .then(ClientCommandManager.literal("totemslot")
                .then(ClientCommandManager.argument("slot", IntegerArgumentType.integer(1,9))
                    .executes(ctx -> {
                        AnchorMacroAdvanced.config.totemSlot = IntegerArgumentType.getInteger(ctx,"slot");
                        AnchorMacroAdvanced.config.save();
                        sendFeedback("Totem slot set to " + AnchorMacroAdvanced.config.totemSlot);
                        return 1;
                    })))
            // Boolean toggles
            .then(ClientCommandManager.literal("safeanchor")
                .then(ClientCommandManager.argument("onoff", StringArgumentType.word())
                    .executes(ctx -> {
                        String val = StringArgumentType.getString(ctx, "onoff");
                        AnchorMacroAdvanced.config.safeAnchor = val.equalsIgnoreCase("on");
                        AnchorMacroAdvanced.config.save();
                        sendFeedback("Safe Anchor set to " + AnchorMacroAdvanced.config.safeAnchor);
                        return 1;
                    })))
            .then(ClientCommandManager.literal("autosearch")
                .then(ClientCommandManager.argument("onoff", StringArgumentType.word())
                    .executes(ctx -> {
                        String val = StringArgumentType.getString(ctx, "onoff");
                        AnchorMacroAdvanced.config.autoSearch = val.equalsIgnoreCase("on");
                        AnchorMacroAdvanced.config.save();
                        sendFeedback("AutoSearch set to " + AnchorMacroAdvanced.config.autoSearch);
                        return 1;
                    })))
        );
    }

    private static void sendFeedback(String msg) {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("[AnchorMacro] " + msg), false);
    }
}
