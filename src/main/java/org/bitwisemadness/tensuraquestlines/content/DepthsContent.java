package org.bitwisemadness.tensuraquestlines.content;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.bitwisemadness.tensuraquestlines.delivery.AuthoredChapter;
import org.bitwisemadness.tensuraquestlines.delivery.QuestSpec;
import org.bitwisemadness.tensuraquestlines.delivery.RewardSpec;
import org.bitwisemadness.tensuraquestlines.delivery.TaskSpec;

/**
 * Major questline 4, Infinite Depths (HexaHexes/tensura-quests#32): beneath
 * the surface lies a descent that does not end, and this line is the pack's
 * PvE spine - learn the depth, beat what guards it, go deeper.
 *
 * <p>The line reads the Infinite Dungeon task family, so every chapter
 * declares {@code infinite_dungeon_api} in its required mods: the chapter
 * ships dormant wherever that event surface is absent, and the conservation
 * rule holds - the dungeon suite owns its gameplay, this line only counts
 * what the suite already reports.</p>
 *
 * <p>The dungeon's dimension id is <b>authoring data</b>, validated against
 * the pack at authoring time (its own {@code dimension_profile} data names
 * {@code infinite_dungeon:dungeon}) and never invented in code.</p>
 */
public final class DepthsContent {

    private static final int THE_OPENING_CHAPTER = 15;
    private static final List<String> INFINITE_DUNGEON = List.of("infinite_dungeon_api");
    private static final List<String> DUNGEON = List.of("infinite_dungeon:dungeon");
    private static final ResourceLocation DUNGEON_DIMENSION =
        ResourceLocation.parse("infinite_dungeon:dungeon");

    private DepthsContent() {
    }

    /**
     * Sub-questline 1, The Opening and Deep Claim (HexaHexes/tensura-questlines#16):
     * the descent spine, from stepping through the gate to the finale
     * threshold. Five quests:
     *
     * <ol>
     *   <li><b>The Opening</b> - the entrance beat, a built-in dimension check
     *       on the dungeon's own dimension. Nothing is hardcoded: the id is the
     *       pack's dimension-profile data, and the gate itself was funded by
     *       Commerce's Tycoon (bridge 3-to-4).</li>
     *   <li><b>First Descent</b> - the first floor, a native Floor Completion
     *       task. The text names the Miner, the job that works the Depths
     *       (bridge 4-to-2).</li>
     *   <li><b>Sounding the Depth</b> - the first native Dungeon Milestone.</li>
     *   <li><b>Deeper Still</b> - the mid milestone tier.</li>
     *   <li><b>Deep Claim</b> - the finale high-threshold milestone, carrying
     *       the 5-to-4 interlock: the text matches the Trials' Final Gate and
     *       its "Proven" reward beat, the counterpart recorded on
     *       HexaHexes/tensura-questlines#25 and in the #32 - #33 registry. The
     *       hard quest link is deliberately deferred to whichever side lands
     *       second: a dependency on a quest that does not exist yet would be
     *       dropped on load and gate nothing.</li>
     * </ol>
     *
     * <p>Milestone layers are first-pass - the suite's live floor generation
     * is the authority - and are tuned in the game-testing round, an ordinary
     * content revision.</p>
     *
     * @return the Opening and Deep Claim chapter spec
     */
    public static AuthoredChapter theOpening() {
        int chapter = THE_OPENING_CHAPTER;
        return AuthoredChapter.requiring(INFINITE_DUNGEON, ChapterIds.chapter(chapter),
            "The Opening and Deep Claim", List.of(
                new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                    "The Opening",
                    "Commerce paid the toll; the way down is open. Step through the gate.",
                    List.of(TaskSpec.dimension(ChapterIds.task(chapter, 0, 0), DUNGEON_DIMENSION)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 6))),
                new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                    "First Descent",
                    "Boots on the first floor. The Miner knows this road better than anyone.",
                    List.of(TaskSpec.floorCompletion(ChapterIds.task(chapter, 1, 0), DUNGEON, 0, 0)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.EMERALD, 8))),
                new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                    "Sounding the Depth",
                    "The dark has a floor count, and you have started keeping it.",
                    List.of(TaskSpec.dungeonMilestone(ChapterIds.task(chapter, 2, 0), DUNGEON, 5)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.DIAMOND, 1))),
                new QuestSpec(ChapterIds.quest(chapter, 3), 4.5D, 0.0D,
                    "Deeper Still",
                    "What sounded like a bottom was only a landing.",
                    List.of(TaskSpec.dungeonMilestone(ChapterIds.task(chapter, 3, 0), DUNGEON, 15)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 3, 0), Items.DIAMOND, 2))),
                new QuestSpec(ChapterIds.quest(chapter, 4), 6.0D, 0.0D,
                    "Deep Claim",
                    "Reserved for the Proven - the Final Gate's champion. The Depths answer only to those the trials have finished.",
                    List.of(TaskSpec.dungeonMilestone(ChapterIds.task(chapter, 4, 0), DUNGEON, 30)),
                    List.of(RewardSpec.item(ChapterIds.reward(chapter, 4, 0), Items.DIAMOND, 4)))
            ));
    }
}
