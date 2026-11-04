package org.bitwisemadness.tensuraquestlines.content;

import java.util.List;
import net.minecraft.world.item.Items;
import org.bitwisemadness.tensuraquestlines.delivery.AuthoredChapter;
import org.bitwisemadness.tensuraquestlines.delivery.QuestSpec;
import org.bitwisemadness.tensuraquestlines.delivery.RewardSpec;
import org.bitwisemadness.tensuraquestlines.delivery.TaskSpec;

/**
 * Major questline 1, Reincarnation (HexaHexes/tensura-quests#29): the
 * Tensura: Reincarnated core progression, from landing in the world to the
 * first awakening.
 *
 * <p>Sub-questline 1, First Steps (HexaHexes/tensura-questlines#2), is the
 * pack's first-hours chapter: vanilla survival beats built from FTB Quests
 * built-ins only, so it completes before any optional pack system is
 * enabled. Its finale points at Race &amp; Nature, the next chapter of this
 * line.</p>
 */
public final class ReincarnationContent {

    private static final int FIRST_STEPS_CHAPTER = 1;
    private static final int RACE_AND_NATURE_CHAPTER = 2;

    private ReincarnationContent() {
    }

    /**
     * @return the First Steps chapter spec
     */
    public static AuthoredChapter firstSteps() {
        int chapter = FIRST_STEPS_CHAPTER;
        return new AuthoredChapter(ChapterIds.chapter(chapter), "First Steps", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "Arrival",
                "The world is new. Make it to dawn.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 0, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.BREAD, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "Shelter Before Dark",
                "Stack something between you and the night.",
                List.of(TaskSpec.item(ChapterIds.task(chapter, 1, 0), Items.COBBLESTONE, 16)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.TORCH, 8))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "First Hunt",
                "Bring back proof you can feed yourself.",
                List.of(TaskSpec.item(ChapterIds.task(chapter, 2, 0), Items.LEATHER, 4)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.COOKED_BEEF, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                "A Full Stomach",
                "A settled routine starts with a stocked larder.",
                List.of(TaskSpec.item(ChapterIds.task(chapter, 3, 0), Items.BREAD, 8)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 3, 0), Items.IRON_INGOT, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 4), 6.0D, 0.0D,
                "The Road Ahead",
                "Next: settle what you are. See Race & Nature.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 4, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 4, 0), Items.IRON_INGOT, 4)))
        ));
    }

    /**
     * Sub-questline 2, Race &amp; Nature
     * (HexaHexes/tensura-questlines#3): discover and settle your race, and
     * hear the first stirring of your spirit.
     *
     * <p>The two milestone quests read the native Tensura Compat: FTB check
     * tasks as registered - a race check and a spirit check, both
     * fieldless - so the chapter never hardcodes a race id and stays valid
     * for every race the pack ships. Its finale points at First Skills, the
     * next chapter of this line.</p>
     *
     * @return the Race &amp; Nature chapter spec
     */
    public static AuthoredChapter raceAndNature() {
        int chapter = RACE_AND_NATURE_CHAPTER;
        return new AuthoredChapter(ChapterIds.chapter(chapter), "Race & Nature", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "What Am I?",
                "Something in you is not human any more. Find out what.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 0, 0))),
                List.of()),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "Settled",
                "Choose a race, and make it yours.",
                List.of(TaskSpec.race(ChapterIds.task(chapter, 1, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.EMERALD, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "First Stirring",
                "Your spirit answers for the first time.",
                List.of(TaskSpec.spirit(ChapterIds.task(chapter, 2, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.GOLD_INGOT, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                "Nature's Shape",
                "Next: what you can do with it. See First Skills.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 3, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 3, 0), Items.EMERALD, 4)))
        ));
    }
}
