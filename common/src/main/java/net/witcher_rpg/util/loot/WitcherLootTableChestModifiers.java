package net.witcher_rpg.util.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.witcher_rpg.item.WitcherItems;

public class WitcherLootTableChestModifiers {
    private static final Identifier WEAPON_SMITH_CHEST =
            Identifier.of("minecraft", "chests/village/village_weaponsmith");
    private static final float silver_ingot_chance = 0.65f;
    private static final float steel_ingot_chance = 0.65f;
    private static final float dark_iron_ingot_chance = 0.15f;
    private static final float meteorite_ingot_chance = 0.15f;


    public static void modifyChestLootTables(){
        net.fabricmc.fabric.api.loot.v3.LootTableEvents.MODIFY.register(new net.fabricmc.fabric.api.loot.v3.LootTableEvents.Modify() {
            @Override
            public void modifyLootTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
                if (source.isBuiltin() && WEAPON_SMITH_CHEST.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(silver_ingot_chance))
                            .with(ItemEntry.builder(WitcherItems.SILVER_INGOT))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    LootPool.Builder poolBuilder2 = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(dark_iron_ingot_chance))
                            .with(ItemEntry.builder(WitcherItems.DARK_IRON_INGOT))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    LootPool.Builder poolBuilder3 = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(steel_ingot_chance))
                            .with(ItemEntry.builder(WitcherItems.STEEL_INGOT))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    LootPool.Builder poolBuilder4 = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(meteorite_ingot_chance))
                            .with(ItemEntry.builder(WitcherItems.METEORITE))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                    tableBuilder.pool(poolBuilder2.build());
                    tableBuilder.pool(poolBuilder3.build());
                    tableBuilder.pool(poolBuilder4.build());
                }
            }
        });
    }
}
