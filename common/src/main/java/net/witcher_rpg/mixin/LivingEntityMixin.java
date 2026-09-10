package net.witcher_rpg.mixin;

import net.spell_engine.Platform;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
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
import net.witcher_rpg.item.WitcherTrinkets;
import net.witcher_rpg.item.component.GlyphSlots;
import net.witcher_rpg.item.component.RunestoneSlots;
import net.witcher_rpg.item.component.WitcherDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

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

        if (flatBonus != 0.0) {
            instance.addTemporaryModifier(new EntityAttributeModifier(
                    modifierBase,
                    flatBonus,
                    EntityAttributeModifier.Operation.ADD_VALUE
            ));
        }

        if (percentBonus != 0.0) {
            Identifier percentModifierId = Identifier.of(modifierBase.getNamespace(), modifierBase.getPath() + "_percent");
            instance.addTemporaryModifier(new EntityAttributeModifier(
                    percentModifierId,
                    percentBonus,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ));
        }
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
                multiplier = (float) instance.getValue();
            }
        }

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
