package net.witcher_rpg.util.loot;

import net.jewelry.gems.GemComponents;
import net.jewelry.gems.GemCut;
import net.jewelry.gems.GemCutRegistry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.compat.jewelry.JewelrySocketFeature;

import java.util.function.Consumer;

public class WitcherLootInjector {
    public static void configure(RegistryWrapper.WrapperLookup registries, Identifier id, Consumer<LootPool> poolAdder) {
        var config = WitcherClassMod.lootInjectionConfig.value;
        var tableId = id.toString();
        var pool = config.entries.get(tableId);
        if (pool == null) {
            return;
        }

        var rolls = pool.rolls() > 0 ? pool.rolls() : 1F;
        LootPool.Builder lootPoolBuilder = LootPool.builder();

        var attempts = Math.ceil(rolls);
        var chance = pool.rolls() / attempts;
        lootPoolBuilder.rolls(BinomialLootNumberProvider.create((int) attempts, (float) chance));
        lootPoolBuilder.bonusRolls(ConstantLootNumberProvider.create(0));

        for (var entry : pool.items()) {
            var entryId = entry.itemId();
            var weight = entry.weight();
            var minAmount = entry.minAmount();
            var maxAmount = entry.maxAmount();
            if (entryId == null || entryId.isEmpty()) {
                continue;
            }
            var item = Registries.ITEM.get(Identifier.of(entryId));
            if (item == null) {
                continue;
            }
            var lootEntry = ItemEntry.builder(item)
                    .weight(weight);
            var cutId = entry.cutId();
            if (cutId != null && !cutId.isEmpty() && JewelrySocketFeature.isJewelryPresent()) {
                var cutEntry = resolveCut(registries, Identifier.of(cutId));
                if (cutEntry != null) {
                    lootEntry.apply(SetComponentsLootFunction.builder(GemComponents.CUT, cutEntry));
                }
            }
            lootPoolBuilder.with(lootEntry);
            lootPoolBuilder.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minAmount, maxAmount)));
        }
        poolAdder.accept(lootPoolBuilder.build());
    }

    private static RegistryEntry<GemCut> resolveCut(RegistryWrapper.WrapperLookup registries, Identifier cutId) {
        return GemCutRegistry.from(registries)
                .flatMap(registry -> registry.getOptional(RegistryKey.of(GemCutRegistry.KEY, cutId)))
                .map(reference -> (RegistryEntry<GemCut>) reference)
                .orElse(null);
    }

    private static LootNumberProvider numberProvider(float min, float max) {
        if (max <= min) {
            return ConstantLootNumberProvider.create(min);
        } else {
            return UniformLootNumberProvider.create(min, max);
        }
    }
}
