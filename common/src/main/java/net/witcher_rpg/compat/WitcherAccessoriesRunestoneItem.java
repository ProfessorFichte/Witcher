package net.witcher_rpg.compat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class WitcherAccessoriesRunestoneItem extends WitcherAccessoriesItem {
    public WitcherAccessoriesRunestoneItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("item.witcher_rpg.runestone.tooltip").formatted(Formatting.GOLD));
    }
}
