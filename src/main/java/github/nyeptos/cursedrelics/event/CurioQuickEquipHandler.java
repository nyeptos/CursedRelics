package github.nyeptos.cursedrelics.event;

import github.nyeptos.cursedrelics.Cursedrelics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

@EventBusSubscriber(modid = Cursedrelics.MOD_ID)
public final class CurioQuickEquipHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }

        ItemStack heldStack = event.getItemStack();
        Item item = heldStack.getItem();

        if (CuriosApi.getCuriosHelper().getCurioTags(item).isEmpty()) {
            return;
        }

        CuriosApi.getCuriosInventory(player)
                .filter(handler -> tryQuickEquip(handler, player, event.getHand(), heldStack, item))
                .ifPresent(handler -> onEquipped(event, player));
    }

    private static void onEquipped(PlayerInteractEvent.RightClickItem event, Player player) {
        player.level().playSound(null, player.blockPosition(),
                SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
        event.setCancellationResult(InteractionResult.CONSUME);
        event.setCanceled(true);
    }

    private static boolean tryQuickEquip(ICuriosItemHandler handler, Player player, InteractionHand hand, ItemStack heldStack, Item item) {
        for (String slotId : CuriosApi.getCuriosHelper().getCurioTags(item)) {
            ICurioStacksHandler stacksHandler = handler.getStacksHandler(slotId).orElse(null);
            if (stacksHandler == null) {
                continue;
            }

            IDynamicStackHandler stackHandler = stacksHandler.getStacks();
            for (int slot = 0; slot < stacksHandler.getSlots(); slot++) {
                if (stackHandler.getStackInSlot(slot).isEmpty()) {
                    stackHandler.setStackInSlot(slot, heldStack.copy());
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    return true;
                }
            }
        }
        return false;
    }

    private CurioQuickEquipHandler() {
    }
}
