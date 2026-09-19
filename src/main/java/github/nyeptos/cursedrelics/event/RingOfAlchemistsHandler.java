package github.nyeptos.cursedrelics.event;

import github.nyeptos.cursedrelics.compat.curios.CuriosIntegration;
import github.nyeptos.cursedrelics.network.AlchemistPendingSyncPayload;
import github.nyeptos.cursedrelics.registry.ModDamageTypes;
import github.nyeptos.cursedrelics.registry.ModItems;
import github.nyeptos.cursedrelics.util.AlchemistRingData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static github.nyeptos.cursedrelics.Cursedrelics.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class RingOfAlchemistsHandler {
    private static final float DRAIN_PER_TICK = 0.05F;
    private static final float CONVERSION_RATE = 0.5F;

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDamagePre(LivingDamageEvent.Pre event) {
        if (event.getSource().is(ModDamageTypes.ALCHEMIST_DRIP)) return;

        LivingEntity victim = event.getEntity();
        if (!(victim instanceof Player player)) return;
        if (!CuriosIntegration.isWearing(player, ModItems.RING_OF_ALCHEMISTS.get())) return;
        if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;

        float incoming = event.getNewDamage();
        if (incoming <= 0) return;

        float currentPending = AlchemistRingData.getPending(player);
        float room = Math.max(0.0F, player.getHealth() - currentPending);

        float converted = incoming * CONVERSION_RATE;
        float deferred = Math.min(converted, room);
        float overflow = converted - deferred;

        float newPending = Math.max(0.0F, currentPending + deferred - overflow);
        AlchemistRingData.setPending(player, newPending);

        event.setNewDamage(overflow);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDamagePost(LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();
        if (!(victim instanceof Player player)) return;
        if (!CuriosIntegration.isWearing(player, ModItems.RING_OF_ALCHEMISTS.get())) return;

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer,
                    new AlchemistPendingSyncPayload(AlchemistRingData.getPending(player), player.getHealth()));
        }
    }

    @SubscribeEvent
    public static void onServerTickPost(ServerTickEvent.Post event) {
        for (ServerLevel level : event.getServer().getAllLevels()) {
            for (Player player : level.players()) {
                float pending = AlchemistRingData.getPending(player);
                if (pending <= 0) continue;

                float drain = Math.min(pending, DRAIN_PER_TICK);
                float newPending = pending - drain;
                AlchemistRingData.setPending(player, newPending);

                float absorption = player.getAbsorptionAmount();
                float drainFromAbsorption = Math.min(drain, absorption);
                float drainFromHealth = drain - drainFromAbsorption;

                if(drainFromAbsorption > 0) {
                    player.setAbsorptionAmount(absorption - drainFromAbsorption);
                }

                float healthAfter = player.getHealth() - drainFromHealth;
                player.setHealth(Math.max(healthAfter, 0.0F));

                if (healthAfter <= 0.0F && !player.isRemoved()) {
                    player.die(player.damageSources().source(ModDamageTypes.ALCHEMIST_DRIP));
                }

                if (player instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer,
                            new AlchemistPendingSyncPayload(newPending, player.getHealth()));
                }
            }
        }
    }
}

