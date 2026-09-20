package net.witcher_rpg.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.spell_engine.network.Packets;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

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
