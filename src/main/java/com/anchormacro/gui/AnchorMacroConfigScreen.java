// Fabric requires a class implementing ModMenuApi
// Simplified example
package com.anchormacro.gui;

import com.anchormacro.AnchorMacroAdvanced;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class AnchorMacroConfigScreen {

    public static void open() {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(new LiteralText("AnchorMacroAdvanced Settings"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(new LiteralText("General"));

        category.addEntry(entryBuilder.startBooleanToggle("Enabled", AnchorMacroAdvanced.config.enabled)
                .setSaveConsumer(val -> { AnchorMacroAdvanced.config.enabled = val; AnchorMacroAdvanced.config.save(); })
                .build());

        category.addEntry(entryBuilder.startBooleanToggle("Safe Anchor", AnchorMacroAdvanced.config.safeAnchor)
                .setSaveConsumer(val -> { AnchorMacroAdvanced.config.safeAnchor = val; AnchorMacroAdvanced.config.save(); })
                .build());

        category.addEntry(entryBuilder.startIntSlider("Action Delay", AnchorMacroAdvanced.config.actionDelay, 0, 10)
                .setSaveConsumer(val -> { AnchorMacroAdvanced.config.actionDelay = val; AnchorMacroAdvanced.config.save(); })
                .build());

        MinecraftClient.getInstance().setScreen(builder.build());
    }
}
