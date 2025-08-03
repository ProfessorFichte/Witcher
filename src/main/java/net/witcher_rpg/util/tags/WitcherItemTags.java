package net.witcher_rpg.util.tags;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.witcher_rpg.WitcherClassMod;

public class WitcherItemTags {

    public static final TagKey<Item> SILVER_SWORDS = register("silver_swords");
    public static final TagKey<Item> SILVER_INGOTS = register("silver_ingots");
    public static final TagKey<Item> STEEL_SWORDS = register("steel_swords");
    public static final TagKey<Item> STEEL_INGOTS = register("steel_swords");
    public static final TagKey<Item> WITCHER_SWORDS = register("witcher_swords");
    public static final TagKey<Item> WITCHER_ARMOR = register("witcher_armor");
    public static final TagKey<Item> WITCHER_MEDALLIONS = register("witcher_medallions");
    public static final TagKey<Item> SIGN_INTENSITY_ENCHANTABLE = register("enchantable/sign_intensity");

    private static TagKey<Item> register(String id) {
        return TagKey.of(RegistryKeys.ITEM, WitcherClassMod.id(id));
    }
}
