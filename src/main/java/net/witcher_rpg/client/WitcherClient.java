package net.witcher_rpg.client;

import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.rewrite.render.armor.AzArmorRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.render.CustomModels;
import net.witcher_rpg.client.armor.*;
import net.witcher_rpg.client.effect.AxiiParticles;
import net.witcher_rpg.client.effect.QuenActiveShieldRenderer;
import net.witcher_rpg.client.entity.YrdenMagicTrapRenderer;
import net.witcher_rpg.client.entity.YrdenRenderer;
import net.witcher_rpg.client.particle.Particles;
import net.witcher_rpg.client.predicate_models.WitcherModelPredicates;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.YrdenEntity;
import net.witcher_rpg.entity.YrdenMagicTrapEntity;
import net.witcher_rpg.item.armor.Armors;


import java.util.List;
import java.util.function.Supplier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

@Environment(EnvType.CLIENT)
public class WitcherClient implements ClientModInitializer {

    public void  onInitializeClient(){
        WitcherModelPredicates.registerModelPredicates();
        CustomModels.registerModelIds(List.of(
                YrdenRenderer.modelId,
                YrdenMagicTrapRenderer.modelId,
                QuenActiveShieldRenderer.modelId,
                Identifier.of(MOD_ID, "projectile/rend")
        ));

        ParticleFactoryRegistry.getInstance().register(Particles.IGNI_SIGN, SoulParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.YRDEN_SIGN, SoulParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.AARD_SIGN, SoulParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.QUEN_SIGN, SoulParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.AXII_SIGN, FireworksSparkParticle.ExplosionFactory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.YRDEN_IMPACT, DamageParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.YRDEN_CLOUD, DragonBreathParticle.Factory::new);

        CustomParticleStatusEffect.register(WitcherStatusEffects.AXII.effect, new AxiiParticles(1));
        CustomParticleStatusEffect.register(WitcherStatusEffects.AXII_PUPPET.effect, new AxiiParticles(3));

        CustomModelStatusEffect.register(WitcherStatusEffects.QUEN_ACTIVE.effect, new QuenActiveShieldRenderer());

        registerArmorRenderer(Armors.witcherArmorSet, WitcherArmorRenderer::witcher);

        registerArmorRenderer(Armors.ursineArmorSet, BearSchoolArmorRenderer::ursine);
        registerArmorRenderer(Armors.enhancedUrsineArmorSet, BearSchoolArmorRenderer::enhanced_ursine);
        registerArmorRenderer(Armors.superiorUrsineArmorSet, BearSchoolArmorRenderer::superior_ursine);

        registerArmorRenderer(Armors.felineSchoolArmorSet, CatSchoolArmorRenderer::feline);
        registerArmorRenderer(Armors.enhancedFelineSchoolArmorSet, CatSchoolArmorRenderer::enhanced_feline);
        registerArmorRenderer(Armors.superiorFelineSchoolArmorSet, CatSchoolArmorRenderer::superior_feline);

        registerArmorRenderer(Armors.felineSchoolArmorSet, CatSchoolArmorRenderer::feline);
        registerArmorRenderer(Armors.enhancedFelineSchoolArmorSet, CatSchoolArmorRenderer::enhanced_feline);
        registerArmorRenderer(Armors.superiorFelineSchoolArmorSet, CatSchoolArmorRenderer::superior_feline);

        registerArmorRenderer(Armors.griffinArmorSet, GriffinSchoolArmorRenderer::griffin);
        registerArmorRenderer(Armors.enhancedGriffinArmorSet, GriffinSchoolArmorRenderer::enhanced_griffin);
        registerArmorRenderer(Armors.superiorGriffinArmorSet, GriffinSchoolArmorRenderer::superior_griffin);

        registerArmorRenderer(Armors.wolvenArmorSet, WolfSchoolArmorRenderer::wolven);
        registerArmorRenderer(Armors.enhancedWolvenArmorSet, WolfSchoolArmorRenderer::enhanced_wolven);
        registerArmorRenderer(Armors.superiorWolvenArmorSet, WolfSchoolArmorRenderer::superior_wolven);

        EntityRendererRegistry.register(YrdenEntity.ENTITY_TYPE,YrdenRenderer::new);
        EntityRendererRegistry.register(YrdenMagicTrapEntity.ENTITY_TYPE,YrdenMagicTrapRenderer::new);
    }

    private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
        AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
    }

}
