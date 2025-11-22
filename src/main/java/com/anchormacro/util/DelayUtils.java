package com.anchormacro.util;

import com.anchormacro.AnchorMacroAdvanced;

public class DelayUtils {

    public static void sleepTicks(int ticks) {
        int delay = ticks * 50;
        if ("human".equals(AnchorMacroAdvanced.config.mode) && AnchorMacroAdvanced.config.legitMode) {
            delay += (int)(Math.random() * 25); // small randomization
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}
