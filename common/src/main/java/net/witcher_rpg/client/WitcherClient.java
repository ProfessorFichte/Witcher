package net.witcher_rpg.client;

import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.particle.SpellFlameParticle;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.witcher_rpg.client.armor.*;
import net.witcher_rpg.client.effect.AxiiParticles;
import net.witcher_rpg.client.effect.QuenActiveShieldRenderer;
import net.witcher_rpg.client.entity.YrdenMagicTrapRenderer;
import net.witcher_rpg.client.entity.YrdenRenderer;
import net.witcher_rpg.client.particle.WitcherParticles;
import net.witcher_rpg.client.predicate_models.WitcherModelPredicates;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.YrdenEntity;
import net.witcher_rpg.entity.YrdenMagicTrapEntity;
import net.witcher_rpg.item.armor.Armors;
import net.witcher_rpg.spell.WitcherSpells;


import java.util.List;
import java.util.function.Supplier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherClient{

    public static void  init(){
        for (var entry: WitcherSpells.entries) {
            if (entry.mutator() != null) {
                SpellTooltip.addDescriptionMutator(entry.id(), entry.mutator());
            }
        }

        WitcherModelPredicates.registerModelPredicates();
        CustomModels.registerModelIds(List.of(
                YrdenRenderer.modelId,
                YrdenMagicTrapRenderer.modelId,
                QuenActiveShieldRenderer.modelId,
                Identifier.of(MOD_ID, "projectile/rend"),
                Identifier.of(MOD_ID, "projectile/crystal_skull")
        ));

        CustomParticleStatusEffect.register(WitcherStatusEffects.AXII.effect, new AxiiParticles(1));
        CustomParticleStatusEffect.register(WitcherStatusEffects.AXII_PUPPET.effect, new AxiiParticles(3));
        CustomParticleStatusEffect.register(
                WitcherStatusEffects.ROSE_OF_REMEMBRANCE.effect,
                new BuffParticleSpawner(
                        BuffParticleSpawner.defaultBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPELL,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        0.5F,
                        Color.RED.toRGBA()))
        );
        CustomParticleStatusEffect.register(
                WitcherStatusEffects.SUNSTONE.effect,
                new BuffParticleSpawner(
                        BuffParticleSpawner.defaultBatch(
                                SpellEngineParticles.MagicParticles.get(
                                        SpellEngineParticles.MagicParticles.Shape.SPELL,
                                        SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
                                0.5F,
                                Color.WHITE.toRGBA())
                )
        );
        CustomParticleStatusEffect.register(
                WitcherStatusEffects.QUEN_SHIELD.effect,
                new BuffParticleSpawner(
                        new ParticleBatch(
                                SpellEngineParticles.aura_effect_622.id().toString(),
                                ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
                                1, 0, 0)
                                .scale(1.4F)
                                .followEntity(true).copy().color(Color.ELECTRIC.alpha(0.5F).toRGBA())
                ).withFrequency(20).scaleWithAmplifier(false)
        );
        CustomParticleStatusEffect.register(
                WitcherStatusEffects.YRDEN_GRIFFIN_MASTER.effect,
                new BuffParticleSpawner(
                        new ParticleBatch(
                                SpellEngineParticles.aura_effect_622.id().toString(),
                                ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER,
                                1, 0, 0)
                                .scale(1.4F)
                                .followEntity(true).copy().color(Color.ARCANE.alpha(0.5F).toRGBA())
                ).withFrequency(20).scaleWithAmplifier(false)
        );

        CustomModelStatusEffect.register(WitcherStatusEffects.QUEN_ACTIVE.effect, new QuenActiveShieldRenderer());

        registerArmorRenderer(Armors.witcherArmorSet, CustomArmorRenderer::kaer_morhen);

        registerArmorRenderer(Armors.felineSchoolArmorSet, CustomArmorRenderer::feline);
        registerArmorRenderer(Armors.enhancedFelineSchoolArmorSet, CustomArmorRenderer::enhanced_feline);
        registerArmorRenderer(Armors.superiorFelineSchoolArmorSet, CustomArmorRenderer::superior_feline);
        registerArmorRenderer(Armors.mastercraftedFelineSchoolArmorSet, CustomArmorRenderer::mastercrafted_feline);
        registerArmorRenderer(Armors.grandmasterFelineSchoolArmorSet, CustomArmorRenderer::grandmaster_feline);

        registerArmorRenderer(Armors.griffinArmorSet, CustomArmorRenderer::griffin);
        registerArmorRenderer(Armors.enhancedGriffinArmorSet, CustomArmorRenderer::enhanced_griffin);
        registerArmorRenderer(Armors.superiorGriffinArmorSet, CustomArmorRenderer::superior_griffin);
        registerArmorRenderer(Armors.mastercraftedGriffinArmorSet, CustomArmorRenderer::mastercrafted_griffin);
        registerArmorRenderer(Armors.grandmasterGriffinArmorSet, CustomArmorRenderer::grandmaster_griffin);

        registerArmorRenderer(Armors.ursineArmorSet, CustomArmorRenderer::ursine);
        registerArmorRenderer(Armors.enhancedUrsineArmorSet, CustomArmorRenderer::enhanced_ursine);
        registerArmorRenderer(Armors.superiorUrsineArmorSet, CustomArmorRenderer::superior_ursine);
        registerArmorRenderer(Armors.mastercraftedUrsineArmorSet, CustomArmorRenderer::mastercrafted_ursine);
        registerArmorRenderer(Armors.grandmasterUrsineArmorSet, CustomArmorRenderer::grandmaster_ursine);

        registerArmorRenderer(Armors.wolvenArmorSet, CustomArmorRenderer::wolven);
        registerArmorRenderer(Armors.enhancedWolvenArmorSet, CustomArmorRenderer::enhanced_wolven);
        registerArmorRenderer(Armors.superiorWolvenArmorSet, CustomArmorRenderer::superior_wolven);
        registerArmorRenderer(Armors.mastercraftedWolvenArmorSet, CustomArmorRenderer::mastercrafted_wolven);
        registerArmorRenderer(Armors.grandmasterWolvenArmorSet, CustomArmorRenderer::grandmaster_wolven);

        EntityRendererRegistry.register(YrdenEntity.ENTITY_TYPE,YrdenRenderer::new);
        EntityRendererRegistry.register(YrdenMagicTrapEntity.ENTITY_TYPE,YrdenMagicTrapRenderer::new);
    }
    public static void registerParticleAppearances() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();

        registry.register(WitcherParticles.IGNI_SIGN, SoulParticle.Factory::new);
        registry.register(WitcherParticles.YRDEN_SIGN, SoulParticle.Factory::new);
        registry.register(WitcherParticles.AARD_SIGN, SoulParticle.Factory::new);
        registry.register(WitcherParticles.QUEN_SIGN, SoulParticle.Factory::new);
        registry.register(WitcherParticles.AXII_SIGN, SpellFlameParticle.FlameFactory::new);
        registry.register(WitcherParticles.YRDEN_IMPACT, DamageParticle.Factory::new);
        registry.register(WitcherParticles.YRDEN_CLOUD, DragonBreathParticle.Factory::new);
    }

    private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
        AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
    }

}
