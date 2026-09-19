package github.nyeptos.cursedrelics;

import com.mojang.logging.LogUtils;
import github.nyeptos.cursedrelics.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(Cursedrelics.MODID)
public class Cursedrelics {
        public static final String MODID = "cursedrelics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Cursedrelics(IEventBus modEventBus, ModContainer modContainer) {
            ModItems.register(modEventBus);
            boolean ironsLoaded = ModList.get().isLoaded("irons_spellbooks");
            Cursedrelics.LOGGER.info("contract: irons_spellbooks loaded = {}", ironsLoaded);
            if (ModList.get().isLoaded("irons_spellbooks")) {
                NeoForge.EVENT_BUS.register(new github.nyeptos.cursedrelics.compat.irons.IronsSpellbooksIntegration());
                Cursedrelics.LOGGER.info("contract: IronsSpellbooksIntegration registered");
            }
     }
}
