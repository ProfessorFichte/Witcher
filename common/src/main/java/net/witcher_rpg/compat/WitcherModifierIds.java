package net.witcher_rpg.compat;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherModifierIds {
    public static UUID perSlotAndItem(UUID slotUuid, String itemPath) {
        return UUID.nameUUIDFromBytes((slotUuid + "/" + itemPath).getBytes(StandardCharsets.UTF_8));
    }

    public static String name(String itemPath) {
        return MOD_ID + ":" + itemPath;
    }
}
