package org.bitwisemadness.tensuraquestlines.gametest;

import dev.architectury.utils.Env;
import dev.ftb.mods.ftbquests.quest.Chapter;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.bitwisemadness.tensuraquestlines.TensuraQuestlines;
import org.bitwisemadness.tensuraquestlines.content.QuestlineContent;
import org.bitwisemadness.tensuraquestlines.content.ReincarnationContent;
import org.bitwisemadness.tensuraquestlines.delivery.AuthoredChapter;
import org.bitwisemadness.tensuraquestlines.delivery.QuestSpec;
import org.bitwisemadness.tensuraquestlines.delivery.RewardSpec;
import org.bitwisemadness.tensuraquestlines.delivery.TaskSpec;

/**
 * Proves the delivery layer builds authored content on a quest file, and
 * that the content set itself is structurally sound.
 *
 * <p>Every test constructs its own {@link TestQuestFile}, so the run's real
 * quest file is never touched.</p>
 */
@GameTestHolder(TensuraQuestlines.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ChapterDeliveryGameTests {

    private ChapterDeliveryGameTests() {
    }

    /** Applying a chapter spec creates the chapter, its quests, tasks and rewards. */
    @GameTest(template = "empty")
    public static void delivery_builds_the_first_steps_chapter(GameTestHelper helper) {
        TestQuestFile file = new TestQuestFile(Env.SERVER, helper.getLevel().registryAccess());
        AuthoredChapter spec = ReincarnationContent.firstSteps();
        helper.assertTrue(spec.apply(file), "first apply must report changes");
        Chapter chapter = file.getChapter(spec.id());
        if (chapter == null) {
            helper.fail("chapter must exist after delivery");
            return;
        }
        helper.assertTrue(chapter.getQuests().size() == spec.quests().size(),
            "every authored quest must be delivered");
        QuestSpec first = spec.quests().get(0);
        helper.assertTrue(file.getQuest(first.id()) != null, "quest ids must resolve");
        helper.assertTrue(file.getTask(first.tasks().get(0).id()) != null, "task ids must resolve");
        helper.assertTrue(file.getReward(first.rewards().get(0).id()) != null, "reward ids must resolve");
        helper.succeed();
    }

    /** A second apply reports no changes and duplicates nothing. */
    @GameTest(template = "empty")
    public static void delivery_is_idempotent(GameTestHelper helper) {
        TestQuestFile file = new TestQuestFile(Env.SERVER, helper.getLevel().registryAccess());
        AuthoredChapter spec = ReincarnationContent.firstSteps();
        spec.apply(file);
        Chapter chapter = file.getChapter(spec.id());
        if (chapter == null) {
            helper.fail("chapter must exist after delivery");
            return;
        }
        int quests = chapter.getQuests().size();
        helper.assertTrue(!spec.apply(file), "second apply must report no changes");
        Chapter after = file.getChapter(spec.id());
        if (after == null) {
            helper.fail("chapter must survive a second apply");
            return;
        }
        helper.assertTrue(after.getQuests().size() == quests, "a second apply must not duplicate quests");
        helper.succeed();
    }

    /** Every id the content set allocates is unique across chapters. */
    @GameTest(template = "empty")
    public static void content_ids_are_unique_across_the_set(GameTestHelper helper) {
        Set<Long> ids = new HashSet<>();
        int count = 0;
        for (AuthoredChapter chapter : QuestlineContent.chapters()) {
            count += collect(ids, chapter.id(), helper, "chapter");
            for (QuestSpec quest : chapter.quests()) {
                count += collect(ids, quest.id(), helper, "quest");
                for (TaskSpec task : quest.tasks()) {
                    count += collect(ids, task.id(), helper, "task");
                }
                for (RewardSpec reward : quest.rewards()) {
                    count += collect(ids, reward.id(), helper, "reward");
                }
            }
        }
        helper.assertTrue(count > 0, "the content set must not be empty");
        helper.succeed();
    }

    private static int collect(Set<Long> ids, long id, GameTestHelper helper, String kind) {
        helper.assertTrue(ids.add(id), "duplicate " + kind + " id " + id);
        return 1;
    }
}
