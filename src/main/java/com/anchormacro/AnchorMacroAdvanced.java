package com.anchormacro;

import com.anchormacro.command.GGCommand;
import com.anchormacro.config.MacroConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class AnchorMacroAdvanced implements ClientModInitializer {

    public static MacroConfig config;

    @Override
    public void onInitializeClient() {
        // Load or create config
        config = MacroConfig.load();

        // Register /gg command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            GGCommand.register(dispatcher);
        });
    }
}
