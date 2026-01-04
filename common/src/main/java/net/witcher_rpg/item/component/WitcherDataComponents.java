package net.witcher_rpg.item.component;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.witcher_rpg.WitcherClassMod;

public class WitcherDataComponents {
    public static final ComponentType<GlyphSlots> GLYPH_SLOTS = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        WitcherClassMod.id("glyph_slots"),
        ComponentType.<GlyphSlots>builder()
            .codec(GlyphSlots.CODEC)
            .build()
    );

    public static final ComponentType<RunestoneSlots> RUNESTONE_SLOTS = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        WitcherClassMod.id("runestone_slots"),
        ComponentType.<RunestoneSlots>builder()
            .codec(RunestoneSlots.CODEC)
            .build()
    );

    public static void register() {
        WitcherClassMod.LOGGER.info("Registering Witcher Data Components");
    }
}
