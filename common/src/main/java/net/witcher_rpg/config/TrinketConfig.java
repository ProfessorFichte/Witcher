package net.witcher_rpg.config;

import net.fabricmc.loader.api.FabricLoader;
import net.spell_engine.rpg_series.config.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;

public class TrinketConfig {
    public LinkedHashMap<String, Entry> entries = new LinkedHashMap<>();

    public static class Entry {
        public static final Entry EMPTY = new Entry();

        public boolean enabled = true;
        public int durability = 0;
        @Nullable public Conditions conditions;
        public List<AttributeModifier> attributes = List.of();

        public Entry withAttributes(List<AttributeModifier> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Entry withRequiredMod(String required_mod) {
            if (conditions == null) {
                conditions = new Conditions();
            }
            conditions.required_mod = required_mod;
            return this;
        }

        public boolean isEnabled() {
            return enabled && (conditions == null || conditions.isSatisfied());
        }
    }

    public static class Conditions {
        @Nullable public String required_mod;

        public boolean isSatisfied() {
            if (required_mod != null || !required_mod.isEmpty()) {
                if (!FabricLoader.getInstance().isModLoaded(required_mod)) {
                    return false;
                }
            }
            return true;
        }
    }
}
