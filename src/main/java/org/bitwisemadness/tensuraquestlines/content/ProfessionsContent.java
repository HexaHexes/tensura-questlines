package org.bitwisemadness.tensuraquestlines.content;

import java.util.List;
import net.minecraft.world.item.Items;
import org.bitwisemadness.tensuraquestlines.delivery.AuthoredChapter;
import org.bitwisemadness.tensuraquestlines.delivery.QuestSpec;
import org.bitwisemadness.tensuraquestlines.delivery.RewardSpec;
import org.bitwisemadness.tensuraquestlines.delivery.TaskSpec;

/**
 * Major questline 2, Professions (HexaHexes/tensura-quests#30): a nation of
 * professionals, where a name is made trade by trade.
 *
 * <p>The line reads the Sympathetic Jobs task family, which tensura_quests
 * registers only behind its jobs version gate. Every chapter therefore
 * declares {@code sympatheticjobs} in its required mods: the questlines ship
 * dormant where the jobs system is absent - the pack currently carries it
 * disabled - and activate unchanged once it is enabled.</p>
 */
public final class ProfessionsContent {

    private static final int FIRST_JOB_CHAPTER = 5;
    private static final List<String> SYMPATHETIC_JOBS = List.of("sympatheticjobs");

    private ProfessionsContent() {
    }

    /**
     * Sub-questline 1, First Job (HexaHexes/tensura-questlines#6): join a
     * first trade and work a day of it.
     *
     * <p>Job names are the pack's twelve real jobs, so neither task can fail
     * silently: {@code miner} doubles as the recorded 2-to-4 bridge ("the
     * Miner works the Depths") and {@code lumberjack} widens the bench
     * without leaving the passive resource trades. Rewards stay vanilla on
     * purpose - Commerce owns coin, and this line should not need a currency
     * mod to hand out its first wages.</p>
     *
     * @return the First Job chapter spec
     */
    public static AuthoredChapter firstJob() {
        int chapter = FIRST_JOB_CHAPTER;
        return AuthoredChapter.requiring(SYMPATHETIC_JOBS, ChapterIds.chapter(chapter), "First Job", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "The Job Board",
                "A nation of professionals, and one board of names.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 0, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 3))),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "Sign On",
                "Pick up the pick: Miner is the oldest trade here.",
                List.of(TaskSpec.jobParticipation(ChapterIds.task(chapter, 1, 0), "miner")),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.EMERALD, 5))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "A Day's Work",
                "A second trade widens the bench.",
                List.of(TaskSpec.jobParticipation(ChapterIds.task(chapter, 2, 0), "lumberjack")),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.EMERALD, 5))),
            new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                "Where the Work Is",
                "Next: prove competence. See Competence tiers.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 3, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 3, 0), Items.EMERALD, 8)))
        ));
    }
}
