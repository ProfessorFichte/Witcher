package net.witcher_rpg.entity.attribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;


/// Witcher's own entity attributes.
///
/// **Registration is deliberately lazy on this line.** Forge 47 locks every vanilla registry outside its
/// own `RegisterEvent` window, and this class is reachable very early (`WitcherTrinkets`, `WitcherStatusEffects`
/// and `WitcherSpellSchools` all name these attributes), so registering from a static field initializer -
/// the way the 1.21.1 branch does - blows up with *"Can not register to a locked registry"* during mod
/// construction. The class initializer therefore only creates `Identifier`s; the registry writes happen in
/// {@link #registerAttributes()}, called from the Fabric-only `EntityAttributesMixin` (`<clinit>` TAIL, i.e.
/// bootstrap) and from Forge's `ATTRIBUTE` `RegisterEvent` window.
///
/// Consumers that only need the attribute *id* (config-shaped attribute modifiers) must use the `*_ID`
/// constants, never the attribute objects, since those are null until registration runs.
public class WitcherAttributes {
    public static final Identifier SIGN_INTENSITY_ID = new Identifier(MOD_ID, "sign_intensity");
    public static final Identifier ADRENALINE_MODIFIER_ID = new Identifier(MOD_ID, "adrenaline_modifier");
    public static final Identifier AARD_INTENSITY_ID = new Identifier(MOD_ID, "aard_intensity");
    public static final Identifier AXII_INTENSITY_ID = new Identifier(MOD_ID, "axii_intensity");
    public static final Identifier IGNI_INTENSITY_ID = new Identifier(MOD_ID, "igni_intensity");
    public static final Identifier QUEN_INTENSITY_ID = new Identifier(MOD_ID, "quen_intensity");
    public static final Identifier YRDEN_INTENSITY_ID = new Identifier(MOD_ID, "yrden_intensity");

    public static EntityAttribute SIGN_INTENSITY;
    public static EntityAttribute ADRENALINE_MODIFIER;
    public static EntityAttribute AARD_INTENSITY;
    public static EntityAttribute AXII_INTENSITY;
    public static EntityAttribute IGNI_INTENSITY;
    public static EntityAttribute QUEN_INTENSITY;
    public static EntityAttribute YRDEN_INTENSITY;

    private static EntityAttribute create(final Identifier id, double base, double min, double max) {
        return new ClampedEntityAttribute("attribute.name." + id.getNamespace() + '.' + id.getPath(), base, min, max).setTracked(true);
    }

    private static boolean created = false;

    /// Creation only, and idempotent: builds the seven attributes, fills the static fields, and hands
    /// back the map keyed by registration id. Forge's `ATTRIBUTE` `RegisterEvent` window feeds it to its
    /// own `RegisterHelper`.
    public static Map<Identifier, EntityAttribute> attributesToRegister() {
        if (created) {
            return Map.of();
        }
        created = true;
        var map = new LinkedHashMap<Identifier, EntityAttribute>();
        map.put(SIGN_INTENSITY_ID, SIGN_INTENSITY = create(SIGN_INTENSITY_ID, 0.0, 0.0, 1024.0));
        map.put(ADRENALINE_MODIFIER_ID, ADRENALINE_MODIFIER = create(ADRENALINE_MODIFIER_ID, 100.0, 100.0, 1024.0));
        map.put(AARD_INTENSITY_ID, AARD_INTENSITY = create(AARD_INTENSITY_ID, 0.0, 0.0, 1024.0));
        map.put(AXII_INTENSITY_ID, AXII_INTENSITY = create(AXII_INTENSITY_ID, 0.0, 0.0, 1024.0));
        map.put(IGNI_INTENSITY_ID, IGNI_INTENSITY = create(IGNI_INTENSITY_ID, 0.0, 0.0, 1024.0));
        map.put(QUEN_INTENSITY_ID, QUEN_INTENSITY = create(QUEN_INTENSITY_ID, 0.0, 0.0, 1024.0));
        map.put(YRDEN_INTENSITY_ID, YRDEN_INTENSITY = create(YRDEN_INTENSITY_ID, 0.0, 0.0, 1024.0));
        return map;
    }

    /// Idempotent. Fabric: `EntityAttributesMixin` (`<clinit>` TAIL on `EntityAttributes`).
    /// Forge registers {@link #attributesToRegister()} through the `ATTRIBUTE` window's helper instead.
    public static void registerAttributes() {
        attributesToRegister().forEach((id, attribute) -> Registry.register(Registries.ATTRIBUTE, id, attribute));
    }
}
