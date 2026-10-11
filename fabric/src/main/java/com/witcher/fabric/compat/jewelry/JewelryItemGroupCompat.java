package com.witcher.fabric.compat.jewelry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jewelry.gems.GemCut;
import net.jewelry.items.Group;
import net.witcher_rpg.WitcherClassMod;

public class JewelryItemGroupCompat {
    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(Group.GEMS_KEY).register(entries -> {
            entries.getDisplayStacks().removeIf(JewelryItemGroupCompat::isOurCut);
            entries.getSearchTabStacks().removeIf(JewelryItemGroupCompat::isOurCut);
        });
    }

    private static boolean isOurCut(net.minecraft.item.ItemStack stack) {
        return GemCut.of(stack)
                .flatMap(GemCut::idOf)
                .map(id -> id.getNamespace().equals(WitcherClassMod.MOD_ID))
                .orElse(false);
    }
}
