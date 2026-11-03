package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;

/**
 * In-place text updates for authored objects.
 *
 * <p>Delivery never replaces an object that already exists: player
 * progression is keyed by object id, so a rebuild would be destructive. Text
 * and thresholds are refreshed instead, which is what a content revision
 * almost always is.</p>
 */
final class TextSync {

    private TextSync() {
    }

    /**
     * @param object the authored object
     * @param title the desired raw title
     * @return true when the stored title changed
     */
    static boolean title(QuestObjectBase object, String title) {
        if (title.equals(object.getRawTitle())) {
            return false;
        }
        object.setRawTitle(title);
        return true;
    }

    /**
     * @param quest the authored quest
     * @param subtitle the desired raw subtitle
     * @return true when the stored subtitle changed
     */
    static boolean subtitle(Quest quest, String subtitle) {
        if (subtitle.equals(quest.getRawSubtitle())) {
            return false;
        }
        quest.setRawSubtitle(subtitle);
        return true;
    }
}
