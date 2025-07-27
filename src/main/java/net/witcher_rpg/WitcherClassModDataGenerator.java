package net.witcher_rpg;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;
import net.witcher_rpg.item.WitcherItems;
import net.witcher_rpg.item.weapon.WeaponsRegister;
import net.witcher_rpg.item.armor.Armors;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WitcherClassModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(UnsmeltGenerator::new);
	}

	public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
		public ItemTagGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			generateWeaponTags(WeaponsRegister.entries);
			generateArmorTags(
					Armors.entries.stream().filter(entry -> entry.name().contains("witcher")).toList(),
					RPGSeriesItemTags.ArmorMetaType.MAGIC
			);
			generateArmorTags(
					Armors.entries.stream().filter(entry -> entry.name().contains("griffin")).toList(),
					RPGSeriesItemTags.ArmorMetaType.MAGIC
			);
			generateArmorTags(
					Armors.entries.stream().filter(entry -> entry.name().contains("wolven")).toList(),
					RPGSeriesItemTags.ArmorMetaType.MAGIC
			);
			generateArmorTags(
					Armors.entries.stream().filter(entry -> entry.name().contains("feline")).toList(),
					RPGSeriesItemTags.ArmorMetaType.MELEE
			);
			generateArmorTags(
					Armors.entries.stream().filter(entry -> entry.name().contains("ursine")).toList(),
					RPGSeriesItemTags.ArmorMetaType.MELEE
			);
		}
	}

	public static class UnsmeltGenerator extends FabricRecipeProvider {
		public UnsmeltGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		public static int UNSMELT_TIME = 300;

		@Override
		public void generate(RecipeExporter exporter) {
			disassembleArmor(exporter, Armors.witcherArmorSet, Items.LEATHER);
			disassembleArmor(exporter, Armors.felineSchoolArmorSet, WitcherItems.STEEL_NUGGET);
			disassembleArmor(exporter, Armors.enhancedFelineSchoolArmorSet, WitcherItems.STEEL_NUGGET);
			disassembleArmor(exporter, Armors.superiorFelineSchoolArmorSet, WitcherItems.STEEL_NUGGET);
			disassembleArmor(exporter, Armors.ursineArmorSet, WitcherItems.STEEL_NUGGET);
			disassembleArmor(exporter, Armors.enhancedUrsineArmorSet, WitcherItems.STEEL_NUGGET);
			disassembleArmor(exporter, Armors.superiorUrsineArmorSet, WitcherItems.STEEL_NUGGET);
			disassembleArmor(exporter, Armors.griffinArmorSet, WitcherItems.SILVER_NUGGET);
			disassembleArmor(exporter, Armors.enhancedGriffinArmorSet, WitcherItems.SILVER_NUGGET);
			disassembleArmor(exporter, Armors.superiorGriffinArmorSet, WitcherItems.SILVER_NUGGET);
			disassembleArmor(exporter, Armors.wolvenArmorSet, WitcherItems.SILVER_NUGGET);
			disassembleArmor(exporter, Armors.enhancedWolvenArmorSet, WitcherItems.SILVER_NUGGET);
			disassembleArmor(exporter, Armors.superiorWolvenArmorSet, WitcherItems.SILVER_NUGGET);

			disassemble(exporter,
					WeaponsRegister.entries.stream()
							.filter(entry -> entry.id().getPath().contains("gold"))
							.map(entry -> (ItemConvertible) entry.item()).toList(),
					Items.GOLD_NUGGET);
			disassemble(exporter,
					WeaponsRegister.entries.stream()
							.filter(entry -> entry.id().getPath().contains("iron"))
							.map(entry -> (ItemConvertible) entry.item()).toList(),
					Items.IRON_NUGGET);
			disassemble(exporter,
					WeaponsRegister.entries.stream()
							.filter(entry -> entry.id().getPath().contains("netherite"))
							.map(entry -> (ItemConvertible) entry.item()).toList(),
					Items.NETHERITE_SCRAP);
			disassemble(exporter,
					WeaponsRegister.entries.stream()
							.filter(entry -> entry.id().getPath().contains("steel"))
							.map(entry -> (ItemConvertible) entry.item()).toList(),
					WitcherItems.STEEL_NUGGET);
			disassemble(exporter,
					WeaponsRegister.entries.stream()
							.filter(entry -> entry.id().getPath().contains("silver"))
							.map(entry -> (ItemConvertible) entry.item()).toList(),
					WitcherItems.SILVER_NUGGET);
		}

		private static void disassembleArmor(RecipeExporter exporter, Armor.Set armorSet, Item output) {
			FabricRecipeProvider.offerSmelting(exporter,
					armorSet.pieces(),
					RecipeCategory.MISC,
					output,
					0.1f,
					UNSMELT_TIME,
					"disassemble"
			);
			FabricRecipeProvider.offerBlasting(exporter,
					armorSet.pieces(),
					RecipeCategory.MISC,
					output,
					0.1f,
					UNSMELT_TIME / 2,
					"disassemble"
			);
		}

		private static void disassemble(RecipeExporter exporter, List<ItemConvertible> items, Item output) {
			FabricRecipeProvider.offerSmelting(exporter,
					items,
					RecipeCategory.MISC,
					output,
					0.1f,
					UNSMELT_TIME,
					"disassemble"
			);
			FabricRecipeProvider.offerBlasting(exporter,
					items,
					RecipeCategory.MISC,
					output,
					0.1f,
					UNSMELT_TIME / 2,
					"disassemble"
			);
		}
	}
}
