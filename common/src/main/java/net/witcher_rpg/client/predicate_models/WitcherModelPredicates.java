package net.witcher_rpg.client.predicate_models;

import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.witcher_rpg.effect.WitcherStatusEffects;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherModelPredicates {
    public interface Registrar {
        void register(Item item, Identifier id, ModelPredicateProvider provider);
    }

    public static void registerModelPredicates(Registrar registrar) {
        Identifier aerondightId = new Identifier("witcher_rpg", "aerondight_sword");
        Item  aerondightItem = Registries.ITEM.get(aerondightId);
        registrar.register(aerondightItem, new Identifier(MOD_ID, "aerondight_charged"),
                (stack, world, entity, seed) -> {
                    if (entity instanceof LivingEntity) {
                        LivingEntity living = entity;
                        StatusEffectInstance effect = living.getStatusEffect(WitcherStatusEffects.AERONDIGHT_CHARGE.effect);
                        if (effect != null && effect.getAmplifier() >= 9) {
                            return 1.0f;
                        }
                    }
                    return 0.0f;
                });
        Identifier irisId = new Identifier("witcher_rpg", "iris_sword");
        Item  irisItem = Registries.ITEM.get(irisId);
        registrar.register(irisItem, new Identifier(MOD_ID, "iris_charged"),
                (stack, world, entity, seed) -> {
                    if (entity instanceof LivingEntity) {
                        LivingEntity living = entity;
                        StatusEffectInstance effect = living.getStatusEffect(WitcherStatusEffects.IRIS_CHARGE.effect);
                        if (effect != null && effect.getAmplifier() >= 9) {
                            return 1.0f;
                        }
                    }
                    return 0.0f;
                });
    }

}
