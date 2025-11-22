package com.anchormacro.gui;

import com.anchormacro.AnchorMacroAdvanced;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.modmenu.api.ConfigScreenFactory;
import me.shedaniel.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.of("AnchorMacroAdvanced Settings"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory general = builder.getOrCreateCategory(Text.of("General"));

            general.addEntry(entryBuilder.startBooleanToggle("Enabled", AnchorMacroAdvanced.config.enabled)
                    .setSaveConsumer(val -> { AnchorMacroAdvanced.config.enabled = val; AnchorMacroAdvanced.config.save(); })
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle("Safe Anchor", AnchorMacroAdvanced.config.safeAnchor)
                    .setSaveConsumer(val -> { AnchorMacroAdvanced.config.safeAnchor = val; AnchorMacroAdvanced.config.save(); })
                    .build());

            general.addEntry(entryBuilder.startIntSlider("Action Delay", AnchorMacroAdvanced.config.actionDelay, 0, 10)
                    .setSaveConsumer(val -> { AnchorMacroAdvanced.config.actionDelay = val; AnchorMacroAdvanced.config.save(); })
                    .build());

            general.addEntry(entryBuilder.startBooleanToggle("AutoSearch", AnchorMacroAdvanced.config.autoSearch)
                    .setSaveConsumer(val -> { AnchorMacroAdvanced.config.autoSearch = val; AnchorMacroAdvanced.config.save(); })
                    .build());

            return builder.build();
        };
    }
}
