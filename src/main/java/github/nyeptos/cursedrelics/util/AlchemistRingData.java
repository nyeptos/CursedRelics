package github.nyeptos.cursedrelics.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public final class AlchemistRingData {
    private static final String TAG_ROOT = "cursedrelics";
    private static final String PENDING_KEY = "alchemist_pending_damage";

    private AlchemistRingData() {}

    public static float getPending(LivingEntity entity) {
        return entity.getPersistentData().getCompound(TAG_ROOT).getFloat(PENDING_KEY);
    }

    public static void setPending(LivingEntity entity, float amount) {
        CompoundTag persistent = entity.getPersistentData();
        CompoundTag root = persistent.getCompound(TAG_ROOT);
        root.putFloat(PENDING_KEY, amount);
        persistent.put(TAG_ROOT, root);
    }
}
