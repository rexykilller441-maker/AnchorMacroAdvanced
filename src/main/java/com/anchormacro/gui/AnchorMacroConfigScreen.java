package com.anchormacro.gui;

import com.anchormacro.AnchorMacroAdvanced;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class AnchorMacroConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("AnchorMacroAdvanced Settings"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(Text.literal("General"));

        // Enabled toggle
        category.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enabled"), AnchorMacroAdvanced.config.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.enabled = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .build());

        // Safe Anchor
        category.addEntry(entryBuilder.startBooleanToggle(Text.literal("Safe Anchor"), AnchorMacroAdvanced.config.safeAnchor)
                .setDefaultValue(false)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.safeAnchor = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .build());

        // Auto Search
        category.addEntry(entryBuilder.startBooleanToggle(Text.literal("Auto Search"), AnchorMacroAdvanced.config.autoSearch)
                .setDefaultValue(true)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.autoSearch = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .build());

        // Air Place
        category.addEntry(entryBuilder.startBooleanToggle(Text.literal("Air Place"), AnchorMacroAdvanced.config.airPlace)
                .setDefaultValue(false)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.airPlace = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .build());

        // Delays
        category.addEntry(entryBuilder.startIntSlider(Text.literal("Place Delay (ticks)"), AnchorMacroAdvanced.config.placeDelay, 0, 20)
                .setDefaultValue(0)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.placeDelay = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .build());

        category.addEntry(entryBuilder.startIntSlider(Text.literal("Charge Delay (ticks)"), AnchorMacroAdvanced.config.chargeDelay, 0, 20)
                .setDefaultValue(0)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.chargeDelay = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .build());

        category.addEntry(entryBuilder.startIntSlider(Text.literal("Detonate Delay (ticks)"), AnchorMacroAdvanced.config.detonateDelay, 0, 20)
                .setDefaultValue(0)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.detonateDelay = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .build());

        // Slots
        category.addEntry(entryBuilder.startIntSlider(Text.literal("Anchor Slot"), AnchorMacroAdvanced.config.anchorSlot, 1, 9)
                .setDefaultValue(1)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.anchorSlot = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .build());

        category.addEntry(entryBuilder.startIntSlider(Text.literal("Glowstone Slot"), AnchorMacroAdvanced.config.glowstoneSlot, 1, 9)
                .setDefaultValue(2)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.glowstoneSlot = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .build());

        category.addEntry(entryBuilder.startIntSlider(Text.literal("Totem Slot"), AnchorMacroAdvanced.config.totemSlot, 1, 9)
                .setDefaultValue(3)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.totemSlot = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .build());

        // Mode (normal / double / human)
        category.addEntry(entryBuilder.startStrField(Text.literal("Mode"), AnchorMacroAdvanced.config.mode)
                .setDefaultValue("normal")
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.mode = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .setTooltip(Text.literal("Options: normal, double, human"))
                .build());

        // Legit Mode toggle
        category.addEntry(entryBuilder.startBooleanToggle(Text.literal("Legit Mode"), AnchorMacroAdvanced.config.legitMode)
                .setDefaultValue(false)
                .setSaveConsumer(val -> { 
                    AnchorMacroAdvanced.config.legitMode = val; 
                    AnchorMacroAdvanced.config.save(); 
                })
                .setTooltip(Text.literal("Adds random delays in human mode"))
                .build());

        builder.setSavingRunnable(() -> {
            AnchorMacroAdvanced.config.save();
        });

        return builder.build();
    }

    public static void open() {
        MinecraftClient.getInstance().setScreen(create(MinecraftClient.getInstance().currentScreen));
    }
                          }
