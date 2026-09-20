package net.witcher_rpg.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.inventory.Inventory;
import net.minecraft.registry.DynamicRegistryManager;
import net.witcher_rpg.item.component.GlyphSlots;
import net.witcher_rpg.item.component.RunestoneSlots;
import net.witcher_rpg.item.component.WitcherDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTransformRecipe.class)
public abstract class SmithingRecipeMixin {

    @Inject(method = "craft", at = @At("RETURN"), cancellable = true)
    private void preserveWitcherComponents(Inventory inventory, DynamicRegistryManager registryManager, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = cir.getReturnValue();
        if (result.isEmpty()) return;

        ItemStack baseItem = inventory.getStack(1);
        if (baseItem.isEmpty()) return;

        boolean modified = false;

        GlyphSlots oldGlyphSlots = WitcherDataComponents.getGlyphSlots(baseItem);
        GlyphSlots resultGlyphSlots = WitcherDataComponents.getGlyphSlots(result);

        if (oldGlyphSlots != null && resultGlyphSlots != null) {
            int correctMaxSlots = getCorrectGlyphSlots(result);
            if (correctMaxSlots > 0 && (!oldGlyphSlots.attachedGlyphs().isEmpty() ||
                oldGlyphSlots.maxSlots() != correctMaxSlots)) {
                GlyphSlots mergedSlots = new GlyphSlots(correctMaxSlots, oldGlyphSlots.attachedGlyphs());
                WitcherDataComponents.setGlyphSlots(result, mergedSlots);
                modified = true;
            }
        }

        RunestoneSlots oldRunestoneSlots = WitcherDataComponents.getRunestoneSlots(baseItem);
        RunestoneSlots newRunestoneSlots = WitcherDataComponents.getRunestoneSlots(result);

        if (oldRunestoneSlots != null && newRunestoneSlots != null) {
            int correctMaxSlots = getCorrectRunestoneSlots(result);
            if (correctMaxSlots > 0 && (!oldRunestoneSlots.attachedRunestones().isEmpty() ||
                oldRunestoneSlots.maxSlots() != correctMaxSlots)) {
                RunestoneSlots mergedSlots = new RunestoneSlots(correctMaxSlots, oldRunestoneSlots.attachedRunestones());
                WitcherDataComponents.setRunestoneSlots(result, mergedSlots);
                modified = true;
            }
        }

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
