package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.ItemReward;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.bitwisemadness.tensuraquests.rewards.UnlockJobSlotReward;

/**
 * One authored reward on a quest.
 *
 * <p>Two kinds exist: a plain item grant, and the Sympathetic Jobs slot
 * unlock - our own reward type, which raises the claiming player's job-slot
 * entitlement through Sympathetic Jobs' own write path (SJ owns slot truth
 * and clamps the result).</p>
 *
 * <p>Rewards are create-only. FTB Quests exposes no setters on
 * {@link ItemReward}, and a reward that was already granted is claim state,
 * so replacing it would be wrong anyway; changing a reward means changing its
 * id, which is a deliberate content revision.</p>
 *
 * @param id the quest-file object id
 * @param kind which reward to construct
 * @param stack the granted stack, for {@link Kind#ITEM}
 * @param count how many are granted, for {@link Kind#ITEM}
 * @param slots how many job slots are unlocked, for {@link Kind#UNLOCK_JOB_SLOT}
 */
public record RewardSpec(long id, Kind kind, ItemStack stack, int count, int slots) {

    /**
     * The reward kinds this layer may author.
     */
    public enum Kind {
        /** Grants {@code count} of an item. */
        ITEM,
        /** Native reward - unlocks Sympathetic Jobs slots for the claimer. */
        UNLOCK_JOB_SLOT
    }

    /**
     * @param id the quest-file object id
     * @param item the granted item
     * @param count how many are granted
     * @return an item-grant spec
     */
    public static RewardSpec item(long id, Item item, int count) {
        return new RewardSpec(id, Kind.ITEM, new ItemStack(item, count), count, 0);
    }

    /**
     * A native job-slot unlock. Sympathetic Jobs clamps the effective limit
     * to its own job count, so the ladder stays well inside that bound.
     *
     * @param id the quest-file object id
     * @param slots how many slots to unlock (the type's minimum is 1)
     * @return a slot-unlock spec
     */
    public static RewardSpec unlockJobSlot(long id, int slots) {
        return new RewardSpec(id, Kind.UNLOCK_JOB_SLOT, ItemStack.EMPTY, 0, slots);
    }

    /**
     * @param file the quest file being delivered into
     * @param quest the quest this reward belongs to
     * @return true when the reward was created
     */
    boolean apply(BaseQuestFile file, Quest quest) {
        if (file.getReward(id) != null) {
            return false;
        }
        create(quest);
        return true;
    }

    private void create(Quest quest) {
        if (kind == Kind.UNLOCK_JOB_SLOT) {
            UnlockJobSlotReward unlock = new UnlockJobSlotReward(id, quest);
            unlock.setSlots(slots);
            unlock.onCreated();
            return;
        }
        ItemReward reward = new ItemReward(id, quest, stack, count);
        reward.onCreated();
    }
}
