package github.nyeptos.cursedrelics.registry;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static github.nyeptos.cursedrelics.Cursedrelics.MODID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> FORGOTTEN_CONTRACT = ITEMS.registerSimpleItem (
            "forgotten_contract",
            new Item.Properties().stacksTo(1)
    );
    public static void register(net.neoforged.bus.api.IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
