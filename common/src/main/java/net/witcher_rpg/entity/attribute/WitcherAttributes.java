package net.witcher_rpg.entity.attribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

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

    private static EntityAttribute register(final Identifier id, double base, double min, double max) {
        EntityAttribute attribute = new ClampedEntityAttribute("attribute.name." + id.getNamespace() + '.' + id.getPath(), base, min, max).setTracked(true);
        return Registry.register(Registries.ATTRIBUTE, id, attribute);
    }

    private static boolean registered = false;

    /// Idempotent. Fabric: `EntityAttributesMixin` (`<clinit>` TAIL on `EntityAttributes`).
    /// Forge: the `ATTRIBUTE` `RegisterEvent` window.
    public static void registerAttributes() {
        if (registered) {
            return;
        }
        registered = true;
        SIGN_INTENSITY = register(SIGN_INTENSITY_ID, 0.0, 0.0, 1024.0);
        ADRENALINE_MODIFIER = register(ADRENALINE_MODIFIER_ID, 100.0, 100.0, 1024.0);
        AARD_INTENSITY = register(AARD_INTENSITY_ID, 0.0, 0.0, 1024.0);
        AXII_INTENSITY = register(AXII_INTENSITY_ID, 0.0, 0.0, 1024.0);
        IGNI_INTENSITY = register(IGNI_INTENSITY_ID, 0.0, 0.0, 1024.0);
        QUEN_INTENSITY = register(QUEN_INTENSITY_ID, 0.0, 0.0, 1024.0);
        YRDEN_INTENSITY = register(YRDEN_INTENSITY_ID, 0.0, 0.0, 1024.0);
    }
}
