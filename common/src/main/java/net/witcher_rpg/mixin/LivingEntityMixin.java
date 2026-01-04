package net.witcher_rpg.mixin;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
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
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.item.component.GlyphSlots;
import net.witcher_rpg.item.component.RunestoneSlots;
import net.witcher_rpg.item.component.WitcherDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

    @Unique
    private static final Identifier RUNESTONE_MODIFIER_BASE = Identifier.of("witcher_rpg", "runestone_bonus");
    @Unique
    private static final Identifier GLYPH_MODIFIER_BASE = Identifier.of("witcher_rpg", "glyph_bonus");

    @Unique
    private ItemStack witcher$lastMainHand = ItemStack.EMPTY;
    @Unique
    private ItemStack witcher$lastChestplate = ItemStack.EMPTY;

    @Unique
    private final java.util.Set<String> witcher$trackedRunestoneAttributes = new java.util.HashSet<>();
    @Unique
    private final java.util.Set<String> witcher$trackedGlyphAttributes = new java.util.HashSet<>();

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
    private void witcher$updateEquipmentAttributeModifiers(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.getWorld().isClient()) {
            return;
        }

        ItemStack currentMainHand = this.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack currentChestplate = this.getEquippedStack(EquipmentSlot.CHEST);

        // Check if equipment changed (or if this is the first tick)
        boolean mainHandChanged = !ItemStack.areEqual(witcher$lastMainHand, currentMainHand);
        boolean chestChanged = !ItemStack.areEqual(witcher$lastChestplate, currentChestplate);
        boolean firstTick = witcher$lastMainHand.isEmpty() && witcher$lastChestplate.isEmpty();

        if (mainHandChanged || chestChanged || firstTick) {
            witcher$updateAllAttributeModifiers(currentMainHand, currentChestplate);
            witcher$lastMainHand = currentMainHand.copy();
            witcher$lastChestplate = currentChestplate.copy();
        }
    }

    @Unique
    private void witcher$updateAllAttributeModifiers(ItemStack mainHand, ItemStack chestplate) {
        // STEP 1: Remove all previously tracked modifiers
        for (String attrId : witcher$trackedRunestoneAttributes) {
            RegistryEntry<EntityAttribute> attribute = Registries.ATTRIBUTE.getEntry(Identifier.of(attrId)).orElse(null);
            if (attribute != null) {
                EntityAttributeInstance instance = this.getAttributeInstance(attribute);
                if (instance != null) {
                    instance.removeModifier(RUNESTONE_MODIFIER_BASE);
                    Identifier percentModifierId = Identifier.of(RUNESTONE_MODIFIER_BASE.getNamespace(), RUNESTONE_MODIFIER_BASE.getPath() + "_percent");
                    instance.removeModifier(percentModifierId);
                }
            }
        }
        witcher$trackedRunestoneAttributes.clear();

        for (String attrId : witcher$trackedGlyphAttributes) {
            RegistryEntry<EntityAttribute> attribute = Registries.ATTRIBUTE.getEntry(Identifier.of(attrId)).orElse(null);
            if (attribute != null) {
                EntityAttributeInstance instance = this.getAttributeInstance(attribute);
                if (instance != null) {
                    instance.removeModifier(GLYPH_MODIFIER_BASE);
                    Identifier percentModifierId = Identifier.of(GLYPH_MODIFIER_BASE.getNamespace(), GLYPH_MODIFIER_BASE.getPath() + "_percent");
                    instance.removeModifier(percentModifierId);
                }
            }
        }
        witcher$trackedGlyphAttributes.clear();

        // STEP 2: Collect all attribute bonuses from equipment
        Map<String, Double> runestoneFlat = new HashMap<>();
        Map<String, Double> runestonePercent = new HashMap<>();
        Map<String, Double> glyphFlat = new HashMap<>();
        Map<String, Double> glyphPercent = new HashMap<>();

        // Process runestones
        if (mainHand != null && !mainHand.isEmpty()) {
            RunestoneSlots slots = mainHand.get(WitcherDataComponents.RUNESTONE_SLOTS);
            if (slots != null && !slots.attachedRunestones().isEmpty()) {
                for (ItemStack runestone : slots.attachedRunestones()) {
                    Identifier runestoneId = Registries.ITEM.getId(runestone.getItem());

                    WitcherTrinkets.Entry runestoneEntry = null;
                    for (var entry : WitcherTrinkets.entries) {
                        if (entry.id().equals(runestoneId)) {
                            runestoneEntry = entry;
                            break;
                        }
                    }

                    if (runestoneEntry == null) continue;

                    var config = runestoneEntry.config();
                    if (config == null || config.attributes == null) continue;

                    for (var attrModifier : config.attributes) {
                        if (attrModifier.attribute == null) continue;
                        if (attrModifier.operation == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE) {
                            runestonePercent.merge(attrModifier.attribute, (double) attrModifier.value, Double::sum);
                        } else {
                            runestoneFlat.merge(attrModifier.attribute, (double) attrModifier.value, Double::sum);
                        }
                    }
                }
            }
        }

        // Process glyphs
        if (chestplate != null && !chestplate.isEmpty()) {
            GlyphSlots slots = chestplate.get(WitcherDataComponents.GLYPH_SLOTS);
            if (slots != null && !slots.attachedGlyphs().isEmpty()) {
                for (ItemStack glyph : slots.attachedGlyphs()) {
                    Identifier glyphId = Registries.ITEM.getId(glyph.getItem());

                    WitcherTrinkets.Entry glyphEntry = null;
                    for (var entry : WitcherTrinkets.entries) {
                        if (entry.id().equals(glyphId)) {
                            glyphEntry = entry;
                            break;
                        }
                    }

                    if (glyphEntry == null) continue;

                    var config = glyphEntry.config();
                    if (config == null || config.attributes == null) continue;

                    for (var attrModifier : config.attributes) {
                        if (attrModifier.attribute == null) continue;
                        if (attrModifier.operation == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE) {
                            glyphPercent.merge(attrModifier.attribute, (double) attrModifier.value, Double::sum);
                        } else {
                            glyphFlat.merge(attrModifier.attribute, (double) attrModifier.value, Double::sum);
                        }
                    }
                }
            }
        }

        // STEP 3: Apply modifiers to all relevant attributes and track them
        for (String attrId : runestoneFlat.keySet()) {
            witcher$applyAttributeModifier(attrId, runestoneFlat.get(attrId), runestonePercent.getOrDefault(attrId, 0.0), RUNESTONE_MODIFIER_BASE);
            witcher$trackedRunestoneAttributes.add(attrId);
        }
        for (String attrId : runestonePercent.keySet()) {
            if (!runestoneFlat.containsKey(attrId)) {
                witcher$applyAttributeModifier(attrId, 0.0, runestonePercent.get(attrId), RUNESTONE_MODIFIER_BASE);
                witcher$trackedRunestoneAttributes.add(attrId);
            }
        }

        for (String attrId : glyphFlat.keySet()) {
            witcher$applyAttributeModifier(attrId, glyphFlat.get(attrId), glyphPercent.getOrDefault(attrId, 0.0), GLYPH_MODIFIER_BASE);
            witcher$trackedGlyphAttributes.add(attrId);
        }
        for (String attrId : glyphPercent.keySet()) {
            if (!glyphFlat.containsKey(attrId)) {
                witcher$applyAttributeModifier(attrId, 0.0, glyphPercent.get(attrId), GLYPH_MODIFIER_BASE);
                witcher$trackedGlyphAttributes.add(attrId);
            }
        }
    }

    @Unique
    private void witcher$applyAttributeModifier(String attrId, double flatBonus, double percentBonus, Identifier modifierBase) {
        RegistryEntry<EntityAttribute> attribute = Registries.ATTRIBUTE.getEntry(Identifier.of(attrId)).orElse(null);
        if (attribute == null) return;

        EntityAttributeInstance instance = this.getAttributeInstance(attribute);
        if (instance == null) return;

        // Note: We don't remove modifiers here since we do that centrally in witcher$updateAllAttributeModifiers

        // Add flat bonus if present
        if (flatBonus != 0.0) {
            instance.addTemporaryModifier(new EntityAttributeModifier(
                    modifierBase,
                    flatBonus,
                    EntityAttributeModifier.Operation.ADD_VALUE
            ));
        }

        // Add percentage bonus if present (as a separate modifier)
        if (percentBonus != 0.0) {
            Identifier percentModifierId = Identifier.of(modifierBase.getNamespace(), modifierBase.getPath() + "_percent");
            instance.addTemporaryModifier(new EntityAttributeModifier(
                    percentModifierId,
                    percentBonus,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ));
        }
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
