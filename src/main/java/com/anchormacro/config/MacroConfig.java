package com.anchormacro.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.anchormacro.AnchorMacroAdvanced;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MacroConfig {

    public int anchorSlot = 1;
    public int glowstoneSlot = 2;
    public int totemSlot = 3;
    public boolean autoSearch = true;
    public boolean safeAnchor = false;
    public String mode = "normal"; // normal, double, human
    public boolean enabled = true;
    public boolean legitMode = false;

    // delays in ticks
    public int placeDelay = 0;
    public int chargeDelay = 0;
    public int detonateDelay = 0;

    // air place option
    public boolean airPlace = false;

    // legacy field
    public int actionDelay = 2;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static MacroConfig load() {
        try {
            File file = new File("config/anchormacro.json");
            if (!file.exists()) {
                MacroConfig config = new MacroConfig();
                config.save();
                return config;
            }
            try (FileReader fr = new FileReader(file)) {
                return gson.fromJson(fr, MacroConfig.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new MacroConfig();
        }
    }

    public void save() {
        try {
            File configDir = new File("config");
            if (!configDir.exists()) configDir.mkdirs();
            try (FileWriter writer = new FileWriter("config/anchormacro.json")) {
                gson.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
