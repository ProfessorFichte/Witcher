package net.witcher_rpg.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_power.api.enchantment.SpellPowerEnchantments;
import net.spell_power.config.EnchantmentsConfig;
import net.spell_power.internals.SchoolFilteredEnchantment;
import net.witcher_rpg.custom.WitcherSpellSchools;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherEnchantments {
    private static final EnchantmentsConfig.PowerEnchantmentConfig CONFIG =
            new EnchantmentsConfig.PowerEnchantmentConfig(false, 5, 1, 11, 0.03F);

    public static final Identifier SIGN_INTENSITY_ID = new Identifier(MOD_ID, "sign_intensity");
    public static final SchoolFilteredEnchantment SIGN_INTENSITY = (SchoolFilteredEnchantment) new SchoolFilteredEnchantment(
            Enchantment.Rarity.RARE,
            () -> CONFIG,
            Set.of(WitcherSpellSchools.SIGN),
            EnchantmentTarget.ARMOR,
            SpellPowerEnchantments.ARMOR)
            .requireTag(net.witcher_rpg.util.tags.WitcherItemTags.SIGN_INTENSITY_ENCHANTABLE)
            .supportWholeTarget();

    public static final Map<Identifier, Enchantment> all = new LinkedHashMap<>(Map.of(
            SIGN_INTENSITY_ID, SIGN_INTENSITY
    ));

    private static boolean claimed = false;

    public static Map<Identifier, Enchantment> enchantmentsToRegister() {
        if (claimed) {
            return Map.of();
        }
        claimed = true;
        return all;
    }

    public static void register() {
        enchantmentsToRegister().forEach((id, enchantment) -> Registry.register(Registries.ENCHANTMENT, id, enchantment));
    }
}
