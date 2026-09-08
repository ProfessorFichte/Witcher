package net.witcher_rpg.mixin;

import net.minecraft.entity.attribute.EntityAttributes;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityAttributes.class)
public class EntityAttributesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void static_tail_witcher_attributes(CallbackInfo ci) {
        WitcherAttributes.registerAttributes();
    }
}
