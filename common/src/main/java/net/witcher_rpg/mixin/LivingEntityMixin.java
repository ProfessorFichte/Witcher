package net.witcher_rpg.mixin;

import net.spell_engine.Platform;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.compat.CriticalStrikeCompat;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.internals.container.SpellContainerSource;
import net.spell_engine.utils.AnimationHelper;
import net.witcher_rpg.effect.WitcherExposed;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import net.witcher_rpg.network.ExposedGlowPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void witcher$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue()
                .add(WitcherAttributes.AARD_INTENSITY)
                .add(WitcherAttributes.ADRENALINE_MODIFIER)
                .add(WitcherAttributes.AXII_INTENSITY)
                .add(WitcherAttributes.IGNI_INTENSITY)
                .add(WitcherAttributes.QUEN_INTENSITY)
                .add(WitcherAttributes.YRDEN_INTENSITY)
                .add(WitcherAttributes.SIGN_INTENSITY)
        ;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void witcher$igniRollVisuals(CallbackInfo ci) {
        WitcherStatusEffects.tickIgniRollVisuals((LivingEntity) (Object) this);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void witcher$reflexesBlock(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (!(entity instanceof ServerPlayerEntity player)) return;
        if (!(player instanceof SpellCasterEntity caster)) return;
        if (!caster.isCastingSpell()) return;

        var process = caster.getSpellCastProcess();
        if (process == null || !process.id().equals(Identifier.of(MOD_ID, "defensive_witcher_mechanics"))) return;
        if (source.isIn(DamageTypeTags.BYPASSES_SHIELD)) return;
        if (!witcher$isFrontalDamage(player, source)) return;

        var playerSpells = SpellContainerSource.getSpellsOf(player);
        var counterattackEntry = SpellRegistry.from(player.getWorld())
                .getEntry(Identifier.of(MOD_ID, "spell_modifiers/counterattack")).orElse(null);
        boolean hasCounterattack = counterattackEntry != null &&
                (playerSpells.passives().contains(counterattackEntry) || playerSpells.modifiers().contains(counterattackEntry));

        AnimationHelper.sendAnimation(player, Platform.tracking(player),
                SpellCast.Animation.MISC, PlayerAnimation.of("witcher_rpg:witcher_reflexes_release"), 1F);
        if (hasCounterattack) {
            player.addStatusEffect(new StatusEffectInstance(WitcherStatusEffects.COUNTERATTACK_READY.entry, 100, 0, false, false, true));
        }

        // Apply cooldown immediately so client re-cast packets are rejected by attemptCasting()
        var spellEntry = SpellRegistry.from(player.getWorld())
                .getEntry(Identifier.of(MOD_ID, "defensive_witcher_mechanics")).orElse(null);
        if (spellEntry != null) {
            int cooldownTicks = Math.round(spellEntry.value().cost.cooldown.duration * 20);
            caster.getCooldownManager().set(spellEntry, cooldownTicks);
        }
        caster.getInteractor().requestClear();
        cir.setReturnValue(false);
    }

    @Unique
    private static boolean witcher$isFrontalDamage(PlayerEntity player, DamageSource source) {
        Entity origin = source.getSource() != null ? source.getSource() : source.getAttacker();
        Vec3d originPos = origin != null ? origin.getPos() : source.getPosition();
        if (originPos == null) return true;
        Vec3d toSource = originPos.subtract(player.getPos());
        toSource = new Vec3d(toSource.x, 0, toSource.z);
        if (toSource.lengthSquared() < 1.0E-4) return true;
        Vec3d facing = Vec3d.fromPolar(0, player.getYaw());
        return toSource.normalize().dotProduct(facing) >= 0;
    }

    @Inject(at = @At("HEAD"), method = "isBlocking", cancellable = true)
    private void witcherBlockingMechanics(final CallbackInfoReturnable<Boolean> info) {
        LivingEntity entity = ((LivingEntity)(Object)this);
        if (!(entity instanceof ServerPlayerEntity player)) return;
        if (!(player instanceof SpellCasterEntity caster)) return;
        if (!caster.isCastingSpell()) return;

        var process = caster.getSpellCastProcess();
        if (process == null) return;

        Identifier spellId = process.id();

        // Witcher Reflexes: block the next hit while the player is casting
        if (spellId.equals(Identifier.of(MOD_ID, "defensive_witcher_mechanics"))) {
            info.setReturnValue(true);
            return;
        }

        // Whirl: stay in blocking state while casting
        if (spellId.equals(Identifier.of(MOD_ID, "whirl"))) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void applyQuenHealBeforeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity damagedTarget = ((LivingEntity) (Object) this);
        if(damagedTarget.isPlayer() && damagedTarget.hasStatusEffect(WitcherStatusEffects.QUEN_ACTIVE.entry) && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)){
            damagedTarget.heal(amount/2);
        }
    }

    @Unique
    private static final Identifier CRITICAL_STRIKE_DAMAGE_ATTRIBUTE_ID = Identifier.of("critical_strike", "damage");
    @Unique
    private static final float DEFAULT_EXPOSED_CRIT_MULTIPLIER = 1.5F;
    @Unique
    private static final float EXPOSED_CRIT_MULTIPLIER_CEILING = 4.0F;

    @ModifyArg(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"), index = 1)
    private float witcher$guaranteedCritOnExposed(DamageSource source, float amount) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (target.getWorld().isClient()) return amount;
        if (!target.hasStatusEffect(WitcherStatusEffects.WITCHER_SENSES_EXPOSED.entry)) return amount;
        if (!source.isOf(DamageTypes.PLAYER_ATTACK)) return amount;
        if (!(source.getAttacker() instanceof PlayerEntity attacker)) return amount;
        UUID exposedSource = WitcherExposed.get(target.getUuid());
        if (exposedSource == null || !attacker.getUuid().equals(exposedSource)) return amount;
        if (CriticalStrikeCompat.isCriticalStrike(source)) return amount;

        float multiplier = DEFAULT_EXPOSED_CRIT_MULTIPLIER;
        var critDamageAttribute = Registries.ATTRIBUTE.getEntry(CRITICAL_STRIKE_DAMAGE_ATTRIBUTE_ID).orElse(null);
        if (critDamageAttribute != null) {
            var instance = attacker.getAttributeInstance(critDamageAttribute);
            if (instance != null) {
                multiplier = (float) (instance.getValue() / 100.0);
            }
        }
        multiplier = MathHelper.clamp(multiplier, 1.0F, EXPOSED_CRIT_MULTIPLIER_CEILING);

        CriticalStrikeCompat.setCriticalStrike(source, multiplier);
        return amount * multiplier;
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"))
    private void witcher$captureExposedSource(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.getWorld().isClient()) return;
        if (effect.getEffectType().value() != WitcherStatusEffects.WITCHER_SENSES_EXPOSED.effect) return;
        if (!(source instanceof ServerPlayerEntity player)) return;
        WitcherExposed.set(self.getUuid(), player.getUuid());
        Platform.util().networkS2C_Send(player, new ExposedGlowPayload(self.getId(), true));
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void decreaseAdrenalineAmplifierOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity damagedTarget = ((LivingEntity) (Object) this);
        if (damagedTarget.isPlayer() && damagedTarget.hasStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry)) {
            if (WitcherStatusEffects.rollAdrenaline(damagedTarget)) {
                return;
            }
            int amplifier = damagedTarget.getStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry).getAmplifier();
            int duration = damagedTarget.getStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry).getDuration();
            damagedTarget.removeStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry);
            if (amplifier > 0) {
                damagedTarget.addStatusEffect(new StatusEffectInstance(WitcherStatusEffects.ADRENALINE_GAIN.entry,
                        duration, amplifier - 1, false, false, true));
            }
        }
    }

}
