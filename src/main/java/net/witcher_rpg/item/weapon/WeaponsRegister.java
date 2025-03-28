package net.witcher_rpg.item.weapon;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.WeaponConfig;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_power.api.SpellPowerMechanics;

import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.item.WitcherGroup;
import net.witcher_rpg.item.WitcherItems;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WeaponsRegister {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();
    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Weapon.Factory factory, WeaponConfig defaults, Equipment.WeaponType type) {
        var entry = new Weapon.Entry(MOD_ID, name, material, factory, defaults, type);
        entry.castSpell();
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

    private static final Identifier ATTACK_DAMAGE = Identifier.ofVanilla("generic.attack_damage");
    private static final Identifier ATTACK_SPEED = Identifier.ofVanilla("generic.attack_speed");
    private static final Identifier ADRENALINE = Identifier.of("witcher_rpg:adrenaline_modifier");
    private static final Identifier AARD_INTENSITY = Identifier.of("witcher_rpg:aard_intensity");
    private static final Identifier AXII_INTENSITY = Identifier.of("witcher_rpg:axii_intensity");
    private static final Identifier IGNI_INTENSITY = Identifier.of("witcher_rpg:igni_intensity");
    private static final Identifier QUEN_INTENSITY = Identifier.of("witcher_rpg:quen_intensity");
    private static final Identifier YRDEN_INTENSITY = Identifier.of("witcher_rpg:yrden_intensity");
    private static final Identifier SIGN_INTENSITY = Identifier.of("witcher_rpg:sign_intensity");

    public static float witcher_sword_attackSpeed = -2.4f;

    private static Weapon.Entry witcherswords(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, WitcherSword::new, new WeaponConfig(damage, -1.6F), Equipment.WeaponType.SWORD);
    }
    private static Weapon.Entry witcher_silver_sword(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, WitcherSilverSwordItem::new, new WeaponConfig(damage, -1.6F), Equipment.WeaponType.SWORD);
    }
    private static Weapon.Entry witcher_steel_sword(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, WitcherSteelSwordItem::new, new WeaponConfig(damage, -1.6F), Equipment.WeaponType.SWORD);
    }
    private static Weapon.Entry aerondight(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, AerondightRelictItem::new, new WeaponConfig(damage, -1.6F), Equipment.WeaponType.SWORD);
    }
    private static Weapon.Entry ultimatum(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, UltimatumRelicItem::new, new WeaponConfig(damage, -1.6F), Equipment.WeaponType.SWORD);
    }
    private static Weapon.Entry winters_blade(String name, Weapon.CustomMaterial material, float damage) {
        return entry(name, material, WintersBladeRelicItem::new, new WeaponConfig(damage, -1.6F), Equipment.WeaponType.SWORD);
    }

    public static final Weapon.Entry iron_witcher_sword = witcherswords("iron_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(Items.IRON_INGOT)), 4.0F)
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,0.5F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.02F))
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry golden_witcher_sword = witcherswords("golden_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.GOLD, () -> Ingredient.ofItems(Items.GOLD_INGOT)), 2.0F)
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,0.5F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.02F))
            .loot(Equipment.LootProperties.of("golden"));
    public static final Weapon.Entry diamond_witcher_sword = witcherswords("diamond_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND)), 5.0F)
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,1.0F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.035F))
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry netherite_witcher_sword = witcherswords("netherite_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)), 6.0F)
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,2.5F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.06F))
            .loot(Equipment.LootProperties.of(3));
    public static final Weapon.Entry steel_witcher_sword = witcher_steel_sword("steel_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(WitcherItems.STEEL_INGOT)), 4.5F)
            .attribute(AttributeModifier.multiply(ADRENALINE,0.035F))
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry dark_iron_witcher_sword = witcher_steel_sword("dark_iron_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(WitcherItems.DARK_IRON_INGOT)), 5.0F)
            .attribute(AttributeModifier.multiply(ADRENALINE,0.065F))
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry dark_steel_witcher_sword = witcher_steel_sword("dark_steel_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(WitcherItems.DARK_STEEL_INGOT)), 6.0F)
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,1.5F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.1F))
            .loot(Equipment.LootProperties.of(3));
    public static final Weapon.Entry witcher_silver_sword = witcher_silver_sword("silver_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.IRON, () -> Ingredient.ofItems(WitcherItems.SILVER_INGOT)), 4.5F)
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,2.5F))
            .loot(Equipment.LootProperties.of(1));
    public static final Weapon.Entry witcher_meteorite_sword = witcher_silver_sword("meteorite_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(WitcherItems.METEORITE_INGOT)), 4.5F)
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,3.0F))
            .loot(Equipment.LootProperties.of(2));
    public static final Weapon.Entry witcher_meteorite_silver_sword = witcher_silver_sword("meteorite_silver_witcher_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(WitcherItems.METEORITE_SILVER_INGOT)), 5.0F)
            .attribute(AttributeModifier.bonus(SIGN_INTENSITY,4.0F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.05F))
            .loot(Equipment.LootProperties.of(3));
public static final Weapon.Entry aerondight = aerondight("aerondight_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE,() -> Ingredient.ofItems(WitcherItems.METEORITE_SILVER_INGOT)), 10.5F);
public static final Weapon.Entry ultimatum = ultimatum("ultimatum_sword",
        Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(WitcherItems.STEEL_INGOT)), 5.5F)
        .attribute(AttributeModifier.multiply(SpellPowerMechanics.CRITICAL_DAMAGE.id, 0.1F))
        .attribute(AttributeModifier.multiply(ADRENALINE,0.075F))
        .attribute(AttributeModifier.bonus(IGNI_INTENSITY,4.0F))
        ;

    public static final Weapon.Entry winters_blade = winters_blade("winters_blade_sword",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(WitcherItems.STEEL_INGOT)), 5.5F)
            .attribute(AttributeModifier.bonus(AARD_INTENSITY,4.0F))
            .attribute(AttributeModifier.multiply(ADRENALINE,0.05F))
            .attribute(AttributeModifier.multiply(SpellPowerMechanics.CRITICAL_DAMAGE.id, 0.15F))
            ;


    private static final String BETTER_END = "betterend";
    private static final String BETTER_NETHER = "betternether";
    private static final String AETHER = "aether";
    //Registration
    public static void register(Map<String, WeaponConfig> configs) {
        if(FabricLoader.getInstance().isModLoaded(BETTER_NETHER) || WitcherClassMod.tweaksConfig.value.ignore_items_required_mods){
            var repair = ingredient("betternether:nether_ruby", FabricLoader.getInstance().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            witcherswords( "ruby_witcher_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair),7.0F)
                    .attribute(AttributeModifier.bonus(SIGN_INTENSITY,4.0F))
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.1F))
                    .loot(Equipment.LootProperties.of(4));

        }
        if(FabricLoader.getInstance().isModLoaded(BETTER_END)|| WitcherClassMod.tweaksConfig.value.ignore_items_required_mods){
            var repair = ingredient("betterend:aeternium_ingot", FabricLoader.getInstance().isModLoaded(BETTER_NETHER), Items.NETHERITE_INGOT);
            witcherswords( "aeternium_witcher_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair),7.0F)
                    .attribute(AttributeModifier.bonus(SIGN_INTENSITY,4.0F))
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.1F))
                    .loot(Equipment.LootProperties.of(4));
        }
        if(FabricLoader.getInstance().isModLoaded(AETHER)|| WitcherClassMod.tweaksConfig.value.ignore_items_required_mods){
            var repair = ingredient("aether:ambrosium_shard", FabricLoader.getInstance().isModLoaded(AETHER), Items.NETHERITE_INGOT);
            witcherswords( "aether_witcher_sword",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, repair),7.0F)
                    .attribute(AttributeModifier.bonus(SIGN_INTENSITY,4.0F))
                    .attribute(AttributeModifier.multiply(ADRENALINE,0.1F))
                    .loot(Equipment.LootProperties.of("aether"));
        }
        Weapon.register(configs, entries, WitcherGroup.WITCHER_KEY);
    }
}
