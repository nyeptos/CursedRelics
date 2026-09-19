package github.nyeptos.cursedrelics;

import com.mojang.logging.LogUtils;
import github.nyeptos.cursedrelics.registry.ModCreativeTabs;
import github.nyeptos.cursedrelics.registry.ModDataComponents;
import github.nyeptos.cursedrelics.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

@Mod(Cursedrelics.MOD_ID)
public class Cursedrelics {
        public static final String MOD_ID = "cursedrelics";
        public static final Logger LOGGER = LogUtils.getLogger();

    public Cursedrelics(IEventBus modEventBus) {
            ModItems.register(modEventBus);
            ModCreativeTabs.register(modEventBus);
            modEventBus.addListener((RegisterCapabilitiesEvent event) -> github.nyeptos.cursedrelics.compat.curios.CuriosIntegration.registerCapabilities());
            modEventBus.addListener(github.nyeptos.cursedrelics.network.ModNetworking::register);
            ModDataComponents.register(modEventBus);
    }
}
