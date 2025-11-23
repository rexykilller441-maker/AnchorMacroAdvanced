package com.anchormacro.util;

import com.anchormacro.AnchorMacroAdvanced;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ItemUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static int getAnchorSlot() {
        if (AnchorMacroAdvanced.config.autoSearch) return findHotbarItem(Items.RESPAWN_ANCHOR);
        return clampGuiSlot(AnchorMacroAdvanced.config.anchorSlot);
    }

    public static int getGlowstoneSlot() {
        if (AnchorMacroAdvanced.config.autoSearch) return findHotbarItem(Items.GLOWSTONE);
        return clampGuiSlot(AnchorMacroAdvanced.config.glowstoneSlot);
    }

    public static int getTotemSlot() {
        if (AnchorMacroAdvanced.config.autoSearch) return findHotbarItem(Items.TOTEM_OF_UNDYING);
        return clampGuiSlot(AnchorMacroAdvanced.config.totemSlot);
    }

    private static int clampGuiSlot(int guiSlot) {
        int s = guiSlot - 1;
        if (s < 0) return 0;
        if (s > 8) return 8;
        return s;
    }

    public static int findHotbarItem(Item item) {
        if (mc.player == null) return -1;
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == item) return i;
        }
        return -1;
    }

    public static void placeBlockAtCursor(Item item) {
        if (mc.player == null || mc.world == null) return;

        HitResult hitResult = mc.crosshairTarget;

        // Ultra-fast air place
        if (AnchorMacroAdvanced.config.airPlace && (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK)) {
            Vec3d playerPos = mc.player.getPos();
            Vec3d lookVec = mc.player.getRotationVec(1.0f);
            BlockPos targetPos = BlockPos.ofFloored(playerPos.add(lookVec.multiply(3)));

            BlockHitResult fakeHit = new BlockHitResult(
                Vec3d.ofCenter(targetPos),
                Direction.DOWN,
                targetPos.down(),
                false
            );

            ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, fakeHit);
            if (result.isAccepted()) mc.player.swingHand(Hand.MAIN_HAND);
            return;
        }

        // Normal placement
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
            if (result.isAccepted()) mc.player.swingHand(Hand.MAIN_HAND);
        } else {
            ActionResult result = mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            if (result.isAccepted()) mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    public static void useItemOnBlock(Item item) {
        if (mc.player == null || mc.world == null) return;

        HitResult hitResult = mc.crosshairTarget;
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
            if (result.isAccepted()) mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    public static void placeGlowstoneInFrontOfAnchor() {
        if (mc.player == null || mc.world == null) return;

        HitResult hitResult = mc.crosshairTarget;
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos anchorPos = blockHitResult.getBlockPos();
            Direction side = blockHitResult.getSide();
            BlockPos glowstonePos = anchorPos.offset(side);

            BlockHitResult newHitResult = new BlockHitResult(
                blockHitResult.getPos(),
                side.getOpposite(),
                glowstonePos,
                false
            );

            ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, newHitResult);
            if (result.isAccepted()) mc.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
