package github.nyeptos.cursedrelics.network;

import github.nyeptos.cursedrelics.Cursedrelics;
import github.nyeptos.cursedrelics.compat.curios.CuriosIntegration;
import github.nyeptos.cursedrelics.registry.ModItems;
import github.nyeptos.cursedrelics.util.RunicFlaskData;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record RunicFlaskUsePayload() implements CustomPacketPayload {

    public static final Type<RunicFlaskUsePayload> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(Cursedrelics.MOD_ID, "runic_flask_use"));

    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, RunicFlaskUsePayload> STREAM_CODEC =
            StreamCodec.unit(new RunicFlaskUsePayload());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RunicFlaskUsePayload payload, ServerPlayer player) {
        Objects.requireNonNull(player.getServer()).execute(() -> {
            ItemStack equipped = CuriosIntegration.findEquipped(player, ModItems.RUNIC_FLASK.get())
                    .orElse(null);

            if (equipped == null) {
                return;
            }

            if (RunicFlaskData.canUse(player, equipped)) {
                CuriosIntegration.mutateEquipped(player, ModItems.RUNIC_FLASK.get(),
                        stack -> RunicFlaskData.applyInstantHeal(player, stack));
            } else {
                RunicFlaskData.notifyCannotUse(player, equipped);
            }
        });
    }

}
