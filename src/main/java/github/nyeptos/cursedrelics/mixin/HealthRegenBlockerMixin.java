package github.nyeptos.cursedrelics.mixin;

import github.nyeptos.cursedrelics.util.ContractData;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class HealthRegenBlockerMixin {

    @Inject(method = "setHealth", at =@At("HEAD"), cancellable = true)
    private void cursedrelics$blockRegen(float health, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object) this;

        if (self.level().isClientSide()) {
            return;
        }

        if (health <= self.getHealth()) {
            return;
        }

        if (ContractData.isMarked(self)) {
            ci.cancel();
        }
    }
}
