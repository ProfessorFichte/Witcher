package net.witcher_rpg.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.witcher_rpg.client.WitcherExposedClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void witcher_rpg$exposedOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (WitcherExposedClient.EXPOSED_BY_ME.contains(entity.getId())) {
            cir.setReturnValue(true);
        }
    }
}
