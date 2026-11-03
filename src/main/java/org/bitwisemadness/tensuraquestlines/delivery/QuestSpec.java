package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import java.util.List;
import java.util.Objects;

/**
 * One authored quest: position on the chapter canvas, text, tasks and
 * rewards.
 *
 * <p>Authored quests are matched by id, never by title or index, so the
 * spec list may grow or reorder freely between releases without duplicating
 * or orphaning anything.</p>
 *
 * @param id the quest-file object id
 * @param x canvas position
 * @param y canvas position
 * @param title the quest title
 * @param subtitle the quest subtitle
 * @param tasks the quest's tasks
 * @param rewards the quest's rewards
 */
public record QuestSpec(long id, double x, double y, String title, String subtitle,
                        List<TaskSpec> tasks, List<RewardSpec> rewards) {

    /**
     * Creates the quest, or refreshes it in place.
     *
     * @param file the quest file being delivered into
     * @param chapter the chapter this quest belongs to
     * @return true when anything was created or changed
     */
    boolean apply(BaseQuestFile file, Chapter chapter) {
        boolean changed = false;
        Quest quest = file.getQuest(id);
        if (quest == null) {
            quest = new Quest(id, chapter);
            quest.onCreated();
            changed = true;
        }
        changed |= TextSync.title(quest, title);
        changed |= TextSync.subtitle(quest, subtitle);
        if (Double.compare(quest.getX(), x) != 0) {
            quest.setX(x);
            changed = true;
        }
        if (Double.compare(quest.getY(), y) != 0) {
            quest.setY(y);
            changed = true;
        }
        for (TaskSpec task : tasks) {
            changed |= task.apply(file, quest);
        }
        for (RewardSpec reward : rewards) {
            changed |= reward.apply(file, quest);
        }
        return changed;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof QuestSpec spec && id == spec.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
