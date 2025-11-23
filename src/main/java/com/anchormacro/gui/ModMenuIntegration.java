package com.anchormacro.gui;

import com.anchormacro.AnchorMacroAdvanced;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("AnchorMacroAdvanced Settings"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
            ConfigCategory delays = builder.getOrCreateCategory(Text.literal("Delays"));

            // General settings
            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enabled"), AnchorMacroAdvanced.config.enabled)
                    .setDefaultValue(false)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.enabled = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Safe Anchor"), AnchorMacroAdvanced.config.safeAnchor)
                    .setDefaultValue(true)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.safeAnchor = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("AutoSearch"), AnchorMacroAdvanced.config.autoSearch)
                    .setDefaultValue(false)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.autoSearch = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Air Place"), AnchorMacroAdvanced.config.airPlace)
                    .setDefaultValue(false)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.airPlace = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .setTooltip(Text.literal("Place anchors in the air"))
                    .build());

            general.addEntry(entryBuilder.startIntSlider(Text.literal("Anchor Slot"), AnchorMacroAdvanced.config.anchorSlot, 1, 9)
                    .setDefaultValue(1)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.anchorSlot = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .build());

            general.addEntry(entryBuilder.startIntSlider(Text.literal("Glowstone Slot"), AnchorMacroAdvanced.config.glowstoneSlot, 1, 9)
                    .setDefaultValue(2)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.glowstoneSlot = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .build());

            general.addEntry(entryBuilder.startIntSlider(Text.literal("Totem Slot"), AnchorMacroAdvanced.config.totemSlot, 1, 9)
                    .setDefaultValue(9)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.totemSlot = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .build());

            general.addEntry(entryBuilder.startStrField(Text.literal("Mode"), AnchorMacroAdvanced.config.mode)
                    .setDefaultValue("normal")
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.mode = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .setTooltip(Text.literal("Options: normal, double, or human"))
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Legit Mode"), AnchorMacroAdvanced.config.legitMode)
                    .setDefaultValue(false)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.legitMode = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .setTooltip(Text.literal("Adds random delays in human mode for more legit gameplay"))
                    .build());

            // Delay settings (in ticks - 1 tick = 50ms)
            delays.addEntry(entryBuilder.startIntSlider(Text.literal("Place Delay"), AnchorMacroAdvanced.config.placeDelay, 0, 20)
                    .setDefaultValue(0)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.placeDelay = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .setTooltip(Text.literal("Delay after placing anchor (ticks)"))
                    .build());

            delays.addEntry(entryBuilder.startIntSlider(Text.literal("Charge Delay"), AnchorMacroAdvanced.config.chargeDelay, 0, 20)
                    .setDefaultValue(0)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.chargeDelay = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .setTooltip(Text.literal("Delay after charging anchor (ticks)"))
                    .build());

            delays.addEntry(entryBuilder.startIntSlider(Text.literal("Detonate Delay"), AnchorMacroAdvanced.config.detonateDelay, 0, 20)
                    .setDefaultValue(0)
                    .setSaveConsumer(val -> { 
                        AnchorMacroAdvanced.config.detonateDelay = val; 
                        AnchorMacroAdvanced.config.save(); 
                    })
                    .setTooltip(Text.literal("Delay before detonating (ticks)"))
                    .build());

            builder.setSavingRunnable(() -> {
                AnchorMacroAdvanced.config.save();
            });

            return builder.build();
        };
    }
}
