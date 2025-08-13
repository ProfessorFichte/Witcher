package net.witcher_rpg.util.tags;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.witcher_rpg.WitcherClassMod;

public class WitcherItemTags {

    public static final TagKey<Item> SILVER_SWORDS = register("silver_swords");
    public static final TagKey<Item> SILVER_INGOTS = register("silver_ingots");
    public static final TagKey<Item> STEEL_SWORDS = register("steel_swords");
    public static final TagKey<Item> STEEL_INGOTS = register("steel_ingots");
    public static final TagKey<Item> WITCHER_SWORDS = register("witcher_swords");
    public static final TagKey<Item> WITCHER_ARMOR = register("witcher_armor");
    public static final TagKey<Item> WITCHER_MEDALLIONS = register("witcher_medallions");
    public static final TagKey<Item> SIGN_INTENSITY_ENCHANTABLE = register("enchantable/sign_intensity");

    public static final TagKey<Item> GLYPHS_0 = register("glyphs_0");
    public static final TagKey<Item> GLYPHS_1 = register("glyphs_1");
    public static final TagKey<Item> GLYPHS_2 = register("glyphs_2");

    public static final TagKey<Item> RELIC_SWORDS_0 = register("relic_swords_0");
    public static final TagKey<Item> RELIC_SWORDS_1 = register("relic_swords_1");
    public static final TagKey<Item> RELIC_SWORDS_2 = register("relic_swords_2");

    public static final TagKey<Item> TRINKETS_0 = register("trinkets_0");
    public static final TagKey<Item> TRINKETS_1 = register("trinkets_1");

    public static final TagKey<Item> ENHANCED_DIAGRAMS = register("enhanced_diagrams");
    public static final TagKey<Item> SUPERIOR_DIAGRAMS = register("superior_diagrams");

    private static TagKey<Item> register(String id) {
        return TagKey.of(RegistryKeys.ITEM, WitcherClassMod.id(id));
    }
}
