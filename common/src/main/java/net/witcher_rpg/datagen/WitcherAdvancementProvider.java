package net.witcher_rpg.datagen;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.datagen.SpellEngineAdvancementHelper;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class WitcherAdvancementProvider implements DataProvider {
    private final DataOutput.PathResolver pathResolver;

    public record Entry(
            Identifier id,
            String title,
            String description,
            @Nullable Identifier parent,
            String iconItemName,
            AdvancementFrame frame,
            boolean showToast,
            boolean announceToChat,
            boolean hidden,
            @Nullable String background,
            SpellEngineCriteriaType criteriaType,
            String criteriaValue
    ) {
        public String titleKey() {
            return "advancements." + id.getNamespace() + "." + id.getPath().replace("/", ".") + ".title";
        }

        public String descriptionKey() {
            return "advancements." + id.getNamespace() + "." + id.getPath().replace("/", ".") + ".description";
        }
    }

    public enum SpellEngineCriteriaType {
        SPELL_BOOK_CREATION,
        ONE_SPELL_BOUND,
        ALL_SPELLS_BOUND,
        SPELL_CAST
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry addEntry(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    static {
        /// FENCING
        addEntry(new Entry(
                id("path_choose_fencing"),
                "Student of Vesemir",
                "Create the Witcher Techniques",
                Identifier.of("more_rpg_content", "root"),
                MOD_ID + ":fencing_spell_book",
                AdvancementFrame.TASK,
                true, true, false, null,
                SpellEngineCriteriaType.SPELL_BOOK_CREATION,
                MOD_ID + ":fencing"
        ));

        addEntry(new Entry(
                id("spell_novice_fencing"),
                "First Fencing Lessons",
                "Obtain your first Witcher Fencing skill",
                id("path_choose_fencing"),
                MOD_ID + ":fencing_spell_book",
                AdvancementFrame.TASK,
                true, true, false, null,
                SpellEngineCriteriaType.ONE_SPELL_BOUND,
                MOD_ID + ":fencing"
        ));

        addEntry(new Entry(
                id("spell_master_fencing"),
                "Master Witcher!",
                "Complete the Witcher Techniques Book",
                id("spell_novice_fencing"),
                MOD_ID + ":fencing_spell_book",
                AdvancementFrame.GOAL,
                true, true, false, null,
                SpellEngineCriteriaType.ALL_SPELLS_BOUND,
                MOD_ID + ":fencing"
        ));

        addEntry(new Entry(
                id("spell_cast_fencing_book"),
                "Speed not Strength!",
                "Use a skill from Witcher Techniques Book",
                id("spell_novice_fencing"),
                MOD_ID + ":fencing_spell_book",
                AdvancementFrame.TASK,
                true, true, false, null,
                SpellEngineCriteriaType.SPELL_CAST,
                "#" + MOD_ID + ":fencing"
        ));
        /// SIGNS
        addEntry(new Entry(
                id("path_choose_signs"),
                "Complex Hand Gestures",
                "Create the Witcher Sign Manual",
                Identifier.of("more_rpg_content", "root"),
                MOD_ID + ":base_signs_spell_book",
                AdvancementFrame.TASK,
                true, true, false, null,
                SpellEngineCriteriaType.SPELL_BOOK_CREATION,
                MOD_ID + ":base_signs"
        ));

        addEntry(new Entry(
                id("spell_novice_signs"),
                "First Sign Hand gesture",
                "Obtain your first Witcher Sign Manual spells",
                id("path_choose_signs"),
                MOD_ID + ":base_signs_spell_book",
                AdvancementFrame.TASK,
                true, true, false, null,
                SpellEngineCriteriaType.ONE_SPELL_BOUND,
                MOD_ID + ":base_signs"
        ));

        addEntry(new Entry(
                id("spell_master_signs"),
                "Master Witcher!",
                "Complete the Witcher Sign Manual",
                id("spell_novice_signs"),
                MOD_ID + ":base_signs_spell_book",
                AdvancementFrame.GOAL,
                true, true, false, null,
                SpellEngineCriteriaType.ALL_SPELLS_BOUND,
                MOD_ID + ":base_signs"
        ));

        addEntry(new Entry(
                id("spell_cast_signs_book"),
                "Steel wins battles, but Signs decide them!",
                "Use a skill from Witcher Sign Manual",
                id("spell_novice_signs"),
                MOD_ID + ":base_signs_spell_book",
                AdvancementFrame.TASK,
                true, true, false, null,
                SpellEngineCriteriaType.SPELL_CAST,
                "#" + MOD_ID + ":base_signs"
        ));
    }

    public WitcherAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        this.pathResolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "advancement");
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Entry entry : entries) {
            JsonObject advancement = createAdvancementJson(entry);
            Path path = pathResolver.resolveJson(entry.id());
            futures.add(DataProvider.writeToPath(writer, advancement, path));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private JsonObject createAdvancementJson(Entry entry) {
        JsonObject advancement = new JsonObject();

        // Display
        JsonObject display = new JsonObject();
        JsonObject icon = new JsonObject();
        icon.addProperty("id", entry.iconItemName().contains(":") ? entry.iconItemName() : MOD_ID + ":" + entry.iconItemName());
        display.add("icon", icon);
        display.add("title", createTranslatable(entry.titleKey()));
        display.add("description", createTranslatable(entry.descriptionKey()));
        display.addProperty("frame", entry.frame().asString());
        display.addProperty("show_toast", entry.showToast());
        display.addProperty("announce_to_chat", entry.announceToChat());
        display.addProperty("hidden", entry.hidden());
        if (entry.background() != null) {
            display.addProperty("background", entry.background());
        }
        advancement.add("display", display);

        // Parent
        if (entry.parent() != null) {
            advancement.addProperty("parent", entry.parent().toString());
        }

        // Criteria
        JsonObject criteria = getCriteriaForType(entry.criteriaType(), entry.criteriaValue());
        advancement.add("criteria", criteria);

        return advancement;
    }

    private JsonObject createTranslatable(String key) {
        JsonObject translatable = new JsonObject();
        translatable.addProperty("translate", key);
        return translatable;
    }

    private JsonObject getCriteriaForType(SpellEngineCriteriaType type, String value) {
        return switch (type) {
            case SPELL_BOOK_CREATION -> SpellEngineAdvancementHelper.criteriaSpellBookCreation(value);
            case ONE_SPELL_BOUND -> SpellEngineAdvancementHelper.criteriaOneSpellBound(value);
            case ALL_SPELLS_BOUND -> SpellEngineAdvancementHelper.criteriaAllSpellsBound(value);
            case SPELL_CAST -> SpellEngineAdvancementHelper.criteriaSpellCast(value);
        };
    }

    public static List<Entry> getEntries() {
        return entries;
    }

    @Override
    public String getName() {
        return "Witcher Advancements";
    }
}
