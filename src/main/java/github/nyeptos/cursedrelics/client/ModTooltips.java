package github.nyeptos.cursedrelics.client;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
