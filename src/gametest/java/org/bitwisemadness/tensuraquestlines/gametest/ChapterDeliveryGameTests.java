package org.bitwisemadness.tensuraquestlines.gametest;

import dev.architectury.utils.Env;
import dev.ftb.mods.ftbquests.quest.Chapter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.fml.ModList;
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

    /**
     * Gating honours the loaded set: a chapter is available exactly when every
     * mod it names is loaded, and a chapter naming an unloaded mod is skipped.
     *
     * <p>The GameTest closure deliberately stages only the systems this repo's
     * own verification needs; a chapter gated on a pack mod that is not staged
     * here is correctly dormant, and its real verification is the in-pack
     * smoke its sub-issue requires. This test therefore checks the contract,
     * not that every declared mod happens to be staged.</p>
     */
    @GameTest(template = "empty")
    public static void chapters_gate_on_their_optional_mods(GameTestHelper helper) {
        int available = 0;
        for (AuthoredChapter chapter : QuestlineContent.chapters()) {
            boolean modsLoaded = true;
            for (String modId : chapter.requiredMods()) {
                modsLoaded &= ModList.get().isLoaded(modId);
            }
            helper.assertTrue(chapter.isAvailable() == modsLoaded,
                "chapter " + chapter.id() + " must be available exactly when its mods are loaded");
            if (chapter.isAvailable()) {
                available++;
            }
        }
        helper.assertTrue(available > 0, "the staged systems must leave at least one chapter deliverable");
        for (String modId : List.of("sympatheticjobs", "lightmanscurrency", "infinite_dungeon_api")) {
            helper.assertTrue(ModList.get().isLoaded(modId),
                "core optional system " + modId + " must stay staged in the GameTest closure");
        }
        AuthoredChapter absent = AuthoredChapter.requiring(List.of("no_such_mod_anywhere"), 1L, "absent", List.of());
        helper.assertTrue(!absent.isAvailable(),
            "a chapter naming an unloaded mod must be unavailable so delivery skips it");
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

    /**
     * Every chapter whose optional systems are staged materialises on a fresh
     * file, quests and all. Chapters gated on an unstaged pack mod are dormant
     * here and skipped - their delivery is exercised in-pack, not least because
     * some of their task types live in mods this closure does not carry.
     */
    @GameTest(template = "empty")
    public static void delivery_builds_every_available_chapter(GameTestHelper helper) {
        TestQuestFile file = new TestQuestFile(Env.SERVER, helper.getLevel().registryAccess());
        for (AuthoredChapter chapter : QuestlineContent.chapters()) {
            if (!chapter.isAvailable()) {
                continue;
            }
            helper.assertTrue(chapter.apply(file),
                "chapter " + chapter.id() + " must report changes on a fresh file");
            Chapter built = file.getChapter(chapter.id());
            if (built == null) {
                helper.fail("chapter " + chapter.id() + " must exist after delivery");
                return;
            }
            helper.assertTrue(built.getQuests().size() == chapter.quests().size(),
                "chapter " + chapter.id() + " must deliver every authored quest");
            for (QuestSpec quest : chapter.quests()) {
                helper.assertTrue(file.getQuest(quest.id()) != null,
                    "quest " + quest.id() + " must resolve");
            }
        }
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
