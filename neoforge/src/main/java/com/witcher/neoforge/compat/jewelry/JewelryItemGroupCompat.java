package com.witcher.neoforge.compat.jewelry;

import net.jewelry.gems.GemCut;
import net.jewelry.items.Group;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.witcher_rpg.WitcherClassMod;

import java.util.ArrayList;
import java.util.List;

public class JewelryItemGroupCompat {
    public static void onBuildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(Group.GEMS_KEY)) return;

        List<ItemStack> toRemove = new ArrayList<>();
        for (var stack : event.getParentEntries()) {
            if (isOurCut(stack)) toRemove.add(stack);
        }
        for (var stack : toRemove) {
            event.remove(stack, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    private static boolean isOurCut(ItemStack stack) {
        return GemCut.of(stack)
                .flatMap(GemCut::idOf)
                .map(id -> id.getNamespace().equals(WitcherClassMod.MOD_ID))
                .orElse(false);
    }
}
