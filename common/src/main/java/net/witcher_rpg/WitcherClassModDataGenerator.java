package net.witcher_rpg;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.item.set.EquipmentSet;
import net.spell_engine.api.item.set.EquipmentSetRegistry;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;
import net.witcher_rpg.datagen.WitcherModelProvider;
import net.witcher_rpg.datagen.WitcherRecipeProvider;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import net.witcher_rpg.item.WitcherArmorDiagrams;
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.item.weapon.WeaponsRegister;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.spell.SetBonuses;
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
		pack.addProvider(EnchantmentGenerator::new);
		pack.addProvider(LangGenerator::new);
		pack.addProvider(WitcherModelProvider::new);
		pack.addProvider(WitcherRecipeProvider::new);
		pack.addProvider(EquipmentSetGenerator::new);
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

		List<String> relicSwords1Keywords = List.of("azure_wrath","reach_of_the_damned","ultimatum","winters");
		List<String> relicSwords2Keywords = List.of("aerondight","iris");
		List<String> silverSwordsKeywords = List.of("silver", "meteorite","aerondight","azure_wrath","reach_of_the_damned");
		List<String> steelSwordsKeywords = List.of("steel", "dark_iron","iris","ultimatum","winters");
		List<String> meleeArmorKeywords = List.of("ursine", "feline");
		List<String> magicArmorKeywords = List.of("witcher", "wolven","griffin");
		TagKey relicsKey = TagKey.of(RegistryKeys.ITEM, Identifier.of("relics_rpgs", "all"));
		List<String> trinkets0Keywords = List.of("crystal_skull","rose_of_remembrance","pure_silver");
		List<String> trinkets1Keywords = List.of("sunstone");

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			///WEAPONS
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
			generateWitcherWeaponTags(
					WeaponsRegister.entries.stream()
							.filter(entry -> relicSwords1Keywords.stream().anyMatch(entry.name()::contains))
							.toList(),
					WitcherItemTags.RELIC_SWORDS_1
			);
			generateWitcherWeaponTags(
					WeaponsRegister.entries.stream()
							.filter(entry -> relicSwords2Keywords.stream().anyMatch(entry.name()::contains))
							.toList(),
					WitcherItemTags.RELIC_SWORDS_2
			);
			///ARMOR
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
			///RELICS
			var relicsAll = getOrCreateTagBuilder(relicsKey);
			WitcherTrinkets.entries.stream()
					.filter(entry -> !entry.name().toLowerCase().contains("medallion"))
					.forEach(entry -> relicsAll.addOptional(entry.id()));
			var glyphs0 = getOrCreateTagBuilder(WitcherItemTags.GLYPHS_0);
			WitcherTrinkets.entries.stream()
					.filter(entry -> entry.name().toLowerCase().contains("lesser"))
					.forEach(entry -> glyphs0.addOptional(entry.id()));
			var glyphs1 = getOrCreateTagBuilder(WitcherItemTags.GLYPHS_1);
			WitcherTrinkets.entries.stream()
					.filter(entry -> entry.name().toLowerCase().contains("glyph"))
					.filter(entry -> !entry.name().toLowerCase().contains("greater"))
					.filter(entry -> !entry.name().toLowerCase().contains("lesser"))
					.forEach(entry -> glyphs1.addOptional(entry.id()));
			var glyphs2 = getOrCreateTagBuilder(WitcherItemTags.GLYPHS_2);
			WitcherTrinkets.entries.stream()
					.filter(entry -> entry.name().toLowerCase().contains("greater"))
					.forEach(entry -> glyphs2.addOptional(entry.id()));
			var trinkets0 = getOrCreateTagBuilder(WitcherItemTags.TRINKETS_0);
			WitcherTrinkets.entries.stream()
					.filter(entry -> trinkets0Keywords.stream().anyMatch(entry.name()::contains)).toList()
					.forEach(entry -> trinkets0.addOptional(entry.id()));
			var trinkets1 = getOrCreateTagBuilder(WitcherItemTags.TRINKETS_1);
			WitcherTrinkets.entries.stream()
					.filter(entry -> trinkets1Keywords.stream().anyMatch(entry.name()::contains)).toList()
					.forEach(entry -> trinkets1.addOptional(entry.id()));
			///DIAGRAMS
			var enhanced = getOrCreateTagBuilder(WitcherItemTags.ENHANCED_DIAGRAMS);
			WitcherArmorDiagrams.ENTRIES.stream()
					.filter(entry -> entry.id().toString().toLowerCase().contains("enhanced"))
					.forEach(entry -> enhanced.addOptional(entry.id()));
			var superior = getOrCreateTagBuilder(WitcherItemTags.SUPERIOR_DIAGRAMS);
			WitcherArmorDiagrams.ENTRIES.stream()
					.filter(entry -> entry.id().toString().toLowerCase().contains("superior"))
					.forEach(entry -> superior.addOptional(entry.id()));
			var mastercrafted = getOrCreateTagBuilder(WitcherItemTags.MASTERCRAFTED_DIAGRAMS);
			WitcherArmorDiagrams.ENTRIES.stream()
					.filter(entry -> entry.id().toString().toLowerCase().contains("mastercrafted"))
					.forEach(entry -> mastercrafted.addOptional(entry.id()));
			var grandmaster = getOrCreateTagBuilder(WitcherItemTags.GRANDMASTER_DIAGRAMS);
			WitcherArmorDiagrams.ENTRIES.stream()
					.filter(entry -> entry.id().toString().toLowerCase().contains("grandmaster"))
					.forEach(entry -> grandmaster.addOptional(entry.id()));
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
			SetBonuses.all.forEach(entry -> {
				translationBuilder.add(EquipmentSet.translationKey(entry.id()), entry.title());
			});
			translationBuilder.add("filled_map.witcher_rpg.feline_hideouts", "Scavenger Hunt: Cat School Gear");
			translationBuilder.add("filled_map.witcher_rpg.griffin_hideouts", "Scavenger Hunt: Griffin School Gear");
			translationBuilder.add("filled_map.witcher_rpg.ursine_hideouts", "Scavenger Hunt: Bear School Gear");
			translationBuilder.add("filled_map.witcher_rpg.wolven_hideouts", "Scavenger Hunt: Wolf School Gear");
		}
	}

	public static class EquipmentSetGenerator extends FabricDynamicRegistryProvider {

		public EquipmentSetGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}
	@Override
	protected void configure(RegistryWrapper.WrapperLookup registries, FabricDynamicRegistryProvider.Entries entries) {
		RegistryEntryLookup<Item> itemLookup = registries.createRegistryLookup().getOrThrow(RegistryKeys.ITEM);
		for (var set: SetBonuses.all) {
			var items = RegistryEntryList.of(
					set.itemSupplier().get().stream()
							.map(id -> itemLookup.getOrThrow(RegistryKey.of(RegistryKeys.ITEM, id)))
							.toList()
			);
			entries.add(
					RegistryKey.of(EquipmentSetRegistry.KEY, set.id()),
					new EquipmentSet.Definition(
							set.id().getPath(),
							items,
							set.bonuses()
					)
			);
		}
	}
		@Override
		public String getName() {
			return "Equipment Set Generator";
		}
	}

}
