package net.witcher_rpg.item;

import com.google.common.base.Suppliers;
import net.spell_engine.PlatformEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.ConfigUtil;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_power.api.SpellPowerMechanics;
import net.witcher_rpg.config.TrinketConfig;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import net.witcher_rpg.spell.SetBonuses;
import net.witcher_rpg.spell.WitcherModifiers;
import net.witcher_rpg.spell.WitcherPassives;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherTrinkets {
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public record ItemArgs(Item.Settings settings, @Nullable AttributeModifiersComponent attributes, String name) { }

    public static Function<ItemArgs, Item> factory = args -> {
        var settings = args.settings;
        if (args.attributes != null) {
            settings.attributeModifiers(args.attributes);
        }
        return new Item(settings);
    };

    private static Function<ItemArgs, Item> getFactory() { return factory; }

    public static final class Entry {
        private final int tier;
        public String lootTheme;
        private final String name;
        private final String translatedName;
        private TrinketConfig.Entry config;
        public TrinketConfig.Entry defaults;
        private final Supplier<Item> item;
        private SpellContainer spellContainer;

        private final List<UnaryOperator<Item.Settings>> settingsMutators = new ArrayList<>();

        public Entry(int tier, String name, String translatedName) {
            this(tier, name, translatedName, TrinketConfig.Entry.EMPTY);
        }

        public Entry(int tier, String name, String translatedName, TrinketConfig.Entry config) {
            this.tier = tier;
            this.name = name;
            this.translatedName = translatedName;
            this.config = config;
            this.defaults = config;

            this.item = Suppliers.memoize(() -> {
                var settings = new Item.Settings().maxCount(1);

                var attributes = (config().attributes != null && !config().attributes.isEmpty())
                        ? ConfigUtil.attributesComponent(Identifier.of(MOD_ID, name), config().attributes).build()
                        : null;

                var spellContainer = spellContainer();
                if (spellContainer != null) {
                    settings = settings.component(SpellDataComponents.SPELL_CONTAINER, spellContainer);
                }

                if (config().durability > 0) {
                    settings = settings.maxDamage(config().durability);
                }

                if (name.contains("medallion")) settings = settings.rarity(Rarity.EPIC);
                if (name.contains("pure_silver") && name.contains("rose_of_remembrance") && name.contains("crystal_skull"))
                    settings = settings.rarity(Rarity.RARE);
                if (name.contains("sunstone")) settings = settings.rarity(Rarity.EPIC);

                for (UnaryOperator<Item.Settings> mutator : settingsMutators) {
                    settings = mutator.apply(settings);
                }

                return getFactory().apply(new ItemArgs(settings, attributes, name));
            });
        }

        public int tier() { return tier; }

        public Identifier id() { return Identifier.of(MOD_ID, name); }

        public String name() { return name; }

        public String translatedName() { return translatedName; }

        public TrinketConfig.Entry config() { return config; }

        public Supplier<Item> item() { return item; }

        @Nullable
        public SpellContainer spellContainer() { return spellContainer; }

        public Entry config(TrinketConfig.Entry config) {
            this.config = config;
            return this;
        }

        public Entry spell(SpellContainer spellContainer) {
            this.spellContainer = spellContainer;
            return this;
        }

        public Entry lootTheme(String lootTheme) {
            this.lootTheme = lootTheme;
            return this;
        }
        public <T> Entry component(ComponentType<T> type, T value) {
            settingsMutators.add(settings -> settings.component(type, value));
            return this;
        }
    }


    public static float medallion_sign_intensity = 0.1F;
    public static float medallion_attack_damage = 0.12F;
    public static float medallion_haste = 0.05F;
    public static float medallion_adrenaline = 0.1F;
    public static float medallion_health = 4.0F;



    //WITCHER MEDALLIONS
    public static final Entry BEAR_SCHOOL_MEDALLION = add(new Entry(10, "bear_school_medallion", "Bear School Medallion"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), medallion_health, EntityAttributeModifier.Operation.ADD_VALUE),
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), medallion_attack_damage, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER.getIdAsString(), medallion_adrenaline, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            )
            .spell(SpellContainers.forRelic(WitcherModifiers.increased_medallion_senses.id()))
            .component(SpellDataComponents.EQUIPMENT_SET, SetBonuses.grandmaster_ursine.id());
    public static final Entry CAT_SCHOOL_MEDALLION = add(new Entry(10, "cat_school_medallion", "Cat School Medallion"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(), medallion_haste, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), medallion_attack_damage, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER.getIdAsString(), medallion_adrenaline, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            )
            .spell(SpellContainers.forRelic(WitcherModifiers.increased_medallion_senses.id()))
            .component(SpellDataComponents.EQUIPMENT_SET, SetBonuses.grandmaster_feline.id());
    public static final Entry GRIFFIN_SCHOOL_MEDALLION = add(new Entry(10, "griffin_school_medallion", "Griffin School Medallion"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.SIGN_INTENSITY.getIdAsString(), medallion_sign_intensity, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                             new AttributeModifier(SpellPowerMechanics.HASTE.id, medallion_haste, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER.getIdAsString(), medallion_adrenaline, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            )
            .spell(SpellContainers.forRelic(WitcherModifiers.increased_medallion_senses.id()))
            .component(SpellDataComponents.EQUIPMENT_SET, SetBonuses.grandmaster_griffin.id());
    public static final Entry WOLF_SCHOOL_MEDALLION = add(new Entry(10, "wolf_school_medallion", "Wolf School Medallion"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.SIGN_INTENSITY.getIdAsString(), medallion_sign_intensity, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), medallion_attack_damage, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER.getIdAsString(), medallion_adrenaline, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            )
            .spell(SpellContainers.forRelic(WitcherModifiers.increased_medallion_senses.id()))
            .component(SpellDataComponents.EQUIPMENT_SET, SetBonuses.grandmaster_wolven.id());
    ///TRINKETS
    public static final Entry PURE_SILVER = add(new Entry(10, "pure_silver", "Pure Silver"))
            .spell(SpellContainers.forRelic(WitcherPassives.pure_silver.id()))
            .config(new TrinketConfig.Entry()
            );
    public static final Entry ROSE_OF_REMEMBRANCE = add(new Entry(10, "rose_of_remembrance", "Rose of Remembrance"))
            .spell(SpellContainers.forRelic(WitcherPassives.ROSE_OF_REMEMBRANCE.id()))
            .config(new TrinketConfig.Entry()
            );
    public static final Entry CRYSTAL_SKULL = add(new Entry(10, "crystal_skull", "Crystal Skull"))
            .spell(SpellContainers.forRelic(WitcherPassives.crystal_skull.id()))
            .config(new TrinketConfig.Entry()
            );
    public static final Entry SUNSTONE = add(new Entry(10, "sunstone", "Sunstone"))
            .spell(SpellContainers.forRelic(Identifier.of("witcher_rpg:sunstone")))
            .config(new TrinketConfig.Entry()
            );


    public static void register(Map<String, TrinketConfig.Entry> config) {
        for (var entry : entries) {
            var key = entry.id().toString();
            var configEntry = config.get(key);
            if (configEntry != null) {
                entry.config(configEntry);
            } else {
                config.put(key, entry.config());
            }
        }

        for(var entry: entries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item().get());
        }
        PlatformEvents.onItemGroupModify(WitcherGroup.WITCHER_KEY, (content, context) -> {
            for(var entry: entries) {
                content.add(entry.item().get());
            }
        });
    }
}
