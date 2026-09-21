package github.nyeptos.cursedrelics.util;

import github.nyeptos.cursedrelics.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RunicFlaskData {
    public static final int MAX_CHARGES = 5;
    public static final int REGEN_INTERNAL_TICKS = 1200;
    public static final int USE_COOLDOWN_TICKS = 100;
    public static final float HEAL_PERCENT = 0.30F;

    private static int getStoredCharges(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.RUNIC_FLASK_CHARGES, MAX_CHARGES);
    }

    private static void setStoredCharges(ItemStack stack, int value) {
        stack.set(ModDataComponents.RUNIC_FLASK_CHARGES, Math.max(0, Math.min(value, MAX_CHARGES)));
    }

    private static long getRegenAnchor(ItemStack stack, long fallback) {
        return stack.getOrDefault(ModDataComponents.RUNIC_FLASK_REGEN_ANCHOR, fallback);
    }

    private static void setRegenAnchor(ItemStack stack, long value) {
        stack.set(ModDataComponents.RUNIC_FLASK_REGEN_ANCHOR, value);
    }

    private static void ensureAnchorInitialized(ItemStack stack, long currentGameTime) {
        if (!stack.has(ModDataComponents.RUNIC_FLASK_REGEN_ANCHOR)) {
            setRegenAnchor(stack, currentGameTime);
        }
    }

    public static int getEffectiveCharges(ItemStack stack, long currentGameTime) {
        int stored = getStoredCharges(stack);
        if (stored >= MAX_CHARGES) {
            return stored;
        }

        long anchor = getRegenAnchor(stack, currentGameTime);
        long elapsed = Math.max(0, currentGameTime - anchor);
        int regenerated = (int) Math.min(elapsed / REGEN_INTERNAL_TICKS, MAX_CHARGES - stored);

        return stored + regenerated;
    }

    public static boolean hasCharge(ItemStack stack, long currentTime) {
        return  getEffectiveCharges(stack, currentTime) > 0;
    }

    public static int crystallize(ItemStack stack, long currentGameTime) {
        int stored = getStoredCharges(stack);
        if (stored >= MAX_CHARGES) {
            return 0;
        }

        long anchor = getRegenAnchor(stack, currentGameTime);
        long elapsed = Math.max(0, currentGameTime - anchor);
        int regenerated = (int) Math.min(elapsed / REGEN_INTERNAL_TICKS, MAX_CHARGES - stored);

        if (regenerated <= 0) {
            return 0;
        }

        setStoredCharges(stack, stored + regenerated);
        setRegenAnchor(stack, anchor + (long) regenerated * REGEN_INTERNAL_TICKS);
        return regenerated;
    }

    private static void consumeCharge(ItemStack stack, long currentGameTime) {
        crystallize(stack, currentGameTime);
        setStoredCharges(stack, getStoredCharges(stack) - 1);
        ensureAnchorInitialized(stack, currentGameTime);
    }

    private static long getCooldownUntil(ItemStack stack) {
        return  stack.getOrDefault(ModDataComponents.RUNIC_FLASK_COOLDOWN_UNTIL, 0L);
    }

    private static void setCooldownUntil(ItemStack stack, long value) {
        stack.set(ModDataComponents.RUNIC_FLASK_COOLDOWN_UNTIL, value);
    }

    public static boolean isOnCooldown(ItemStack stack, long currentGameTime) {
        return  currentGameTime < getCooldownUntil(stack);
    }

    public static long getRemainingCooldownTicks(ItemStack stack, long currentGameTime) {
        return Math.max(0, getCooldownUntil(stack) - currentGameTime);
    }

    private static void startCooldown(ItemStack stack, long currentGameTime) {
        setCooldownUntil(stack, currentGameTime + USE_COOLDOWN_TICKS);
    }

    public static float computeHealAmount(LivingEntity target) {
        return  target.getMaxHealth() * HEAL_PERCENT;
    }

    public static boolean canUse(Player player, ItemStack stack) {
        long now = player.level().getGameTime();
        return hasCharge(stack, now) && !isOnCooldown(stack, now);
    }

    public static void applyInstantHeal(Player player, ItemStack stack) {
        long now = player.level().getGameTime();
        consumeCharge(stack, now);
        startCooldown(stack, now);
        player.heal(computeHealAmount(player));
        playHealSound(player);
    }

    public static void notifyCannotUse(Player player, ItemStack stack) {
        long now = player.level().getGameTime();
        if (!isOnCooldown(stack, now)) {
            return;
        }

        Component message = Component.translatable("message.cursedrelics.runic_flask.on_cooldown")
                .withStyle(ChatFormatting.RED);

        player.displayClientMessage(message, true);
    }

    public static void playHealSound(Player player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 0.6F, 1.6F);
    }

    public static void playRegenSound(Player player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 0.5F, 1.0F);
    }

    private RunicFlaskData() {
    }
}
