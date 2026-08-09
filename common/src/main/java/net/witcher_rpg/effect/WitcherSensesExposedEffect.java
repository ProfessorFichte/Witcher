package net.witcher_rpg.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.more_rpg_classes.client.particle.MoreParticles;
import net.more_rpg_classes.client.particle.PopupParticleEffect;
import net.spell_engine.api.effect.CustomStatusEffect;

public class WitcherSensesExposedEffect extends CustomStatusEffect {
    public WitcherSensesExposedEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public void onApplied(LivingEntity livingEntity, int amplifier) {
        super.onApplied(livingEntity, amplifier);
        if (livingEntity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    new PopupParticleEffect(MoreParticles.POPUP, WitcherStatusEffects.WITCHER_SENSES_EXPOSED.id, false, livingEntity.getId()),
                    livingEntity.getX(), livingEntity.getEyeY() + 0.2, livingEntity.getZ(),
                    1, 0, 0, 0, 0);
        }
    }
}
