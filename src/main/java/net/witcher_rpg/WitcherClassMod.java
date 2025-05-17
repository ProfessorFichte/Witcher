package net.witcher_rpg;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.ConfigFile;
import net.witcher_rpg.client.particle.Particles;
import net.witcher_rpg.config.EffectsConfig;
import net.witcher_rpg.config.TweaksConfig;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.effect.Effects;
import net.witcher_rpg.entity.YrdenEntity;
import net.witcher_rpg.entity.YrdenMagicTrapEntity;
import net.witcher_rpg.sounds.Sounds;
import net.witcher_rpg.worldgen.OreGen;
import net.witcher_rpg.blocks.WitcherBlocks;
import net.witcher_rpg.item.armor.Armors;
import net.tinyconfig.ConfigManager;
import net.witcher_rpg.config.Default;
import net.witcher_rpg.custom.CustomSpells;
import net.witcher_rpg.item.WitcherGroup;
import net.witcher_rpg.item.WitcherItems;
import net.witcher_rpg.item.weapon.WeaponsRegister;
import net.witcher_rpg.util.loot.WitcherLootTableChestModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WitcherClassMod implements ModInitializer {
	public static final String MOD_ID = "witcher_rpg";
    public static final Logger LOGGER = LoggerFactory.getLogger("witcher_rpg");

	public static ConfigManager<ConfigFile.Equipment> itemConfig = new ConfigManager<>
			("equipment_v1", Default.itemConfig)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();

	public static ConfigManager<EffectsConfig> effectsConfig = new ConfigManager<EffectsConfig>
			("effects_v1", new EffectsConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<TweaksConfig>
			("tweaks", new TweaksConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();

	private void registerItemGroup() {
		WitcherGroup.WITCHER = FabricItemGroup.builder()
				.icon(() -> new ItemStack(Armors.witcherArmorSet.chest.asItem()))
				.displayName(Text.translatable("itemGroup." + MOD_ID + ".general"))
				.build();
		Registry.register(Registries.ITEM_GROUP, WitcherGroup.WITCHER_KEY, WitcherGroup.WITCHER);
	}


	@Override
	public void onInitialize() {
		itemConfig.refresh();
		effectsConfig.refresh();
		tweaksConfig.refresh();
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			tweaksConfig.value.ignore_items_required_mods = true;
		}
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
	}
	static{
		YrdenEntity.ENTITY_TYPE = Registry.register(
				Registries.ENTITY_TYPE,
				Identifier.of(MOD_ID, "yrden"),
				FabricEntityTypeBuilder.<YrdenEntity>create(SpawnGroup.MISC, YrdenEntity::new)
						.dimensions(EntityDimensions.changing(6F, 0.5F))
						.fireImmune()
						.trackRangeBlocks(128)
						.trackedUpdateRate(20)
						.build()
		);
		YrdenMagicTrapEntity.ENTITY_TYPE = Registry.register(
				Registries.ENTITY_TYPE,
				Identifier.of(MOD_ID, "yrden_magical_trap"),
				FabricEntityTypeBuilder.<YrdenMagicTrapEntity>create(SpawnGroup.MISC, YrdenMagicTrapEntity::new)
						.dimensions(EntityDimensions.changing(6F, 0.5F))
						.fireImmune()
						.trackRangeBlocks(128)
						.trackedUpdateRate(20)
						.build()
		);
	}
	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}