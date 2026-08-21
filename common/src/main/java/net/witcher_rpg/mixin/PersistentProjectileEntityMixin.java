package net.witcher_rpg.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.internals.casting.SpellCastSyncHelper;
import net.spell_engine.internals.container.SpellContainerSource;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.spell.WitcherModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {

    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void entityHitArrowDeflect(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity hitEntity = entityHitResult.getEntity();
        if (!(hitEntity instanceof PlayerEntity playerEntity)) return;
        if (!(playerEntity instanceof SpellCasterEntity caster)) return;
        if (!caster.isCastingSpell()) return;

        var process = caster.getSpellCastProcess();
        if (process == null || !process.id().equals(Identifier.of(MOD_ID, "defensive_witcher_mechanics"))) return;

        if (playerEntity.getWorld().isClient()) return;

        Entity arrowEntity = (Entity) (Object) this;
        if (!(arrowEntity instanceof PersistentProjectileEntity arrow)) return;
        if (!(arrow.getOwner() instanceof LivingEntity shooter)) return;

        // Only deflect if the player has the arrow_deflection passive spell unlocked
        var arrowDeflectionEntry = SpellRegistry.from(playerEntity.getWorld())
                .getEntry(WitcherModifiers.arrow_deflection.id()).orElse(null);
        var playerSpells = SpellContainerSource.getSpellsOf(playerEntity);
        boolean hasArrowDeflection = arrowDeflectionEntry != null &&
                (playerSpells.passives().contains(arrowDeflectionEntry) || playerSpells.modifiers().contains(arrowDeflectionEntry));
        if (!hasArrowDeflection) return; // No deflection — fall through to witcher$reflexesBlock for normal damage blocking
        if (!witcher$isFrontalArrow(playerEntity, arrow)) return; // Arrow arriving from behind — fall through to witcher$reflexesBlock

        ci.cancel(); // Cancel onEntityHit entirely — arrow is deflected, no damage applied

        Vec3d toShooter = shooter.getPos()
                .add(0, shooter.getStandingEyeHeight() / 2.0, 0)
                .subtract(playerEntity.getPos().add(0, playerEntity.getStandingEyeHeight() / 2.0, 0))
                .normalize()
                .multiply(arrow.getVelocity().length());

        arrow.setVelocity(toShooter);
        arrow.setOwner(playerEntity);
        arrow.age = 0;

        arrow.setYaw((float)(MathHelper.atan2(toShooter.x, toShooter.z) * (180.0 / Math.PI)));
        arrow.setPitch((float)(MathHelper.atan2(toShooter.y, toShooter.horizontalLength()) * (180.0 / Math.PI)));
        arrow.prevYaw = arrow.getYaw();
        arrow.prevPitch = arrow.getPitch();

        // Apply cooldown so client re-cast packets are rejected by attemptCasting()
        var spellEntry = SpellRegistry.from(playerEntity.getWorld())
                .getEntry(Identifier.of(MOD_ID, "defensive_witcher_mechanics")).orElse(null);
        if (spellEntry != null) {
            int cooldownTicks = Math.round(spellEntry.value().cost.cooldown.duration * 20);
            caster.getCooldownManager().set(spellEntry, cooldownTicks);
        }
        SpellCastSyncHelper.clearCasting(playerEntity);
    }

    @Unique
    private static boolean witcher$isFrontalArrow(PlayerEntity player, PersistentProjectileEntity arrow) {
        Vec3d toArrow = arrow.getPos().subtract(player.getPos());
        toArrow = new Vec3d(toArrow.x, 0, toArrow.z);
        if (toArrow.lengthSquared() < 1.0E-4) return true;
        toArrow = toArrow.normalize();

        Vec3d facing = Vec3d.fromPolar(0, player.getYaw());
        return toArrow.dotProduct(facing) >= 0;
    }

    @Shadow
    protected boolean inGround;

    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void witcher$quenAbsorb(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity hit = entityHitResult.getEntity();

        if (hit instanceof PlayerEntity player
                && player.hasStatusEffect(WitcherStatusEffects.QUEN_ACTIVE.entry)) {

            if ((Object)this instanceof PersistentProjectileEntity arrow) {
                ci.cancel();

                DamageSource source = arrow.getDamageSources().arrow(arrow, arrow.getOwner());
                hit.damage(source, (float) arrow.getDamage());

                arrow.discard();
                this.inGround = false;
                arrow.velocityDirty = true;
            }
        }
    }
}
