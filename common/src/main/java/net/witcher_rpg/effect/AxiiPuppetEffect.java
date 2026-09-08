package net.witcher_rpg.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import net.more_rpg_classes.client.particle.MoreParticles;
import net.more_rpg_classes.client.particle.PopupParticleEffect;
import net.more_rpg_classes.effect.ControlEnemyStatusEffect;
import net.witcher_rpg.util.tags.WitcherEntityTags;

public class AxiiPuppetEffect extends ControlEnemyStatusEffect {
    private static final int ICON_REFRESH_INTERVAL_TICKS = 40;

    protected AxiiPuppetEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public double controlRange() {
        return 32.0;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (entity.hasStatusEffect(WitcherStatusEffects.AXII_PUPPET.effect)) {
            popIcon(entity);
        }
    }

    // 1.20.1: `applyUpdateEffect` returns void.
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        if (entity.age % ICON_REFRESH_INTERVAL_TICKS == 0) {
            popIcon(entity);
        }
    }

    private static void popIcon(LivingEntity entity) {
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    new PopupParticleEffect(MoreParticles.POPUP, WitcherStatusEffects.AXII_PUPPET.id, false, entity.getId()),
                    entity.getX(), entity.getEyeY() + 0.2, entity.getZ(), 1, 0, 0, 0, 0);
        }
    }

    @Override
    protected boolean isImmune(LivingEntity entity) {
        return entity.getType().isIn(WitcherEntityTags.AXII_EFFECT_IMMUNE);
    }

    @Override
    protected void onImmune(LivingEntity entity) {
        entity.removeStatusEffect(WitcherStatusEffects.AXII_PUPPET.effect);
    }

    @Override
    protected void onControlledNonMob(LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(WitcherStatusEffects.AXII.effect, 40, 0, false, false, true));
        entity.removeStatusEffect(WitcherStatusEffects.AXII_PUPPET.effect);
    }
}
