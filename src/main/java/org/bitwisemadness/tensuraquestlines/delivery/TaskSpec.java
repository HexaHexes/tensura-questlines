package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.task.CheckmarkTask;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import io.github.manasmods.tensura_ftb.tasks.AbilityTask;
import io.github.manasmods.tensura_ftb.tasks.AwakeningTask;
import io.github.manasmods.tensura_ftb.tasks.ExistenceTask;
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
 * <p>Vanilla-side content uses FTB Quests' own built-ins (checkmark, item),
 * so it completes from player action alone. Tensura-side content reads the
 * native Tensura Compat: FTB task types exactly as registered - the
 * never-duplicate rule - through {@link Tensura} payloads. Where a native
 * type's fields are private and setter-less, the payload is applied through
 * the task's own {@code readData} keys, which is the format FTB itself
 * persists.</p>
 *
 * @param id the quest-file object id
 * @param kind which task to construct
 * @param stack the required stack for {@link Kind#ITEM}
 * @param count the required amount for {@link Kind#ITEM}
 * @param tensura the native-type payload, or null for the vanilla kinds
 */
public record TaskSpec(long id, Kind kind, ItemStack stack, int count, Tensura tensura) {

    /**
     * The task kinds this layer may author.
     */
    public enum Kind {
        /** Completes when the player clicks the task. */
        CHECKMARK,
        /** Completes when the player holds {@code count} of the item. */
        ITEM,
        /** Native check - the player has settled a race. */
        RACE,
        /** Native check - the player's spirit has stirred. */
        SPIRIT,
        /** Native check on one named ability's stage. */
        ABILITY,
        /** Native threshold on one existence counter. */
        EXISTENCE,
        /** Native check on the player's awakening. */
        AWAKENING
    }

    /**
     * The native-type payload: which skill to watch and at what stage, which
     * existence counter to threshold, or which awakening to require.
     *
     * @param skill the ability to watch, for {@link Kind#ABILITY}
     * @param abilityStatus the ability stage required, for {@link Kind#ABILITY}
     * @param existence the counter to threshold, for {@link Kind#EXISTENCE}
     * @param existenceValue the threshold, for {@link Kind#EXISTENCE}
     * @param awakening the awakening required, for {@link Kind#AWAKENING}
     */
    public record Tensura(ResourceLocation skill, AbilityTask.AcquisitionStatus abilityStatus,
                          ExistenceTask.ExistenceType existence, long existenceValue,
                          AwakeningTask.AwakeningStatus awakening) {
    }

    /**
     * @param id the quest-file object id
     * @return a click-to-complete spec
     */
    public static TaskSpec checkmark(long id) {
        return new TaskSpec(id, Kind.CHECKMARK, ItemStack.EMPTY, 0, null);
    }

    /**
     * @param id the quest-file object id
     * @param item the required item
     * @param count how many the player must hold
     * @return an item-requirement spec
     */
    public static TaskSpec item(long id, Item item, int count) {
        return new TaskSpec(id, Kind.ITEM, new ItemStack(item, count), count, null);
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
            count, null);
    }

    /**
     * A native race check: completes when the player has settled a race.
     *
     * @param id the quest-file object id
     * @return a race-check spec
     */
    public static TaskSpec race(long id) {
        return new TaskSpec(id, Kind.RACE, ItemStack.EMPTY, 0, null);
    }

    /**
     * A native spirit check: completes when the player's spirit has stirred.
     *
     * @param id the quest-file object id
     * @return a spirit-check spec
     */
    public static TaskSpec spirit(long id) {
        return new TaskSpec(id, Kind.SPIRIT, ItemStack.EMPTY, 0, null);
    }

    /**
     * A native ability check on one named skill. The skill id must exist in
     * the pack (its skill index is the authority), or the task can never
     * complete.
     *
     * @param id the quest-file object id
     * @param skill the skill to watch, e.g. {@code tensura:fighter}
     * @param status the stage required - {@code LEARNT} or {@code MASTERED}
     * @return an ability-check spec
     */
    public static TaskSpec ability(long id, ResourceLocation skill, AbilityTask.AcquisitionStatus status) {
        return new TaskSpec(id, Kind.ABILITY, ItemStack.EMPTY, 0,
            new Tensura(skill, status, null, 0L, null));
    }

    /**
     * A native existence threshold: completes when the chosen counter
     * reaches {@code value}. Counters that track peaks (the {@code MAX_*}
     * kinds) never fall back, so the beat stays earned once reached.
     *
     * @param id the quest-file object id
     * @param existence the counter to watch
     * @param value the threshold
     * @return an existence-threshold spec
     */
    public static TaskSpec existence(long id, ExistenceTask.ExistenceType existence, long value) {
        return new TaskSpec(id, Kind.EXISTENCE, ItemStack.EMPTY, 0,
            new Tensura(null, null, existence, value, null));
    }

    /**
     * A native awakening check.
     *
     * @param id the quest-file object id
     * @param awakening the awakening required, or {@code ANY}
     * @return an awakening-check spec
     */
    public static TaskSpec awakening(long id, AwakeningTask.AwakeningStatus awakening) {
        return new TaskSpec(id, Kind.AWAKENING, ItemStack.EMPTY, 0,
            new Tensura(null, null, null, 0L, awakening));
    }

    /**
     * Creates the task, or refreshes the existing one where the type allows
     * it. Native types without setters are create-only: their configuration
     * is part of the content revision, not a value delivery can drift.
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
            task = configuredAbility(quest);
        } else if (kind == Kind.EXISTENCE) {
            task = configuredExistence(quest);
        } else if (kind == Kind.AWAKENING) {
            task = configuredAwakening(quest);
        } else {
            task = new CheckmarkTask(id, quest);
        }
        task.onCreated();
    }

    private AbilityTask configuredAbility(Quest quest) {
        AbilityTask ability = new AbilityTask(id, quest);
        CompoundTag tag = new CompoundTag();
        tag.putString("ability", tensura.skill().toString());
        tag.putString("status", tensura.abilityStatus().name());
        ability.readData(tag, quest.getQuestFile().holderLookup());
        return ability;
    }

    private ExistenceTask configuredExistence(Quest quest) {
        ExistenceTask existence = new ExistenceTask(id, quest);
        CompoundTag tag = new CompoundTag();
        tag.putString("existence", tensura.existence().name());
        existence.readData(tag, quest.getQuestFile().holderLookup());
        existence.setValue(tensura.existenceValue());
        return existence;
    }

    private AwakeningTask configuredAwakening(Quest quest) {
        AwakeningTask awakening = new AwakeningTask(id, quest);
        CompoundTag tag = new CompoundTag();
        tag.putString("status", tensura.awakening().name());
        awakening.readData(tag, quest.getQuestFile().holderLookup());
        return awakening;
    }

    private boolean update(Task existing) {
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
