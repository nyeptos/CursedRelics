package github.nyeptos.cursedrelics.client;

import github.nyeptos.cursedrelics.Cursedrelics;
import github.nyeptos.cursedrelics.network.RunicFlaskUsePayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Cursedrelics.MOD_ID, value = Dist.CLIENT)
public final class RunicFlaskKeyHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (ModKeyMappings.USE_RUNIC_FLASK.consumeClick()) {
            PacketDistributor.sendToServer(new RunicFlaskUsePayload());
        }
    }

    private RunicFlaskKeyHandler() {
    }
}
