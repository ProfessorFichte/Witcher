package net.witcher_rpg.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class Sounds {
    public static final Identifier AARD_SIGN_ID = Identifier.of(MOD_ID, "aard_sign");
    public static SoundEvent AARD_SIGN_EVENT= SoundEvent.of(AARD_SIGN_ID);
    public static final Identifier IGNI_SIGN_ID = Identifier.of(MOD_ID, "igni_sign");
    public static SoundEvent IGNI_SIGN_EVENT= SoundEvent.of(IGNI_SIGN_ID);
    public static final Identifier QUEN_SIGN_ID = Identifier.of(MOD_ID, "quen_sign");
    public static SoundEvent QUEN_SIGN_EVENT= SoundEvent.of(QUEN_SIGN_ID);
    public static final Identifier YRDEN_SIGN_ID = Identifier.of(MOD_ID, "yrden_sign");
    public static SoundEvent YRDEN_SIGN_EVENT= SoundEvent.of(YRDEN_SIGN_ID);
    public static final Identifier AARD_FROST_SIGN_ID = Identifier.of(MOD_ID, "aard_frost_sign");
    public static SoundEvent AARD_FROST_SIGN_EVENT= SoundEvent.of(AARD_FROST_SIGN_ID);
    public static final Identifier AXII_SIGN_ID = Identifier.of(MOD_ID, "axii_sign");
    public static SoundEvent AXII_SIGN_EVENT= SoundEvent.of(AXII_SIGN_ID);
    public static final Identifier QUEN_BREAK_ID = Identifier.of(MOD_ID, "quen_sign_break");
    public static SoundEvent QUEN_BREAK_EVENT= SoundEvent.of(QUEN_BREAK_ID);
    public static final Identifier REND_SPELL_ID = Identifier.of(MOD_ID, "rend_spell");
    public static SoundEvent REND_SPELL_EVENT= SoundEvent.of(REND_SPELL_ID);
    public static final Identifier WHIRL_ID = Identifier.of(MOD_ID, "whirl");
    public static SoundEvent WHIRL_EVENT= SoundEvent.of(WHIRL_ID);

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, AARD_SIGN_ID, AARD_SIGN_EVENT);
        Registry.register(Registries.SOUND_EVENT, IGNI_SIGN_ID, IGNI_SIGN_EVENT);
        Registry.register(Registries.SOUND_EVENT, QUEN_SIGN_ID, QUEN_SIGN_EVENT);
        Registry.register(Registries.SOUND_EVENT, YRDEN_SIGN_ID, YRDEN_SIGN_EVENT);
        Registry.register(Registries.SOUND_EVENT, AARD_FROST_SIGN_ID, AARD_FROST_SIGN_EVENT);
        Registry.register(Registries.SOUND_EVENT, AXII_SIGN_ID, AXII_SIGN_EVENT);
        Registry.register(Registries.SOUND_EVENT, QUEN_BREAK_ID, QUEN_BREAK_EVENT);
        Registry.register(Registries.SOUND_EVENT, REND_SPELL_ID, REND_SPELL_EVENT);
        Registry.register(Registries.SOUND_EVENT, WHIRL_ID, WHIRL_EVENT);

    }
}
