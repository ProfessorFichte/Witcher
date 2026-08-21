package net.witcher_rpg.item.weapon;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.WeaponConfig;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.Weapon;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.item.WitcherGroup;
import net.witcher_rpg.item.WitcherMaterials;
import net.witcher_rpg.spell.WitcherPassives;
import net.witcher_rpg.spell.WitcherSpells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WeaponsRegister {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();
    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Weapon.Factory factory, WeaponConfig defaults, Equipment.WeaponType weaponType) {
        var entry = new Weapon.Entry(MOD_ID, name, material, factory, defaults, weaponType);
        entry.spellContainer(SpellContainers.forMeleeWeapon());
        entries.add(entry);
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = Identifier.of(idString);
        if (requirement) {
            return () -> {
                return Ingredient.ofItems(fallback);
            };
        } else {
            return () -> {
                var item = Registries.ITEM.get(id);
                var ingredient = item != null ? item : fallback;
                return Ingredient.ofItems(ingredient);
            };
        }
    }

    /// MINECRAFT ATTRIBUTES
    private static final Identifier ATTACK_DAMAGE = Identifier.ofVanilla("generic.attack_damage");
    private static final Identifier ATTACK_SPEED = Identifier.ofVanilla("generic.attack_speed");
    /// WITCHER ATTRIBUTES
    private static final Identifier ADRENALINE = Identifier.of("witcher_rpg:adrenaline_modifier");
    private static final Identifier AARD_INTENSITY = Identifier.of("witcher_rpg:aard_intensity");
    private static final Identifier AXII_INTENSITY = Identifier.of("witcher_rpg:axii_intensity");
    private static final Identifier IGNI_INTENSITY = Identifier.of("witcher_rpg:igni_intensity");
    private static final Identifier QUEN_INTENSITY = Identifier.of("witcher_rpg:quen_intensity");
    private static final Identifier YRDEN_INTENSITY = Identifier.of("witcher_rpg:yrden_intensity");
    private static final Identifier SIGN_INTENSITY = Identifier.of("witcher_rpg:sign_intensity");
    /// CRITICAL STRIKE MOD ATTRIBUTES
    private static final String CRIT_MOD_ID = "critical_strike";
    private static final Identifier CRIT_CHANCE_ID = Identifier.of(CRIT_MOD_ID, "chance");
    private static final Identifier CRIT_DAMAGE_ID = Identifier.of(CRIT_MOD_ID, "damage");
    /// MRPG-LIB ATTRIBUTES
    private static final Identifier ARMOR_PIERCING = Identifier.of("more_rpg_classes:armor_piercing");
    private static final Identifier BLEEDING_CHANCE = Identifier.of("more_rpg_classes:bleeding_chance");
    private static final Identifier BURNING_CHANCE = Identifier.of("more_rpg_classes:burning_chance");
    private static final Identifier FREEZE_CHANCE = Identifier.of("more_rpg_classes:freeze_chance");
    private static final Identifier POISON_CHANCE = Identifier.of("more_rpg_classes:poison_chance");
    private static final Identifier STAGGER_CHANCE = Identifier.of("more_rpg_classes:stagger_chance");
    private static final Identifier STUN_CHANCE = Identifier.of("more_rpg_classes:stun_chance");


    public static float witcher_sword_attackSpeed = -2.4f;

    private static final int TIER1_RUNESTONE_SLOTS = 1;
    private static final int TIER2_RUNESTONE_SLOTS = 2;
    private static final int TIER3_RUNESTONE_SLOTS = 3;

    private static int determineRunestoneSlots(String swordName) {
        if (swordName.contains("netherite_witcher_sword") ||
            swordName.contains("dark_steel_witcher_sword") ||
            swordName.contains("meteorite_silver_witcher_sword") ||
            swordName.contains("ruby_witcher_sword") ||
            swordName.contains("aeternium_witcher_sword") ||
            swordName.contains("aether_witcher_sword")) {
            return TIER2_RUNESTONE_SLOTS;
        }
        if (swordName.contains("winters_blade_sword") ||
            swordName.contains("ultimatum_sword") ||
            swordName.contains("azure_wrath_sword") ||
            swordName.contains("reach_of_the_damned_sword") ||
            swordName.contains("aerondight_sword") ||
            swordName.contains("iris_sword")) {
            return TIER3_RUNESTONE_SLOTS;
        }
        return TIER1_RUNESTONE_SLOTS;
    }

    ///WITCHER PASSIVES
    public static Identifier silver_sword = Identifier.of(MOD_ID, "silver_sword_passive");
    public static Identifier aerondight_passive = Identifier.of(MOD_ID, "aerondight_passive");
    public static Identifier reach_of_the_damned_passive = Identifier.of(MOD_ID, "reach_of_the_damned_passive");
    public static Identifier iris_passive = Identifier.of(MOD_ID, "iris_passive");


    private static Weapon.Entry witcherswords(String name, Weapon.CustomMaterial material, float damage) {
        int slots = determineRunestoneSlots(name);
        Weapon.Factory factory = slots == 2 ? WitcherSword.with2Slots() : WitcherSword.with1Slot();
        return entry(name, material, factory, new WeaponConfig(damage, witcher_sword_attackSpeed), Equipment.WeaponType.SPELL_BLADE)
                .spellContainer(SpellContainers.forMeleeWeapon().withSpellId(WitcherSpells.defensive_witcher_mechanics.id())
                        .withAdditionalSpell(List.of(WitcherPassives.silver_sword_passive().id().toString())));
    }
    private static Weapon.Entry witcherSilverSwords(String name, Weapon.CustomMaterial material, float damage) {
        int slots = determineRunestoneSlots(name);
        Weapon.Factory factory = slots == 2 ? WitcherSword.with2Slots() : WitcherSword.with1Slot();
        return entry(name, material, factory, new WeaponConfig(damage, witcher_sword_attackSpeed), Equipment.WeaponType.SPELL_BLADE)
                .spellContainer(SpellContainers.forMeleeWeapon().withSpellId(WitcherSpells.defensive_witcher_mechanics.id()));
    }

    private static Weapon.Entry witcherrelicswords(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, WitcherRelicSword::new, new WeaponConfig(damage, witcher_sword_attackSpeed), Equipment.WeaponType.SPELL_BLADE)
                .spellContainer(SpellContainers.forMeleeWeapon().withSpellId(WitcherSpells.defensive_witcher_mechanics.id()));
    }
    private static Weapon.Entry witcherSilverRelicSwords(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, WitcherRelicSword::new, new WeaponConfig(damage, witcher_sword_attackSpeed), Equipment.WeaponType.SPELL_BLADE)
                .spellContainer(SpellContainers.forMeleeWeapon().withSpellId(WitcherSpells.defensive_witcher_mechanics.id())
                        .withAdditionalSpell(List.of(WitcherPassives.silver_sword_passive().id().toString())));
    }

    public static final Weapon.Entry iron_witcher_sword = witcherswords("iron_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 4.0F)
            .translatedName("Iron Witcher Sword")
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,0.5F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.02F))
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry golden_witcher_sword = witcherswords("golden_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 2.0F)
            .translatedName("Golden Witcher Sword")
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,0.5F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.02F))
            .loot(Equipment.LootProperties.of("golden"));
    public static final Weapon.Entry diamond_witcher_sword = witcherswords("diamond_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 5.0F)
            .translatedName("Diamond Witcher Sword")
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,1.0F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.035F))
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry netherite_witcher_sword = witcherswords("netherite_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 6.0F)
            .translatedName("Netherite Witcher Sword")
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,2.5F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.06F))
            .loot(Equipment.LootProperties.of(3));
    public static final Weapon.Entry steel_witcher_sword = witcherswords("steel_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(WitcherMaterials.STEEL_INGOT.item())), 4.5F)
            .translatedName("Steel Witcher Sword")
            .attribute(AttributeModifier.multiply(ADRENALINE,0.035F))
            .attribute(AttributeModifier.multiply(BLEEDING_CHANCE,0.05F))
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry dark_iron_witcher_sword = witcherswords("dark_iron_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(WitcherMaterials.DARK_IRON_INGOT.item())), 5.0F)
            .translatedName("Dark Iron Witcher Sword")
            .attribute(AttributeModifier.multiply(ADRENALINE,0.065F))
            .attribute(AttributeModifier.multiply(BLEEDING_CHANCE,0.1F))
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry dark_steel_witcher_sword = witcherswords("dark_steel_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(WitcherMaterials.DARK_STEEL_INGOT.item())), 6.0F)
            .translatedName("Dark Steel Witcher Sword")
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,1.5F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.1F))
            .attribute(AttributeModifier.multiply(BLEEDING_CHANCE,0.15F))
            .loot(Equipment.LootProperties.of(3));
    public static final Weapon.Entry witcher_silver_sword = witcherSilverSwords("silver_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(WitcherMaterials.SILVER_INGOT.item())), 4.5F)
            .translatedName("Silver Witcher Sword")
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,2.5F))
            .withAdditionalSpell(silver_sword.toString())
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry witcher_meteorite_sword = witcherSilverSwords("meteorite_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(WitcherMaterials.METEORITE_INGOT.item())), 4.5F)
            .translatedName("Meteorite Witcher Sword")
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,3.0F))
            .withAdditionalSpell(silver_sword.toString())
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry witcher_meteorite_silver_sword = witcherSilverSwords("meteorite_silver_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(WitcherMaterials.METEORITE_SILVER_INGOT.item())), 5.0F)
            .translatedName("Meteorite Silver Witcher Sword")
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,4.0F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.05F))
            .withAdditionalSpell(silver_sword.toString())
            .loot(Equipment.LootProperties.of(3));

    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";
    private static final String AETHER = "aether";
    private static final String ARSENAL = "arsenal";
    //Registration
    public static void register(Map<String, WeaponConfig> configs) {
        if(FabricLoader.getInstance().isModLoaded(BETTER_NETHER) || WitcherClassMod.tweaksConfig.value.ignore_items_required_mods){
            var repair = ingredient("betternether:nether_ruby", FabricLoader.getInstance().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            witcherswords( "ruby_witcher_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair),7.0F)
                    .translatedName("Ruby Witcher Sword")
                    .attribute(AttributeModifier.bonus(SIGN_INTENSITY,4.0F))
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.1F))
                    .loot(Equipment.LootProperties.of(4));

        }
        if(FabricLoader.getInstance().isModLoaded(BETTER_END)|| WitcherClassMod.tweaksConfig.value.ignore_items_required_mods){
            var repair = ingredient("betterend:aeternium_ingot", FabricLoader.getInstance().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            witcherswords( "aeternium_witcher_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair),7.0F)
                    .translatedName("Aeternium Witcher Sword")
                    .attribute(AttributeModifier.bonus(SIGN_INTENSITY,4.0F))
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.1F))
                    .loot(Equipment.LootProperties.of(4));
        }
        if(FabricLoader.getInstance().isModLoaded(AETHER)|| WitcherClassMod.tweaksConfig.value.ignore_items_required_mods){
            var repair = ingredient("aether:ambrosium_shard", FabricLoader.getInstance().isModLoaded(AETHER), Items.NETHERITE_INGOT);
            witcherswords( "aether_witcher_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair),7.0F)
                    .translatedName("Heavenly Witcher Sword")
                    .attribute(AttributeModifier.bonus(SIGN_INTENSITY,4.0F))
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.1F))
                    .loot(Equipment.LootProperties.of("aether"));
        }
        if(FabricLoader.getInstance().isModLoaded(ARSENAL)|| WitcherClassMod.tweaksConfig.value.ignore_items_required_mods){
            witcherrelicswords("winters_blade_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(WitcherMaterials.STEEL_INGOT.item())), 6.5F)
                    .translatedName("Winter´s Blade")
                    .attribute(AttributeModifier.multiply(FREEZE_CHANCE,0.08F))
                    .attribute(AttributeModifier.multiply(ARMOR_PIERCING,0.15F))
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.05F))
                    .attribute(AttributeModifier.multiply(CRIT_DAMAGE_ID, 0.15F))
                    .rarity = Rarity.RARE;
            witcherrelicswords("ultimatum_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(WitcherMaterials.STEEL_INGOT.item())), 6.5F)
                    .translatedName("Ultimatum")
                    .attribute(AttributeModifier.bonus(IGNI_INTENSITY,3.0F))
                    .attribute(AttributeModifier.multiply(CRIT_DAMAGE_ID, 0.1F))
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.075F))
                    .attribute(AttributeModifier.multiply(STAGGER_CHANCE,0.12F))
                    .rarity = Rarity.RARE;
            witcherSilverRelicSwords("azure_wrath_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND,() -> Ingredient.ofItems(WitcherMaterials.SILVER_INGOT.item())), 6.0F)
                    .translatedName("Azure Wrath")
                    .attribute(AttributeModifier.bonus(SIGN_INTENSITY,4.0F))
                    .attribute(AttributeModifier.bonus(YRDEN_INTENSITY,3.0F))
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.05F))
                    .attribute(AttributeModifier.multiply(STUN_CHANCE,0.08F))
                    .rarity = Rarity.RARE;
            witcherSilverRelicSwords("reach_of_the_damned_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND,() -> Ingredient.ofItems(WitcherMaterials.SILVER_INGOT.item())), 6.0F)
                    .translatedName("Reach of the Damned")
                    .attribute(AttributeModifier.bonus(SIGN_INTENSITY,4.0F))
                    .attribute(AttributeModifier.bonus(AXII_INTENSITY,3.0F))
                    .attribute(AttributeModifier.bonus(QUEN_INTENSITY,3.0F))
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.05F))
                    .withAdditionalSpell(reach_of_the_damned_passive.toString())
                    .rarity = Rarity.RARE;
            witcherSilverRelicSwords("aerondight_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE,() -> Ingredient.ofItems(WitcherMaterials.METEORITE_SILVER_INGOT.item())), 6.5F)
                    .translatedName("Aerondight")
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.1F))
                    .attribute(AttributeModifier.bonus(SIGN_INTENSITY,5.0F))
                    .withAdditionalSpell(WitcherPassives.aerondight_passive().id().toString())
                    .rarity = Rarity.EPIC;
            witcherrelicswords("iris_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE,() -> Ingredient.ofItems(WitcherMaterials.DARK_STEEL_INGOT.item())), 7.0F)
                    .translatedName("Iris")
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.15F))
                    .withAdditionalSpell(WitcherPassives.iris_passive().id().toString())
                    .rarity = Rarity.EPIC;
        }
        Weapon.register(configs, entries, WitcherGroup.WITCHER_KEY);
    }
}
