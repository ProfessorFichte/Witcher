package net.witcher_rpg.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.spell_engine.entity.SpellCloud;

public class YrdenEntity extends SpellCloud {
    public static EntityType<YrdenEntity> ENTITY_TYPE;

    public YrdenEntity(EntityType<? extends SpellCloud> entityType, World world) {
        super(entityType, world);
    }
}