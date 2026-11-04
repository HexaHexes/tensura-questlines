package org.bitwisemadness.tensuraquestlines.content;

import io.github.manasmods.tensura_ftb.tasks.AbilityTask;
import io.github.manasmods.tensura_ftb.tasks.AwakeningTask;
import io.github.manasmods.tensura_ftb.tasks.ExistenceTask;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
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
    private static final int FIRST_SKILLS_CHAPTER = 3;
    private static final int AWAKENINGS_CHAPTER = 4;

    private ReincarnationContent() {
    }

    /**
     * @return the First Steps chapter spec
     */
    public static AuthoredChapter firstSteps() {
        int chapter = FIRST_STEPS_CHAPTER;
        return AuthoredChapter.of(ChapterIds.chapter(chapter), "First Steps", List.of(
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
        return AuthoredChapter.of(ChapterIds.chapter(chapter), "Race & Nature", List.of(
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

    /**
     * Sub-questline 3, First Skills (HexaHexes/tensura-questlines#4): train
     * the skills the pack actually ships enabled.
     *
     * <p>Both ability quests read the native Tensura Compat: FTB ability
     * task, authored through its own {@code ability}/{@code status} data keys
     * because the fields are private. The cheapest tiers are targeted
     * deliberately: a common skill (Fighter), and a resistance skill, which
     * the pack's own {@code resistance_config.toml} teaches from accumulated
     * damage points. The {@code tensura_skill_disabled} curation was audited
     * before shipping - its blacklist is empty in this pack, and stextras'
     * {@code validSkillsTOLock} lists skills a player may *choose* to lock -
     * so neither requirement is a disabled skill.</p>
     *
     * @return the First Skills chapter spec
     */
    public static AuthoredChapter firstSkills() {
        int chapter = FIRST_SKILLS_CHAPTER;
        return AuthoredChapter.of(ChapterIds.chapter(chapter), "First Skills", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "A Skill to Call Your Own",
                "The ability menu decides who you become. Open it.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 0, 0))),
                List.of()),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "First Discipline",
                "Learn Fighter - the broadest cheap start.",
                List.of(TaskSpec.ability(ChapterIds.task(chapter, 1, 0),
                    ResourceLocation.parse("tensura:fighter"), AbilityTask.AcquisitionStatus.LEARNT)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.EMERALD, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "The Book of Skills",
                "A study book holds a skill you have not met yet.",
                List.of(TaskSpec.item(ChapterIds.task(chapter, 2, 0),
                    ResourceLocation.parse("tensura_skill_books:skill_study_book"), 1)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.IRON_INGOT, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                "Toughen Up",
                "Get hit, learn, resist: physical attack resistance.",
                List.of(TaskSpec.ability(ChapterIds.task(chapter, 3, 0),
                    ResourceLocation.parse("tensura:physical_attack_resistance"),
                    AbilityTask.AcquisitionStatus.LEARNT)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 3, 0), Items.GOLD_INGOT, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 4), 6.0D, 0.0D,
                "Tools of the Trade",
                "Next: what you are becoming. See Awakenings.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 4, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 4, 0), Items.EMERALD, 4)))
        ));
    }

    /**
     * Sub-questline 4, Awakenings (HexaHexes/tensura-questlines#5): the
     * line's climax and its two handoffs.
     *
     * <p>The two milestone quests read the native existence task, which
     * reports a live counter as progress and completes at the configured
     * threshold; peak counters ({@code MAX_AURA}, {@code MAX_EP}) are used so
     * the feat stays earned. The awakening quest reads the native awakening
     * task with {@code ANY}: whatever the player becomes counts, so no path
     * is forced on them. Thresholds are first-pass numbers, tuned in the
     * game-testing round.</p>
     *
     * @return the Awakenings chapter spec
     */
    public static AuthoredChapter awakenings() {
        int chapter = AWAKENINGS_CHAPTER;
        return AuthoredChapter.of(ChapterIds.chapter(chapter), "Awakenings", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "The Stirring",
                "Your aura answers before you do.",
                List.of(TaskSpec.existence(ChapterIds.task(chapter, 0, 0),
                    ExistenceTask.ExistenceType.MAX_AURA, 50L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 3))),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "The Deepening",
                "Power compounds. The peak is what counts.",
                List.of(TaskSpec.existence(ChapterIds.task(chapter, 1, 0),
                    ExistenceTask.ExistenceType.MAX_EP, 500L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.GOLD_INGOT, 4))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "Awakening",
                "Whatever you become, become it fully.",
                List.of(TaskSpec.awakening(ChapterIds.task(chapter, 2, 0),
                    AwakeningTask.AwakeningStatus.ANY)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.DIAMOND, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                "Who I Will Be",
                "Two roads from here: a trade, or a proving.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 3, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 3, 0), Items.DIAMOND, 3)))
        ));
    }
}
