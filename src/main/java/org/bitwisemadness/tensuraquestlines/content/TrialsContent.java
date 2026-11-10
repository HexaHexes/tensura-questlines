package org.bitwisemadness.tensuraquestlines.content;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.bitwisemadness.tensuraquestlines.delivery.AuthoredChapter;
import org.bitwisemadness.tensuraquestlines.delivery.QuestSpec;
import org.bitwisemadness.tensuraquestlines.delivery.RewardSpec;
import org.bitwisemadness.tensuraquestlines.delivery.TaskSpec;

/**
 * Major questline 5, Trials of Power (HexaHexes/tensura-quests#33): strength in
 * Tempest is proven, not given, and each trial strips a different muscle until
 * the final gate opens.
 *
 * <p>The line reads only vanilla signals - advancements, statistics, item
 * holdings and kills - from the pack's combat-and-magic mods. No new reader is
 * built for any of them (the major's non-goal); where a mod exposes no
 * observable, the beat is re-expressed, and the audit for each chapter lives
 * in its PR.</p>
 *
 * <p>No quest in this line requires a Tensura: Reincarnated skill, so the
 * line-wide disabled-skill rule holds by construction. Rewards are vanilla
 * valuables.</p>
 */
public final class TrialsContent {

    private static final int SPELLCRAFT_CHAPTER = 18;
    private static final List<String> SPELLCRAFT_MODS = List.of("irons_spellbooks", "mysticism");
    private static final ResourceLocation SPELL_BOOK_EQUIP =
        ResourceLocation.parse("irons_spellbooks:irons_spellbooks/spell_book_equip");
    private static final ResourceLocation SPELL_BOOK_IRON =
        ResourceLocation.parse("irons_spellbooks:irons_spellbooks/spell_book_iron");
    private static final ResourceLocation MAKE_INSCRIPTION_TABLE =
        ResourceLocation.parse("irons_spellbooks:irons_spellbooks/make_inscription_table");
    private static final ResourceLocation INK_LEGENDARY =
        ResourceLocation.parse("irons_spellbooks:irons_spellbooks/ink_legendary");
    private static final ResourceLocation MYSTIC_LIGHTNING_STRIKES =
        ResourceLocation.parse("mysticism:lightning_strikes");

    private TrialsContent() {
    }

    /**
     * Sub-questline 1, Spellcraft (HexaHexes/tensura-questlines#20): Iron's
     * Spells tiers plus the Mysticism branch.
     *
     * <p><b>Observable-signal audit</b> (the sub-issue's acceptance box):</p>
     *
     * <ul>
     *   <li>Iron's Spells ships its own advancement tree; the beats read four
     *       of its nodes by id - {@code spell_book_equip} (equip a spell book
     *       in the curio slot, the moment casting becomes possible),
     *       {@code spell_book_iron}, {@code make_inscription_table} and the
     *       challenge node {@code ink_legendary}.</li>
     *   <li>Mysticism ships no advancements, but it does register a custom
     *       statistic, {@code mysticism:lightning_strikes} (its lang key is
     *       {@code stat.mysticism.lightning_strikes}), which a stat task reads
     *       directly.</li>
     *   <li><b>Finding:</b> {@code unraveling_spells} is <em>not installed</em>
     *       in the pack (a full mod-list scan finds no such mod). The draft's
     *       "Mysticism/Unraveling" branch is therefore re-expressed as the two
     *       paths that do exist: the Inscription path (Iron's Spells) and the
     *       Mystic path (Mysticism). Nothing is built for a mod that is not
     *       there.</li>
     * </ul>
     *
     * <p>No beat reads a Tensura skill, so the disabled-skill rule holds.</p>
     *
     * @return the Spellcraft chapter spec
     */
    public static AuthoredChapter spellcraft() {
        int chapter = SPELLCRAFT_CHAPTER;
        return AuthoredChapter.requiring(SPELLCRAFT_MODS, ChapterIds.chapter(chapter),
            "Spellcraft", List.of(
                new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                    "First Incantation",
                    "Equip a spell book and speak your first word of power - the skills the Six taught you now have a caster's voice.",
                    List.of(TaskSpec.advancement(ChapterIds.task(chapter, 0, 0), SPELL_BOOK_EQUIP)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 6))),
                new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                    "Apprentice's Grimoire",
                    "An iron-bound grimoire. The shelf is growing.",
                    List.of(TaskSpec.advancement(ChapterIds.task(chapter, 1, 0), SPELL_BOOK_IRON)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.EMERALD, 10))),
                new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                    "The Inscribed Path",
                    "Build an inscription table and write your own spells instead of buying them.",
                    List.of(TaskSpec.advancement(ChapterIds.task(chapter, 2, 0), MAKE_INSCRIPTION_TABLE)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.DIAMOND, 1))),
                new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                    "The Mystic Path",
                    "Mysticism answers a different call - and it answers in lightning.",
                    List.of(TaskSpec.statistic(ChapterIds.task(chapter, 3, 0), MYSTIC_LIGHTNING_STRIKES, 1)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 3, 0), Items.DIAMOND, 1))),
                new QuestSpec(ChapterIds.quest(chapter, 4), 6.0D, 0.0D,
                    "Archmage's Trace",
                    "Legendary ink, the mark of a finished mage. Commerce's coin bought the reagents; the trials take it from here.",
                    List.of(TaskSpec.advancement(ChapterIds.task(chapter, 4, 0), INK_LEGENDARY)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 4, 0), Items.DIAMOND, 3)))
            ));
    }
}
