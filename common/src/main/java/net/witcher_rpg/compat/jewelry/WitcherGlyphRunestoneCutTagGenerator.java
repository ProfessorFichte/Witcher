package net.witcher_rpg.compat.jewelry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.jewelry.api.datagen.GemCutTagGenerator;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class WitcherGlyphRunestoneCutTagGenerator extends GemCutTagGenerator {
    public WitcherGlyphRunestoneCutTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        var glyphCuts = getOrCreateTagBuilder(WitcherGlyphRunestoneCutTags.GLYPH_CUTS);
        var runestoneCuts = getOrCreateTagBuilder(WitcherGlyphRunestoneCutTags.RUNESTONE_CUTS);

        for (var cut : WitcherGlyphRunestoneCuts.CUTS) {
            if (cut.glyph()) {
                glyphCuts.addOptional(cut.id());
            } else {
                runestoneCuts.addOptional(cut.id());
            }
        }

        restrict(WitcherGlyphRunestoneCutTags.GLYPH_CUTS);
        restrict(WitcherGlyphRunestoneCutTags.RUNESTONE_CUTS);
    }
}
