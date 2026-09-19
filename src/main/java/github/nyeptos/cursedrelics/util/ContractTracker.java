package github.nyeptos.cursedrelics.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public final class ContractTracker {
    private static final Set<LivingEntity> TRACKED = Collections.newSetFromMap(new WeakHashMap<>());

    private ContractTracker() {}

    public static void track(LivingEntity entity) {
        TRACKED.add(entity);
    }

    public static void enforceAll() {
        TRACKED.removeIf(e -> !e.isAlive());
        for (LivingEntity entity : TRACKED) {
            if (!ContractData.isMarked(entity)) {
                ContractData.mark(entity);
            }

            if (entity.level() instanceof ServerLevel level && entity.tickCount % 10 == 0) {
                double width = entity.getBbWidth();
                double height = entity.getBbHeight();
                level.sendParticles(
                        ParticleTypes.SOUL,
                        entity.getX(), entity.getY() + height / 2.0, entity.getZ(), 2, width / 2.0, height / 2.0, width / 2.0, 0.02
                );
            }
        }
    }
}
