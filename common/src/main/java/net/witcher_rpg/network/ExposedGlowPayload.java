package net.witcher_rpg.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public record ExposedGlowPayload(int entityId, boolean active) implements CustomPayload {
    public static final CustomPayload.Id<ExposedGlowPayload> ID =
            new CustomPayload.Id<>(Identifier.of(MOD_ID, "exposed_glow"));

    public static final PacketCodec<RegistryByteBuf, ExposedGlowPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, ExposedGlowPayload::entityId,
            PacketCodecs.BOOL, ExposedGlowPayload::active,
            ExposedGlowPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
