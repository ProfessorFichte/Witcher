package com.witcher.forge.compat.curios;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_engine.api.item.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitcherCurioGlyphItem extends WitcherCurioItem {
    public WitcherCurioGlyphItem(Item.Settings settings, @Nullable ItemAttributeModifiers customAttributes) {
        super(settings, customAttributes);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.witcher_rpg.glyph.tooltip").formatted(Formatting.GOLD));
    }
}
