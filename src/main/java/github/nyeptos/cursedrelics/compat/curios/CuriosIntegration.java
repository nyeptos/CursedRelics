package github.nyeptos.cursedrelics.compat.curios;

import github.nyeptos.cursedrelics.registry.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;
import java.util.function.Consumer;

public final class CuriosIntegration {
    private CuriosIntegration() {}

    public static void registerCapabilities() {
        CuriosApi.registerCurio(ModItems.FORGOTTEN_CONTRACT.get(), new ICurioItem(){});
        CuriosApi.registerCurio(ModItems.RING_OF_ALCHEMISTS.get(), new ICurioItem(){});
        CuriosApi.registerCurio(ModItems.RUNIC_FLASK.get(), new ICurioItem(){});
    }

    public static boolean isWearing(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosInventory(entity)
                .map(handler -> handler.findFirstCurio(item).isPresent())
                .orElse(false);
    }

    public static Optional<ItemStack> findEquipped(LivingEntity entity, Item item) {
        return CuriosApi.getCuriosInventory(entity)
                .flatMap(handler -> handler.findFirstCurio(item))
                .map(SlotResult::stack);
    }

    public static void mutateEquipped(LivingEntity entity, Item item, Consumer<ItemStack> mutator) {
        Optional<ICuriosItemHandler> handlerOpt = CuriosApi.getCuriosInventory(entity);
        if (handlerOpt.isEmpty()) {
            return;
        }

        IItemHandlerModifiable equipped = handlerOpt.get().getEquippedCurios();
        for (int slot = 0; slot < equipped.getSlots(); slot++) {
            ItemStack stack = equipped.getStackInSlot(slot);
            if (stack.getItem() == item) {
                mutator.accept(stack);
                equipped.setStackInSlot(slot, stack);
                return;
            }
        }

    }
}
