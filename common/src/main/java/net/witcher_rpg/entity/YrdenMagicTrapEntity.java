package net.witcher_rpg.entity;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.entity.SpellEntity;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.SpellExecution;
import net.spell_engine.internals.impact.SpellImpacts;
import net.spell_engine.internals.target.EntityRelations;
import net.spell_engine.fx.ParticleHelper;
import net.spell_power.api.SpellPower;
import net.witcher_rpg.custom.WitcherSpellSchools;
import net.witcher_rpg.entity.attribute.WitcherAttributes;

import java.util.List;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;


public class YrdenMagicTrapEntity extends Entity implements SpellEntity.Spawned {
    public static EntityType<YrdenMagicTrapEntity > ENTITY_TYPE;
    // Both batches referenced "witcher_rpg.json:yrden_cloud" in V1 — an unregistered id
    // (the particle is registered as `witcher_rpg:yrden_cloud` in WitcherParticles), so
    // neither rendered anything. Repointed at the real id here.
    // `yrden_cloud` is a plain SimpleParticleType, so only the batch geometry applies.
    public static final ParticleGroup yrden_damage_circle = ParticleGroupBuilder.of(Identifier.of(MOD_ID, "yrden_cloud"))
            .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                    .count(15)
                    .speed(0.001F, 0.02F));
    public static final ParticleGroup yrden_damage_spehre = ParticleGroupBuilder.of(Identifier.of(MOD_ID, "yrden_cloud"))
            .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                    .count(15)
                    .speed(0.001F, 0.02F));


    private Identifier spellId;
    private int timeToLive = 20;
    private int ownerId;
    public static final Identifier yrdenSoundId = Identifier.of(MOD_ID, "yrden_sign");
    public static final SoundEvent yrdenSound = SoundEvent.of(yrdenSoundId);

    public YrdenMagicTrapEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void onSpawnedBySpell(Args args) {
        var owner = args.owner();
        var spellId = args.spell().getKey().get().getValue();
        var spawn = args.spawnData();
        this.spellId = spellId;
        this.getDataTracker().set(SPELL_ID_TRACKER, this.spellId.toString());
        this.ownerId = owner.getId();
        this.getDataTracker().set(OWNER_ID_TRACKER, this.ownerId);
        this.timeToLive = spawn.time_to_live_seconds * 20;
        this.getDataTracker().set(TIME_TO_LIVE_TRACKER, this.timeToLive);
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }


    @Override
    public boolean damage(DamageSource source, float amount) {
        this.getWorld().playSoundFromEntity(null, this, yrdenSound, SoundCategory.PLAYERS, 1F, 1F);
        return super.damage(source, amount);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        var spellEntry = getSpellEntry();
        if (spellEntry == null) {
            var spell = spellEntry.value();
            var width = spell.range * 2;
            var height = spell.range;
            return EntityDimensions.changing(width, height);
        } else {
            return super.getDimensions(pose);
        }
    }

    private static final TrackedData<String> SPELL_ID_TRACKER  = DataTracker.registerData(YrdenMagicTrapEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Integer> OWNER_ID_TRACKER  = DataTracker.registerData(YrdenMagicTrapEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TIME_TO_LIVE_TRACKER  = DataTracker.registerData(YrdenMagicTrapEntity.class, TrackedDataHandlerRegistry.INTEGER);


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(SPELL_ID_TRACKER, "");
        builder.add(OWNER_ID_TRACKER, 0);
        builder.add(TIME_TO_LIVE_TRACKER, 0);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        var rawSpellId = this.getDataTracker().get(SPELL_ID_TRACKER);
        if (rawSpellId != null && !rawSpellId.isEmpty()) {
            this.spellId = Identifier.of(rawSpellId);
        }
        this.timeToLive = this.getDataTracker().get(TIME_TO_LIVE_TRACKER);
        this.calculateDimensions();
    }


    private enum NBTKey {
        OWNER_ID("OwnerId"),
        SPELL_ID("SpellId"),
        TIME_TO_LIVE("TTL"),
        ;

        public final String key;
        NBTKey(String key) {
            this.key = key;
        }
    }


    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.spellId = Identifier.of(nbt.getString(NBTKey.SPELL_ID.key));
        this.ownerId = nbt.getInt(NBTKey.OWNER_ID.key);
        this.timeToLive = nbt.getInt(NBTKey.TIME_TO_LIVE.key);
        this.getDataTracker().set(SPELL_ID_TRACKER, this.spellId.toString());
        this.getDataTracker().set(OWNER_ID_TRACKER, this.ownerId);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putString(NBTKey.SPELL_ID.key, this.spellId.toString());
        nbt.putInt(NBTKey.OWNER_ID.key, this.ownerId);
        nbt.putInt(NBTKey.TIME_TO_LIVE.key, this.timeToLive);
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    private static final int checkInterval = 4;
    private static final int checkDamageInterval = 40;


    @Override
    public void tick() {
        float yrden_intensity = 0;
        var owner = this.cachedOwner;
        if(owner == null){
            yrden_intensity = 1;
        }
        else{
            yrden_intensity = (float) owner.getAttributeValue((RegistryEntry<EntityAttribute>) WitcherAttributes.YRDEN_INTENSITY);
        }
        super.tick();
        var spellEntry = getSpellEntry();
        if (spellEntry == null) {
            return;
        }
        var world = this.getWorld();
        if (world.isClient()) {

        } else {
            if (this.age > this.timeToLive) {
                this.kill();
            }
            if (this.age % checkInterval == 0) {
                var entities = getWorld().getOtherEntities(this, this.getBoundingBox().expand(3.5F));
                for (var entity : entities) {
                    if(entity instanceof PersistentProjectileEntity projectile){
                        if (!isProtected(projectile.getOwner())) {
                            entity.playSound(yrdenSound,1F,1F);
                            if(!entity.getWorld().isClient()){
                                ParticleHelper.sendBatches(entity, List.of(yrden_damage_circle));
                                ParticleHelper.sendBatches(entity, List.of(yrden_damage_spehre));
                            }
                            projectile.kill();
                        }
                    }
                    if (entity instanceof LivingEntity livingEntity) {
                        if (!isProtected(livingEntity)) {
                            if(this.age % checkDamageInterval == 0){
                                RegistryEntry<Spell> yrdenGlyphSpellImpact = SpellRegistry.from(owner.getWorld()).getEntry(Identifier.of(MOD_ID, "yrden_glyph_impact")).get();
                                SpellImpacts.performImpacts(owner.getWorld(), owner, livingEntity, livingEntity, yrdenGlyphSpellImpact,
                                        yrdenGlyphSpellImpact.value().impacts, new SpellExecution.ImpactContext().power(SpellPower.getSpellPower(WitcherSpellSchools.YRDEN, owner)).position(livingEntity.getPos()));
                                livingEntity.playSound(yrdenSound,1F,1F);
                                if(!entity.getWorld().isClient()){
                                    ParticleHelper.sendBatches(entity, List.of(yrden_damage_circle));
                                    ParticleHelper.sendBatches(entity, List.of(yrden_damage_spehre));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isProtected(Entity other) {
        var owner = this.getOwner();
        if (owner == null) {
            return false;
        }
        var relation = EntityRelations.getRelation(owner, other);
        switch (relation) {
            case ALLY, FRIENDLY -> {
                return true;
            }
            case NEUTRAL, MIXED, HOSTILE -> {
                return false;
            }
        }
        return false;
    }

    @Nullable public RegistryEntry<Spell> getSpellEntry() {
        return SpellRegistry.from(this.getWorld()).getEntry(this.spellId).orElse(null);
    }

    public int getTimeToLive() {
        return this.timeToLive;
    }

    private LivingEntity cachedOwner = null;
    @Nullable
    public LivingEntity getOwner() {
        if (cachedOwner != null) {
            return cachedOwner;
        }
        var owner = this.getWorld().getEntityById(this.ownerId);
        if (owner instanceof LivingEntity livingOwner) {
            cachedOwner = livingOwner;
            return livingOwner;
        }
        return null;
    }
}
