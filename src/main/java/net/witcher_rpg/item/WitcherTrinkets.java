package net.witcher_rpg.item;

import com.google.common.base.Suppliers;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigUtil;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_power.api.SpellPowerMechanics;
import net.witcher_rpg.config.TrinketConfig;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherTrinkets {
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public record ItemArgs(Item.Settings settings, @Nullable AttributeModifiersComponent attributes) { }
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
                var settings = new Item.Settings()
                        .maxCount(1);
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

                var rarity = rarityFrom(tier);
                if (rarity != Rarity.COMMON) {
                    settings = settings.rarity(rarity);
                }
                return getFactory().apply(new ItemArgs(settings, attributes));
            });
        }

        private static Rarity rarityFrom(int tier) {
            return switch (tier) {
                case 0, 1 -> Rarity.COMMON;
                case 2 -> Rarity.UNCOMMON;
                case 3 -> Rarity.RARE;
                default -> Rarity.EPIC;
            };
        }

        public int tier() {
            return tier;
        }

        public Identifier id() {
            return Identifier.of(MOD_ID, name);
        }

        public String name() {
            return name;
        }

        public String translatedName() {
            return translatedName;
        }

        public TrinketConfig.Entry config() {
            return config;
        }

        public Supplier<Item> item() {
            return item;
        }

        @Nullable public SpellContainer spellContainer() {
            return spellContainer;
        }

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

        public boolean isEnabled() {
            return true;
        }
    }

    public static float medallion_sign_intensity = 0.1F;
    public static float medallion_attack_damage = 0.12F;
    public static float medallion_haste = 0.05F;
    public static float medallion_adrenaline = 0.1F;
    public static float medallion_health = 0.1F;
    public static float lesser_glyph_power = 1.0F;
    public static float glyph_power = 1.5F;
    public static float greater_glyph_power = 2.0F;



    //WITCHER MEDALLIONS
    public static final Entry BEAR_SCHOOL_MEDALLION = add(new Entry(10, "bear_school_medallion", "Bear School Medallion"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:bear_school_medallion")))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), medallion_health, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), medallion_attack_damage, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER.getIdAsString(), medallion_adrenaline, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry CAT_SCHOOL_MEDALLION = add(new Entry(10, "cat_school_medallion", "Cat School Medallion"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:cat_school_medallion")))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(), medallion_haste, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), medallion_attack_damage, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER.getIdAsString(), medallion_adrenaline, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry GRIFFIN_SCHOOL_MEDALLION = add(new Entry(10, "griffin_school_medallion", "Griffin School Medallion"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:griffin_school_medallion")))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.SIGN_INTENSITY.getIdAsString(), medallion_sign_intensity, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                             new AttributeModifier(SpellPowerMechanics.HASTE.id, medallion_haste, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER.getIdAsString(), medallion_adrenaline, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)

                    ))
            );
    public static final Entry WOLF_SCHOOL_MEDALLION = add(new Entry(10, "wolf_school_medallion", "Wolf School Medallion"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:wolf_school_medallion")))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.SIGN_INTENSITY.getIdAsString(), medallion_sign_intensity, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), medallion_attack_damage, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER.getIdAsString(), medallion_adrenaline, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    ///GLYPHS
    public static final Entry LESSER_AARD_GLYPH = add(new Entry(10, "lesser_aard_glyph", "Lesser Aard Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AARD_INTENSITY.getIdAsString(), lesser_glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry LESSER_AXII_GLYPH = add(new Entry(10, "lesser_axii_glyph", "Lesser Axii Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AXII_INTENSITY.getIdAsString(), lesser_glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry LESSER_IGNI_GLYPH = add(new Entry(10, "lesser_igni_glyph", "Lesser Igni Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.IGNI_INTENSITY.getIdAsString(), lesser_glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry LESSER_QUEN_GLYPH = add(new Entry(10, "lesser_quen_glyph", "Lesser Quen Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.QUEN_INTENSITY.getIdAsString(), lesser_glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry LESSER_YRDEN_GLYPH = add(new Entry(10, "lesser_yrden_glyph", "Lesser Yrden Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.YRDEN_INTENSITY.getIdAsString(), lesser_glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry AARD_GLYPH = add(new Entry(10, "aard_glyph", "Aard Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AARD_INTENSITY.getIdAsString(), glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry AXII_GLYPH = add(new Entry(10, "axii_glyph", "Axii Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AXII_INTENSITY.getIdAsString(), glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry IGNI_GLYPH = add(new Entry(10, "igni_glyph", "Igni Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.IGNI_INTENSITY.getIdAsString(), glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry QUEN_GLYPH = add(new Entry(10, "quen_glyph", "Quen Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.QUEN_INTENSITY.getIdAsString(), glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry YRDEN_GLYPH = add(new Entry(10, "yrden_glyph", "Yrden Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.YRDEN_INTENSITY.getIdAsString(), glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry GREATER_AARD_GLYPH = add(new Entry(10, "greater_aard_glyph", "Greater Aard Glyph"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:greater_aard_glyph")))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AARD_INTENSITY.getIdAsString(), greater_glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry GREATER_AXII_GLYPH = add(new Entry(10, "greater_axii_glyph", "Greater Axii Glyph"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:greater_axii_glyph")))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AXII_INTENSITY.getIdAsString(), greater_glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry GREATER_IGNI_GLYPH = add(new Entry(10, "greater_igni_glyph", "Greater Igni Glyph"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:greater_igni_glyph")))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.IGNI_INTENSITY.getIdAsString(), greater_glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry GREATER_QUEN_GLYPH = add(new Entry(10, "greater_quen_glyph", "Greater Quen Glyph"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:greater_quen_glyph")))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.QUEN_INTENSITY.getIdAsString(), greater_glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry GREATER_YRDEN_GLYPH = add(new Entry(10, "greater_yrden_glyph", "Greater Yrden Glyph"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:greater_yrden_glyph")))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.YRDEN_INTENSITY.getIdAsString(), greater_glyph_power, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    ///TRINKETS
    public static final Entry PURE_SILVER = add(new Entry(10, "pure_silver", "Pure Silver"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:pure_silver")))
            .config(new TrinketConfig.Entry()
            );
    public static final Entry ROSE_OF_REMEMBRANCE = add(new Entry(10, "rose_of_remembrance", "Rose of Remembrance"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:rose_of_remembrance")))
            .config(new TrinketConfig.Entry()
            );
    public static final Entry CRYSTAL_SKULL = add(new Entry(10, "crystal_skull", "Crystal Skull"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:crystal_skull")))
            .config(new TrinketConfig.Entry()
            );
    public static final Entry SUNSTONE = add(new Entry(10, "sunstone", "Sunstone"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("witcher_rpg:sunstone")))
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
            if (entry.isEnabled()) {
                Registry.register(Registries.ITEM, entry.id(), entry.item().get());
            }
        }
        ItemGroupEvents.modifyEntriesEvent(WitcherGroup.WITCHER_KEY).register(content -> {
            for(var entry: entries) {
                if (entry.isEnabled()) {
                    content.add(entry.item().get());
                }
            }
        });
    }
}
