package watermelonmojito.controller_support.mixin.core;

import watermelonmojito.controller_support.config.ControllerSupportConfig;
import net.minecraft.core.block.BlockFarmland;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(value = BlockFarmland.class, remap = false)
public class Mixin_BlockFarmland {
    @Redirect(
            method = "onEntityWalking",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Random;nextInt(I)I"
            )
    )
    private int tryCancelTrample(Random instance, int bound) {
        if (ControllerSupportConfig.disableTrample) {
            return 1;
        }
        return instance.nextInt(bound);
    }
}
