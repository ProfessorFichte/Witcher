package net.witcher_rpg.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.registry.Registries;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.item.component.GlyphSlots;
import net.witcher_rpg.item.component.GlyphTooltipComponent;
import net.witcher_rpg.item.component.RunestoneSlots;
import net.witcher_rpg.item.component.RunestoneTooltipComponent;
import net.witcher_rpg.item.component.WitcherDataComponents;
import net.witcher_rpg.util.tags.WitcherItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackTooltipMixin {

    @Inject(method = "getTooltipData", at = @At("HEAD"), cancellable = true)
    private void addWitcherTooltipData(CallbackInfoReturnable<Optional<TooltipData>> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        boolean isGlyphAttachable = stack.isIn(WitcherItemTags.GLYPH_ATTACHABLE);
        GlyphSlots glyphSlots = stack.get(WitcherDataComponents.GLYPH_SLOTS);

        if (isGlyphAttachable) {
            if (glyphSlots == null) {
                int defaultSlots = getDefaultGlyphSlots(stack);
                glyphSlots = new GlyphSlots(defaultSlots, List.of());
            }

            if (glyphSlots.maxSlots() > 0) {
                cir.setReturnValue(Optional.of(new GlyphTooltipComponent(glyphSlots)));
                return;
            }
        }

        boolean isRunestoneAttachable = stack.isIn(WitcherItemTags.RUNESTONE_ATTACHABLE);
        RunestoneSlots runestoneSlots = stack.get(WitcherDataComponents.RUNESTONE_SLOTS);

        if (isRunestoneAttachable) {
            if (runestoneSlots == null) {
                int defaultSlots = getDefaultRunestoneSlots(stack);
                runestoneSlots = new RunestoneSlots(defaultSlots, List.of());
            }

            if (runestoneSlots.maxSlots() > 0) {
                cir.setReturnValue(Optional.of(new RunestoneTooltipComponent(runestoneSlots)));
            }
        }
    }

    private static int getDefaultGlyphSlots(ItemStack item) {
        String itemId = Registries.ITEM.getId(item.getItem()).toString();
        if (itemId.contains("netherite")) return 2;
        if (itemId.contains("_grandmaster")) return Armors.TIER5_GLYPH_SLOTS;
        if (itemId.contains("_mastercrafted")) return Armors.TIER4_GLYPH_SLOTS;
        if (itemId.contains("_superior")) return Armors.TIER3_GLYPH_SLOTS;
        if (itemId.contains("_enhanced")) return Armors.TIER2_GLYPH_SLOTS;
        return 1;
    }

    private static int getDefaultRunestoneSlots(ItemStack item) {
        String itemId = Registries.ITEM.getId(item.getItem()).toString();
        if (itemId.contains("netherite")) return 2;
        if (itemId.contains("gvalchir")) return 3;
        if (itemId.contains("ardaenye")) return 3;
        if (itemId.contains("moonblade")) return 3;
        if (itemId.contains("dyaebl")) return 3;
        if (itemId.contains("winters_blade")) return 3;
        return 1;
    }
}
