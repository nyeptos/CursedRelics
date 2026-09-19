package github.nyeptos.cursedrelics.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import github.nyeptos.cursedrelics.client.ClientAlchemistRingData;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class PendingHeartMixin {
    @Unique
    private static final ResourceLocation PENDING_HEART_FULL =
            ResourceLocation.fromNamespaceAndPath("cursedrelics", "hud/pending_heart_full");
    @Unique
    private static final ResourceLocation PENDING_HEART_HALF =
            ResourceLocation.fromNamespaceAndPath("cursedrelics", "hud/pending_heart_half");

    @Unique
    private int cursedrelics$currentL =  -1;

    @Inject(method = "renderHearts", at = @At("HEAD"))
    private void cursedrelics$resetCounter(GuiGraphics guiGraphics, net.minecraft.world.entity.player.Player player,
                                           int x, int y, int height, int offsetHeartIndex,
                                           float maxHealth, int currentHealth, int displayHealth, int absorptionAmount,
                                           boolean renderHighlight, CallbackInfo ci) {
        int i = Mth.ceil((double) maxHealth / 2.0);
        int j = Mth.ceil((double) absorptionAmount / 2.0);
        this.cursedrelics$currentL = i + j -1;
    }

    @WrapOperation(
            method = "renderHearts",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
                    ordinal = 0)
    )
    private void cursedrelics$trackIndex(Gui instance, GuiGraphics guiGraphics, Gui.HeartType heartType,
                                         int hx, int hy, boolean hardcore, boolean halfHeart, boolean blinking,
                                         Operation<Void> original) {
        original.call(instance, guiGraphics, heartType, hx, hy, hardcore, halfHeart, blinking);
        this.cursedrelics$currentL--;
    }

    @WrapOperation(
            method = "renderHearts",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
                    ordinal = 3)
    )

    private void cursedrelics$overlayPending(Gui instance, GuiGraphics guiGraphics, Gui.HeartType heartType,
                                             int hx, int hy, boolean hardcore, boolean halfHeart, boolean blinking,
                                             Operation<Void> original) {
        original.call(instance, guiGraphics, heartType, hx, hy, hardcore, halfHeart, blinking);

        int index = this.cursedrelics$currentL + 1;

        int pendingHalfHearts = ClientAlchemistRingData.getPendingHalfHearts();
        if (index >= 0 && index < pendingHalfHearts) {
            boolean isLastPendingSlot = index == pendingHalfHearts - 1;
            var sprite = ClientAlchemistRingData.isPendingOdd() && isLastPendingSlot
                    ? PENDING_HEART_HALF : PENDING_HEART_FULL;
            guiGraphics.blitSprite(sprite, hx, hy, 9, 9);
        }
    }
}
