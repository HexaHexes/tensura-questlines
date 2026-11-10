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
    private static final int WYRMHUNTER_CHAPTER = 19;
    private static final int ENCHANTERS_LADDER_CHAPTER = 20;
    private static final List<String> SPELLCRAFT_MODS = List.of("irons_spellbooks", "mysticism");
    private static final List<String> WYRMHUNTER_MODS = List.of("iceandfire", "questkilltask");
    private static final List<String> ENCHANTER_MODS = List.of("apothic_enchanting", "apotheosis");
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
    private static final ResourceLocation DRAGON_EGG =
        ResourceLocation.parse("iceandfire:iceandfire/dragon_egg");
    private static final ResourceLocation DRAGON_ARMOR =
        ResourceLocation.parse("iceandfire:iceandfire/dragonarmor");
    private static final ResourceLocation FIRE_DRAGON = ResourceLocation.parse("iceandfire:fire_dragon");
    private static final ResourceLocation ICE_DRAGON = ResourceLocation.parse("iceandfire:ice_dragon");
    private static final ResourceLocation LIGHTNING_DRAGON =
        ResourceLocation.parse("iceandfire:lightning_dragon");
    private static final ResourceLocation ENCHANTER_TABLE =
        ResourceLocation.parse("apothic_enchanting:enchanter");
    private static final ResourceLocation AFFIX_HAVEN = ResourceLocation.parse("apotheosis:progression/haven");
    private static final ResourceLocation ENCH_ENCHANT_60 = ResourceLocation.parse("apothic_enchanting:60ench");
    private static final ResourceLocation ENCH_ENCHANT_100 = ResourceLocation.parse("apothic_enchanting:100ench");

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

    /**
     * Sub-questline 2, Wyrmhunter (HexaHexes/tensura-questlines#21): Ice and
     * Fire CE progression, from a stolen egg to all three dragons down.
     *
     * <p><b>Observable-signal audit</b> (the sub-issue's acceptance box):</p>
     *
     * <ul>
     *   <li>Entity ids are validated against the pack's Ice and Fire CE
     *       registration: its lang ships {@code entity.iceandfire.fire_dragon},
     *       {@code ice_dragon} and {@code lightning_dragon}.</li>
     *   <li>Advancement ids are real nodes in {@code data/iceandfire/advancement/}:
     *       {@code dragon_egg} ("Mother of Dragons") and {@code dragonarmor}.</li>
     *   <li>Kills read <b>FTB's own kill task</b>, never rebuilt. The pack's
     *       {@code Quest-Kill-Task} mod has no task class of its own - it
     *       enhances FTB's kill handling with attribution (its mixins and
     *       {@code DamageTracker}) - so the beats use the native type and the
     *       attribution improvement comes with the pack. The chapter gates on
     *       both mods.</li>
     *   <li><b>Constraint honoured:</b> {@code iceandfirexbettercombat} is
     *       disabled, so nothing here is combo-flavoured - only holdings and
     *       kills.</li>
     * </ul>
     *
     * @return the Wyrmhunter chapter spec
     */
    public static AuthoredChapter wyrmhunter() {
        int chapter = WYRMHUNTER_CHAPTER;
        return AuthoredChapter.requiring(WYRMHUNTER_MODS, ChapterIds.chapter(chapter),
            "Wyrmhunter", List.of(
                new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                    "An Egg",
                    "Any dragon egg will do. Every wyrmhunt begins with one stolen from a nest.",
                    List.of(TaskSpec.advancement(ChapterIds.task(chapter, 0, 0), DRAGON_EGG)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 8))),
                new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                    "First Wyrm",
                    "Bring down a fire dragon - the first rung of the hunt.",
                    List.of(TaskSpec.kill(ChapterIds.task(chapter, 1, 0), FIRE_DRAGON, 1)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.DIAMOND, 1))),
                new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                    "Hoard Curator",
                    "Turn dragon scales into armor. A hunter's hoard is what they wear.",
                    List.of(TaskSpec.advancement(ChapterIds.task(chapter, 2, 0), DRAGON_ARMOR)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.DIAMOND, 2))),
                new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                    "Wyrmhunter",
                    "Fire, ice and lightning, all brought low. This is the pacing the deeper trials will demand.",
                    List.of(
                        TaskSpec.kill(ChapterIds.task(chapter, 3, 0), FIRE_DRAGON, 1),
                        TaskSpec.kill(ChapterIds.task(chapter, 3, 1), ICE_DRAGON, 1),
                        TaskSpec.kill(ChapterIds.task(chapter, 3, 2), LIGHTNING_DRAGON, 1)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 3, 0), Items.DIAMOND, 4)))
            ));
    }

    /**
     * Sub-questline 3, Enchanter's Ladder (HexaHexes/tensura-questlines#22):
     * the Apotheosis / ApothicEnchanting tiers.
     *
     * <p><b>Observable-signal audit</b> (the sub-issue's acceptance box):</p>
     *
     * <ul>
     *   <li>ApothicEnchanting ships a real advancement ladder; the beats read
     *       {@code apothic_enchanting:enchanter} ("obtain an enchanting table")
     *       and its level nodes {@code 60ench} and {@code 100ench}, all keyed
     *       to the mod's own {@code apothic_enchanting:enchanted} trigger.</li>
     *   <li>The affix step reads Apotheosis' live World-Tier node,
     *       {@code apotheosis:progression/haven} (fires on picking up an affix
     *       item).</li>
     *   <li><b>Finding:</b> Apotheosis' older per-affix advancements (gem,
     *       socket, and friends) ship under {@code old_data/}, <em>not</em>
     *       {@code data/}, so they are not loaded and cannot be read - the
     *       affix beat uses the live node instead.</li>
     * </ul>
     *
     * <p>Rewards stay vanilla; the ladder is the point and this keeps the
     * item surface safe.</p>
     *
     * @return the Enchanter's Ladder chapter spec
     */
    public static AuthoredChapter enchanter() {
        int chapter = ENCHANTERS_LADDER_CHAPTER;
        return AuthoredChapter.requiring(ENCHANTER_MODS, ChapterIds.chapter(chapter),
            "Enchanter's Ladder", List.of(
                new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                    "The Table",
                    "Obtain an enchanting table and start unveiling the mystery.",
                    List.of(TaskSpec.advancement(ChapterIds.task(chapter, 0, 0), ENCHANTER_TABLE)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 8))),
                new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                    "Affix Apprentice",
                    "Pick up an affix item - the first taste of Apotheosis gear.",
                    List.of(TaskSpec.advancement(ChapterIds.task(chapter, 1, 0), AFFIX_HAVEN)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.DIAMOND, 1))),
                new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                    "Higher Grades",
                    "Enchant an item at level 60 - hell and sea shelved into the table.",
                    List.of(TaskSpec.advancement(ChapterIds.task(chapter, 2, 0), ENCH_ENCHANT_60)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.DIAMOND, 2))),
                new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                    "Master Enchanter",
                    "Level 100. Commerce funds the shelves; the ladder is yours alone.",
                    List.of(TaskSpec.advancement(ChapterIds.task(chapter, 3, 0), ENCH_ENCHANT_100)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 3, 0), Items.DIAMOND, 4)))
            ));
    }
}
