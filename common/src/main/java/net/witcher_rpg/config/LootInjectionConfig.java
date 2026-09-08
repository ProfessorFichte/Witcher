package net.witcher_rpg.config;

import net.witcher_rpg.item.WitcherMaterials;

import java.util.LinkedHashMap;
import java.util.List;

public class LootInjectionConfig {
    /// Literal id rather than `WitcherTrinkets.PURE_SILVER.id()`: this class is reached from
    /// `WitcherClassMod`'s static initializer, and touching `WitcherTrinkets` from there drags in the whole
    /// spell graph (and with it More RPG Library's `MRPGCEffects`, whose class initializer needs Spell
    /// Power's attributes). On Forge 47 that happens during mod construction, long before the `ATTRIBUTE`
    /// `RegisterEvent` window, and blows up.
    private static final String PURE_SILVER_ID = "witcher_rpg:pure_silver";

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
                        new Item(PURE_SILVER_ID, 1,1,1))));
        loot.entries.put(
                "witcher_rpg:blocks/deepslate_silver_ore",
                new Entry(0.025f, List.of(
                        new Item(PURE_SILVER_ID, 1,1,1))));
        return loot;
    }
}
