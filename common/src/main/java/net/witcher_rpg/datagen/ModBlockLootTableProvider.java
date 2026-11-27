package net.witcher_rpg.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.witcher_rpg.blocks.WitcherBlocks;
import net.witcher_rpg.item.WitcherMaterials;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generate() {
        Map<WitcherBlocks.Entry, WitcherMaterials.Entry> ores = Map.of(
                WitcherBlocks.SILVER_ORE, WitcherMaterials.RAW_SILVER,
                WitcherBlocks.DEEPSLATE_SILVER_ORE, WitcherMaterials.RAW_SILVER,
                WitcherBlocks.METEORITE_ORE, WitcherMaterials.METEORITE,
                WitcherBlocks.DARK_IRON_ORE, WitcherMaterials.RAW_DARK_IRON,
                WitcherBlocks.DEEPSLATE_DARK_IRON_ORE, WitcherMaterials.RAW_DARK_IRON,
                WitcherBlocks.NETHER_DARK_IRON_ORE, WitcherMaterials.RAW_DARK_IRON);
        for (var entry : WitcherBlocks.all) {
            var drop = ores.get(entry);
            if (drop != null) {
                addDrop(entry.block(), oreDrop(entry.block(), drop.item()));
            } else {
                addDrop(entry.block());
            }
        }
    }

    private LootTable.Builder oreDrop(Block block, Item drop) {
        RegistryWrapper.Impl<Enchantment> enchantments = registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        return dropsWithSilkTouch(block, applyExplosionDecay(block, ItemEntry.builder(drop)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                .apply(ApplyBonusLootFunction.oreDrops(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }
}
