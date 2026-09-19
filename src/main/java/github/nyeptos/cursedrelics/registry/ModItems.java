package github.nyeptos.cursedrelics.registry;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static github.nyeptos.cursedrelics.Cursedrelics.MOD_ID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredItem<Item> FORGOTTEN_CONTRACT = ITEMS.registerSimpleItem (
            "forgotten_contract",
            new Item.Properties().stacksTo(1)
    );

    public static final DeferredItem<Item> RING_OF_ALCHEMISTS =  ITEMS.registerSimpleItem(
            "ring_of_alchemists",
            new Item.Properties().stacksTo(1)
    );

    public static final DeferredItem<Item> RUNIC_FLASK =  ITEMS.registerSimpleItem(
            "runic_flask",
            new Item.Properties().stacksTo(1)
    );

    public static void register(net.neoforged.bus.api.IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
