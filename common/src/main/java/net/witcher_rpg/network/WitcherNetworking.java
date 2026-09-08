package net.witcher_rpg.network;

import net.minecraft.server.network.ServerPlayerEntity;
import net.spell_engine.network.Packets;
import org.jetbrains.annotations.Nullable;

/// Loader-neutral send seam for this mod's own S2C packets.
///
/// Spell Engine's `Platform.util().networkS2C_Send` cannot be reused here: on Forge 47 it writes into
/// Spell Engine's own `SimpleChannel`, which dispatches by **message class** and throws
/// `IllegalArgumentException: Invalid message …` for a payload it does not know. Each platform
/// entrypoint installs its own sender instead (Fabric: `ServerPlayNetworking.send`; Forge: this mod's
/// `SimpleChannel`).
public class WitcherNetworking {
    public interface Sender {
        void send(ServerPlayerEntity player, Packets.Payload payload);
    }

    @Nullable
    private static Sender sender;

    public static void install(Sender sender) {
        WitcherNetworking.sender = sender;
    }

    public static void sendToPlayer(ServerPlayerEntity player, Packets.Payload payload) {
        var sender = WitcherNetworking.sender;
        if (sender != null) {
            sender.send(player, payload);
        }
    }
}
