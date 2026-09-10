package net.witcher_rpg.item;

import com.google.common.base.Suppliers;
import net.spell_engine.PlatformEvents;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.witcher_rpg.util.MrpgAttributeIds;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.ConfigUtil;
import net.spell_engine.api.item.ItemAttributeModifiers;
import net.spell_engine.api.item.SpellItemData;
import net.spell_engine.utils.AttributeModifierUtil;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_power.api.SpellPowerMechanics;
import net.witcher_rpg.config.TrinketConfig;
import net.witcher_rpg.entity.attribute.WitcherAttributes;
import net.witcher_rpg.spell.SetBonuses;
import net.witcher_rpg.spell.WitcherModifiers;
import net.witcher_rpg.spell.WitcherPassives;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
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

    public record ItemArgs(Item.Settings settings, @Nullable ItemAttributeModifiers attributes, String name) { }

    public static Function<ItemArgs, Item> factory = args -> {
        var settings = args.settings;

        // Use custom item classes for glyphs and runestones
        Item item;
        if (args.name.contains("glyph")) {
            item = new GlyphItem(settings);
        } else if (args.name.contains("runestone")) {
            item = new RunestoneItem(settings);
        } else {
            item = new Item(settings);
        }

        // 1.20.1 has no `Item.Settings#attributeModifiers`: the modifiers are held per item and served
        // through Spell Engine's `ItemStackAttributeModifiersMixin`.
        if (args.attributes != null) {
            AttributeModifierUtil.setItemModifiers(item, args.attributes);
        }
        return item;
    };

    /// `EntityAttribute#getIdAsString()` is 1.21-only.
    public static String attributeId(EntityAttribute attribute) {
        return Registries.ATTRIBUTE.getId(attribute).toString();
    }

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
                        ? ConfigUtil.attributesComponent(new Identifier(MOD_ID, name), config().attributes).build()
                        : null;

                var spellContainer = spellContainer();
                if (spellContainer != null) {
                    SpellItemData.defaults(settings).spellContainer(spellContainer);
                }

                if (config().durability > 0) {
                    settings = settings.maxDamage(config().durability);
                }

                if (name.contains("medallion")) settings = settings.rarity(Rarity.EPIC);
                if (name.contains("lesser")) settings = settings.rarity(Rarity.COMMON);
                if (!name.contains("lesser") && !name.contains("greater") && name.contains("glyph"))
                    settings = settings.rarity(Rarity.UNCOMMON);
                if (name.contains("greater")) settings = settings.rarity(Rarity.RARE);
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

        public Identifier id() { return new Identifier(MOD_ID, name); }

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
        /// 1.20.1 stand-in for `Item.Settings#component(EQUIPMENT_SET, id)`
        public Entry equipmentSet(Identifier equipmentSetId) {
            settingsMutators.add(settings -> {
                SpellItemData.defaults(settings).equipmentSet(equipmentSetId);
                return settings;
            });
            return this;
        }

        public boolean isEnabled() { return true; }
    }


    public static float medallion_sign_intensity = 0.1F;
    public static float medallion_attack_damage = 0.12F;
    public static float medallion_haste = 0.05F;
    public static float medallion_adrenaline = 0.1F;
    public static float medallion_health = 4.0F;
    public static float lesser_glyph_power = 0.25F;
    public static float glyph_power = 0.5F;
    public static float greater_glyph_power = 1.0F;
    public static float lesser_runestone_power = 0.02F;
    public static float runestone_power = 0.03F;
    public static float greater_runestone_power = 0.05F;
    public static float lesser_runestone_effect_weak = 0.05F;
    public static float runestone_effect_weak = 0.075F;
    public static float greater_runestone_effect_weak = 0.1F;
    public static float lesser_runestone_effect_strong = 0.035F;
    public static float runestone_effect_strong = 0.05F;
    public static float greater_runestone_effect_strong = 0.075F;



    //WITCHER MEDALLIONS
    public static final Entry BEAR_SCHOOL_MEDALLION = add(new Entry(10, "bear_school_medallion", "Bear School Medallion"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(attributeId(EntityAttributes.GENERIC_MAX_HEALTH), medallion_health, EntityAttributeModifier.Operation.ADDITION),
                            new AttributeModifier(attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE), medallion_attack_damage, EntityAttributeModifier.Operation.MULTIPLY_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER_ID.toString(), medallion_adrenaline, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            )
            .spell(SpellContainers.forRelic(WitcherModifiers.increased_medallion_senses.id()))
            .equipmentSet(SetBonuses.grandmaster_ursine.id());
    public static final Entry CAT_SCHOOL_MEDALLION = add(new Entry(10, "cat_school_medallion", "Cat School Medallion"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(attributeId(EntityAttributes.GENERIC_ATTACK_SPEED), medallion_haste, EntityAttributeModifier.Operation.MULTIPLY_BASE),
                            new AttributeModifier(attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE), medallion_attack_damage, EntityAttributeModifier.Operation.MULTIPLY_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER_ID.toString(), medallion_adrenaline, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            )
            .spell(SpellContainers.forRelic(WitcherModifiers.increased_medallion_senses.id()))
            .equipmentSet(SetBonuses.grandmaster_feline.id());
    public static final Entry GRIFFIN_SCHOOL_MEDALLION = add(new Entry(10, "griffin_school_medallion", "Griffin School Medallion"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.SIGN_INTENSITY_ID.toString(), medallion_sign_intensity, EntityAttributeModifier.Operation.MULTIPLY_BASE),
                             new AttributeModifier(SpellPowerMechanics.HASTE.id, medallion_haste, EntityAttributeModifier.Operation.MULTIPLY_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER_ID.toString(), medallion_adrenaline, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            )
            .spell(SpellContainers.forRelic(WitcherModifiers.increased_medallion_senses.id()))
            .equipmentSet(SetBonuses.grandmaster_griffin.id());
    public static final Entry WOLF_SCHOOL_MEDALLION = add(new Entry(10, "wolf_school_medallion", "Wolf School Medallion"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.SIGN_INTENSITY_ID.toString(), medallion_sign_intensity, EntityAttributeModifier.Operation.MULTIPLY_BASE),
                            new AttributeModifier(attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE), medallion_attack_damage, EntityAttributeModifier.Operation.MULTIPLY_BASE),
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER_ID.toString(), medallion_adrenaline, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            )
            .spell(SpellContainers.forRelic(WitcherModifiers.increased_medallion_senses.id()))
            .equipmentSet(SetBonuses.grandmaster_wolven.id());
    ///GLYPHS
    public static final Entry LESSER_AARD_GLYPH = add(new Entry(10, "lesser_aard_glyph", "Lesser Aard Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AARD_INTENSITY_ID.toString(), lesser_glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry LESSER_AXII_GLYPH = add(new Entry(10, "lesser_axii_glyph", "Lesser Axii Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AXII_INTENSITY_ID.toString(), lesser_glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry LESSER_IGNI_GLYPH = add(new Entry(10, "lesser_igni_glyph", "Lesser Igni Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.IGNI_INTENSITY_ID.toString(), lesser_glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry LESSER_QUEN_GLYPH = add(new Entry(10, "lesser_quen_glyph", "Lesser Quen Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.QUEN_INTENSITY_ID.toString(), lesser_glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry LESSER_YRDEN_GLYPH = add(new Entry(10, "lesser_yrden_glyph", "Lesser Yrden Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.YRDEN_INTENSITY_ID.toString(), lesser_glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry AARD_GLYPH = add(new Entry(10, "aard_glyph", "Aard Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AARD_INTENSITY_ID.toString(), glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry AXII_GLYPH = add(new Entry(10, "axii_glyph", "Axii Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AXII_INTENSITY_ID.toString(), glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry IGNI_GLYPH = add(new Entry(10, "igni_glyph", "Igni Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.IGNI_INTENSITY_ID.toString(), glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry QUEN_GLYPH = add(new Entry(10, "quen_glyph", "Quen Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.QUEN_INTENSITY_ID.toString(), glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry YRDEN_GLYPH = add(new Entry(10, "yrden_glyph", "Yrden Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.YRDEN_INTENSITY_ID.toString(), glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry GREATER_AARD_GLYPH = add(new Entry(10, "greater_aard_glyph", "Greater Aard Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AARD_INTENSITY_ID.toString(), greater_glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry GREATER_AXII_GLYPH = add(new Entry(10, "greater_axii_glyph", "Greater Axii Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.AXII_INTENSITY_ID.toString(), greater_glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry GREATER_IGNI_GLYPH = add(new Entry(10, "greater_igni_glyph", "Greater Igni Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.IGNI_INTENSITY_ID.toString(), greater_glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry GREATER_QUEN_GLYPH = add(new Entry(10, "greater_quen_glyph", "Greater Quen Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.QUEN_INTENSITY_ID.toString(), greater_glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    public static final Entry GREATER_YRDEN_GLYPH = add(new Entry(10, "greater_yrden_glyph", "Greater Yrden Glyph"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.YRDEN_INTENSITY_ID.toString(), greater_glyph_power, EntityAttributeModifier.Operation.ADDITION)
                    ))
            );
    ///RUNESTONES
    public static final Entry LESSER_DAZHBOG_RUNESTONE = add(new Entry(10, "lesser_dazhbog_runestone", "Lesser Dazhbog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.BURNING_CHANCE, lesser_runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry DAZHBOG_RUNESTONE = add(new Entry(10, "dazhbog_runestone", "Dazhbog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.BURNING_CHANCE, runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry GREATER_DAZHBOG_RUNESTONE = add(new Entry(10, "greater_dazhbog_runestone", "Greater Dazhbog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.BURNING_CHANCE, greater_runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry LESSER_CHERNOBOG_RUNESTONE = add(new Entry(10, "lesser_chernobog_runestone", "Lesser Chernobog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE), lesser_runestone_power, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry CHERNOBOG_RUNESTONE = add(new Entry(10, "chernobog_runestone", "Chernobog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE), runestone_power, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry GREATER_CHERNOBOG_RUNESTONE = add(new Entry(10, "greater_chernobog_runestone", "Greater Chernobog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(attributeId(EntityAttributes.GENERIC_ATTACK_DAMAGE), greater_runestone_power, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry LESSER_STRIBOG_RUNESTONE = add(new Entry(10, "lesser_stribog_runestone", "Lesser Stribog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.STAGGER_CHANCE, lesser_runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry STRIBOG_RUNESTONE = add(new Entry(10, "stribog_runestone", "Stribog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.STAGGER_CHANCE, runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry GREATER_STRIBOG_RUNESTONE = add(new Entry(10, "greater_stribog_runestone", "Greater Stribog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.STAGGER_CHANCE, greater_runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry LESSER_SVAROG_RUNESTONE = add(new Entry(10, "lesser_svarog_runestone", "Lesser Svarog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.ARMOR_PIERCING, lesser_runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry SVAROG_RUNESTONE = add(new Entry(10, "svarog_runestone", "Svarog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.ARMOR_PIERCING, runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry GREATER_SVAROG_RUNESTONE = add(new Entry(10, "greater_svarog_runestone", "Greater Svarog Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.ARMOR_PIERCING, greater_runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry LESSER_TRIGLAV_RUNESTONE = add(new Entry(10, "lesser_triglav_runestone", "Lesser Triglav Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.STUN_CHANCE, lesser_runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry TRIGLAV_RUNESTONE = add(new Entry(10, "triglav_runestone", "Triglav Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.STUN_CHANCE, runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry GREATER_TRIGLAV_RUNESTONE = add(new Entry(10, "greater_triglav_runestone", "Greater Triglav Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.STUN_CHANCE, greater_runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry LESSER_PERUN_RUNESTONE = add(new Entry(10, "lesser_perun_runestone", "Lesser Perun Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER_ID.toString(), lesser_runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry PERUN_RUNESTONE = add(new Entry(10, "perun_runestone", "Perun Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER_ID.toString(), runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry GREATER_PERUN_RUNESTONE = add(new Entry(10, "greater_perun_runestone", "Greater Perun Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.ADRENALINE_MODIFIER_ID.toString(), greater_runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry LESSER_VELES_RUNESTONE = add(new Entry(10, "lesser_veles_runestone", "Lesser Veles Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.SIGN_INTENSITY_ID.toString(), lesser_runestone_power, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry VELES_RUNESTONE = add(new Entry(10, "veles_runestone", "Veles Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.SIGN_INTENSITY_ID.toString(), runestone_power, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry GREATER_VELES_RUNESTONE = add(new Entry(10, "greater_veles_runestone", "Greater Veles Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WitcherAttributes.SIGN_INTENSITY_ID.toString(), greater_runestone_power, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry LESSER_MORANA_RUNESTONE = add(new Entry(10, "lesser_morana_runestone", "Lesser Morana Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.POISON_CHANCE, lesser_runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry MORANA_RUNESTONE = add(new Entry(10, "morana_runestone", "Morana Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.POISON_CHANCE, runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry GREATER_MORANA_RUNESTONE = add(new Entry(10, "greater_morana_runestone", "Greater Morana Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.POISON_CHANCE, greater_runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry LESSER_ZORIA_RUNESTONE = add(new Entry(10, "lesser_zoria_runestone", "Lesser Zoria Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.FREEZE_CHANCE, lesser_runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry ZORIA_RUNESTONE = add(new Entry(10, "zoria_runestone", "Zoria Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.FREEZE_CHANCE, runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry GREATER_ZORIA_RUNESTONE = add(new Entry(10, "greater_zoria_runestone", "Greater Zoria Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.FREEZE_CHANCE, greater_runestone_effect_strong, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry LESSER_DEVANA_RUNESTONE = add(new Entry(10, "lesser_devana_runestone", "Lesser Devana Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.BLEEDING_CHANCE, lesser_runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry DEVANA_RUNESTONE = add(new Entry(10, "devana_runestone", "Devana Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.BLEEDING_CHANCE, runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
    public static final Entry GREATER_DEVANA_RUNESTONE = add(new Entry(10, "greater_devana_runestone", "Greater Devana Runestone"))
            .config(new TrinketConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(MrpgAttributeIds.BLEEDING_CHANCE, greater_runestone_effect_weak, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    ))
            );
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
            .spell(SpellContainers.forRelic(new Identifier("witcher_rpg:sunstone")))
            .config(new TrinketConfig.Entry()
            );


    /// Creation only: applies the config to every entry (or seeds the config from the entry defaults),
    /// then installs the creative-tab hook and returns the enabled items keyed by registration id.
    /// Forge's `ITEM` `RegisterEvent` window feeds the map to its own `RegisterHelper`.
    public static Map<Identifier, Item> itemsToRegister(Map<String, TrinketConfig.Entry> config) {
        for (var entry : entries) {
            var key = entry.id().toString();
            var configEntry = config.get(key);
            if (configEntry != null) {
                entry.config(configEntry);
            } else {
                config.put(key, entry.config());
            }
        }

        var map = new LinkedHashMap<Identifier, Item>();
        for(var entry: entries) {
            if (entry.isEnabled()) {
                map.put(entry.id(), entry.item().get());
            }
        }
        PlatformEvents.onItemGroupModify(WitcherGroup.WITCHER_KEY, (content, context) -> {
            for(var entry: entries) {
                if (entry.isEnabled()) {
                    content.add(entry.item().get());
                }
            }
        });
        return map;
    }

    public static void register(Map<String, TrinketConfig.Entry> config) {
        itemsToRegister(config).forEach((id, item) -> Registry.register(Registries.ITEM, id, item));
    }
}
