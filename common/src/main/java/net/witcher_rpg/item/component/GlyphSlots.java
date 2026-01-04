package net.witcher_rpg.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record GlyphSlots(int maxSlots, List<ItemStack> attachedGlyphs) {
    public static final Codec<GlyphSlots> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.fieldOf("max_slots").forGetter(GlyphSlots::maxSlots),
            ItemStack.CODEC.listOf().fieldOf("glyphs").forGetter(GlyphSlots::attachedGlyphs)
        ).apply(instance, GlyphSlots::new)
    );

    public static final GlyphSlots EMPTY = new GlyphSlots(0, List.of());

    public boolean canAttachGlyph() {
        return attachedGlyphs.size() < maxSlots;
    }

    public GlyphSlots withGlyph(ItemStack glyph) {
        List<ItemStack> newGlyphs = new ArrayList<>(attachedGlyphs);
        newGlyphs.add(glyph.copy());
        return new GlyphSlots(maxSlots, newGlyphs);
    }

    public GlyphSlots removeAllGlyphs() {
        return new GlyphSlots(maxSlots, List.of());
    }
}
