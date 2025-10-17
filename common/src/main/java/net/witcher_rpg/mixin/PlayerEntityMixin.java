package net.witcher_rpg.mixin;


import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.witcher_rpg.WitcherClassMod.tweaksConfig;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "attack")
    public void witcherStackAdrenaline(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        EntityAttributeInstance adrenaline = ((LivingEntity) (Object) this).getAttributeInstance(WitcherAttributes.ADRENALINE_MODIFIER);
        int value1 = (int) adrenaline.getValue();
        if (player instanceof ServerPlayerEntity) {
            if (value1 != 100) {
                value1 = value1 -100;
                int adrenaline_duration_multiplier = value1 * 3;
                if(!player.hasStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry)){
                    player.addStatusEffect(new StatusEffectInstance(WitcherStatusEffects.ADRENALINE_GAIN.entry,
                            200+adrenaline_duration_multiplier,0,false,false,true));
                }
                else
                {
                    int currentAmplifier = player.getStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry).getAmplifier();
                    int currentDuration = player.getStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry).getDuration();
                    int effectDuration = adrenaline_duration_multiplier + currentDuration;
                    if(effectDuration > (tweaksConfig.value.adrenaline_max_seconds_duration *20)){
                        effectDuration = (tweaksConfig.value.adrenaline_max_seconds_duration *20);
                    }
                    if(currentAmplifier < tweaksConfig.value.adrenaline_max_amplifier-1){
                        player.addStatusEffect(new StatusEffectInstance(WitcherStatusEffects.ADRENALINE_GAIN.entry,effectDuration,
                                currentAmplifier+1,false,false,true));
                    }
                    else{
                        player.addStatusEffect(new StatusEffectInstance(WitcherStatusEffects.ADRENALINE_GAIN.entry,effectDuration,
                                tweaksConfig.value.adrenaline_max_amplifier-1,false,false,true));
                    }
                }
            }
        }
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float modifyWitcherDamageBattleTrance(float damage) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.hasStatusEffect(WitcherStatusEffects.BATTLE_TRANCE.entry) && player.hasStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry)){
            int adrenaline_effect_amplifier = player.getStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry).getAmplifier() + 1;
            return (float) (damage + (1 + (adrenaline_effect_amplifier / 10)));
        }
        return damage;
    }


}
