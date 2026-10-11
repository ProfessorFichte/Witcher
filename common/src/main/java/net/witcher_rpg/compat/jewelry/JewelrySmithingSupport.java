package net.witcher_rpg.compat.jewelry;

import net.jewelry.gems.GemComponents;
import net.jewelry.gems.SocketsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class JewelrySmithingSupport {
    public static int socketCount(ItemStack stack) {
        SocketsComponent sockets = stack.get(GemComponents.SOCKETS);
        return sockets != null ? sockets.count() : 0;
    }

    public static boolean preserveGlyphSockets(ItemStack base, ItemStack result, int correctSlotCount) {
        return preserve(base, result, correctSlotCount, JewelrySockets.GLYPH_SOCKET_TYPE);
    }

    public static boolean preserveRunestoneSockets(ItemStack base, ItemStack result, int correctSlotCount) {
        return preserve(base, result, correctSlotCount, JewelrySockets.RUNESTONE_SOCKET_TYPE);
    }

    private static boolean preserve(ItemStack base, ItemStack result, int correctSlotCount, Identifier socketType) {
        SocketsComponent baseSockets = base.get(GemComponents.SOCKETS);
        if (baseSockets == null || correctSlotCount <= 0) return false;

        List<SocketsComponent.Socket> filled = new ArrayList<>();
        for (var socket : baseSockets.sockets()) {
            if (socket.gem().isPresent()) filled.add(socket);
        }

        SocketsComponent resultSockets = result.get(GemComponents.SOCKETS);
        boolean alreadyCorrect = resultSockets != null && resultSockets.count() == correctSlotCount && filled.isEmpty();
        if (alreadyCorrect) return false;

        List<SocketsComponent.Socket> merged = new ArrayList<>();
        for (int i = 0; i < correctSlotCount; i++) {
            merged.add(i < filled.size() ? filled.get(i) : SocketsComponent.Socket.ofType(socketType));
        }

        result.set(GemComponents.SOCKETS, new SocketsComponent(merged));
        return true;
    }
}
