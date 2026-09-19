package github.nyeptos.cursedrelics.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public final class ModKeyMappings {
    public static final String CATEGORY = "key.categories.cursedrelics";

    public static final KeyMapping USE_RUNIC_FLASK = new KeyMapping(
            "key.cursedrelics.use_runic_flask",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_H,
            CATEGORY
    );

    private ModKeyMappings() {
    }
}
