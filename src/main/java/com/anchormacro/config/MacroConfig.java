package com.anchormacro.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    
    // NEW: Separate delays for each action (in ticks)
    public int placeDelay = 0;      // Delay after placing anchor
    public int chargeDelay = 0;     // Delay after charging anchor
    public int detonateDelay = 0;   // Delay before detonating
    
    // NEW: Air place feature
    public boolean airPlace = false;
    
    // DEPRECATED: kept for backwards compatibility
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
            return gson.fromJson(new FileReader(file), MacroConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new MacroConfig();
        }
    }

    public void save() {
        try {
            File configDir = new File("config");
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            try (FileWriter writer = new FileWriter("config/anchormacro.json")) {
                gson.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
