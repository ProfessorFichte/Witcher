package net.witcher_rpg.compat.jewelry;

import com.mrpg_lib.entity.attribute.MRPGCEntityAttributes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.jewelry.api.GemCutBuilder;
import net.jewelry.api.datagen.GemCutGenerator;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.witcher_rpg.entity.attribute.WitcherAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherGlyphRunestoneCuts extends GemCutGenerator {
    public WitcherGlyphRunestoneCuts(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    public record CutEntry(GemCutBuilder.Entry entry, String translatedName, boolean glyph) {
        public Identifier id() { return entry.id(); }
    }

    public static final List<CutEntry> CUTS = new ArrayList<>();

    private static final float LESSER_GLYPH_POWER = 0.25F;
    private static final float GLYPH_POWER = 0.5F;
    private static final float GREATER_GLYPH_POWER = 1.0F;

    private static final float LESSER_RUNESTONE_POWER = 0.02F;
    private static final float RUNESTONE_POWER = 0.03F;
    private static final float GREATER_RUNESTONE_POWER = 0.05F;

    private static final float LESSER_RUNESTONE_EFFECT_WEAK = 0.05F;
    private static final float RUNESTONE_EFFECT_WEAK = 0.075F;
    private static final float GREATER_RUNESTONE_EFFECT_WEAK = 0.1F;

    private static final float LESSER_RUNESTONE_EFFECT_STRONG = 0.035F;
    private static final float RUNESTONE_EFFECT_STRONG = 0.05F;
    private static final float GREATER_RUNESTONE_EFFECT_STRONG = 0.075F;

    private static Identifier attr(RegistryEntry<EntityAttribute> attribute) {
        return Identifier.of(attribute.getIdAsString());
    }

    static {
        glyph("lesser_aard_glyph", "Lesser Aard Glyph", WitcherGlyphRunestones.LESSER_GLYPH_OF_INFUSION, WitcherAttributes.AARD_INTENSITY, LESSER_GLYPH_POWER, 0x4FC3F7);
        glyph("aard_glyph", "Aard Glyph", WitcherGlyphRunestones.GLYPH, WitcherAttributes.AARD_INTENSITY, GLYPH_POWER, 0x4FC3F7);
        glyph("greater_aard_glyph", "Greater Aard Glyph", WitcherGlyphRunestones.GREATER_GLYPH, WitcherAttributes.AARD_INTENSITY, GREATER_GLYPH_POWER, 0x4FC3F7);

        glyph("lesser_axii_glyph", "Lesser Axii Glyph", WitcherGlyphRunestones.LESSER_GLYPH_OF_INFUSION, WitcherAttributes.AXII_INTENSITY, LESSER_GLYPH_POWER, 0x5FAD41);
        glyph("axii_glyph", "Axii Glyph", WitcherGlyphRunestones.GLYPH, WitcherAttributes.AXII_INTENSITY, GLYPH_POWER, 0x5FAD41);
        glyph("greater_axii_glyph", "Greater Axii Glyph", WitcherGlyphRunestones.GREATER_GLYPH, WitcherAttributes.AXII_INTENSITY, GREATER_GLYPH_POWER, 0x5FAD41);

        glyph("lesser_igni_glyph", "Lesser Igni Glyph", WitcherGlyphRunestones.LESSER_GLYPH_OF_INFUSION, WitcherAttributes.IGNI_INTENSITY, LESSER_GLYPH_POWER, 0xFF4500);
        glyph("igni_glyph", "Igni Glyph", WitcherGlyphRunestones.GLYPH, WitcherAttributes.IGNI_INTENSITY, GLYPH_POWER, 0xFF4500);
        glyph("greater_igni_glyph", "Greater Igni Glyph", WitcherGlyphRunestones.GREATER_GLYPH, WitcherAttributes.IGNI_INTENSITY, GREATER_GLYPH_POWER, 0xFF4500);

        glyph("lesser_quen_glyph", "Lesser Quen Glyph", WitcherGlyphRunestones.LESSER_GLYPH_OF_INFUSION, WitcherAttributes.QUEN_INTENSITY, LESSER_GLYPH_POWER, 0xFFEE00);
        glyph("quen_glyph", "Quen Glyph", WitcherGlyphRunestones.GLYPH, WitcherAttributes.QUEN_INTENSITY, GLYPH_POWER, 0xFFEE00);
        glyph("greater_quen_glyph", "Greater Quen Glyph", WitcherGlyphRunestones.GREATER_GLYPH, WitcherAttributes.QUEN_INTENSITY, GREATER_GLYPH_POWER, 0xFFEE00);

        glyph("lesser_yrden_glyph", "Lesser Yrden Glyph", WitcherGlyphRunestones.LESSER_GLYPH_OF_INFUSION, WitcherAttributes.YRDEN_INTENSITY, LESSER_GLYPH_POWER, 0x4A1F7A);
        glyph("yrden_glyph", "Yrden Glyph", WitcherGlyphRunestones.GLYPH, WitcherAttributes.YRDEN_INTENSITY, GLYPH_POWER, 0x4A1F7A);
        glyph("greater_yrden_glyph", "Greater Yrden Glyph", WitcherGlyphRunestones.GREATER_GLYPH, WitcherAttributes.YRDEN_INTENSITY, GREATER_GLYPH_POWER, 0x4A1F7A);

        runestone("lesser_dazhbog_runestone", "Lesser Dazhbog Runestone", WitcherGlyphRunestones.LESSER_RUNESTONE, attr(MRPGCEntityAttributes.BURNING_CHANCE), LESSER_RUNESTONE_EFFECT_STRONG, 0xFF9500);
        runestone("dazhbog_runestone", "Dazhbog Runestone", WitcherGlyphRunestones.RUNESTONE, attr(MRPGCEntityAttributes.BURNING_CHANCE), RUNESTONE_EFFECT_STRONG, 0xFF9500);
        runestone("greater_dazhbog_runestone", "Greater Dazhbog Runestone", WitcherGlyphRunestones.GREATER_RUNESTONE, attr(MRPGCEntityAttributes.BURNING_CHANCE), GREATER_RUNESTONE_EFFECT_STRONG, 0xFF9500);

        runestone("lesser_chernobog_runestone", "Lesser Chernobog Runestone", WitcherGlyphRunestones.LESSER_RUNESTONE, attr(EntityAttributes.GENERIC_ATTACK_DAMAGE), LESSER_RUNESTONE_POWER, 0x3D0000);
        runestone("chernobog_runestone", "Chernobog Runestone", WitcherGlyphRunestones.RUNESTONE, attr(EntityAttributes.GENERIC_ATTACK_DAMAGE), RUNESTONE_POWER, 0x3D0000);
        runestone("greater_chernobog_runestone", "Greater Chernobog Runestone", WitcherGlyphRunestones.GREATER_RUNESTONE, attr(EntityAttributes.GENERIC_ATTACK_DAMAGE), GREATER_RUNESTONE_POWER, 0x3D0000);

        runestone("lesser_stribog_runestone", "Lesser Stribog Runestone", WitcherGlyphRunestones.LESSER_RUNESTONE, attr(MRPGCEntityAttributes.STAGGER_CHANCE), LESSER_RUNESTONE_EFFECT_WEAK, 0x9B30FF);
        runestone("stribog_runestone", "Stribog Runestone", WitcherGlyphRunestones.RUNESTONE, attr(MRPGCEntityAttributes.STAGGER_CHANCE), RUNESTONE_EFFECT_WEAK, 0x9B30FF);
        runestone("greater_stribog_runestone", "Greater Stribog Runestone", WitcherGlyphRunestones.GREATER_RUNESTONE, attr(MRPGCEntityAttributes.STAGGER_CHANCE), GREATER_RUNESTONE_EFFECT_WEAK, 0x9B30FF);

        runestone("lesser_svarog_runestone", "Lesser Svarog Runestone", WitcherGlyphRunestones.LESSER_RUNESTONE, attr(MRPGCEntityAttributes.ARMOR_PIERCING), LESSER_RUNESTONE_EFFECT_WEAK, 0xB8590A);
        runestone("svarog_runestone", "Svarog Runestone", WitcherGlyphRunestones.RUNESTONE, attr(MRPGCEntityAttributes.ARMOR_PIERCING), RUNESTONE_EFFECT_WEAK, 0xB8590A);
        runestone("greater_svarog_runestone", "Greater Svarog Runestone", WitcherGlyphRunestones.GREATER_RUNESTONE, attr(MRPGCEntityAttributes.ARMOR_PIERCING), GREATER_RUNESTONE_EFFECT_WEAK, 0xB8590A);

        runestone("lesser_triglav_runestone", "Lesser Triglav Runestone", WitcherGlyphRunestones.LESSER_RUNESTONE, attr(MRPGCEntityAttributes.STUN_CHANCE), LESSER_RUNESTONE_EFFECT_STRONG, 0xE0F7FF);
        runestone("triglav_runestone", "Triglav Runestone", WitcherGlyphRunestones.RUNESTONE, attr(MRPGCEntityAttributes.STUN_CHANCE), RUNESTONE_EFFECT_STRONG, 0xE0F7FF);
        runestone("greater_triglav_runestone", "Greater Triglav Runestone", WitcherGlyphRunestones.GREATER_RUNESTONE, attr(MRPGCEntityAttributes.STUN_CHANCE), GREATER_RUNESTONE_EFFECT_STRONG, 0xE0F7FF);

        runestone("lesser_perun_runestone", "Lesser Perun Runestone", WitcherGlyphRunestones.LESSER_RUNESTONE, attr(WitcherAttributes.ADRENALINE_MODIFIER), LESSER_RUNESTONE_EFFECT_STRONG, 0x00CFFF);
        runestone("perun_runestone", "Perun Runestone", WitcherGlyphRunestones.RUNESTONE, attr(WitcherAttributes.ADRENALINE_MODIFIER), RUNESTONE_EFFECT_STRONG, 0x00CFFF);
        runestone("greater_perun_runestone", "Greater Perun Runestone", WitcherGlyphRunestones.GREATER_RUNESTONE, attr(WitcherAttributes.ADRENALINE_MODIFIER), GREATER_RUNESTONE_EFFECT_STRONG, 0x00CFFF);

        runestone("lesser_veles_runestone", "Lesser Veles Runestone", WitcherGlyphRunestones.LESSER_RUNESTONE, attr(WitcherAttributes.SIGN_INTENSITY), LESSER_RUNESTONE_POWER, 0x7FFFD4);
        runestone("veles_runestone", "Veles Runestone", WitcherGlyphRunestones.RUNESTONE, attr(WitcherAttributes.SIGN_INTENSITY), RUNESTONE_POWER, 0x7FFFD4);
        runestone("greater_veles_runestone", "Greater Veles Runestone", WitcherGlyphRunestones.GREATER_RUNESTONE, attr(WitcherAttributes.SIGN_INTENSITY), GREATER_RUNESTONE_POWER, 0x7FFFD4);

        runestone("lesser_morana_runestone", "Lesser Morana Runestone", WitcherGlyphRunestones.LESSER_RUNESTONE, attr(MRPGCEntityAttributes.POISON_CHANCE), LESSER_RUNESTONE_EFFECT_WEAK, 0x66CC00);
        runestone("morana_runestone", "Morana Runestone", WitcherGlyphRunestones.RUNESTONE, attr(MRPGCEntityAttributes.POISON_CHANCE), RUNESTONE_EFFECT_WEAK, 0x66CC00);
        runestone("greater_morana_runestone", "Greater Morana Runestone", WitcherGlyphRunestones.GREATER_RUNESTONE, attr(MRPGCEntityAttributes.POISON_CHANCE), GREATER_RUNESTONE_EFFECT_WEAK, 0x66CC00);

        runestone("lesser_zoria_runestone", "Lesser Zoria Runestone", WitcherGlyphRunestones.LESSER_RUNESTONE, attr(MRPGCEntityAttributes.FREEZE_CHANCE), LESSER_RUNESTONE_EFFECT_STRONG, 0x16215C);
        runestone("zoria_runestone", "Zoria Runestone", WitcherGlyphRunestones.RUNESTONE, attr(MRPGCEntityAttributes.FREEZE_CHANCE), RUNESTONE_EFFECT_STRONG, 0x16215C);
        runestone("greater_zoria_runestone", "Greater Zoria Runestone", WitcherGlyphRunestones.GREATER_RUNESTONE, attr(MRPGCEntityAttributes.FREEZE_CHANCE), GREATER_RUNESTONE_EFFECT_STRONG, 0x16215C);

        runestone("lesser_devana_runestone", "Lesser Devana Runestone", WitcherGlyphRunestones.LESSER_RUNESTONE, attr(MRPGCEntityAttributes.BLEEDING_CHANCE), LESSER_RUNESTONE_EFFECT_WEAK, 0xA60000);
        runestone("devana_runestone", "Devana Runestone", WitcherGlyphRunestones.RUNESTONE, attr(MRPGCEntityAttributes.BLEEDING_CHANCE), RUNESTONE_EFFECT_WEAK, 0xA60000);
        runestone("greater_devana_runestone", "Greater Devana Runestone", WitcherGlyphRunestones.GREATER_RUNESTONE, attr(MRPGCEntityAttributes.BLEEDING_CHANCE), GREATER_RUNESTONE_EFFECT_WEAK, 0xA60000);
    }

    @Override
    protected void generate(Entries entries) {
        for (var cut : CUTS) {
            entries.add(cut.entry());
        }
    }

    private static void glyph(String cutName, String translatedName, WitcherGlyphRunestones.Entry gem, Identifier attribute, float value, int color) {
        var id = Identifier.of(MOD_ID, cutName);
        var cuttingItem = WitcherGlyphRunestones.cutTableSentinel();
        var built = GemCutBuilder.create(id, cuttingItem)
                .addValue(attribute, value)
                .color(color)
                .customIcon()
                .build();
        CUTS.add(new CutEntry(built, translatedName, true));
    }

    private static void glyph(String cutName, String translatedName, WitcherGlyphRunestones.Entry gem, RegistryEntry<EntityAttribute> attribute, float value, int color) {
        glyph(cutName, translatedName, gem, attr(attribute), value, color);
    }

    private static void runestone(String cutName, String translatedName, WitcherGlyphRunestones.Entry gem, Identifier attribute, float value, int color) {
        var id = Identifier.of(MOD_ID, cutName);
        var cuttingItem = WitcherGlyphRunestones.cutTableSentinel();
        var built = GemCutBuilder.create(id, cuttingItem)
                .multiplyBase(attribute, value)
                .color(color)
                .customIcon()
                .build();
        CUTS.add(new CutEntry(built, translatedName, false));
    }

    @Override
    public String getName() {
        return "witcher_rpg gem cuts";
    }
}
