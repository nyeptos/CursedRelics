package github.nyeptos.cursedrelics.event;

import github.nyeptos.cursedrelics.compat.curios.CuriosIntegration;
import github.nyeptos.cursedrelics.network.AlchemistPendingSyncPayload;
import github.nyeptos.cursedrelics.registry.ModItems;
import github.nyeptos.cursedrelics.util.AlchemistRingData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static github.nyeptos.cursedrelics.Cursedrelics.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class RingOfAlchemistsHealHandler {

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;
        if(!CuriosIntegration.isWearing(player, ModItems.RING_OF_ALCHEMISTS.get())) return;

        float pending = AlchemistRingData.getPending(player);
        if (pending <= 0) return;

        float healAmount = event.getAmount();
        float reduced = Math.min(pending, healAmount);
        float newPending = pending - reduced;
        AlchemistRingData.setPending(player, newPending);

        event.setAmount(healAmount - reduced);

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer,
                    new AlchemistPendingSyncPayload(newPending, player.getHealth()));
        }
    }
}
