package github.nyeptos.cursedrelics.registry;

import github.nyeptos.cursedrelics.Cursedrelics;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Cursedrelics.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CURSEDRELICS_TAB =
            CREATIVE_MODE_TABS.register("cursedrelics_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cursedrelics.cursedrelics_tab"))
                    .icon(() -> new ItemStack(ModItems.FORGOTTEN_CONTRACT.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.FORGOTTEN_CONTRACT.get());
                        output.accept(ModItems.RING_OF_ALCHEMISTS.get());
                        output.accept(ModItems.RUNIC_FLASK.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
