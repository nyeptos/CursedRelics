package github.nyeptos.cursedrelics.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public final class ContractData {
    private static final String TAG_ROOT = "cursedrelics";
    private static final String MARKED_KEY = "marked";

    private ContractData() {}

    public static boolean isMarked(LivingEntity entity) {
        CompoundTag persistent = entity.getPersistentData();
        if (!persistent.contains(TAG_ROOT, Tag.TAG_COMPOUND)) return false;
        return persistent.getCompound(TAG_ROOT).getBoolean(MARKED_KEY);
    }

    public static void mark(LivingEntity entity) {
        CompoundTag persistent = entity.getPersistentData();
        CompoundTag root = persistent.getCompound(TAG_ROOT);
        root.putBoolean(MARKED_KEY, true);
        persistent.put(TAG_ROOT, root);
    }
}
