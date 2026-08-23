package com.witcher.neoforge.compat.curios;

import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitcherCurioGlyphItem extends WitcherCurioItem {
    public WitcherCurioGlyphItem(Item.Settings settings, @Nullable AttributeModifiersComponent customAttributes) {
        super(settings, customAttributes);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("item.witcher_rpg.glyph.tooltip").formatted(Formatting.GOLD));
    }
}
