package net.witcher_rpg;

import net.spell_engine.Platform;
import net.spell_engine.PlatformEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.event.CombatEvents;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.utils.AnimationHelper;
import net.witcher_rpg.util.loot.WitcherLootInjector;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.spell_engine.rpg_series.loot.LootConfig;
import net.spell_engine.rpg_series.loot.LootHelper;
import net.witcher_rpg.config.*;
import net.witcher_rpg.custom.CustomSpellImpacts;
import net.witcher_rpg.custom.WitcherSchoolWeakness;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.WitcherEntities;
import net.witcher_rpg.item.WitcherMaterials;
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.sounds.Sounds;
import net.witcher_rpg.spell.WitcherSpells;
import net.witcher_rpg.blocks.WitcherBlocks;
import net.witcher_rpg.item.armor.Armors;
import net.tiny_config.ConfigManager;
import net.witcher_rpg.item.WitcherGroup;
import net.witcher_rpg.item.weapon.WeaponsRegister;
import net.witcher_rpg.worldgen.map.ModMapDecorations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.witcher_rpg.util.loot.Defaults;


public class WitcherClassMod {
	public static final String MOD_ID = "witcher_rpg";
    public static final Logger LOGGER = LoggerFactory.getLogger("witcher_rpg");

	public static ConfigManager<ConfigFile.Equipment> itemConfig = new ConfigManager<>
			("equipment_v4", Default.itemConfig)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<ConfigFile.Effects> effectConfig = new ConfigManager<>
			("effects_v0", new ConfigFile.Effects())
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
	public static ConfigManager<TrinketConfig> trinketConfig = new ConfigManager<>
			("trinkets_v0", new TrinketConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<LootConfig> lootEquipmentConfig = new ConfigManager<>
			("loot_equipment_v1", Defaults.itemLootConfig)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.constrain(config -> LootConfig.constrainValues(config, Defaults.itemLootConfig))
			.build();
	public static ConfigManager<WeaknessConfig> weaknessConfig = new ConfigManager<>
			("elemental_weaknesses", WitcherSchoolWeakness.createDefault())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.validate(WeaknessConfig::isValid)
			.build();
	public static final ConfigManager<LootInjectionConfig> lootInjectionConfig = new ConfigManager<>
			("loot_injection", LootInjectionConfig.init())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();


	public static void init() {
		lootEquipmentConfig.refresh();
		trinketConfig.refresh();
		itemConfig.refresh();
		effectConfig.refresh();
		weaknessConfig.refresh();
		tweaksConfig.refresh();
		lootInjectionConfig.refresh();
		if (Platform.util().isDevelopmentEnvironment()) {
			tweaksConfig.value.ignore_items_required_mods = true;
		}
		WitcherSpellSchools.initialize();
		CustomSpellImpacts.registerCustomImpacts();
		/// SPECIFIC LOOT INJECTIONS
		PlatformEvents.onLootTableModify(context -> {
			var tableId = context.tableId().toString();
			if (!lootInjectionConfig.value.entries.containsKey(tableId)) {
				return;
			}
			WitcherLootInjector.configure(context.registries(), context.tableId(), context::addPool);
		});
		/// TAG BASED LOOT INJECTION
		LootHelper.TAG_CACHE.refresh();
		PlatformEvents.onLootTableModify(context -> {
			LootHelper.configure(context.registries(), context.tableId(), context::existingPools, context::addPool, lootEquipmentConfig.value, "witcher_rpg");
		});
		PlatformEvents.onServerStarted((server) -> {
			LootHelper.updateTagCache(lootEquipmentConfig.value);
		});
		PlatformEvents.onDataPackReloadComplete(() -> {
			LootHelper.updateTagCache(lootEquipmentConfig.value);
		});
		CombatEvents.PLAYER_SHIELD_BLOCK.register(args -> {
			PlayerEntity player = args.player();
			if (player.getWorld().isClient()) return;
			if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
			if (!(player instanceof SpellCasterEntity caster)) return;
			if (!caster.isCastingSpell()) return;

			var process = caster.getSpellCastProcess();
			if (process == null || !process.id().equals(Identifier.of(MOD_ID, "defensive_witcher_mechanics"))) return;

			AnimationHelper.sendAnimation(serverPlayer, Platform.tracking(serverPlayer),
					SpellCast.Animation.MISC, PlayerAnimation.of("witcher_rpg:witcher_reflexes"), 1F);
			caster.getInteractor().requestClear();
		});
	}

	public static void registerMapDecorations() {
		ModMapDecorations.register();
	}

	public static void registerBlocks() {
		WitcherBlocks.register();
	}
	public static void registerSounds() {
		Sounds.register();
	}
	public static void registerEntities() {
		WitcherEntities.register();
	}
	public static void registerItems() {
		WitcherGroup.WITCHER = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
				.icon(() -> new ItemStack(WitcherTrinkets.WOLF_SCHOOL_MEDALLION.item().get()))
				.displayName(Text.translatable("itemGroup." + MOD_ID + ".general"))
				.build();
		Registry.register(Registries.ITEM_GROUP, WitcherGroup.WITCHER_KEY, WitcherGroup.WITCHER);
		WitcherGroup.registerItemGroups();
		WitcherMaterials.registerModItems();
		WeaponsRegister.register(itemConfig.value.weapons);
		Armors.register(itemConfig.value.armor_sets);
		WitcherTrinkets.register(trinketConfig.value.entries);
		itemConfig.save();
		trinketConfig.save();
	}
	public static void registerEffects() {
		WitcherStatusEffects.register(effectConfig.value);
		effectConfig.save();
	}
	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}