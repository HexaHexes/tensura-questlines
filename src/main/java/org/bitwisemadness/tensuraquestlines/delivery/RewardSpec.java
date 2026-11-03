package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.ItemReward;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * One authored reward on a quest: a plain item grant.
 *
 * <p>Rewards are create-only. FTB Quests exposes no setters on
 * {@link ItemReward}, and a reward that was already granted is claim state,
 * so replacing it would be wrong anyway; changing a reward means changing its
 * id, which is a deliberate content revision.</p>
 *
 * @param id the quest-file object id
 * @param stack the granted stack
 * @param count how many are granted
 */
public record RewardSpec(long id, ItemStack stack, int count) {

    /**
     * @param id the quest-file object id
     * @param item the granted item
     * @param count how many are granted
     * @return an item-grant spec
     */
    public static RewardSpec item(long id, Item item, int count) {
        return new RewardSpec(id, new ItemStack(item, count), count);
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
        ItemReward reward = new ItemReward(id, quest, stack, count);
        reward.onCreated();
        return true;
    }
}
