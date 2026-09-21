package github.nyeptos.cursedrelics.client;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import static github.nyeptos.cursedrelics.Cursedrelics.MOD_ID;

@EventBusSubscriber(modid = MOD_ID,  bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ModTooltips {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        String key = event.getItemStack().getDescriptionId() + ".desc";
        if (I18n.exists(key)) {
            event.getToolTip().add(Component.translatable(key));
        }
    }

    private ModTooltips() {
    }
}
