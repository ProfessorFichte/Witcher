package net.witcher_rpg.item.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;

import java.util.List;

import static net.witcher_rpg.WitcherClassMod.MOD_ID;

public class UpgradeItem extends Item {
    public static final Text APPLIES_TO_TEXT = Text.translatable(
                    Util.createTranslationKey("item",new Identifier("smithing_template.applies_to")))
            .formatted(Formatting.GRAY);
    public static final String HINT_TRANSLATION_KEY = Util.createTranslationKey("item", new Identifier(MOD_ID, "dimeritium_ingot.applies_to"));
    public static final Text HINT_TEXT = Text.translatable(HINT_TRANSLATION_KEY)
            .formatted(Formatting.GRAY);

    private final String appliesToTranslationKey;
    public UpgradeItem(Item.Settings settings, String appliesToTranslationKey) {
        super(settings.fireproof().rarity(Rarity.EPIC));
        this.appliesToTranslationKey = appliesToTranslationKey;
    }

    public void appendTooltip(ItemStack stack,  World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(HINT_TEXT);
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(APPLIES_TO_TEXT);
        tooltip.add(ScreenTexts.space().append(Text.translatable(appliesToTranslationKey)).formatted(Formatting.BLUE));
    }
}
