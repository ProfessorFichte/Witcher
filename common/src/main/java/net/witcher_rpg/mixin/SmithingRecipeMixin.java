package net.witcher_rpg.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.witcher_rpg.compat.jewelry.JewelrySmithingSupport;
import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTransformRecipe.class)
public abstract class SmithingRecipeMixin {

    @Inject(method = "craft", at = @At("RETURN"), cancellable = true)
    private void preserveWitcherComponents(SmithingRecipeInput input, RegistryWrapper.WrapperLookup lookup, CallbackInfoReturnable<ItemStack> cir) {
        if (!JewelrySocketFeature.isJewelryPresent()) return;
        ItemStack result = cir.getReturnValue();
        if (result.isEmpty()) return;

        ItemStack baseItem = input.base();
        if (baseItem.isEmpty()) return;

        boolean modified = JewelrySmithingSupport.preserveGlyphSockets(baseItem, result, getCorrectGlyphSlots(result));
        modified |= JewelrySmithingSupport.preserveRunestoneSockets(baseItem, result, getCorrectRunestoneSlots(result));

        if (modified) {
            cir.setReturnValue(result);
        }
    }

    private int getCorrectGlyphSlots(ItemStack item) {
        String itemId = net.minecraft.registry.Registries.ITEM.getId(item.getItem()).toString();

        if (itemId.contains("grandmaster")) return 3;
        if (itemId.contains("mastercrafted") || itemId.contains("superior")) return 2;
        if (itemId.contains("enhanced") || itemId.contains("basic")) return 1;

        if (itemId.contains("netherite") || itemId.contains("diamond")) return 2;

        return 1;
    }

    private int getCorrectRunestoneSlots(ItemStack item) {
        String itemId = net.minecraft.registry.Registries.ITEM.getId(item.getItem()).toString();

        if (itemId.contains("relic")) return 3;
        if (itemId.contains("netherite") || itemId.contains("dark_steel") || itemId.contains("steel_sword")) return 2;
        if (itemId.contains("sword")) return 1;

        if (itemId.contains("netherite") || itemId.contains("diamond")) return 2;

        return 1;
    }
}
