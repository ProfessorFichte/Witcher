package net.witcher_rpg.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.witcher_rpg.WitcherClassMod;

public class WitcherGroup {
    public static Identifier ID = new Identifier(WitcherClassMod.MOD_ID, "generic");
    public static RegistryKey<ItemGroup> WITCHER_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),new Identifier(WitcherClassMod.MOD_ID,"generic"));
    public static ItemGroup WITCHER;

    public static ItemGroup create() {
        if (WITCHER == null) {
            WITCHER = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                    .icon(() -> new ItemStack(WitcherTrinkets.WOLF_SCHOOL_MEDALLION.item().get()))
                    .displayName(Text.translatable("itemGroup." + WitcherClassMod.MOD_ID + ".general"))
                    .build();
        }
        return WITCHER;
    }

    public static void registerItemGroups() {
        WitcherClassMod.LOGGER.info("Registering Item Groups for " + WitcherClassMod.MOD_ID);
    }
}
