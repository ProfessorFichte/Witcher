package net.witcher_rpg;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.spell_power.api.enchantment.SpellPowerEnchanting;
import net.witcher_rpg.client.particle.Particles;
import net.witcher_rpg.config.EffectsConfig;
import net.witcher_rpg.config.EnchantingConfig;
import net.witcher_rpg.custom.Enchantment_WitcherSpellSchool;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.effect.Effects;
import net.witcher_rpg.entity.YrdenEntity;
import net.witcher_rpg.entity.YrdenMagicTrapEntity;
import net.witcher_rpg.sounds.Sounds;
import net.witcher_rpg.worldgen.OreGen;
import net.witcher_rpg.blocks.WitcherBlocks;
import net.witcher_rpg.item.armor.Armors;
import net.spell_engine.api.item.ItemConfig;
import net.tinyconfig.ConfigManager;
import net.witcher_rpg.config.Default;
import net.witcher_rpg.custom.CustomSpells;
import net.witcher_rpg.item.WitcherGroup;
import net.witcher_rpg.item.WitcherItems;
import net.witcher_rpg.item.weapon.WeaponsRegister;
import net.witcher_rpg.util.loot.WitcherLootTableChestModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WitcherClassMod implements ModInitializer {
	public static final String MOD_ID = "witcher_rpg";
    public static final Logger LOGGER = LoggerFactory.getLogger("witcher_rpg");


	public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<ItemConfig>
			("items_v5", Default.itemConfig)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();

	public static ConfigManager<EffectsConfig> effectsConfig = new ConfigManager<EffectsConfig>
			("effects_v2", new EffectsConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static final ConfigManager<EnchantingConfig> enchantmentConfig = new ConfigManager<EnchantingConfig>
			("enchantments", new EnchantingConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.schemaVersion(4)
			.build();

	private void registerItemGroup() {
		WitcherGroup.WITCHER = FabricItemGroup.builder()
				.icon(() -> new ItemStack(WeaponsRegister.witcher_silver_sword.item()))
				.displayName(Text.translatable("itemGroup." + MOD_ID + ".general"))
				.build();
		Registry.register(Registries.ITEM_GROUP, WitcherGroup.WITCHER_KEY, WitcherGroup.WITCHER);
	}


	@Override
	public void onInitialize() {
		itemConfig.refresh();
		effectsConfig.refresh();
		enchantmentConfig.refresh();
		WitcherSpellSchools.initialize();
		WitcherLootTableChestModifiers.modifyChestLootTables();
		WitcherItems.registerModItems();
		Particles.register();
		Effects.register();
		WitcherGroup.registerItemGroups();
		OreGen.register();
		WitcherBlocks.register();
		CustomSpells.register();
		Sounds.register();
		WeaponsRegister.register(itemConfig.value.weapons);
		Armors.register(itemConfig.value.armor_sets);
		itemConfig.save();
		registerItemGroup();

		for(var entry: Enchantment_WitcherSpellSchool.all.entrySet()) {
			Registry.register(Registries.ENCHANTMENT, entry.getKey(), entry.getValue());
		}
		attachEnchantmentsToSchools();
	}
	private void attachEnchantmentsToSchools() {
		for(var school: SpellSchools.all()) {
			var poweringEnchantments = Enchantment_WitcherSpellSchool.all.entrySet().stream()
					.filter(entry -> entry.getValue().poweredSchools().contains(school))
					.map(Map.Entry::getValue)
					.toList();
			school.addSource(SpellSchool.Trait.POWER, new SpellSchool.Source(SpellSchool.Apply.MULTIPLY, query -> {
				double value = 0;
				for (var enchantment: poweringEnchantments) {
					var level = SpellPowerEnchanting.getEnchantmentLevel(enchantment, query.entity(), null);
					value = enchantment.amplified(value, level);
				}
				return value;
			}));
		}
	}

	static{
		YrdenEntity.ENTITY_TYPE = Registry.register(
				Registries.ENTITY_TYPE,
				new Identifier(MOD_ID, "yrden"),
				FabricEntityTypeBuilder.<YrdenEntity>create(SpawnGroup.MISC, YrdenEntity::new)
						.dimensions(EntityDimensions.changing(6F, 0.5F))
						.fireImmune()
						.trackRangeBlocks(128)
						.trackedUpdateRate(20)
						.build()
		);
		YrdenMagicTrapEntity.ENTITY_TYPE = Registry.register(
				Registries.ENTITY_TYPE,
				new Identifier(MOD_ID, "yrden_magical_trap"),
				FabricEntityTypeBuilder.<YrdenMagicTrapEntity>create(SpawnGroup.MISC, YrdenMagicTrapEntity::new)
						.dimensions(EntityDimensions.changing(6F, 0.5F))
						.fireImmune()
						.trackRangeBlocks(128)
						.trackedUpdateRate(20)
						.build()
		);
	}
}