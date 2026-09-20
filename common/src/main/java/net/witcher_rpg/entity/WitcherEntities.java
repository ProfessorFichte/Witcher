package net.witcher_rpg.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherEntities {
    private static boolean created = false;

    public static Map<Identifier, EntityType<?>> entityTypesToRegister() {
        if (created) {
            return Map.of();
        }
        created = true;
        var map = new LinkedHashMap<Identifier, EntityType<?>>();
        YrdenEntity.ENTITY_TYPE = EntityType.Builder.<YrdenEntity>create(YrdenEntity::new, SpawnGroup.MISC)
                .setDimensions(6F, 0.5F)
                .makeFireImmune()
                .maxTrackingRange(128)
                .trackingTickInterval(20)
                .build("yrden");
        map.put(new Identifier(MOD_ID, "yrden"), YrdenEntity.ENTITY_TYPE);
        YrdenMagicTrapEntity.ENTITY_TYPE = EntityType.Builder.<YrdenMagicTrapEntity>create(YrdenMagicTrapEntity::new, SpawnGroup.MISC)
                .setDimensions(6F, 0.5F)
                .makeFireImmune()
                .maxTrackingRange(128)
                .trackingTickInterval(20)
                .build("yrden_magical_trap");
        map.put(new Identifier(MOD_ID, "yrden_magical_trap"), YrdenMagicTrapEntity.ENTITY_TYPE);
        return map;
    }

    public static void register() {
        entityTypesToRegister().forEach((id, type) -> Registry.register(Registries.ENTITY_TYPE, id, type));
    }
}
