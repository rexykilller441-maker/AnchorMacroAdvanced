package com.anchormacro;

import com.anchormacro.command.GGCommand;
import com.anchormacro.config.MacroConfig;
import com.anchormacro.macro.MacroExecutor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AnchorMacroAdvanced implements ClientModInitializer {

    public static MacroConfig config;
    private static KeyBinding toggleMacroKey;
    private static KeyBinding executeMacroKey;

    @Override
    public void onInitializeClient() {
        // Load or create config
        config = MacroConfig.load();

        // Register keybinds
        toggleMacroKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.anchormacro.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.anchormacro"
        ));

        executeMacroKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.anchormacro.execute",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT, // ~ key
            "category.anchormacro"
        ));

        // Register tick event for macro execution and keybind handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Tick the macro state machine
            MacroExecutor.tick();

            // Toggle macro on/off
            while (toggleMacroKey.wasPressed()) {
                config.enabled = !config.enabled;
                config.save();
                if (client.player != null) {
                    client.player.sendMessage(
                        net.minecraft.text.Text.literal("[AnchorMacro] " + (config.enabled ? "Enabled" : "Disabled")),
                        false
                    );
                }
            }

            // Execute macro (works regardless of enabled status)
            while (executeMacroKey.wasPressed()) {
                if (client.player != null && client.world != null) {
                    MacroExecutor.runMacro();
                }
            }
        });

        // Register /gg command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            GGCommand.register(dispatcher);
        });
    }

    // Switch between old and new macro modes
    public static void setMacroMode(String mode) {
        if ("old".equalsIgnoreCase(mode)) {
            MacroExecutor.setMode(MacroExecutor.Mode.OLD);
        } else if ("new".equalsIgnoreCase(mode)) {
            MacroExecutor.setMode(MacroExecutor.Mode.NEW);
        }
    }
}
