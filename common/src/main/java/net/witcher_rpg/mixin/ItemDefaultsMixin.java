package net.witcher_rpg.mixin;

import net.minecraft.item.Item;
import net.witcher_rpg.item.component.WitcherDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemDefaultsMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void witcher_adoptDefaults(Item.Settings settings, CallbackInfo ci) {
        WitcherDataComponents.adoptPendingDefaults((Item) (Object) this, settings);
    }
}
