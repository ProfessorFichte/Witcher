package com.witcher.fabric.compat.trinkets;

import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitcherTrinketRunestoneItem extends WitcherTrinketItem {
    public WitcherTrinketRunestoneItem(Item.Settings settings, @Nullable AttributeModifiersComponent customAttributes) {
        super(settings, customAttributes);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("item.witcher_rpg.runestone.tooltip").formatted(Formatting.GOLD));
    }
}
