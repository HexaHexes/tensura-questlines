package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import java.util.List;
import java.util.Objects;
import net.neoforged.fml.ModList;

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
 * <p>A chapter may declare the optional content systems it reads
 * ({@link #requiredMods}): delivery skips it when any of them is absent, so
 * lines built on optional systems (Sympathetic Jobs, Lightman's Currency,
 * the dungeon suite) ship dormant instead of breaking the load.</p>
 *
 * @param id the quest-file object id
 * @param title the chapter title
 * @param requiredMods mod ids that must all be loaded for this chapter to
 *                     deliver; empty for content that only needs the mod's
 *                     own required dependencies
 * @param quests the chapter's quests, in authoring order
 */
public record AuthoredChapter(long id, String title, List<String> requiredMods, List<QuestSpec> quests) {

    /**
     * @param id the quest-file object id
     * @param title the chapter title
     * @param quests the chapter's quests
     * @return a chapter with no optional-system requirement
     */
    public static AuthoredChapter of(long id, String title, List<QuestSpec> quests) {
        return new AuthoredChapter(id, title, List.of(), quests);
    }

    /**
     * @param requiredMods mod ids that must all be loaded
     * @param id the quest-file object id
     * @param title the chapter title
     * @param quests the chapter's quests
     * @return a chapter that delivers only when every listed mod is present
     */
    public static AuthoredChapter requiring(List<String> requiredMods, long id, String title,
                                            List<QuestSpec> quests) {
        return new AuthoredChapter(id, title, List.copyOf(requiredMods), quests);
    }

    /**
     * @return true when every mod this chapter reads is loaded
     */
    public boolean isAvailable() {
        for (String modId : requiredMods) {
            if (!ModList.get().isLoaded(modId)) {
                return false;
            }
        }
        return true;
    }

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
