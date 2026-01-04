package net.witcher_rpg.util.loot;

import net.spell_engine.rpg_series.loot.LootConfig;

import java.util.List;

public class Defaults {
    public final static LootConfig itemLootConfig;

    static {
        var WG0 = "#witcher_rpg:glyphs_0";
        var WG1 = "#witcher_rpg:glyphs_1";
        var WG2 = "#witcher_rpg:glyphs_2";

        var WR0 = "#witcher_rpg:runestones_0";
        var WR1 = "#witcher_rpg:runestones_1";
        var WR2 = "#witcher_rpg:runestones_2";

        var WRS0 = "#witcher_rpg:relic_swords_0";
        var WRS1 = "#witcher_rpg:relic_swords_1";
        var WRS2 = "#witcher_rpg:relic_swords_2";

        var WT0 = "#witcher_rpg:trinkets_0";
        var WT1 = "#witcher_rpg:trinkets_1";

        var WED = "#witcher_rpg:enhanced_diagrams";
        var WSD = "#witcher_rpg:superior_diagrams";
        var WMD = "#witcher_rpg:mastercrafted_diagrams";
        var WGD = "#witcher_rpg:grandmaster_diagrams";

        itemLootConfig = new LootConfig();
        var items = itemLootConfig.injectors;
        var items_regex = itemLootConfig.regex_injectors;

        ///WITCHER LOOT TABLES
        List.of("witcher_rpg:chests/witcher_grave").
                forEach(id ->
                        items.put(id, new LootConfig.Pool()
                                .rolls(0.8)
                                .add(WG0).weight(2)
                                .add(WR0).weight(2)
                                .add(WRS0).weight(1)
                                .add(WED).weight(1)
                        ));
        List.of("witcher_rpg:chests/old_ruins").
                forEach(id ->
                        items.put(id, new LootConfig.Pool()
                                .rolls(1.0)
                                .add(WG0).weight(2)
                                .add(WR0).weight(2)
                                .add(WRS1).weight(1)
                                .add(WED).weight(1)
                                .add(WSD).weight(1)
                                .add(WT0).weight(1)
                        ));
        List.of("witcher_rpg:chests/hideout_base").
                forEach(id ->
                        items.put(id, new LootConfig.Pool()
                                .bonus_rolls(1)
                                .rolls(1.0)
                                .add(WT0).weight(3)
                                .add(WT1).weight(1)
                                .add(WG1).weight(2)
                                .add(WR1).weight(2)
                                .add(WRS1, true).weight(2)
                                .add(WRS2).weight(1)
                        ));


        ///THE CONJUNCTION OF THE SPHERES LOOT TABLES
        List.of("tcots_witcher:chests/village/desert_herbalist",
                        "tcots_witcher:chests/village/plains_herbalist",
                        "tcots_witcher:chests/village/savanna_herbalist",
                        "tcots_witcher:chests/village/snowy_herbalist",
                        "tcots_witcher:chests/village/taiga_herbalist").
                forEach(id ->
                        items.put(id, new LootConfig.Pool()
                                .rolls(0.3)
                                .add(WG0)
                                .add(WR0)
                        ));
        List.of("tcots_witcher:chests/troll/forest_troll_barrel",
                        "tcots_witcher:chests/troll/ice_troll_barrel",
                        "tcots_witcher:chests/troll/troll_barrel").
                forEach(id ->
                        items.put(id, new LootConfig.Pool()
                                .rolls(0.4)
                                .add(WG0)
                                .add(WR0)
                                .add(WG1)
                                .add(WR1)
                                .add(WED)
                                .add(WSD)
                        ));
        List.of("tcots_witcher:chests/ice_giant_treasure").
                forEach(id ->
                        items.put(id, new LootConfig.Pool()
                                .bonus_rolls(1)
                                .rolls(1.0)
                                .add(WG2)
                                .add(WR2)
                                .add(WSD)
                                .add(WT0)
                                .add(WSD)
                                .add(WMD)
                                .add(WGD)
                        ));
        List.of("tcots_witcher:entities/ice_giant").
                forEach(id ->
                        items.put(id, new LootConfig.Pool()
                                .rolls(1.0)
                                .add(WRS1, true)
                        ));

        /// VANILLA MINECRAFT LOOT TABLES
        List.of("minecraft:chests/bastion_treasure")
                .forEach(id -> items.put(id, new LootConfig.Pool()
                        .rolls(0.75)
                        .add(WMD)
                        .add(WGD)
                ));
        List.of("minecraft:chests/village/village_weaponsmith")
                .forEach(id -> items.put(id, new LootConfig.Pool()
                        .rolls(0.5)
                        .add(WR0).weight(1)
                        .add(WED).weight(2)
                        .add(WSD).weight(1)
                ));
        List.of("minecraft:chests/village/village_armorer")
                .forEach(id -> items.put(id, new LootConfig.Pool()
                        .rolls(0.5)
                        .add(WG0).weight(1)
                        .add(WED).weight(2)
                        .add(WSD).weight(1)
                ));
        List.of("minecraft:chests/stronghold_library",
                        "minecraft:chests/woodland_mansion")
                .forEach(id -> items.put(id, new LootConfig.Pool()
                        .rolls(0.75)
                        .add(WMD).weight(1)
                        .add(WSD).weight(2)
                ));
        List.of("minecraft:chests/pillager_outpost")
                .forEach(id -> items.put(id, new LootConfig.Pool()
                        .rolls(0.75)
                        .add(WED)
                ));
    }
}
