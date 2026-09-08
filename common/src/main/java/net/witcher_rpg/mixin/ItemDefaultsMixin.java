package net.witcher_rpg.mixin;

import net.minecraft.item.Item;
import net.witcher_rpg.item.component.WitcherDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/// Adopts item-level defaults attached to `Item.Settings` (`WitcherDataComponents.defaults(settings)`, the
/// 1.20.1 stand-in for `Item.Settings#component`) onto the constructed item.
@Mixin(Item.class)
public class ItemDefaultsMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void witcher_adoptDefaults(Item.Settings settings, CallbackInfo ci) {
        WitcherDataComponents.adoptPendingDefaults((Item) (Object) this, settings);
    }
}
