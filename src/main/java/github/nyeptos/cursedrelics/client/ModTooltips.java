package github.nyeptos.cursedrelics.client;

import github.nyeptos.cursedrelics.Cursedrelics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import static github.nyeptos.cursedrelics.Cursedrelics.MOD_ID;

@EventBusSubscriber(modid = MOD_ID,  bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ModTooltips {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (!Cursedrelics.MOD_ID.equals(id.getNamespace())) return;

        String descKey = stack.getItem().getDescriptionId() + ".desc";
        if (I18n.exists(descKey + ".1")) {
            for (int line = 1; I18n.exists(descKey + "." + line); line++) {
                event.getToolTip().add(Component.translatable(descKey + "." + line));
            }
        } else if (I18n.exists(descKey)) {
            event.getToolTip().add(Component.translatable(descKey));
        }
    }
}
