package com.anchormacro.command;

import com.anchormacro.AnchorMacroAdvanced;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;

public class GGCommand {

    public static void register(net.minecraft.client.command.CommandDispatcher<net.minecraft.client.command.ClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("gg")
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
                    sendFeedback("Action Delay: " + cfg.actionDelay);
                    return 1;
                }))
            .then(ClientCommandManager.literal("action")
                .then(ClientCommandManager.literal("delay")
                    .then(ClientCommandManager.argument("ticks", IntegerArgumentType.integer(0))
                        .executes(ctx -> {
                            int ticks = IntegerArgumentType.getInteger(ctx, "ticks");
                            AnchorMacroAdvanced.config.actionDelay = ticks;
                            AnchorMacroAdvanced.config.save();
                            sendFeedback("Action delay set to " + ticks + " ticks");
                            return 1;
                        }))))
            .then(ClientCommandManager.literal("modes")
                .then(ClientCommandManager.argument("mode", StringArgumentType.word())
                    .executes(ctx -> {
                        String mode = StringArgumentType.getString(ctx, "mode");
                        if (!mode.equals("normal") && !mode.equals("double") && !mode.equals("human")) {
                            sendFeedback("Invalid mode! Use normal/double/human");
                            return 0;
                        }
                        AnchorMacroAdvanced.config.mode = mode;
                        AnchorMacroAdvanced.config.save();
                        sendFeedback("Mode set to " + mode);
                        return 1;
                    })))
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
        );
    }

    private static void sendFeedback(String msg) {
        MinecraftClient.getInstance().player.sendMessage(new net.minecraft.text.LiteralText("[AnchorMacro] " + msg), false);
    }
}
