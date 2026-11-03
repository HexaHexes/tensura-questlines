package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import java.util.List;
import java.util.Objects;

/**
 * One authored chapter: the unit the content packages define and the
 * delivery layer applies to the live quest file.
 *
 * <p>Applying is idempotent. A chapter that already exists is refreshed in
 * place rather than recreated, because FTB Quests keys player progression to
 * object ids - recreating would wipe completions. Quests are added when
 * missing and updated when present; nothing is ever removed by delivery
 * (removals are a deliberate, manual content revision).</p>
 *
 * @param id the quest-file object id
 * @param title the chapter title
 * @param quests the chapter's quests, in authoring order
 */
public record AuthoredChapter(long id, String title, List<QuestSpec> quests) {

    /**
     * Creates the chapter, or refreshes it in place.
     *
     * @param file the quest file being delivered into
     * @return true when anything was created or changed
     */
    public boolean apply(BaseQuestFile file) {
        boolean changed = false;
        Chapter chapter = file.getChapter(id);
        if (chapter == null) {
            chapter = new Chapter(id, file, file.getDefaultChapterGroup());
            chapter.onCreated();
            changed = true;
        }
        changed |= TextSync.title(chapter, title);
        for (QuestSpec quest : quests) {
            changed |= quest.apply(file, chapter);
        }
        return changed;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof AuthoredChapter chapter && id == chapter.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
