package github.nyeptos.cursedrelics.network;

import github.nyeptos.cursedrelics.client.ClientAlchemistRingData;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetworking {
    private ModNetworking() {}

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                AlchemistPendingSyncPayload.TYPE,
                AlchemistPendingSyncPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() ->
                        ClientAlchemistRingData.set(payload.pending(), payload.health()))
        );

        registrar.playToServer(
                RunicFlaskUsePayload.TYPE,
                RunicFlaskUsePayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() ->
                        RunicFlaskUsePayload.handle(payload, (ServerPlayer) context.player()))
        );
    }
}
