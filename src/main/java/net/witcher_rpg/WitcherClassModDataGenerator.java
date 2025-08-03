package net.witcher_rpg;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import net.witcher_rpg.item.WitcherItems;
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.item.weapon.WeaponsRegister;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.spell.WitcherSpells;
import net.witcher_rpg.util.tags.WitcherItemTags;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherClassModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();
		pack.addProvider(SpellGen::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(UnsmeltGenerator::new);
		pack.addProvider(EnchantmentGenerator::new);
		pack.addProvider(ModelProvider::new);
		pack.addProvider(LangGenerator::new);
	}

	public static class SpellGen extends SpellGenerator {
		public SpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateSpells(Builder builder) {
			for (var entry: WitcherSpells.entries) {
				builder.add(entry.id(), entry.spell());
			}
		}
	}

	public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
		public ItemTagGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}


		public void generateWitcherWeaponTags(List<Weapon.Entry> weapons, TagKey tagKey) {
			Iterator var2 = weapons.iterator();
			while(var2.hasNext()) {
				Weapon.Entry weapon = (Weapon.Entry)var2.next();
				FabricTagProvider<Item>.FabricTagBuilder tag = this.getOrCreateTagBuilder(tagKey);
				tag.addOptional(weapon.id());
			}
		}

		public void generateWitcherArmorTag(List<Armor.Entry> armors, TagKey tagKey) {
			Iterator var3 = armors.iterator();
			while(var3.hasNext()) {
				Armor.Entry armor = (Armor.Entry)var3.next();
				FabricTagProvider<Item>.FabricTagBuilder tag = this.getOrCreateTagBuilder(tagKey);
				Iterator var19 = armor.armorSet().pieceIds().iterator();

				while(var19.hasNext()) {
					Object id = var19.next();
					tag.addOptional((Identifier)id);
				}
			}
		}

		List<String> silverSwordsKeywords = List.of("silver", "meteorite","aerondight","azure_wrath","reach_of_the_damned");
		List<String> steelSwordsKeywords = List.of("steel", "dark_iron","iris","ultimatum","winters");
		List<String> meleeArmorKeywords = List.of("ursine", "feline");
		List<String> magicArmorKeywords = List.of("witcher", "wolven","griffin");
		TagKey relicsKey = TagKey.of(RegistryKeys.ITEM, Identifier.of("relics_rpgs", "all"));

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			generateWeaponTags(WeaponsRegister.entries);
			generateWitcherWeaponTags(
					WeaponsRegister.entries,WitcherItemTags.WITCHER_SWORDS
			);
			generateWitcherWeaponTags(
					WeaponsRegister.entries.stream()
							.filter(entry -> silverSwordsKeywords.stream().anyMatch(entry.name()::contains))
							.toList(),
					WitcherItemTags.SILVER_SWORDS
			);
			generateWitcherWeaponTags(
					WeaponsRegister.entries.stream()
							.filter(entry -> steelSwordsKeywords.stream().anyMatch(entry.name()::contains))
							.toList(),
					WitcherItemTags.STEEL_SWORDS
			);
			generateArmorTags(
					Armors.entries.stream().filter(entry -> magicArmorKeywords.stream().anyMatch(entry.name()::contains)).toList(),
					RPGSeriesItemTags.ArmorMetaType.MAGIC
			);
			generateArmorTags(
					Armors.entries.stream().filter(entry -> meleeArmorKeywords.stream().anyMatch(entry.name()::contains)).toList(),
					RPGSeriesItemTags.ArmorMetaType.MELEE
			);
			generateWitcherArmorTag(
					Armors.entries,WitcherItemTags.WITCHER_ARMOR
			);
			var relicsAll = getOrCreateTagBuilder(relicsKey);
			WitcherTrinkets.entries.stream()
					.filter(entry -> !entry.name().toLowerCase().contains("medallion"))
					.forEach(entry -> relicsAll.addOptional(entry.id()));
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
	private static class EnchantmentGenerator extends FabricDynamicRegistryProvider {
		public EnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}
		@Override
		protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
			RegistryEntryLookup<Item> itemLookup = registries.createRegistryLookup().getOrThrow(RegistryKeys.ITEM);
			var bonus = 0.03F;
			var signIntensityId = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "sign_intensity"));
			Enchantment.Builder signIntensity = Enchantment.builder(
							Enchantment.definition(
									itemLookup.getOrThrow(WitcherItemTags.SIGN_INTENSITY_ENCHANTABLE),
									2, 5,
									Enchantment.leveledCost(1, 11),
									Enchantment.leveledCost(12, 11),
									1,
									AttributeModifierSlot.ARMOR)
					)
					.addEffect(
							EnchantmentEffectComponentTypes.ATTRIBUTES,
							new AttributeEnchantmentEffect(
									Identifier.of(MOD_ID, "sign_intensity"),
									WitcherAttributes.SIGN_INTENSITY,
									EnchantmentLevelBasedValue.linear(bonus),
									EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
					);
			entries.add(signIntensityId, signIntensity.build(signIntensityId.getValue()));
		}

		@Override
		public String getName() {
			return "enchantments";
		}
	}

	public static class ModelProvider extends FabricModelProvider {
		public ModelProvider(FabricDataOutput output) {
			super(output);
		}
		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

		}


		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {
			WitcherTrinkets.entries.forEach(entry -> {
				Item item = entry.item().get();
				Identifier itemId = Registries.ITEM.getId(item);
				Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());
				JsonObject json = new JsonObject();
				json.addProperty("parent", "item/generated");
				JsonObject textures = new JsonObject();
				textures.addProperty("layer0", "witcher_rpg:item/trinkets/" + entry.name());
				json.add("textures", textures);
				itemModelGenerator.writer.accept(modelId, () -> json);
			});
		}
	}

	public static class LangGenerator extends FabricLanguageProvider {
		protected LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, "en_us", registryLookup);
		}

		@Override
		public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, FabricLanguageProvider.TranslationBuilder translationBuilder) {
			WitcherTrinkets.entries.forEach(entry ->
					translationBuilder.add(entry.item().get().getTranslationKey(), entry.translatedName())
			);
			WitcherSpells.entries.forEach(entry -> {
				var id = entry.id();
				translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name" , entry.title());
				translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description" , entry.description());
			});
			WitcherStatusEffects.entries.forEach(entry -> {
				translationBuilder.add(entry.effect.getTranslationKey(), entry.title);
				translationBuilder.add(entry.effect.getTranslationKey() + ".description", entry.description);
			});
			translationBuilder.add("filled_map.witcher_rpg.feline_hideouts", "Scavenger Hunt: Cat School Gear");
			translationBuilder.add("filled_map.witcher_rpg.griffin_hideouts", "Scavenger Hunt: Griffin School Gear");
			translationBuilder.add("filled_map.witcher_rpg.ursine_hideouts", "Scavenger Hunt: Bear School Gear");
			translationBuilder.add("filled_map.witcher_rpg.wolven_hideouts", "Scavenger Hunt: Wolf School Gear");
		}
	}
}
