package net.witcher_rpg.config;

import net.witcher_rpg.item.WitcherMaterials;
import net.witcher_rpg.item.WitcherTrinkets;

import java.util.LinkedHashMap;
import java.util.List;

public class LootInjectionConfig {
    public record Item(String itemId, int weight, int minAmount,int maxAmount) {  }
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
        return loot;
    }
}
