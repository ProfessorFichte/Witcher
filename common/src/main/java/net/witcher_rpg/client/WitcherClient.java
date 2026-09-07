package net.witcher_rpg.client;

import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.rpg_foundation.armor_api.client.ArmorRenderers;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.witcher_rpg.client.armor.*;
import net.witcher_rpg.client.effect.AxiiParticles;
import net.witcher_rpg.client.effect.QuenActiveShieldRenderer;
import net.witcher_rpg.client.particle.WitcherParticles;
import net.witcher_rpg.client.predicate_models.WitcherModelPredicates;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.item.armor.Armors;

import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.particle.DamageParticle;
import net.minecraft.client.particle.DragonBreathParticle;


public class WitcherClient{

    public static void  init(){
        WitcherModelPredicates.registerModelPredicates();

        CustomParticleStatusEffect.register(
                WitcherStatusEffects.AXII.effect,
                new AxiiParticles(1)
        );
        CustomParticleStatusEffect.register(
                WitcherStatusEffects.AXII_PUPPET.effect,
                new AxiiParticles(3)
        );
        CustomParticleStatusEffect.register(
                WitcherStatusEffects.ROSE_OF_REMEMBRANCE.effect,
                new BuffParticleSpawner(
                        ParticleGroupBuilder.magic(SpellEngineParticles.magic_spell, ParticleGroup.Motion.ASCEND, Color.RED)
                                .batch(ParticleGroupBuilder.Batches.casting(0.5F, 0.12F)
                                        .andThen(b -> b.speed(0.11F, 0.12F).extent(-0.2F))))
        );
        CustomParticleStatusEffect.register(
                WitcherStatusEffects.SUNSTONE.effect,
                new BuffParticleSpawner(
                        ParticleGroupBuilder.magic(SpellEngineParticles.magic_spell, ParticleGroup.Motion.FLOAT, Color.WHITE)
                                .batch(ParticleGroupBuilder.Batches.casting(0.5F, 0.12F)
                                        .andThen(b -> b.speed(0.11F, 0.12F).extent(-0.2F))))
        );
        CustomParticleStatusEffect.register(
                WitcherStatusEffects.QUEN_SHIELD.effect,
                new BuffParticleSpawner(
                        ParticleGroupBuilder.of(SpellEngineParticles.area_effect_622)
                                .facing(ParticleGroup.Facing.CAMERA)
                                .attached()
                                .scale(1.4F)
                                .color(Color.ELECTRIC.alpha(0.5F))
                                .batch(b -> b.shape(ParticleGroup.Shape.LINE).count(1).speed(0F, 0F))
                ).withFrequency(20).scaleWithAmplifier(false)
        );
        CustomParticleStatusEffect.register(
                WitcherStatusEffects.YRDEN_GRIFFIN_MASTER.effect,
                new BuffParticleSpawner(
                        ParticleGroupBuilder.of(SpellEngineParticles.area_effect_622)
                                .facing(ParticleGroup.Facing.CAMERA)
                                .attached()
                                .scale(1.4F)
                                .color(Color.ARCANE.alpha(0.5F))
                                .batch(b -> b.shape(ParticleGroup.Shape.LINE).count(1).speed(0F, 0F))
                ).withFrequency(20).scaleWithAmplifier(false)
        );

        CustomModelStatusEffect.register(WitcherStatusEffects.QUEN_ACTIVE.effect, new QuenActiveShieldRenderer());

        registerArmorRenderer(Armors.witcherArmorSet, CustomArmorRenderer.kaer_morhen());

        registerArmorRenderer(Armors.felineSchoolArmorSet, CustomArmorRenderer.feline());
        registerArmorRenderer(Armors.enhancedFelineSchoolArmorSet, CustomArmorRenderer.enhanced_feline());
        registerArmorRenderer(Armors.superiorFelineSchoolArmorSet, CustomArmorRenderer.superior_feline());
        registerArmorRenderer(Armors.mastercraftedFelineSchoolArmorSet, CustomArmorRenderer.mastercrafted_feline());
        registerArmorRenderer(Armors.grandmasterFelineSchoolArmorSet, CustomArmorRenderer.grandmaster_feline());

        registerArmorRenderer(Armors.griffinArmorSet, CustomArmorRenderer.griffin());
        registerArmorRenderer(Armors.enhancedGriffinArmorSet, CustomArmorRenderer.enhanced_griffin());
        registerArmorRenderer(Armors.superiorGriffinArmorSet, CustomArmorRenderer.superior_griffin());
        registerArmorRenderer(Armors.mastercraftedGriffinArmorSet, CustomArmorRenderer.mastercrafted_griffin());
        registerArmorRenderer(Armors.grandmasterGriffinArmorSet, CustomArmorRenderer.grandmaster_griffin());

        registerArmorRenderer(Armors.ursineArmorSet, CustomArmorRenderer.ursine());
        registerArmorRenderer(Armors.enhancedUrsineArmorSet, CustomArmorRenderer.enhanced_ursine());
        registerArmorRenderer(Armors.superiorUrsineArmorSet, CustomArmorRenderer.superior_ursine());
        registerArmorRenderer(Armors.mastercraftedUrsineArmorSet, CustomArmorRenderer.mastercrafted_ursine());
        registerArmorRenderer(Armors.grandmasterUrsineArmorSet, CustomArmorRenderer.grandmaster_ursine());

        registerArmorRenderer(Armors.wolvenArmorSet, CustomArmorRenderer.wolven());
        registerArmorRenderer(Armors.enhancedWolvenArmorSet, CustomArmorRenderer.enhanced_wolven());
        registerArmorRenderer(Armors.superiorWolvenArmorSet, CustomArmorRenderer.superior_wolven());
        registerArmorRenderer(Armors.mastercraftedWolvenArmorSet, CustomArmorRenderer.mastercrafted_wolven());
        registerArmorRenderer(Armors.grandmasterWolvenArmorSet, CustomArmorRenderer.grandmaster_wolven());
    }

    public interface ParticleAppearanceRegistrar {
        <T extends ParticleEffect> void register(ParticleType<T> type, SpriteFactory<T> factory);
    }

    @FunctionalInterface
    public interface SpriteFactory<T extends ParticleEffect> {
        ParticleFactory<T> create(SpriteProvider spriteProvider);
    }

    public static void registerParticleAppearances(ParticleAppearanceRegistrar registrar) {
        registrar.register(WitcherParticles.IGNI_SIGN, SoulParticle.Factory::new);
        registrar.register(WitcherParticles.YRDEN_SIGN, SoulParticle.Factory::new);
        registrar.register(WitcherParticles.AARD_SIGN, SoulParticle.Factory::new);
        registrar.register(WitcherParticles.QUEN_SIGN, SoulParticle.Factory::new);
        registrar.register(WitcherParticles.AXII_SIGN, SoulParticle.Factory::new);
        registrar.register(WitcherParticles.YRDEN_IMPACT, DamageParticle.Factory::new);
        registrar.register(WitcherParticles.YRDEN_CLOUD, DragonBreathParticle.Factory::new);
    }

    private static void registerArmorRenderer(Armor.Set set, GeoArmorRenderer renderer) {
        ArmorRenderers.register(renderer, set.head, set.chest, set.legs, set.feet);
    }

}
