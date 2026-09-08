package net.witcher_rpg.compat;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

/// Shared derivation of the attribute-modifier identity used by both accessory integrations
/// (Trinkets on Fabric, Curios on Forge).
///
/// On 1.20.1 an `EntityAttributeModifier` is keyed by a `UUID` + display name rather than by the 1.21
/// `Identifier`, and both slot APIs hand the item a **slot-unique** UUID. Folding the item id into that
/// UUID reproduces the 1.21 `<slot>/<item>` modifier id (`Identifier#withSuffixedPath`, which does not
/// exist here): bonuses stack across slots, and swapping a different item into the same slot cannot
/// reuse a key and trip vanilla's "Modifier is already applied" guard.
public class WitcherModifierIds {
    public static UUID perSlotAndItem(UUID slotUuid, String itemPath) {
        return UUID.nameUUIDFromBytes((slotUuid + "/" + itemPath).getBytes(StandardCharsets.UTF_8));
    }

    public static String name(String itemPath) {
        return MOD_ID + ":" + itemPath;
    }
}
