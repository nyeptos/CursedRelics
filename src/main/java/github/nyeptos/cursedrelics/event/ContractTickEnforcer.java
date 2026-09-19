package github.nyeptos.cursedrelics.event;

import github.nyeptos.cursedrelics.util.ContractTracker;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import static github.nyeptos.cursedrelics.Cursedrelics.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class ContractTickEnforcer {
        @SubscribeEvent
        public static void onServerTickPost(ServerTickEvent.Post event) {
            ContractTracker.enforceAll();
        }
}
