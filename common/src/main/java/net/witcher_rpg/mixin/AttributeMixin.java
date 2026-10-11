package net.witcher_rpg.mixin;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
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
    private void getMergedSignIntensity(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<Double> cir) {
        if (attribute != AARD_INTENSITY && attribute != AXII_INTENSITY && attribute != IGNI_INTENSITY
                && attribute != QUEN_INTENSITY && attribute != YRDEN_INTENSITY) {
            return;
        }
        EntityAttributeInstance specific = this.custom.get(attribute);
        if (specific == null && !this.fallback.has(attribute)) {
            return;
        }
        EntityAttributeInstance generic = this.custom.get(SIGN_INTENSITY);
        if (generic == null || (generic.getModifiers().isEmpty() && generic.getBaseValue() <= 0)) {
            return;
        }

        double base = (specific != null ? specific.getBaseValue() : this.fallback.getBaseValue(attribute)) + generic.getBaseValue();
        double added = 0;
        double multipliedBase = 0;
        double multipliedTotal = 1;
        for (EntityAttributeInstance instance : new EntityAttributeInstance[]{specific, generic}) {
            if (instance == null) continue;
            for (EntityAttributeModifier modifier : instance.getModifiers()) {
                switch (modifier.operation()) {
                    case ADD_VALUE -> added += modifier.value();
                    case ADD_MULTIPLIED_BASE -> multipliedBase += modifier.value();
                    case ADD_MULTIPLIED_TOTAL -> multipliedTotal *= 1.0 + modifier.value();
                }
            }
        }
        double flat = base + added;
        double total = flat + flat * multipliedBase;
        total *= multipliedTotal;
        cir.setReturnValue(attribute.value().clamp(total));
    }
}
