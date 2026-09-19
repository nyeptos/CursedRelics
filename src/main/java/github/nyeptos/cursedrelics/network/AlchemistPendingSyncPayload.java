package github.nyeptos.cursedrelics.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record AlchemistPendingSyncPayload(float pending, float health) implements CustomPacketPayload {
    public static final Type<AlchemistPendingSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("cursedrelics", "alchemist_pending_sync"));

    public static final StreamCodec<ByteBuf, AlchemistPendingSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.FLOAT, AlchemistPendingSyncPayload::pending,
                    ByteBufCodecs.FLOAT, AlchemistPendingSyncPayload::health,
                    AlchemistPendingSyncPayload::new
            );

    @Override
    public @NotNull Type<AlchemistPendingSyncPayload> type() {
        return TYPE;
    }
}
