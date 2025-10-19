package net.witcher_rpg.mixin;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.utils.AnimationHelper;
import net.witcher_rpg.effect.WitcherStatusEffects;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Random;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;;

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

    @Inject(at = @At("HEAD"), method = "isBlocking", cancellable = true)
    private void witcherBlockingMechanics( final CallbackInfoReturnable<Boolean> info) {
        LivingEntity player2 = ((LivingEntity) (Object) this);
        if (player2 instanceof ServerPlayerEntity player && player instanceof SpellCasterEntity caster) {
            var spellEntryWhirl = SpellRegistry.from(player.getWorld()).getEntry(Identifier.of(MOD_ID, "whirl")).orElse(null);
            var spellWhirl = spellEntryWhirl.value();
            RegistryEntry <StatusEffect>  effect = WitcherStatusEffects.WITCHER_REFLEXES.entry;
            Spell spell = caster.getCurrentSpell();
            ItemStack stack = player.getEquippedStack(EquipmentSlot.MAINHAND);
            if (stack.isIn(ItemTags.SWORDS) && player2.hasStatusEffect(WitcherStatusEffects.WITCHER_REFLEXES.entry) && !caster.isCastingSpell() && !player.isUsingItem() && !player.isSleeping()) {
                info.setReturnValue(true);
            }
            if (spell != null
                    && Objects.equals(caster.getCurrentSpell(), spellWhirl)) {
                info.setReturnValue(true);
            }
        }
    }
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void animationWitcherReflexes(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity damagedTarget = ((LivingEntity) (Object) this);
        RegistryEntry <StatusEffect>  effect = WitcherStatusEffects.WITCHER_REFLEXES.entry;
        ItemStack stack = damagedTarget.getEquippedStack(EquipmentSlot.MAINHAND);
        if (stack.isIn(ItemTags.SWORDS) &&  damagedTarget instanceof ServerPlayerEntity player && player instanceof SpellCasterEntity caster && !caster.isCastingSpell()
                && !player.isUsingItem() && !player.isSleeping() && damagedTarget.hasStatusEffect(effect)
                && !source.isIn(DamageTypeTags.BYPASSES_SHIELD)) {
            var tracker = PlayerLookup.tracking(damagedTarget);
            AnimationHelper.sendAnimation(player, tracker, SpellCast.Animation.MISC, "witcher_rpg:witcher_reflexes", 1F);
            int amplifier = player.getStatusEffect(effect).getAmplifier();
            int duration = player.getStatusEffect(effect).getDuration();
            if(amplifier == 0){
                player.removeStatusEffect(effect);
            }else{
                player.removeStatusEffect(effect);
                player.addStatusEffect(new StatusEffectInstance(effect,
                        duration,amplifier-1,false,false,true));
            }
        }
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void applyQuenHealBeforeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity damagedTarget = ((LivingEntity) (Object) this);
        if(damagedTarget.isPlayer() && damagedTarget.hasStatusEffect(WitcherStatusEffects.QUEN_ACTIVE.entry) && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)){
            damagedTarget.heal(amount/2);
        }
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void decreaseAdrenalineAmplifierOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity damagedTarget = ((LivingEntity) (Object) this);
        if(damagedTarget.isPlayer() && damagedTarget.hasStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry)){
            int adrenaline_effect_amplifier = damagedTarget.getStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry).getAmplifier();
            int adrenaline_effect_duration = damagedTarget.getStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry).getDuration();
            float adrenaline_attribute_player = (float) (damagedTarget.getAttributeValue(WitcherAttributes.ADRENALINE_MODIFIER)-100.0F);
            float random = new Random().nextFloat(100);
            if(adrenaline_effect_amplifier != 0){
                if(random > adrenaline_attribute_player){
                    damagedTarget.removeStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry);
                    damagedTarget.addStatusEffect(new StatusEffectInstance(WitcherStatusEffects.ADRENALINE_GAIN.entry,
                            adrenaline_effect_duration,adrenaline_effect_amplifier-1,false,false,true));
                }

            }else{
                damagedTarget.removeStatusEffect(WitcherStatusEffects.ADRENALINE_GAIN.entry);
            }
        }
    }
}
