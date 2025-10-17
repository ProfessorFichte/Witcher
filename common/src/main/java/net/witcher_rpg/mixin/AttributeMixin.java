package net.witcher_rpg.mixin;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import static net.witcher_rpg.entity.attribute.WitcherAttributes.*;

@Mixin(value = AttributeContainer.class)
public class AttributeMixin {
    @Final @Shadow
    private Map<EntityAttribute, EntityAttributeInstance> custom;
    @Final @Shadow
    private DefaultAttributeContainer fallback;

    @Inject(at = @At("HEAD"), method = "getValue", cancellable = true)
    private void getAttributeValueAardSign(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<Double> cir) {
        if(attribute == AARD_INTENSITY){
            EntityAttributeInstance signamount = this.custom.get(SIGN_INTENSITY);
            double value1 = 0;
            if(signamount != null) {
                value1 = signamount.getValue();
            }
            double sign = value1;
            EntityAttributeInstance addedsign = this.custom.get(SIGN_INTENSITY);
            if(signamount != null) {
                addedsign.getValue();
            }
            EntityAttributeInstance aardInstance = this.custom.get(AARD_INTENSITY);
            double total = this.fallback.getValue(AARD_INTENSITY);
            if(aardInstance != null){
                total = aardInstance.getValue();
            }
            if( sign > 0 && attribute == AARD_INTENSITY){
                total += sign;
            }
            if(sign > 0){
                cir.setReturnValue(total);
            }
        }
    }
    @Inject(at = @At("HEAD"), method = "getValue", cancellable = true)
    private void getAttributeValueAxiiSign(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<Double> cir) {
        if(attribute == AXII_INTENSITY){
            EntityAttributeInstance signamount = this.custom.get(SIGN_INTENSITY);
            double value1 = 0;
            if(signamount != null) {
                value1 = signamount.getValue();
            }
            double sign = value1;
            EntityAttributeInstance addedsign = this.custom.get(SIGN_INTENSITY);
            if(signamount != null) {
                addedsign.getValue();
            }
            EntityAttributeInstance axiiInstance = this.custom.get(AXII_INTENSITY);
            double total = this.fallback.getValue(AXII_INTENSITY);
            if(axiiInstance != null){
                total = axiiInstance.getValue();
            }
            if( sign > 0 && attribute == AXII_INTENSITY){
                total += sign;
            }
            if(sign > 0){
                cir.setReturnValue(total);
            }
        }
    }
    @Inject(at = @At("HEAD"), method = "getValue", cancellable = true)
    private void getAttributeValueIgniSign(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<Double> cir) {
        if(attribute == IGNI_INTENSITY){
            EntityAttributeInstance signamount = this.custom.get(SIGN_INTENSITY);
            double value1 = 0;
            if(signamount != null) {
                value1 = signamount.getValue();
            }
            double sign = value1;
            EntityAttributeInstance addedsign = this.custom.get(SIGN_INTENSITY);
            if(signamount != null) {
                addedsign.getValue();
            }
            EntityAttributeInstance igniInstance = this.custom.get(IGNI_INTENSITY);
            double total = this.fallback.getValue(IGNI_INTENSITY);
            if(igniInstance != null){
                total = igniInstance.getValue();
            }
            if( sign > 0 && attribute == IGNI_INTENSITY){
                total += sign;
            }
            if(sign > 0){
                cir.setReturnValue(total);
            }
        }
    }
    @Inject(at = @At("HEAD"), method = "getValue", cancellable = true)
    private void getAttributeValueQuenSign(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<Double> cir) {
        if(attribute == QUEN_INTENSITY){
            EntityAttributeInstance signamount = this.custom.get(SIGN_INTENSITY);
            double value1 = 0;
            if(signamount != null) {
                value1 = signamount.getValue();
            }
            double sign = value1;
            EntityAttributeInstance addedsign = this.custom.get(SIGN_INTENSITY);
            if(signamount != null) {
                addedsign.getValue();
            }
            EntityAttributeInstance quenInstance = this.custom.get(QUEN_INTENSITY);
            double total = this.fallback.getValue(QUEN_INTENSITY);
            if(quenInstance != null){
                total = quenInstance.getValue();
            }
            if( sign > 0 && attribute == QUEN_INTENSITY){
                total += sign;
            }
            if(sign > 0){
                cir.setReturnValue(total);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getValue", cancellable = true)
    private void getAttributeValueYrdenSign(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<Double> cir) {
        if(attribute == YRDEN_INTENSITY){
            EntityAttributeInstance signamount = this.custom.get(SIGN_INTENSITY);
            double value1 = 0;
            if(signamount != null) {
                value1 = signamount.getValue();
            }
            double sign = value1;
            EntityAttributeInstance addedsign = this.custom.get(SIGN_INTENSITY);
            if(signamount != null) {
                addedsign.getValue();
            }
            EntityAttributeInstance yrdenInstance = this.custom.get(YRDEN_INTENSITY);
            double total = this.fallback.getValue(YRDEN_INTENSITY);
            if(yrdenInstance != null){
                total = yrdenInstance.getValue();
            }
            if( sign > 0 && attribute == YRDEN_INTENSITY){
                total += sign;
            }
            if(sign > 0){
                cir.setReturnValue(total);
            }
        }
    }

}