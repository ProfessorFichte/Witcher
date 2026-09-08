package net.witcher_rpg.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.spell_engine.network.Packets;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

/// S2C packet toggling the "exposed" glow outline on an entity.
///
/// 1.20.1 has no `CustomPayload` / `PacketCodec`: the packet is a plain record carrying its channel id
/// plus a write/read pair, per Spell Engine's {@link Packets.Payload} contract.
public record ExposedGlowPayload(int entityId, boolean active) implements Packets.Payload {
    public static final Identifier ID = new Identifier(MOD_ID, "exposed_glow");

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeInt(entityId);
        buffer.writeBoolean(active);
    }

    public static ExposedGlowPayload read(PacketByteBuf buffer) {
        return new ExposedGlowPayload(buffer.readInt(), buffer.readBoolean());
    }
}
