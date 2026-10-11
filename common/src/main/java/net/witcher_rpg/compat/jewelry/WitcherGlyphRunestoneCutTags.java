package net.witcher_rpg.compat.jewelry;

import net.jewelry.gems.GemCut;
import net.jewelry.gems.GemCutRegistry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherGlyphRunestoneCutTags {
    public static final TagKey<GemCut> GLYPH_CUTS = TagKey.of(GemCutRegistry.KEY, Identifier.of(MOD_ID, "glyph_cuts"));
    public static final TagKey<GemCut> RUNESTONE_CUTS = TagKey.of(GemCutRegistry.KEY, Identifier.of(MOD_ID, "runestone_cuts"));
}
