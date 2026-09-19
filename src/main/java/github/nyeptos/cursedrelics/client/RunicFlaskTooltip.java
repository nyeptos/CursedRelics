package github.nyeptos.cursedrelics.client;

import github.nyeptos.cursedrelics.Cursedrelics;
import github.nyeptos.cursedrelics.registry.ModItems;
import github.nyeptos.cursedrelics.util.RunicFlaskData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = Cursedrelics.MOD_ID, value = Dist.CLIENT)
public class RunicFlaskTooltip {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() != ModItems.RUNIC_FLASK.get()) {
            return;
        }

        Player player = event.getEntity();
        long gameTime = player != null ? player.level().getGameTime() : 0L;

        int charges = RunicFlaskData.getEffectiveCharges(event.getItemStack(), gameTime);

        event.getToolTip().add(Component.translatable(
                "tooltip.cursedrelics.runic_flask.charges", charges, RunicFlaskData.MAX_CHARGES
        ));
    }

    private RunicFlaskTooltip() {
    }
}
