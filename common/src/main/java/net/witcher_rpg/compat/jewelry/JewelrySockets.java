package net.witcher_rpg.compat.jewelry;

import net.jewelry.gems.GemComponents;
import net.jewelry.gems.SocketsComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class JewelrySockets {
    public static final Identifier GLYPH_SOCKET_TYPE = Identifier.of(MOD_ID, "glyph_socket");
    public static final Identifier RUNESTONE_SOCKET_TYPE = Identifier.of(MOD_ID, "runestone_socket");

    public static void applyGlyphSockets(Item.Settings settings, int count) {
        settings.component(GemComponents.SOCKETS, SocketsComponent.empty(count, GLYPH_SOCKET_TYPE));
    }

    public static void applyRunestoneSockets(Item.Settings settings, int count) {
        settings.component(GemComponents.SOCKETS, SocketsComponent.empty(count, RUNESTONE_SOCKET_TYPE));
    }
}
