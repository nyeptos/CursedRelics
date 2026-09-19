package github.nyeptos.cursedrelics.event;

import github.nyeptos.cursedrelics.Cursedrelics;
import github.nyeptos.cursedrelics.compat.curios.CuriosIntegration;
import github.nyeptos.cursedrelics.registry.ModItems;
import github.nyeptos.cursedrelics.util.RunicFlaskData;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Cursedrelics.MOD_ID)
public final class RunicFlaskRegenHandler {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        event.getServer().getPlayerList().getPlayers()
                .forEach(RunicFlaskRegenHandler::tickPlayer);
    }

    private static void tickPlayer(ServerPlayer player) {
        long gameTime = player.level().getGameTime();
        CuriosIntegration.mutateEquipped(player, ModItems.RUNIC_FLASK.get(), stack -> {
            if (RunicFlaskData.crystallize(stack, gameTime) > 0) {
                RunicFlaskData.playRegenSound(player);
            }
        });
    }

    private RunicFlaskRegenHandler() {
    }
}
