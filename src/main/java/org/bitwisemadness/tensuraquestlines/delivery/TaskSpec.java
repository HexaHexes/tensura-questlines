package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.task.CheckmarkTask;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import io.github.manasmods.tensura_ftb.tasks.AbilityTask;
import io.github.manasmods.tensura_ftb.tasks.RaceTask;
import io.github.manasmods.tensura_ftb.tasks.SpiritTask;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * One authored task inside a quest.
 *
 * <p>Two FTB Quests built-ins cover the vanilla-side content: a
 * click-to-complete checkmark and an item requirement. Both complete from
 * player action alone, so no external intake source is needed and the
 * content works before any optional pack system is enabled. Domain task
 * types (counters, jobs, dungeons, economy) join the same shape when the
 * chapters that use them land.</p>
 *
 * @param id the quest-file object id
 * @param kind which built-in to construct
 * @param stack the required stack for {@link Kind#ITEM}
 * @param count the required amount for {@link Kind#ITEM}
 * @param skill the ability to watch, for {@link Kind#ABILITY} only
 * @param status the acquisition stage to require, for {@link Kind#ABILITY} only
 */
public record TaskSpec(long id, Kind kind, ItemStack stack, int count,
                       ResourceLocation skill, AbilityTask.AcquisitionStatus status) {

    /**
     * The task kinds this layer may author.
     */
    public enum Kind {
        /** Completes when the player clicks the task. */
        CHECKMARK,
        /** Completes when the player holds {@code count} of the item. */
        ITEM,
        /** Native Tensura Compat: FTB check - the player has settled a race. */
        RACE,
        /** Native Tensura Compat: FTB check - the player's spirit has stirred. */
        SPIRIT,
        /** Native Tensura Compat: FTB check on one named ability's stage. */
        ABILITY
    }

    /**
     * @param id the quest-file object id
     * @return a click-to-complete spec
     */
    public static TaskSpec checkmark(long id) {
        return new TaskSpec(id, Kind.CHECKMARK, ItemStack.EMPTY, 0, null, null);
    }

    /**
     * @param id the quest-file object id
     * @param item the required item
     * @param count how many the player must hold
     * @return an item-requirement spec
     */
    public static TaskSpec item(long id, Item item, int count) {
        return new TaskSpec(id, Kind.ITEM, new ItemStack(item, count), count, null, null);
    }

    /**
     * An item requirement for an item that is not a compile-time constant -
     * a pack mod's book, for example. The item is resolved from the registry
     * when the spec is built, so the owning mod must be present (declared as
     * a dependency) or the lookup yields nothing usable.
     *
     * @param id the quest-file object id
     * @param itemId the registry name of the required item
     * @param count how many the player must hold
     * @return an item-requirement spec
     */
    public static TaskSpec item(long id, ResourceLocation itemId, int count) {
        return new TaskSpec(id, Kind.ITEM, new ItemStack(BuiltInRegistries.ITEM.get(itemId), count),
            count, null, null);
    }

    /**
     * A native ability check on one named skill.
     *
     * <p>The ability task's fields are private with no setters, so the skill
     * and stage are authored the way FTB persists them: through
     * {@code readData} with the task's own {@code ability}/{@code status}
     * keys. The skill id must exist in the pack (its skill index is the
     * authority), or the task can never complete.</p>
     *
     * @param id the quest-file object id
     * @param skill the skill to watch, e.g. {@code tensura:fighter}
     * @param status the stage required - {@code LEARNT} or {@code MASTERED}
     * @return an ability-check spec
     */
    public static TaskSpec ability(long id, ResourceLocation skill, AbilityTask.AcquisitionStatus status) {
        return new TaskSpec(id, Kind.ABILITY, ItemStack.EMPTY, 0, skill, status);
    }

    /**
     * A native race check: completes when the player has settled a race.
     *
     * @param id the quest-file object id
     * @return a race-check spec
     */
    public static TaskSpec race(long id) {
        return new TaskSpec(id, Kind.RACE, ItemStack.EMPTY, 0, null, null);
    }

    /**
     * A native spirit check: completes when the player's spirit has stirred.
     *
     * @param id the quest-file object id
     * @return a spirit-check spec
     */
    public static TaskSpec spirit(long id) {
        return new TaskSpec(id, Kind.SPIRIT, ItemStack.EMPTY, 0, null, null);
    }

    /**
     * Creates the task, or refreshes the threshold of the existing one.
     *
     * @param file the quest file being delivered into
     * @param quest the quest this task belongs to
     * @return true when anything was created or changed
     */
    boolean apply(BaseQuestFile file, Quest quest) {
        Task existing = file.getTask(id);
        if (existing == null) {
            create(quest);
            return true;
        }
        return update(existing);
    }

    private void create(Quest quest) {
        Task task;
        if (kind == Kind.ITEM) {
            task = new ItemTask(id, quest).setStackAndCount(stack, count);
        } else if (kind == Kind.RACE) {
            task = new RaceTask(id, quest);
        } else if (kind == Kind.SPIRIT) {
            task = new SpiritTask(id, quest);
        } else if (kind == Kind.ABILITY) {
            AbilityTask ability = new AbilityTask(id, quest);
            CompoundTag tag = new CompoundTag();
            tag.putString("ability", skill.toString());
            tag.putString("status", status.name());
            ability.readData(tag, quest.getQuestFile().holderLookup());
            task = ability;
        } else {
            task = new CheckmarkTask(id, quest);
        }
        task.onCreated();
    }

    private boolean update(Task existing) {
        if (kind == Kind.ABILITY) {
            return false;
        }
        if (kind != Kind.ITEM || !(existing instanceof ItemTask task)) {
            return false;
        }
        ItemStack current = task.getItemStack();
        if (current.getItem() == stack.getItem() && current.getCount() == count) {
            return false;
        }
        task.setStackAndCount(stack, count);
        return true;
    }
}
