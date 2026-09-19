package github.nyeptos.cursedrelics.event;

import github.nyeptos.cursedrelics.util.ContractData;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

import static github.nyeptos.cursedrelics.Cursedrelics.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
public class ContractHealBlocker {

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (ContractData.isMarked(entity)) {
            event.setCanceled(true);
        }
    }
}
