package net.witcher_rpg.client.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.fx.ParticleHelper;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class AxiiParticles implements CustomParticleStatusEffect.Spawner {
    private final ParticleGroup particles;

    public AxiiParticles(int particleCount) {
        // `axii_sign_cast` is a plain SimpleParticleType, so only the batch geometry applies.
        // V1 passed angle 360, which SPHERE ignores (angle is a CONE spread) — dropped.
        this.particles = ParticleGroupBuilder.of(Identifier.of(MOD_ID, "axii_sign_cast"))
                .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                        .count(particleCount)
                        .speed(0.05F, 0.08F));
    }

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        var scaledParticles = particles.copy();
        scaledParticles.batch.count *= (1);
        ParticleHelper.play(livingEntity.getWorld(), livingEntity, scaledParticles);
    }
}
