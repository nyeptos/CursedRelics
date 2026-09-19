package github.nyeptos.cursedrelics.client;

import net.minecraft.util.Mth;

public final class ClientAlchemistRingData {
    private static float pending = 0.0F;
    private static float health = 0.0F;

    private ClientAlchemistRingData()  {}

    public static void set(float newPending, float newHealth) {
        pending = newPending;
        health = newHealth;
    }

    public static float getPending() {
        return pending;
    }

    public static float getHealth() {
        return health;
    }

    public static int getPendingHalfHearts() {
        if (pending <= 0) return 0;
        return Mth.ceil(Mth.ceil(pending) / 2.0F);
    }

    public static boolean isPendingOdd() {
        if (pending <= 0) return false;
        return (Mth.ceil(pending) % 2) == 1;
    }
}
