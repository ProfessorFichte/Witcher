package net.witcher_rpg.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class Sounds {
    public static class Entry {
        private final Identifier id;
        private final SoundEvent soundEvent;
        private RegistryEntry<SoundEvent> entry;
        private int variants = 1;

        public Entry(Identifier id, SoundEvent soundEvent) {
            this.id = id;
            this.soundEvent = soundEvent;
        }

        public Entry(String name) {
            this(Identifier.of(MOD_ID, name));
        }

        public Entry(Identifier id) {
            this(id, SoundEvent.of(id));
        }

        public Entry travelDistance(float distance) {
            return new Entry(id, SoundEvent.of(id, distance));
        }

        public Entry variants(int variants) {
            this.variants = variants;
            return this;
        }

        public Identifier id() {
            return id;
        }

        public SoundEvent soundEvent() {
            return soundEvent;
        }

        public RegistryEntry<SoundEvent> entry() {
            return entry;
        }

        public int variants() {
            return variants;
        }
    }

    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static final Entry AARD_SIGN = add(new Entry("aard_sign"));
    public static final Entry IGNI_SIGN = add(new Entry("igni_sign"));
    public static final Entry QUEN_SIGN = add(new Entry("quen_sign"));
    public static final Entry YRDEN_SIGN = add(new Entry("yrden_sign"));
    public static final Entry AARD_FROST_SIGN = add(new Entry("aard_frost_sign"));
    public static final Entry AXII_SIGN = add(new Entry("axii_sign"));
    public static final Entry QUEN_BREAK = add(new Entry("quen_sign_break"));
    public static final Entry REND_SPELL = add(new Entry("rend_spell"));
    public static final Entry WHIRL = add(new Entry("whirl"));
    public static final Entry WITCHER_SENSES_EXPOSED = add(new Entry("witcher_senses_exposed"));

    public static void register() {
        for (var entry: entries) {
            entry.entry = Registry.registerReference(Registries.SOUND_EVENT, entry.id(), entry.soundEvent());
        }
    }
}
