package net.witcher_rpg.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class RunestoneItem extends Item {
    public RunestoneItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack,  World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.witcher_rpg.runestone.tooltip").formatted(Formatting.GOLD));
    }
}
