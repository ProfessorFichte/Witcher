package net.witcher_rpg.custom;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.entity.attribute.WitcherAttributes;

import static net.spell_power.api.SpellPowerMechanics.PERCENT_ATTRIBUTE_BASELINE;

public class WitcherSpellSchools {


    public static final SpellSchool SIGN = new SpellSchool(SpellSchool.Archetype.MAGIC,
            Identifier.of(SpellPowerMod.ID, "sign"),
            0xfffeca,
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(WitcherClassMod.MOD_ID, "signs")),
            WitcherAttributes.SIGN_INTENSITY);
    public static final SpellSchool AARD = new SpellSchool(SpellSchool.Archetype.MAGIC,
            Identifier.of(SpellPowerMod.ID, "aard"),
            0x3beeff,
            DamageTypes.MAGIC,
            WitcherAttributes.AARD_INTENSITY);
    public static final SpellSchool AXII = new SpellSchool(SpellSchool.Archetype.MAGIC,
            Identifier.of(SpellPowerMod.ID, "axii"),
            0x008000,
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(WitcherClassMod.MOD_ID, "axii")),
            WitcherAttributes.AXII_INTENSITY);
    public static final SpellSchool IGNI = new SpellSchool(SpellSchool.Archetype.MAGIC,
            Identifier.of(SpellPowerMod.ID, "igni"),
            0xdd4e00,
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(WitcherClassMod.MOD_ID, "igni")),
            WitcherAttributes.IGNI_INTENSITY);
    public static final SpellSchool QUEN= new SpellSchool(SpellSchool.Archetype.MAGIC,
            Identifier.of(SpellPowerMod.ID, "quen"),
            0xfffeca,
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(WitcherClassMod.MOD_ID, "quen")),
            WitcherAttributes.QUEN_INTENSITY);
    public static final SpellSchool YRDEN= new SpellSchool(SpellSchool.Archetype.MAGIC,
            Identifier.of(SpellPowerMod.ID, "yrden"),
            0xe717fe,
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(WitcherClassMod.MOD_ID, "yrden")),
            WitcherAttributes.YRDEN_INTENSITY);
    public static final SpellSchool WITCHER_MELEE = new SpellSchool(SpellSchool.Archetype.MELEE,
            Identifier.of(SpellPowerMod.ID, "witcher_melee"),
            0xb3b3b3,
            DamageTypes.PLAYER_ATTACK,
            EntityAttributes.GENERIC_ATTACK_DAMAGE);


    public static void initialize() {
        int adrenaline_crit_damage_div = 3;
        int adrenaline_crit_chance_div = 10;
        final Identifier SPELL_POWER = Identifier.of(SpellPowerMod.ID, "spell_power");

        SIGN.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD, query -> {
            var power = query.entity().getAttributeValue(WitcherAttributes.SIGN_INTENSITY);
            var world = query.entity().getWorld();
            var spellpower = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(SPELL_POWER);
            if (spellpower.isPresent()) {
                var level = EnchantmentHelper.getLevel(spellpower.get(), query.entity().getMainHandStack());
                power *= 1 + (0.05 * level);
            }
            return power;
        });
        SIGN.addSource(SpellSchool.Trait.HASTE, SpellSchool.Apply.ADD, query -> {
                var value = query.entity().getAttributeValue(SpellPowerMechanics.HASTE.attributeEntry);
                var rate = (value  / PERCENT_ATTRIBUTE_BASELINE);
                return rate - 1;
        });
        SIGN.addSource(SpellSchool.Trait.CRIT_DAMAGE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_damage_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        SIGN.addSource(SpellSchool.Trait.CRIT_CHANCE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_chance_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        SpellSchools.configureSpellCritDamage(SIGN);
        SpellSchools.configureSpellHaste(SIGN);
        SpellSchools.configureSpellCritChance(SIGN);
        SpellSchools.register(SIGN);
        ///AARD
        AARD.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD, query -> {
            var power = query.entity().getAttributeValue(WitcherSpellSchools.AARD.attributeEntry);
            var power2 = query.entity().getAttributeValue(SpellSchools.LIGHTNING.attributeEntry);
            power *= power2 + 1 ;
            var world = query.entity().getWorld();
            var spellpower = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(SPELL_POWER);
            if (spellpower.isPresent()) {
                var level = EnchantmentHelper.getLevel(spellpower.get(), query.entity().getMainHandStack());
                power *= 1 + (0.05 * level);
            }
            return power;
        });
        AARD.addSource(SpellSchool.Trait.HASTE, SpellSchool.Apply.ADD, query -> {
            var value = query.entity().getAttributeValue(SpellPowerMechanics.HASTE.attributeEntry);
            var rate = (value  / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        AARD.addSource(SpellSchool.Trait.CRIT_DAMAGE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_damage_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        AARD.addSource(SpellSchool.Trait.CRIT_CHANCE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_chance_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        SpellSchools.configureSpellCritDamage(AARD);
        SpellSchools.configureSpellHaste(AARD);
        SpellSchools.configureSpellCritChance(AARD);
        SpellSchools.register(AARD);
        ///AXII
        AXII.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD, query -> {
            var power = query.entity().getAttributeValue(WitcherSpellSchools.AXII.attributeEntry);
            var power2 = query.entity().getAttributeValue(SpellSchools.SOUL.attributeEntry);
            power *= power2 + 1 ;
            var world = query.entity().getWorld();
            var spellpower = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(SPELL_POWER);
            if (spellpower.isPresent()) {
                var level = EnchantmentHelper.getLevel(spellpower.get(), query.entity().getMainHandStack());
                power *= 1 + (0.05 * level);
            }
            return power;
        });
        AXII.addSource(SpellSchool.Trait.HASTE, SpellSchool.Apply.ADD, query -> {
            var value = query.entity().getAttributeValue(SpellPowerMechanics.HASTE.attributeEntry);
            var rate = (value  / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        AXII.addSource(SpellSchool.Trait.CRIT_DAMAGE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_damage_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        AXII.addSource(SpellSchool.Trait.CRIT_CHANCE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_chance_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        SpellSchools.configureSpellCritDamage(AXII);
        SpellSchools.configureSpellHaste(AXII);
        SpellSchools.configureSpellCritChance(AXII);
        SpellSchools.register(AXII);
        ///IGNI
        IGNI.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD, query -> {
            var power = query.entity().getAttributeValue(WitcherSpellSchools.IGNI.attributeEntry);
            var power2 = query.entity().getAttributeValue(SpellSchools.FIRE.attributeEntry);
            power *= power2 + 1 ;
            var world = query.entity().getWorld();
            var spellpower = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(SPELL_POWER);
            if (spellpower.isPresent()) {
                var level = EnchantmentHelper.getLevel(spellpower.get(), query.entity().getMainHandStack());
                power *= 1 + (0.05 * level);
            }
            return power;
        });
        IGNI.addSource(SpellSchool.Trait.HASTE, SpellSchool.Apply.ADD, query -> {
            var value = query.entity().getAttributeValue(SpellPowerMechanics.HASTE.attributeEntry);
            var rate = (value  / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        IGNI.addSource(SpellSchool.Trait.CRIT_DAMAGE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_damage_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        IGNI.addSource(SpellSchool.Trait.CRIT_CHANCE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_chance_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        SpellSchools.configureSpellCritDamage(IGNI);
        SpellSchools.configureSpellHaste(IGNI);
        SpellSchools.configureSpellCritChance(IGNI);
        SpellSchools.register(IGNI);
        ///QUEN
        QUEN.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD, query -> {
            var power = query.entity().getAttributeValue(WitcherSpellSchools.QUEN.attributeEntry);
            var power2 = query.entity().getAttributeValue(SpellSchools.LIGHTNING.attributeEntry);
            power *= power2 + 1 ;
            var world = query.entity().getWorld();
            var spellpower = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(SPELL_POWER);
            if (spellpower.isPresent()) {
                var level = EnchantmentHelper.getLevel(spellpower.get(), query.entity().getMainHandStack());
                power *= 1 + (0.05 * level);
            }
            return power;
        });
        QUEN.addSource(SpellSchool.Trait.HASTE, SpellSchool.Apply.ADD, query -> {
            var value = query.entity().getAttributeValue(SpellPowerMechanics.HASTE.attributeEntry);
            var rate = (value  / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        QUEN.addSource(SpellSchool.Trait.CRIT_DAMAGE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_damage_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        QUEN.addSource(SpellSchool.Trait.CRIT_CHANCE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_chance_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        SpellSchools.configureSpellCritDamage(QUEN);
        SpellSchools.configureSpellHaste(QUEN);
        SpellSchools.configureSpellCritChance(QUEN);
        SpellSchools.register(QUEN);
        ///YRDEN
        YRDEN.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD, query -> {
            var power = query.entity().getAttributeValue(WitcherSpellSchools.YRDEN.attributeEntry);
            var power2 = query.entity().getAttributeValue(SpellSchools.ARCANE.attributeEntry);
            power *= power2 + 1 ;
            var world = query.entity().getWorld();
            var spellpower = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(SPELL_POWER);
            if (spellpower.isPresent()) {
                var level = EnchantmentHelper.getLevel(spellpower.get(), query.entity().getMainHandStack());
                power *= 1 + (0.05 * level);
            }
            return power;
        });
        YRDEN.addSource(SpellSchool.Trait.HASTE, SpellSchool.Apply.ADD, query -> {
            var value = query.entity().getAttributeValue(SpellPowerMechanics.HASTE.attributeEntry);
            var rate = (value  / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        YRDEN.addSource(SpellSchool.Trait.CRIT_DAMAGE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_damage_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        YRDEN.addSource(SpellSchool.Trait.CRIT_CHANCE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage
                    + query.entity().getAttributeValue(SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry);
            var value2 = (query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER) - 100) / adrenaline_crit_chance_div;
            var rate = ((value + value2) / PERCENT_ATTRIBUTE_BASELINE);
            return rate - 1;
        });
        SpellSchools.configureSpellCritDamage(YRDEN);
        SpellSchools.configureSpellHaste(YRDEN);
        SpellSchools.configureSpellCritChance(YRDEN);
        SpellSchools.register(YRDEN);
        WITCHER_MELEE.addSource(SpellSchool.Trait.POWER, SpellSchool.Apply.ADD, query -> {
            var power = query.entity().getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            var world = query.entity().getWorld();
            var sharpness = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SHARPNESS);
            if (sharpness.isPresent()) {
                var level = EnchantmentHelper.getLevel(sharpness.get(), query.entity().getMainHandStack());
                power *= 1 + (0.05 * level);
            }
            return power;
        });
        WITCHER_MELEE.addSource(SpellSchool.Trait.CRIT_DAMAGE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_damage_percentage
                    + query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER )- 100/ adrenaline_crit_damage_div;
            return (value/ PERCENT_ATTRIBUTE_BASELINE) -1;
        });
        WITCHER_MELEE.addSource(SpellSchool.Trait.CRIT_CHANCE, SpellSchool.Apply.ADD, query -> {
            var value = SpellPowerMod.attributesConfig.value.base_spell_critical_chance_percentage
                    + query.entity().getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER )- 100/ adrenaline_crit_damage_div;
            return (value/ PERCENT_ATTRIBUTE_BASELINE)-1;

        });
        SpellSchools.configureSpellCritDamage(WITCHER_MELEE);
        SpellSchools.configureSpellHaste(WITCHER_MELEE);
        SpellSchools.configureSpellCritChance(WITCHER_MELEE);
        SpellSchools.register(WITCHER_MELEE);

    }
}
