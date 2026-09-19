package github.nyeptos.cursedrelics.event;

import github.nyeptos.cursedrelics.registry.ModItems;
import github.nyeptos.cursedrelics.util.ContractData;
import github.nyeptos.cursedrelics.util.ContractTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static github.nyeptos.cursedrelics.Cursedrelics.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ContractMarkHandler {
    private static final int MAX_OWNER_CHAIN_DEPTH =4;

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDamagePost(LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();

        Player attacker = resolveAttacker(event.getSource());
        if (attacker == null) {
            return;
        }
        if (attacker ==victim) {
            return;
        }
        if (!github.nyeptos.cursedrelics.compat.curios.CuriosIntegration.isWearing(attacker, ModItems.FORGOTTEN_CONTRACT.get())) return;

        ContractData.mark(victim);
        ContractTracker.track(victim);
    }

    private static Player resolveAttacker(DamageSource source) {
        Player directAttacker = resolveOwnerChain(source.getEntity(), 0);
        if (directAttacker != null) {
            return directAttacker;
        }
        return resolveOwnerChain(source.getDirectEntity(), 0);
    }

    private static Player resolveOwnerChain(Entity entity, int depth) {
        if (entity == null || depth > MAX_OWNER_CHAIN_DEPTH) return null;
        return switch (entity) {
            case Player player -> player;
            case Projectile projectile -> resolveOwnerChain(projectile.getOwner(), depth + 1);
            case OwnableEntity ownable when ownable.getOwner() instanceof Entity ownerEntity -> resolveOwnerChain(ownerEntity, depth + 1);
            default -> null;
        };
    }
}
