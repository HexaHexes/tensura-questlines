package org.bitwisemadness.tensuraquestlines.content;

import java.util.List;
import net.minecraft.world.item.Items;
import org.bitwisemadness.tensuraquestlines.delivery.AuthoredChapter;
import org.bitwisemadness.tensuraquestlines.delivery.QuestSpec;
import org.bitwisemadness.tensuraquestlines.delivery.RewardSpec;
import org.bitwisemadness.tensuraquestlines.delivery.TaskSpec;
import org.bitwisemadness.tensuraquests.tasks.JobRequirement;

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
    private static final int COMPETENCE_CHAPTER = 6;
    private static final int WIDER_BENCH_CHAPTER = 7;
    private static final int DOUBLE_LIFE_CHAPTER = 8;
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

    /**
     * Sub-questline 2, Competence tiers (HexaHexes/tensura-questlines#7):
     * early mastery, one trade deep.
     *
     * <p>Tiers are 5 / 10 / 15, chosen against the pack's own curve
     * ({@code XP(25) = 5188}, {@code maxLevel = 25}) and leaving the
     * capstone tier to Master of Trades. The mastery type is job-specific -
     * it cannot express "any job" - so the ladder carries {@code miner},
     * matching First Job and the recorded 2-to-4 bridge ("the Miner works
     * the Depths"). Thresholds are first-pass numbers, tuned in the
     * game-testing round.</p>
     *
     * @return the Competence tiers chapter spec
     */
    public static AuthoredChapter competence() {
        int chapter = COMPETENCE_CHAPTER;
        return AuthoredChapter.requiring(SYMPATHETIC_JOBS, ChapterIds.chapter(chapter), "Competence", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "Getting Good",
                "Five levels in: the pick starts to know you.",
                List.of(TaskSpec.jobMastery(ChapterIds.task(chapter, 0, 0), "miner", 5)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 6))),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "Steady Hand",
                "Ten levels: competence stops being luck.",
                List.of(TaskSpec.jobMastery(ChapterIds.task(chapter, 1, 0), "miner", 10)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.EMERALD, 10))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "Depth in One",
                "Fifteen levels of one trade, before you spread thin.",
                List.of(TaskSpec.jobMastery(ChapterIds.task(chapter, 2, 0), "miner", 15)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.DIAMOND, 1))),
            new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                "Where It Leads",
                "Next: room for more trades. See The Wider Bench.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 3, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 3, 0), Items.EMERALD, 12)))
        ));
    }

    /**
     * Sub-questline 3, The Wider Bench (HexaHexes/tensura-questlines#8):
     * earn and spend job-slot unlocks.
     *
     * <p>Each rung proves breadth first - a multi-job mastery threshold over
     * the two trades First Job and Competence established - and then grants
     * one slot through the native unlock reward. The reward raises the
     * claiming player's effective slot limit (Sympathetic Jobs uses the
     * override when set and its own global default otherwise, clamping to its
     * job count), so the ladder is meaningful above the pack's default of
     * three. Rungs are one-time quests; nothing here needs repeatable quest
     * support.</p>
     *
     * @return the Wider Bench chapter spec
     */
    public static AuthoredChapter widerBench() {
        int chapter = WIDER_BENCH_CHAPTER;
        return AuthoredChapter.requiring(SYMPATHETIC_JOBS, ChapterIds.chapter(chapter), "The Wider Bench", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "More Than One Trade",
                "A bench with one tool is a hobby.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 0, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 8))),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "Room for More",
                "Prove both trades, then take the slot.",
                List.of(TaskSpec.multiJobMastery(ChapterIds.task(chapter, 1, 0),
                    List.of(new JobRequirement("miner", 5), new JobRequirement("lumberjack", 5)))),
                List.of(RewardSpec.unlockJobSlot(ChapterIds.reward(chapter, 1, 0), 1))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "Bench Expansion",
                "Deeper in both, and one slot further.",
                List.of(TaskSpec.multiJobMastery(ChapterIds.task(chapter, 2, 0),
                    List.of(new JobRequirement("miner", 10), new JobRequirement("lumberjack", 10)))),
                List.of(RewardSpec.unlockJobSlot(ChapterIds.reward(chapter, 2, 0), 1))),
            new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                "The Full Rack",
                "Both trades at fifteen, and the last slot this pack needs. Next: Double Life.",
                List.of(TaskSpec.multiJobMastery(ChapterIds.task(chapter, 3, 0),
                    List.of(new JobRequirement("miner", 15), new JobRequirement("lumberjack", 15)))),
                List.of(RewardSpec.unlockJobSlot(ChapterIds.reward(chapter, 3, 0), 1)))
        ));
    }

    /**
     * Sub-questline 4, Double Life (HexaHexes/tensura-questlines#9):
     * multi-job mastery - carrying two trades to the top at once.
     *
     * <p>Both rungs read the multi-job mastery task, so nothing here needs a
     * new type: the ladder is the two trades First Job and Competence raised,
     * driven towards the pack's level cap (25). The finale hands off to Master
     * of Trades, whose capstone adds the third trade the pack's default of
     * three slots already allows.</p>
     *
     * @return the Double Life chapter spec
     */
    public static AuthoredChapter doubleLife() {
        int chapter = DOUBLE_LIFE_CHAPTER;
        return AuthoredChapter.requiring(SYMPATHETIC_JOBS, ChapterIds.chapter(chapter), "Double Life", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "Two Trades",
                "Two trades, both near the top of their curve.",
                List.of(TaskSpec.multiJobMastery(ChapterIds.task(chapter, 0, 0),
                    List.of(new JobRequirement("miner", 20), new JobRequirement("lumberjack", 20)))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 16))),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "Balancing Act",
                "Both at the cap. This is the double life.",
                List.of(TaskSpec.multiJobMastery(ChapterIds.task(chapter, 1, 0),
                    List.of(new JobRequirement("miner", 25), new JobRequirement("lumberjack", 25)))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.DIAMOND, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "Journeyman of All",
                "Next: add the third trade. See Master of Trades.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 2, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.EMERALD, 20)))
        ));
    }
}
