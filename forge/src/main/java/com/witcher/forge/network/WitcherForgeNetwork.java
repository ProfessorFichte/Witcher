package com.witcher.forge.network;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.spell_engine.network.Packets;
import net.witcher_rpg.WitcherClassMod;
import net.witcher_rpg.network.ExposedGlowPayload;
import net.witcher_rpg.network.WitcherNetworking;

/// Forge 47 networking: one `SimpleChannel` carrying this mod's payloads. 1.20.1 has no
/// `CustomPayload` registry, so each message is registered with its own encoder/decoder pair.
///
/// Spell Engine's transport cannot be reused for sending either: its `SimpleChannel` dispatches by
/// message **class** and throws `IllegalArgumentException: Invalid message …` for a payload it does not
/// know, so this channel does the sending as well (installed into `WitcherNetworking`).
public class WitcherForgeNetwork {
    public static final Identifier CHANNEL_NAME = new Identifier(WitcherClassMod.MOD_ID, "main");
    private static final String PROTOCOL_VERSION = "1";

    /// Lenient version predicates: a peer without the channel (vanilla client, mod-less server) still
    /// connects, matching Fabric's behaviour.
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(CHANNEL_NAME)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION))
            .serverAcceptedVersions(NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION))
            .simpleChannel();

    private static boolean registered = false;

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;

        CHANNEL.messageBuilder(ExposedGlowPayload.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .encoder((packet, buffer) -> packet.write(buffer))
                .decoder(ExposedGlowPayload::read)
                // The handler body resolves the client class lazily, so a dedicated server never loads it.
                .consumerMainThread((packet, contextSupplier) -> {
                    contextSupplier.get().enqueueWork(() -> WitcherForgeClientNetwork.handleExposedGlow(packet));
                    contextSupplier.get().setPacketHandled(true);
                })
                .add();

        WitcherNetworking.install(WitcherForgeNetwork::sendToPlayer);
    }

    public static void sendToPlayer(ServerPlayerEntity player, Packets.Payload payload) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), payload);
    }

    private WitcherForgeNetwork() { }
}
