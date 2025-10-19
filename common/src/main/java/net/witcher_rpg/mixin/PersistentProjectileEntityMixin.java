package net.witcher_rpg.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.witcher_rpg.effect.WitcherStatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setFireTicks(I)V"), cancellable = true)
    private void entityHitArrowDeflect(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity hitEntity = entityHitResult.getEntity();
        if (hitEntity instanceof PlayerEntity playerEntity
                && playerEntity.hasStatusEffect(WitcherStatusEffects.ARROW_DEFLECTION.entry) && playerEntity.isBlocking())
        {
            if (!playerEntity.getWorld().isClient()) {
                Entity arrowEntity = (Entity) (Object) this;

                if (arrowEntity instanceof PersistentProjectileEntity arrow && arrow.getOwner() instanceof LivingEntity shooter) {
                    ci.cancel();
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
                }
            }
        }
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
