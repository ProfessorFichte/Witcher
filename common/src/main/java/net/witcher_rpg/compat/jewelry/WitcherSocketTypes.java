package net.witcher_rpg.compat.jewelry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.jewelry.api.SocketTypeBuilder;
import net.jewelry.api.datagen.SocketTypeGenerator;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherSocketTypes extends SocketTypeGenerator {
    public WitcherSocketTypes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    private static final String GLYPH_SOCKET_FRAME = "\uDBA0\uDD00";
    private static final String GLYPH_SOCKET_FILL = "\uDBA0\uDD01";
    private static final String RUNESTONE_SOCKET_FRAME = "\uDBA0\uDD02";
    private static final String RUNESTONE_SOCKET_FILL = "\uDBA0\uDD03";
    private static final String OVERLAY_BACKSPACE = "\uDBA0\uDD04";

    private static final String GLYPH_SOCKET_FILLED_ICON = GLYPH_SOCKET_FRAME + OVERLAY_BACKSPACE + GLYPH_SOCKET_FILL;
    private static final String RUNESTONE_SOCKET_FILLED_ICON = RUNESTONE_SOCKET_FRAME + OVERLAY_BACKSPACE + RUNESTONE_SOCKET_FILL;

    private static final int GLYPH_EMPTY_COLOR = 0x9BB4C2;
    private static final int GLYPH_FILL_COLOR = 0x4FD1C5;
    private static final int RUNESTONE_EMPTY_COLOR = 0x4A5D73;
    private static final int RUNESTONE_FILL_COLOR = 0xC9A24B;

    @Override
    protected void generate(Entries entries) {
        entries.add(SocketTypeBuilder.create(JewelrySockets.GLYPH_SOCKET_TYPE)
                .accepts(WitcherGlyphRunestoneCutTags.GLYPH_CUTS)
                .title(Text.translatable("socket." + MOD_ID + ".glyph_socket"))
                .empty(GLYPH_SOCKET_FRAME, GLYPH_EMPTY_COLOR)
                .fill(GLYPH_SOCKET_FILLED_ICON, GLYPH_FILL_COLOR)
                .build());

        entries.add(SocketTypeBuilder.create(JewelrySockets.RUNESTONE_SOCKET_TYPE)
                .accepts(WitcherGlyphRunestoneCutTags.RUNESTONE_CUTS)
                .title(Text.translatable("socket." + MOD_ID + ".runestone_socket"))
                .empty(RUNESTONE_SOCKET_FRAME, RUNESTONE_EMPTY_COLOR)
                .fill(RUNESTONE_SOCKET_FILLED_ICON, RUNESTONE_FILL_COLOR)
                .build());
    }

    @Override
    public String getName() {
        return "witcher_rpg socket types";
    }
}
