package github.nyeptos.cursedrelics.registry;

import com.mojang.serialization.Codec;
import github.nyeptos.cursedrelics.Cursedrelics;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Cursedrelics.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>>
    RUNIC_FLASK_CHARGES =
            DATA_COMPONENT_TYPES.register("runic_flask_charges", () ->
                    DataComponentType
                            .<Integer>builder()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.VAR_INT)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>>
    RUNIC_FLASK_REGEN_ANCHOR =
            DATA_COMPONENT_TYPES.register("runic_flask_regen_anchor", () ->
                    DataComponentType.<Long>builder()
                            .persistent(Codec.LONG)
                            .networkSynchronized(ByteBufCodecs.VAR_LONG)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>>
    RUNIC_FLASK_COOLDOWN_UNTIL =
            DATA_COMPONENT_TYPES.register("runic_flask_cooldown_until", () ->
                    DataComponentType.<Long>builder()
                            .persistent(Codec.LONG)
                            .networkSynchronized(ByteBufCodecs.VAR_LONG)
                            .build());

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENT_TYPES.register(modEventBus);
    }

    private ModDataComponents() {
    }
 }
