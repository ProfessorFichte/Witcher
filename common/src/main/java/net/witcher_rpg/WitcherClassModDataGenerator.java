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
import net.witcher_rpg.blocks.WitcherBlocks;
import net.witcher_rpg.datagen.*;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import net.witcher_rpg.item.WitcherArmorDiagrams;
import net.witcher_rpg.item.WitcherMaterials;
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
		WitcherVanillaAdvancementProvider.init();
		var pack = fabricDataGenerator.createPack();
		pack.addProvider(SpellGen::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(EnchantmentGenerator::new);
		pack.addProvider(LangGenerator::new);
		pack.addProvider(WitcherModelProvider::new);
		pack.addProvider(WitcherRecipeProvider::new);
		pack.addProvider(WitcherSmithingRecipeGenerator::new);
		pack.addProvider(EquipmentSetGenerator::new);
		pack.addProvider(WeaponAttributesGenerator::new);
		pack.addProvider(WitcherAdvancementProvider::new);
		pack.addProvider(WitcherVanillaAdvancementProvider::new);
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

		public void generateWitcherChestplateTag(List<Armor.Entry> armors, TagKey tagKey) {
			Iterator var3 = armors.iterator();
			while(var3.hasNext()) {
				Armor.Entry armor = (Armor.Entry)var3.next();
				FabricTagProvider<Item>.FabricTagBuilder tag = this.getOrCreateTagBuilder(tagKey);
				Iterator var19 = armor.armorSet().pieceIds().iterator();

				while(var19.hasNext()) {
					Object id = var19.next();
					Identifier identifier = (Identifier)id;
					if (identifier.getPath().contains("_chest")) {
						tag.addOptional(identifier);
					}
				}
			}
		}

		List<String> relicSwords1Keywords = List.of("azure_wrath","reach_of_the_damned","ultimatum","winters");
		List<String> relicSwords2Keywords = List.of("aerondight","iris");
		List<String> silverSwordsKeywords = List.of("silver", "meteorite","aerondight","azure_wrath","reach_of_the_damned");
		List<String> steelSwordsKeywords = List.of("steel", "dark_iron","iris","ultimatum","winters");
		List<String> meleeArmorKeywords = List.of("ursine", "feline");
		List<String> magicArmorKeywords = List.of("witcher", "wolven","griffin");
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
			var glyphs0 = getOrCreateTagBuilder(WitcherItemTags.GLYPHS_0);
			WitcherTrinkets.entries.stream()
					.filter(entry -> entry.name().toLowerCase().contains("lesser"))
					.filter(entry -> entry.name().toLowerCase().contains("glyph"))
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
					.filter(entry -> entry.name().toLowerCase().contains("glyph"))
					.forEach(entry -> glyphs2.addOptional(entry.id()));
			var runestones0 = getOrCreateTagBuilder(WitcherItemTags.RUNESTONES_0);
			WitcherTrinkets.entries.stream()
					.filter(entry -> entry.name().toLowerCase().contains("lesser"))
					.filter(entry -> entry.name().toLowerCase().contains("runestone"))
					.forEach(entry -> runestones0.addOptional(entry.id()));
			var runestones1 = getOrCreateTagBuilder(WitcherItemTags.RUNESTONES_1);
			WitcherTrinkets.entries.stream()
					.filter(entry -> entry.name().toLowerCase().contains("runestone"))
					.filter(entry -> !entry.name().toLowerCase().contains("greater"))
					.filter(entry -> !entry.name().toLowerCase().contains("lesser"))
					.forEach(entry -> runestones1.addOptional(entry.id()));
			var runestones2 = getOrCreateTagBuilder(WitcherItemTags.RUNESTONES_2);
			WitcherTrinkets.entries.stream()
					.filter(entry -> entry.name().toLowerCase().contains("greater"))
					.filter(entry -> entry.name().toLowerCase().contains("runestone"))
					.forEach(entry -> runestones2.addOptional(entry.id()));
			var trinkets0 = getOrCreateTagBuilder(WitcherItemTags.TRINKETS_0);
			WitcherTrinkets.entries.stream()
					.filter(entry -> trinkets0Keywords.stream().anyMatch(entry.name()::contains)).toList()
					.forEach(entry -> trinkets0.addOptional(entry.id()));
			var trinkets1 = getOrCreateTagBuilder(WitcherItemTags.TRINKETS_1);
			WitcherTrinkets.entries.stream()
					.filter(entry -> trinkets1Keywords.stream().anyMatch(entry.name()::contains)).toList()
					.forEach(entry -> trinkets1.addOptional(entry.id()));
			///DIAGRAMS
			var tier5ArmorTag = getOrCreateTagBuilder(RPGSeriesItemTags.LootTiers.get(5, RPGSeriesItemTags.LootCategory.ARMORS));
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
			WitcherArmorDiagrams.ENTRIES.stream()
					.filter(entry -> entry.id().toString().toLowerCase().contains("grandmaster"))
					.forEach(entry -> tier5ArmorTag.addOptional(entry.id()));
			///MISC
			WitcherMaterials.ENTRIES.stream()
					.filter(entry -> entry.id().toString().toLowerCase().contains("dimeritium_ingot"))
					.forEach(entry -> tier5ArmorTag.addOptional(entry.id()));

			///GLYPH & RUNESTONE ATTACHABLE
			// Glyph Attachable - all witcher chestplates
			generateWitcherChestplateTag(Armors.entries, WitcherItemTags.GLYPH_ATTACHABLE);

			// Runestone Attachable - all witcher swords
			var runestoneAttachable = getOrCreateTagBuilder(WitcherItemTags.RUNESTONE_ATTACHABLE);
			WeaponsRegister.entries.forEach(weapon -> runestoneAttachable.addOptional(weapon.id()));

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
			translationBuilder.add("itemGroup.witcher_rpg.general", "Witcher");
			/// ITEMS
			WitcherTrinkets.entries.forEach(entry ->
					translationBuilder.add(entry.item().get().getTranslationKey(), entry.translatedName())
			);
			WitcherMaterials.ENTRIES.forEach(entry ->
							translationBuilder.add(entry.item().getTranslationKey(), entry.translatedName())
					);
			WeaponsRegister.entries.forEach(entry -> {
				if (entry.item() != null && entry.translatedName() != null && !entry.translatedName().isEmpty()) {
					translationBuilder.add(entry.item(), entry.translatedName());
				}
			});
			Armors.entries.forEach(entry -> {
				var set = entry.armorSet();
				if (set.headTranslation != null && !set.headTranslation.isEmpty()) {
					translationBuilder.add(((Item) set.head).getTranslationKey(), set.headTranslation);
				}
				if (set.chestTranslation != null && !set.chestTranslation.isEmpty()) {
					translationBuilder.add(((Item) set.chest).getTranslationKey(), set.chestTranslation);
				}
				if (set.legsTranslation != null && !set.legsTranslation.isEmpty()) {
					translationBuilder.add(((Item) set.legs).getTranslationKey(), set.legsTranslation);
				}
				if (set.feetTranslation != null && !set.feetTranslation.isEmpty()) {
					translationBuilder.add(((Item) set.feet).getTranslationKey(), set.feetTranslation);
				}
			});
			translationBuilder.add("item.witcher_rpg.base_signs_spell_book", "Witcher Sign Manual");
			translationBuilder.add("item.witcher_rpg.base_signs.spell_scroll", "Witcher Sign Scroll");
			translationBuilder.add("item.witcher_rpg.fencing_spell_book", "Witcher Techniques");
			translationBuilder.add("item.witcher_rpg.fencing.spell_scroll", "Fencing Instruction");
			translationBuilder.add("item.witcher_rpg.master_spell_book", "Master Witcher Book");
			/// SMITHING TEMPLATES
			translationBuilder.add("item.witcher_rpg.enhanced_diagram", "Smithing Template");
			translationBuilder.add( "smithing_template.witcher_rpg.enhanced.applies_to", "Witcher Gear");
			translationBuilder.add( "smithing_template.witcher_rpg.enhanced.ingredients", "Steel Ingot / Silver Ingot");
			translationBuilder.add( "smithing_template.witcher_rpg.enhanced.title", "Diagram, Enhanced Witcher Gear");
			translationBuilder.add( "smithing_template.witcher_rpg.enhanced.base_slot_description", "Add Witcher Gear here.");
			translationBuilder.add( "smithing_template.witcher_rpg.enhanced.additions_slot_description", "Add Steel Ingot or Silver Ingot");

			translationBuilder.add("item.witcher_rpg.superior_diagram", "Smithing Template");
			translationBuilder.add( "smithing_template.witcher_rpg.superior.applies_to", "Enhanced Witcher Gear");
			translationBuilder.add( "smithing_template.witcher_rpg.superior.ingredients", "Dark Iron Ingot / Meteorite Ingot");
			translationBuilder.add( "smithing_template.witcher_rpg.superior.title", "Diagram, Superior Witcher Gear");
			translationBuilder.add( "smithing_template.witcher_rpg.superior.base_slot_description", "Add Enhanced Witcher Gear here.");
			translationBuilder.add( "smithing_template.witcher_rpg.superior.additions_slot_description", "Add Dark Iron Ingot or Meteorite Ingot");

			translationBuilder.add("item.witcher_rpg.mastercrafted_diagram", "Smithing Template");
			translationBuilder.add( "smithing_template.witcher_rpg.mastercrafted.applies_to", "Superior Witcher Gear");
			translationBuilder.add( "smithing_template.witcher_rpg.mastercrafted.ingredients", "Dark Steel Ingot / Meteorite Silver Ingot");
			translationBuilder.add( "smithing_template.witcher_rpg.mastercrafted.title", "Diagram, Mastercrafted Witcher Gear");
			translationBuilder.add( "smithing_template.witcher_rpg.mastercrafted.base_slot_description", "Add Superior Witcher Gear here.");
			translationBuilder.add( "smithing_template.witcher_rpg.mastercrafted.additions_slot_description", "Add Dark Steel Ingot or Meteorite Silver Ingot");

			translationBuilder.add("item.witcher_rpg.grandmaster_diagram", "Smithing Template");
			translationBuilder.add( "smithing_template.witcher_rpg.grandmaster.applies_to", "Mastercrafted Witcher Gear");
			translationBuilder.add( "smithing_template.witcher_rpg.grandmaster.ingredients", "Dimeritium Ingot");
			translationBuilder.add( "smithing_template.witcher_rpg.grandmaster.title", "Diagram, Grandmaster Witcher Gear");
			translationBuilder.add( "smithing_template.witcher_rpg.grandmaster.base_slot_description", "Add Mastercrafted Witcher Gear here.");
			translationBuilder.add( "smithing_template.witcher_rpg.grandmaster.additions_slot_description", "Add Dimeritium Ingot");

			translationBuilder.add("item.witcher_rpg.dimeritium_ingot.applies_to", "Mastercrafted Witcher Gear");
			translationBuilder.add("item.witcher_rpg.smithing_template.hint", "Witcher Gear upgrade Ingot");

			/// SPELLS
			WitcherSpells.entries.forEach(entry -> {
				var id = entry.id();
				translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name" , entry.title());
				translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description" , entry.description());
			});
			/// STATUS EFFECTS
			WitcherStatusEffects.entries.forEach(entry -> {
				translationBuilder.add(entry.effect.getTranslationKey(), entry.title);
				translationBuilder.add(entry.effect.getTranslationKey() + ".description", entry.description);
			});
			/// SET BONUSES
			SetBonuses.all.forEach(entry -> {
				translationBuilder.add(EquipmentSet.translationKey(entry.id()), entry.title());
			});
			// ADVANCEMENTS
			for (var entry : WitcherAdvancementProvider.getEntries()) {
				translationBuilder.add(entry.titleKey(), entry.title());
				translationBuilder.add(entry.descriptionKey(), entry.description());
			}
			for (var entry : WitcherVanillaAdvancementProvider.getEntries()) {
				translationBuilder.add(entry.titleKey(), entry.title());
				translationBuilder.add(entry.descriptionKey(), entry.description());
			}
			/// BLOCKS
			WitcherBlocks.all.forEach(entry -> {
				translationBuilder.add(entry.block().getTranslationKey(), entry.translatedName());
			});
			///MISC
			translationBuilder.add("filled_map.witcher_rpg.feline_hideouts", "Scavenger Hunt: Cat School Gear");
			translationBuilder.add("filled_map.witcher_rpg.griffin_hideouts", "Scavenger Hunt: Griffin School Gear");
			translationBuilder.add("filled_map.witcher_rpg.ursine_hideouts", "Scavenger Hunt: Bear School Gear");
			translationBuilder.add("filled_map.witcher_rpg.wolven_hideouts", "Scavenger Hunt: Wolf School Gear");

			translationBuilder.add("item.witcher_rpg.runestone.tooltip", "Attachable in the Anvil on Items with Runestone Slots.");
			translationBuilder.add("item.witcher_rpg.glyph.tooltip", "Attachable in the Anvil on Items with Glyph Slots.");

			translationBuilder.add("item.witcher_rpg.runestone_render.tooltip", "[Left Alt] for Runestone details");
			translationBuilder.add("item.witcher_rpg.glyph_render.tooltip", "[Left Alt] for Glyph details");

			translationBuilder.add("item.witcher_rpg.glyph_slots.tooltip", "Glyph Slots: ");
			translationBuilder.add("item.witcher_rpg.runestone_slots.tooltip", "Runestone Slots: ");

			translationBuilder.add("item.witcher_rpg.empty_glyph_slot", "Empty Glyph Slot");
			translationBuilder.add("item.witcher_rpg.empty_runestone_slot", "Empty Runestone Slot");

			translationBuilder.add("enchantment.witcher_rpg.sign_intensity", "Sign Intensity");
			translationBuilder.add("enchantment.witcher_rpg.sign_intensity.desc", "Increases all kinds of sign spell damage you deal");
			translationBuilder.add("enchantment.witcher_rpg.sign_intensity.description", "Increases all kinds of sign spell damage you deal");

			translationBuilder.add("entity.witcher_rpg.yrden_magical_trap", "Magical Trap");
			///ATTRIBUTES
			translationBuilder.add("attribute.name.witcher_rpg.adrenaline_modifier", "Adrenaline Gain");
			translationBuilder.add("attribute.name.witcher_rpg.sign_intensity", "Sign Intensity");
			translationBuilder.add("attribute.name.witcher_rpg.aard_intensity", "Aard Sign Intensity");
			translationBuilder.add("attribute.name.witcher_rpg.axii_intensity","Axii Sign Intensity" );
			translationBuilder.add("attribute.name.witcher_rpg.igni_intensity", "Igni Sign Intensity");
			translationBuilder.add("attribute.name.witcher_rpg.quen_intensity", "Quen Sign Intensity");
			translationBuilder.add("attribute.name.witcher_rpg.yrden_intensity", "Yrden Sign Intensity");
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
