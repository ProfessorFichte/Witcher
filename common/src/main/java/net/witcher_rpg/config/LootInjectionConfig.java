package net.witcher_rpg.config;

import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;
import net.witcher_rpg.compat.jewelry.WitcherGlyphRunestones;
import net.witcher_rpg.item.WitcherMaterials;
import net.witcher_rpg.item.WitcherTrinkets;

import java.util.LinkedHashMap;
import java.util.List;

public class LootInjectionConfig {
    public record Item(String itemId, int weight, int minAmount, int maxAmount, String cutId) {
        public Item(String itemId, int weight, int minAmount, int maxAmount) {
            this(itemId, weight, minAmount, maxAmount, null);
        }

        public static Item cut(String itemId, String cutId, int weight) {
            return new Item(itemId, weight, 1, 1, cutId);
        }
    }
    public record Entry(float rolls, List<Item> items) { }
    public LinkedHashMap<String, Entry> entries = new LinkedHashMap<>();

    public static LootInjectionConfig init() {
        LootInjectionConfig loot = new LootInjectionConfig();
        loot.entries.put(
                "minecraft:chests/village/village_weaponsmith",
                new Entry(0.5f, List.of(
                        new Item(WitcherMaterials.SILVER_INGOT.id().toString(), 3,1,3),
                        new Item(WitcherMaterials.STEEL_INGOT.id().toString(), 3,1,3),
                        new Item(WitcherMaterials.DARK_IRON_INGOT.id().toString(), 1,1,1),
                        new Item(WitcherMaterials.METEORITE_INGOT.id().toString(), 1,1,1)
                        )));
        loot.entries.put(
                "witcher_rpg:blocks/silver_ore",
                new Entry(0.025f, List.of(
                        new Item(WitcherTrinkets.PURE_SILVER.id().toString(), 1,1,1))));
        loot.entries.put(
                "witcher_rpg:blocks/deepslate_silver_ore",
                new Entry(0.025f, List.of(
                        new Item(WitcherTrinkets.PURE_SILVER.id().toString(), 1,1,1))));

        if (JewelrySocketFeature.isJewelryPresent()) {
            var lesserGlyph = WitcherGlyphRunestones.LESSER_GLYPH_OF_INFUSION.id().toString();
            var glyph = WitcherGlyphRunestones.GLYPH.id().toString();
            var greaterGlyph = WitcherGlyphRunestones.GREATER_GLYPH.id().toString();
            var lesserRunestone = WitcherGlyphRunestones.LESSER_RUNESTONE.id().toString();
            var runestone = WitcherGlyphRunestones.RUNESTONE.id().toString();
            var greaterRunestone = WitcherGlyphRunestones.GREATER_RUNESTONE.id().toString();

            loot.entries.put(
                    "witcher_rpg:chests/witcher_grave",
                    new Entry(0.15f, List.of(
                            Item.cut(lesserGlyph, "witcher_rpg:lesser_aard_glyph", 1),
                            Item.cut(lesserGlyph, "witcher_rpg:lesser_igni_glyph", 1),
                            Item.cut(lesserRunestone, "witcher_rpg:lesser_dazhbog_runestone", 1),
                            Item.cut(lesserRunestone, "witcher_rpg:lesser_chernobog_runestone", 1)
                            )));
            loot.entries.put(
                    "witcher_rpg:chests/old_ruins",
                    new Entry(0.2f, List.of(
                            Item.cut(glyph, "witcher_rpg:aard_glyph", 2),
                            Item.cut(glyph, "witcher_rpg:quen_glyph", 2),
                            Item.cut(glyph, "witcher_rpg:yrden_glyph", 1),
                            Item.cut(runestone, "witcher_rpg:dazhbog_runestone", 2),
                            Item.cut(runestone, "witcher_rpg:triglav_runestone", 1)
                            )));
            loot.entries.put(
                    "witcher_rpg:chests/hideout_base",
                    new Entry(0.1f, List.of(
                            Item.cut(greaterGlyph, "witcher_rpg:greater_yrden_glyph", 1),
                            Item.cut(greaterGlyph, "witcher_rpg:greater_quen_glyph", 1),
                            Item.cut(greaterRunestone, "witcher_rpg:greater_triglav_runestone", 1),
                            Item.cut(greaterRunestone, "witcher_rpg:greater_perun_runestone", 1)
                            )));
        }

        return loot;
    }
}
