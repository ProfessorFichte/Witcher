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

/// The `sign_intensity` armor enchantment as a Java class: 1.20.1 has no data-driven enchantments, so the
/// generated `data/witcher_rpg/enchantment/sign_intensity.json` is replaced by this, mirroring Spell Power's
/// own 1.20.1 port and More RPG Library's `MRPGCEnchantments`.
///
/// Numbers come straight from the modern definition: weight 2 → `RARE`, `max_level` 5, cost `1 + 11/level`,
/// `+0.03` multiplied-base per level on the Sign school, armor slots, gated on
/// `#witcher_rpg:enchantable/sign_intensity`.
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

    private static boolean registered = false;

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        for (var entry : all.entrySet()) {
            Registry.register(Registries.ENCHANTMENT, entry.getKey(), entry.getValue());
        }
    }
}
