package net.witcher_rpg.network;

import net.minecraft.server.network.ServerPlayerEntity;
import net.spell_engine.network.Packets;
import org.jetbrains.annotations.Nullable;

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
