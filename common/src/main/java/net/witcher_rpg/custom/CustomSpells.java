package net.witcher_rpg.custom;

public class CustomSpells {
    public static void register() {
        /*
        ///////WITCHER_SPELLS
        /// AARD
        CustomSpellHandler.register(Identifier.of(MOD_ID, "aard"), (data) -> {
            CustomSpellHandler.Data data1 = (CustomSpellHandler.Data) data;
            var spellEntry = SpellRegistry.from(data1.caster().getWorld()).getEntry(Identifier.of(MOD_ID, "aard")).orElse(null);
            var spell = spellEntry.value();
            Spell.Impact[] impacts = spell.impact;

            Predicate<Entity> selectionPredicate = (target2) -> {
                return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.DIRECT, TargetHelper.Intent.HARMFUL, data1.caster(), target2)
                );
            };
            if (!data1.caster().getWorld().isClient) {
                List<Entity> list = data1.caster().getWorld().getOtherEntities(data1.caster(), data1.caster().getBoundingBox().expand(spell.range), selectionPredicate);
                float knockback= spell.impact[0].action.damage.knockback;
                for (Entity entity : list) {
                    if(entity instanceof PersistentProjectileEntity arrow){
                        Vec3d vec3d = arrow.getPos().subtract(arrow.getX(), arrow.getY(), arrow.getZ());
                        arrow.setVelocity(vec3d);
                    }else if (entity.isLiving()){
                        SpellHelper.performImpacts(entity.getWorld(), data1.caster(), entity, entity, spellEntry,impacts ,data1.impactContext());
                        LivingEntity livingEntity = (LivingEntity) entity;
                        Vec3d currentMovement = entity.getVelocity();
                        entity.setVelocity(currentMovement.x , currentMovement.y + 0.5, currentMovement.z);
                        entity.velocityModified = true;
                        livingEntity.takeKnockback(knockback* 0.4F,currentMovement.x,currentMovement.z);
                    }

                }
            }
            return true;
        });
        /// AARD_SWEEP
        CustomSpellHandler.register(Identifier.of(MOD_ID, "aard_sweep"), (data) -> {
            CustomSpellHandler.Data data1 = (CustomSpellHandler.Data) data;
            var spellEntry = SpellRegistry.from(data1.caster().getWorld()).getEntry(Identifier.of(MOD_ID, "aard_sweep")).orElse(null);
            var spell = spellEntry.value();
            Spell.Impact[] impacts = spell.impact;

            Predicate<Entity> selectionPredicate = (target2) -> {
                return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.DIRECT, TargetHelper.Intent.HARMFUL, data1.caster(), target2)
                );
            };
            float knockback= spell.impact[0].action.damage.knockback;
            if (!data1.caster().getWorld().isClient) {
                List<Entity> list = data1.caster().getWorld().getOtherEntities(data1.caster(), data1.caster().getBoundingBox().expand(spell.range), selectionPredicate);
                for (Entity entity : list) {
                    if(entity instanceof PersistentProjectileEntity arrow){
                        Vec3d vec3d = arrow.getPos().subtract(arrow.getX(), arrow.getY(), arrow.getZ());
                        arrow.setVelocity(vec3d);
                    }else if (entity.isLiving()){
                        SpellHelper.performImpacts(entity.getWorld(), data1.caster(), entity, entity, spellEntry, impacts,data1.impactContext());
                        LivingEntity livingEntity = (LivingEntity) entity;
                        Vec3d currentMovement = entity.getVelocity();
                        entity.setVelocity(currentMovement.x , currentMovement.y + 0.5, currentMovement.z);
                        entity.velocityModified = true;
                        livingEntity.takeKnockback(knockback * 0.4F,currentMovement.x,currentMovement.z);
                    }

                }
            }
            return true;
        });
        /// QUEN_ACTIVE_SHIELD
        CustomSpellHandler.register(Identifier.of(MOD_ID, "quen_active_shield"), (data) -> {
            CustomSpellHandler.Data data1 = (CustomSpellHandler.Data) data;
            if (!data1.caster().getWorld().isClient) {
                float currentAbsorption = data1.caster().getAbsorptionAmount();
                if(currentAbsorption == 0 ){
                    return true;
                }
            }
            return false;
        });
        /// REND
        CustomSpellHandler.register(Identifier.of(MOD_ID, "rend"), (data) -> {
            CustomSpellHandler.Data data1 = (CustomSpellHandler.Data) data;
            var spellEntry = SpellRegistry.from(data1.caster().getWorld()).getEntry(Identifier.of(MOD_ID, "rend")).orElse(null);
            var spell = spellEntry.value();
            for (Entity entity : data1.targets()) {
                SpellSchool school = spell.school;
                float spell_power_coefficient =spell.impact[0].action.damage.spell_power_coefficient;
                if(data1.caster().hasStatusEffect(Effects.ADRENALINE_GAIN.registryEntry)){
                    int amplifier = data1.caster().getStatusEffect(Effects.ADRENALINE_GAIN.registryEntry).getAmplifier()+1;
                    spell_power_coefficient = spell_power_coefficient * ((float) amplifier / 10);
                }
                if (entity instanceof LivingEntity living) {
                    spellSchoolDamageCalculation(school,spell_power_coefficient,living,data1.caster());
                    if (!data1.caster().getWorld().isClient) {
                        if (living.isBlocking()) {
                            living.addStatusEffect(new StatusEffectInstance(MRPGCEffects.STUNNED.registryEntry, 40, 0, false, false, true));
                        }
                    }
                }
                return true;
            }
            return true;
        });


         */
    }
}
