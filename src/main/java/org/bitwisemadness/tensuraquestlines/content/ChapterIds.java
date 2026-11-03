package org.bitwisemadness.tensuraquestlines.content;

/**
 * Id allocation for authored content.
 *
 * <p>Every object id is derived from its chapter number, so ids are stable
 * across releases - player progression keys on them - and collision-free by
 * construction. The base sits far above the range FTB Quests generates for
 * editor-created objects; each chapter owns a {@code 0x1000} block split
 * into quests ({@code 0x100}), tasks ({@code 0x400}) and rewards
 * ({@code 0x800}), with sixteen slots per quest in the latter two.</p>
 */
public final class ChapterIds {

    private static final long BASE = 0x7E51_0000L;
    private static final long CHAPTER_STEP = 0x1000L;

    private ChapterIds() {
    }

    /**
     * @param chapter the chapter number within the content set
     * @return the chapter's object id
     */
    public static long chapter(int chapter) {
        return BASE + chapter * CHAPTER_STEP;
    }

    /**
     * @param chapter the chapter number
     * @param quest the quest index within the chapter
     * @return the quest's object id
     */
    public static long quest(int chapter, int quest) {
        return chapter(chapter) + 0x100L + quest;
    }

    /**
     * @param chapter the chapter number
     * @param quest the quest index within the chapter
     * @param task the task index within the quest
     * @return the task's object id
     */
    public static long task(int chapter, int quest, int task) {
        return chapter(chapter) + 0x400L + quest * 0x10L + task;
    }

    /**
     * @param chapter the chapter number
     * @param quest the quest index within the chapter
     * @param reward the reward index within the quest
     * @return the reward's object id
     */
    public static long reward(int chapter, int quest, int reward) {
        return chapter(chapter) + 0x800L + quest * 0x10L + reward;
    }
}
