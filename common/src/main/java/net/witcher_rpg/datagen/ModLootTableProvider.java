package net.witcher_rpg.datagen;

import com.mrpg_lib.compat.spell_engine.loot.SpecificSpellScrollPoolLootFunction;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.ExplorationMapLootFunction;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public abstract class ModLootTableProvider extends SimpleFabricLootTableProvider {
    private BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter;

    protected ModLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup, LootContextType type) {
        super(output, registryLookup, type);
    }

    protected abstract void generate();

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        this.exporter = exporter;
        generate();
    }

    protected void table(String path, LootPool.Builder... pools) {
        var table = LootTable.builder();
        for (var pool : pools) table.pool(pool);
        exporter.accept(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, path)), table);
    }

    protected static LootPool.Builder pool(LootNumberProvider rolls, LootPoolEntry.Builder<?>... entries) {
        var pool = LootPool.builder().rolls(rolls);
        for (var entry : entries) pool.with(entry);
        return pool;
    }

    protected static LootNumberProvider constant(float value) {
        return ConstantLootNumberProvider.create(value);
    }

    protected static LootNumberProvider uniform(float min, float max) {
        return UniformLootNumberProvider.create(min, max);
    }

    protected static LeafEntry.Builder<?> item(String id, int weight, LootFunction.Builder... functions) {
        Item item = Registries.ITEM.get(Identifier.of(id));
        if (item == Items.AIR) throw new IllegalStateException("Loot table item is not registered: " + id);
        var entry = ItemEntry.builder(item).weight(weight);
        for (var function : functions) entry.apply(function);
        return entry;
    }


    protected static LeafEntry.Builder<?> empty(int weight) {
        return EmptyEntry.builder().weight(weight);
    }

    protected static LeafEntry.Builder<?> nested(String id, int weight) {
        return LootTableEntry.builder(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(id))).weight(weight);
    }

    protected static ConditionalLootFunction.Builder<?> count(LootNumberProvider count) {
        return SetCountLootFunction.builder(count);
    }

    protected static EnchantWithLevelsLootFunction.Builder enchant(float min, float max) {
        return new EnchantWithLevelsLootFunction.Builder(uniform(min, max));
    }

    protected static LootFunction.Builder scroll(int tierMin, int tierMax, int count, String... pools) {
        return SpecificSpellScrollPoolLootFunction.builder(List.of(pools), constant(tierMin), constant(tierMax), constant(count), List.of());
    }


    protected static LootFunction.Builder explorationMap(String destination, String decoration, int zoom, boolean skipExistingChunks) {
        return new ExplorationMapLootFunction.Builder()
                .withDestination(TagKey.of(RegistryKeys.STRUCTURE, Identifier.of(destination)))
                .withDecoration(Registries.MAP_DECORATION_TYPE.getEntry(Identifier.of(decoration)).orElseThrow())
                .withZoom((byte) zoom)
                .withSkipExistingChunks(skipExistingChunks);
    }

    protected static LootFunction.Builder translatedName(String key) {
        return SetNameLootFunction.builder(Text.translatable(key), SetNameLootFunction.Target.CUSTOM_NAME);
    }

    protected static LootCondition.Builder chance(float chance) {
        return RandomChanceLootCondition.builder(chance);
    }



    public static class Chests extends ModLootTableProvider {
        public Chests(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup, LootContextTypes.CHEST);
        }

        @Override
        protected void generate() {
            chestsFelineHideout();
            chestsGriffinHideout();
            chestsHideoutBase();
            chestsOldRuins();
            chestsUrsineHideout();
            chestsWitcherGrave();
            chestsWolvenHideout();
            mapsFelineScavengerHunt();
            mapsGriffinScavengerHunt();
            mapsScavengerHunts();
            mapsUrsineScavengerHunt();
            mapsWolvenScavengerHunt();
        }

        private void chestsFelineHideout() {
            table("chests/feline_hideout",
                pool(uniform(1, 1),
                        nested("witcher_rpg:chests/hideout_base", 1)));
        }

        private void chestsGriffinHideout() {
            table("chests/griffin_hideout",
                pool(uniform(1, 1),
                        nested("witcher_rpg:chests/hideout_base", 1)));
        }

        private void chestsHideoutBase() {
            table("chests/hideout_base",
                pool(uniform(2, 4),
                        item("minecraft:chain", 10, count(uniform(1, 3))),
                        item("witcher_rpg:meteorite_ingot", 10, count(uniform(1, 3))),
                        item("witcher_rpg:dark_iron_ingot", 10, count(uniform(1, 2))),
                        item("mrpg_lib:hardened_leather", 7, count(uniform(1, 2))),
                        item("spell_engine:spell_scroll", 7, scroll(2, 4, 1, "#witcher_rpg:spell_scroll/signs", "#witcher_rpg:spell_scroll/fencing")),
                        item("witcher_rpg:meteorite_witcher_sword", 4),
                        item("witcher_rpg:diamond_witcher_sword", 4),
                        item("witcher_rpg:dark_iron_witcher_sword", 4),
                        item("witcher_rpg:dimeritium_ingot", 2)).bonusRolls(constant(1)));
        }

        private void chestsOldRuins() {
            table("chests/old_ruins",
                pool(uniform(1, 1),
                        empty(1),
                        nested("witcher_rpg:maps/scavenger_hunts", 1)),
                pool(uniform(2, 4),
                        item("minecraft:chain", 5, count(uniform(1, 3))),
                        item("witcher_rpg:silver_ingot", 10, count(uniform(1, 3))),
                        item("witcher_rpg:steel_ingot", 10, count(uniform(1, 2))),
                        item("mrpg_lib:hardened_leather", 7, count(uniform(1, 2))),
                        item("spell_engine:spell_scroll", 7, scroll(1, 2, 1, "#witcher_rpg:spell_scroll/signs", "#witcher_rpg:spell_scroll/fencing"))).bonusRolls(constant(1)),
                pool(uniform(1, 2),
                        item("witcher_rpg:witcher_head", 1),
                        item("witcher_rpg:witcher_chest", 1),
                        item("witcher_rpg:witcher_legs", 1),
                        item("witcher_rpg:witcher_feet", 1),
                        item("witcher_rpg:silver_witcher_sword", 1),
                        item("witcher_rpg:iron_witcher_sword", 1),
                        item("witcher_rpg:steel_witcher_sword", 1)).bonusRolls(constant(1)));
        }

        private void chestsUrsineHideout() {
            table("chests/ursine_hideout",
                pool(uniform(1, 1),
                        nested("witcher_rpg:chests/hideout_base", 1)));
        }

        private void chestsWitcherGrave() {
            table("chests/witcher_grave",
                pool(uniform(1, 1),
                        empty(3),
                        nested("witcher_rpg:maps/scavenger_hunts", 1)),
                pool(uniform(1, 3),
                        item("minecraft:leather", 10, count(uniform(1, 3))),
                        item("witcher_rpg:silver_nugget", 10, count(uniform(3, 5))),
                        item("witcher_rpg:steel_nugget", 10, count(uniform(3, 5))),
                        item("witcher_rpg:raw_silver", 10, count(uniform(1, 3))),
                        item("witcher_rpg:silver_ingot", 5, count(uniform(1, 2))),
                        item("witcher_rpg:steel_nugget", 10, count(uniform(1, 2))),
                        item("mrpg_lib:hardened_leather", 7, count(uniform(1, 2))),
                        item("spell_engine:spell_scroll", 5, scroll(1, 3, 1, "#witcher_rpg:spell_scroll/signs", "#witcher_rpg:spell_scroll/fencing"))).bonusRolls(constant(1)),
                pool(uniform(1, 1),
                        empty(1),
                        item("witcher_rpg:witcher_head", 1),
                        item("witcher_rpg:witcher_chest", 1),
                        item("witcher_rpg:witcher_legs", 1),
                        item("witcher_rpg:witcher_feet", 1),
                        item("witcher_rpg:silver_witcher_sword", 1),
                        item("witcher_rpg:iron_witcher_sword", 1),
                        item("witcher_rpg:steel_witcher_sword", 1)));
        }

        private void chestsWolvenHideout() {
            table("chests/wolven_hideout",
                pool(uniform(1, 1),
                        nested("witcher_rpg:chests/hideout_base", 1)));
        }

        private void mapsFelineScavengerHunt() {
            table("maps/feline_scavenger_hunt",
                pool(uniform(1, 1),
                        item("minecraft:map", 1, explorationMap("witcher_rpg:feline_hideouts", "witcher_rpg:witcher_hideout", 2, false), translatedName("filled_map.witcher_rpg.feline_hideouts"))));
        }

        private void mapsGriffinScavengerHunt() {
            table("maps/griffin_scavenger_hunt",
                pool(uniform(1, 1),
                        item("minecraft:map", 1, explorationMap("witcher_rpg:griffin_hideouts", "witcher_rpg:witcher_hideout", 2, false), translatedName("filled_map.witcher_rpg.griffin_hideouts"))));
        }

        private void mapsScavengerHunts() {
            table("maps/scavenger_hunts",
                pool(uniform(1, 1),
                        nested("witcher_rpg:maps/feline_scavenger_hunt", 1),
                        nested("witcher_rpg:maps/griffin_scavenger_hunt", 1),
                        nested("witcher_rpg:maps/ursine_scavenger_hunt", 1),
                        nested("witcher_rpg:maps/wolven_scavenger_hunt", 1)));
        }

        private void mapsUrsineScavengerHunt() {
            table("maps/ursine_scavenger_hunt",
                pool(uniform(1, 1),
                        item("minecraft:map", 1, explorationMap("witcher_rpg:ursine_hideouts", "witcher_rpg:witcher_hideout", 2, false), translatedName("filled_map.witcher_rpg.ursine_hideouts"))));
        }

        private void mapsWolvenScavengerHunt() {
            table("maps/wolven_scavenger_hunt",
                pool(uniform(1, 1),
                        item("minecraft:map", 1, explorationMap("witcher_rpg:wolven_hideouts", "witcher_rpg:witcher_hideout", 2, false), translatedName("filled_map.witcher_rpg.wolven_hideouts"))));
        }
    }
}
