package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.task.AdvancementTask;
import dev.ftb.mods.ftbquests.quest.task.CheckmarkTask;
import dev.ftb.mods.ftbquests.quest.task.DimensionTask;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import dev.ftb.mods.ftbquests.quest.task.KillTask;
import dev.ftb.mods.ftbquests.quest.task.StatTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import io.github.manasmods.tensura_ftb.tasks.AbilityTask;
import io.github.manasmods.tensura_ftb.tasks.AwakeningTask;
import io.github.manasmods.tensura_ftb.tasks.ExistenceTask;
import io.github.manasmods.tensura_ftb.tasks.RaceTask;
import io.github.manasmods.tensura_ftb.tasks.SpiritTask;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.bitwisemadness.tensuraquests.tasks.DungeonMilestoneTask;
import org.bitwisemadness.tensuraquests.tasks.DungeonTask;
import org.bitwisemadness.tensuraquests.tasks.EconomyTask;
import org.bitwisemadness.tensuraquests.tasks.FloorCompletionTask;
import org.bitwisemadness.tensuraquests.tasks.JobMasteryTask;
import org.bitwisemadness.tensuraquests.tasks.JobParticipationTask;
import org.bitwisemadness.tensuraquests.tasks.JobRequirement;
import org.bitwisemadness.tensuraquests.tasks.LayerCompletionTask;
import org.bitwisemadness.tensuraquests.tasks.MinibossDefeatTask;
import org.bitwisemadness.tensuraquests.tasks.MultiJobMasteryTask;
import org.bitwisemadness.tensuraquests.tasks.PlayerTransactionTask;
import org.bitwisemadness.tensuraquests.tasks.SalesMilestoneTask;
import org.bitwisemadness.tensuraquests.tasks.TradingPartnerTask;

/**
 * One authored task inside a quest.
 *
 * <p>Vanilla-side content uses FTB Quests' own built-ins (checkmark, item), so
 * it completes from player action alone. Everything else reads the native
 * task types registered by our ecosystem exactly as they are - the
 * never-duplicate rule - through {@link Tensura} payloads. Where a native
 * type's fields are private and setter-less, the payload is applied through
 * the task's own {@code readData} keys, which is the format FTB itself
 * persists.</p>
 *
 * <p>Each kind owns its construction: {@link Kind} carries one factory per
 * task type, so adding a type means adding a constant rather than growing a
 * dispatcher.</p>
 *
 * @param id the quest-file object id
 * @param kind which task to construct
 * @param stack the required stack for {@link Kind#ITEM} when it is known at
 *              authoring time (vanilla items)
 * @param count the required amount for {@link Kind#ITEM}
 * @param itemId the required item's registry name for {@link Kind#ITEM} when
 *               it belongs to another mod; resolved when the task is created,
 *               never when the spec is built, so a chapter whose mod is absent
 *               cannot break class loading
 * @param builtin the FTB built-in payload (dimension, advancement, statistic),
 *                or null for kinds that use one of the other payloads
 * @param tensura the native-type payload, or null for the vanilla kinds
 */
public record TaskSpec(long id, Kind kind, ItemStack stack, int count, ResourceLocation itemId,
                       Builtin builtin, Tensura tensura) {

    /**
     * A spec whose only payload is its kind and id, or a vanilla-side payload.
     *
     * @param id the quest-file object id
     * @param kind which task to construct
     * @param stack the required stack, for the vanilla item kind
     * @param count the required amount, for the vanilla item kind
     * @param itemId the modded item's registry name, for the item kind
     * @param tensura the native-type payload, for the native kinds
     */
    public TaskSpec(long id, Kind kind, ItemStack stack, int count, ResourceLocation itemId,
                    Tensura tensura) {
        this(id, kind, stack, count, itemId, null, tensura);
    }

    /**
     * The task kinds this layer may author, each knowing how to build itself.
     */
    public enum Kind {
        /** Completes when the player clicks the task. */
        CHECKMARK {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return new CheckmarkTask(spec.id(), quest);
            }
        },
        /** Built-in check: completes when the player is in the named dimension. */
        DIMENSION {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return new DimensionTask(spec.id(), quest)
                    .withDimension(ResourceKey.create(Registries.DIMENSION, spec.builtin().dimension()));
            }
        },
        /** Built-in check: completes when the player earns the named advancement. */
        ADVANCEMENT {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredAdvancement(quest);
            }
        },
        /** Built-in threshold on a statistic - vanilla or a mod's custom stat. */
        STAT {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredStat(quest);
            }
        },
        /** Built-in kill count on one entity type. */
        KILL {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredKill(quest);
            }
        },
        /** Completes when the player holds {@code count} of the item. */
        ITEM {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return new ItemTask(spec.id(), quest).setStackAndCount(spec.requiredStack(), spec.count());
            }
        },
        /** Native check - the player has settled a race. */
        RACE {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return new RaceTask(spec.id(), quest);
            }
        },
        /** Native check - the player's spirit has stirred. */
        SPIRIT {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return new SpiritTask(spec.id(), quest);
            }
        },
        /** Native check on one named ability's stage. */
        ABILITY {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredAbility(quest);
            }
        },
        /** Native threshold on one existence counter. */
        EXISTENCE {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredExistence(quest);
            }
        },
        /** Native check on the player's awakening. */
        AWAKENING {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredAwakening(quest);
            }
        },
        /** Native check that the player has joined and worked one named job. */
        JOB_PARTICIPATION {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredJobParticipation(quest);
            }
        },
        /** Native threshold on one named job's level. */
        JOB_MASTERY {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredJobMastery(quest);
            }
        },
        /** Native threshold across several jobs at once. */
        MULTI_JOB_MASTERY {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredMultiJobMastery(quest);
            }
        },
        /** Native count of completed trades the player took part in. */
        PLAYER_TRANSACTION {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredPlayerTransaction(quest);
            }
        },
        /** Native threshold on the coin value of the player's sales. */
        SALES_MILESTONE {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredSalesMilestone(quest);
            }
        },
        /** Native count of distinct counterparties the player traded with. */
        TRADING_PARTNER {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredTradingPartner(quest);
            }
        },
        /** Native check on entering one exact layer/floor of the dungeon. */
        FLOOR_COMPLETION {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredFloorCompletion(quest);
            }
        },
        /** Native threshold on reaching a dungeon layer or deeper. */
        DUNGEON_MILESTONE {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredDungeonMilestone(quest);
            }
        },
        /** Native count of credited miniboss defeats in the dungeon. */
        MINIBOSS_DEFEAT {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredMinibossDefeat(quest);
            }
        },
        /** Native count of completed dungeon layers. */
        LAYER_COMPLETION {
            @Override
            Task create(TaskSpec spec, Quest quest) {
                return spec.configuredLayerCompletion(quest);
            }
        };

        /**
         * @param spec the authored spec
         * @param quest the quest the task belongs to
         * @return the constructed, not-yet-created task
         */
        abstract Task create(TaskSpec spec, Quest quest);
    }

    /**
     * The FTB built-in payload: the registry name a built-in task reads.
     *
     * <p>FTB's built-ins keep their target in private fields with no setters,
     * so they are authored the way FTB persists them - through the task's own
     * {@code readData} keys. Every id here is pack data, validated against the
     * live registry rather than invented.</p>
     *
     * @param dimension the dimension id, for {@link Kind#DIMENSION}
     * @param advancement the advancement id, for {@link Kind#ADVANCEMENT}
     * @param stat the statistic id, for {@link Kind#STAT} - a vanilla
     *             statistic or a mod's own custom stat
     * @param entity the entity type id, for {@link Kind#KILL}
     * @param criterion the advancement criterion, or blank for the whole
     *                  advancement, for {@link Kind#ADVANCEMENT}
     */
    public record Builtin(ResourceLocation dimension, ResourceLocation advancement,
                          ResourceLocation stat, ResourceLocation entity, String criterion) {

        /** @param id the dimension id @return a dimension payload */
        public static Builtin dimension(ResourceLocation id) {
            return new Builtin(id, null, null, null, null);
        }

        /** @param id the advancement id @return a whole-advancement payload */
        public static Builtin advancement(ResourceLocation id) {
            return new Builtin(null, id, null, null, "");
        }

        /** @param id the statistic id @return a statistic payload */
        public static Builtin stat(ResourceLocation id) {
            return new Builtin(null, null, id, null, null);
        }

        /** @param id the entity type id @return a kill payload */
        public static Builtin kill(ResourceLocation id) {
            return new Builtin(null, null, null, id, null);
        }
    }

    /**
     * The native-type payload: which skill to watch and at what stage, which
     * existence counter to threshold, which awakening to require, which jobs
     * to measure, which trade fact to count, or which dungeon depth to reach.
     *
     * @param skill the ability to watch, for {@link Kind#ABILITY}
     * @param abilityStatus the ability stage required, for {@link Kind#ABILITY}
     * @param existence the counter to threshold, for {@link Kind#EXISTENCE}
     * @param existenceValue the threshold, for {@link Kind#EXISTENCE}
     * @param awakening the awakening required, for {@link Kind#AWAKENING}
     * @param job the Sympathetic Jobs payload, for the job kinds
     * @param economy the Lightman's Currency payload, for the economy kinds
     * @param dungeon the Infinite Dungeon payload, for the dungeon kinds
     */
    public record Tensura(ResourceLocation skill, AbilityTask.AcquisitionStatus abilityStatus,
                          ExistenceTask.ExistenceType existence, long existenceValue,
                          AwakeningTask.AwakeningStatus awakening, Job job, Economy economy,
                          Dungeon dungeon) {
    }

    /**
     * The Sympathetic Jobs payload: one job, or several at once.
     *
     * @param name the job to watch, for participation and mastery; null for
     *             the multi-job kind
     * @param level the level threshold, for mastery
     * @param requirements the per-job level requirements, for multi-job mastery
     */
    public record Job(String name, int level, List<JobRequirement> requirements) {

        /**
         * @param name the job name
         * @return a participation payload for that job
         */
        public static Job participation(String name) {
            return new Job(name, 0, List.of());
        }

        /**
         * @param name the job name
         * @param level the level threshold
         * @return a single-job mastery payload
         */
        public static Job mastery(String name, int level) {
            return new Job(name, level, List.of());
        }

        /**
         * @param requirements the per-job level requirements
         * @return a multi-job mastery payload
         */
        public static Job multi(List<JobRequirement> requirements) {
            return new Job(null, 0, List.copyOf(requirements));
        }
    }

    /**
     * The economy payload: a threshold on one of the three trade facts.
     *
     * @param target the threshold - a trade count, a coin value or a partner
     *               count, depending on the kind
     * @param allowTeamTrades whether a teammate's trade may credit this task;
     *                        false keeps the attribution per-player
     */
    public record Economy(long target, boolean allowTeamTrades) {

        /**
         * @param target the threshold
         * @return a per-player economy payload
         */
        public static Economy perPlayer(long target) {
            return new Economy(target, false);
        }
    }

    /**
     * The Infinite Dungeon payload: which dimension counts as the dungeon,
     * and which depth or visit the task measures.
     *
     * @param dimensions the dimension ids that count, as data validated
     *                   against the pack; never hardcoded in code
     * @param target the number of qualifying visits or layers
     * @param layer the exact layer, for floor completion
     * @param floor the exact floor, for floor completion
     * @param requiredLayer the depth threshold, at-least semantics
     */
    public record Dungeon(List<String> dimensions, long target, int layer, int floor, int requiredLayer) {

        /**
         * @param dimensions the dungeon dimension ids
         * @param layer the exact layer
         * @param floor the exact floor
         * @return a floor-visit payload
         */
        public static Dungeon floor(List<String> dimensions, int layer, int floor) {
            return new Dungeon(List.copyOf(dimensions), 1L, layer, floor, 0);
        }

        /**
         * @param dimensions the dungeon dimension ids
         * @param requiredLayer the depth threshold
         * @return a depth-threshold payload
         */
        public static Dungeon milestone(List<String> dimensions, int requiredLayer) {
            return new Dungeon(List.copyOf(dimensions), 1L, 0, 0, requiredLayer);
        }

        /**
         * @param dimensions the dungeon dimension ids
         * @param requiredLayer the layer whose minibosses count (exact match)
         * @param target how many credited defeats the task requires
         * @return a miniboss payload
         */
        public static Dungeon raid(List<String> dimensions, int requiredLayer, long target) {
            return new Dungeon(List.copyOf(dimensions), target, 0, 0, requiredLayer);
        }

        /**
         * @param dimensions the dungeon dimension ids
         * @param target how many layers must be completed
         * @return a layer-count payload
         */
        public static Dungeon layers(List<String> dimensions, long target) {
            return new Dungeon(List.copyOf(dimensions), target, 0, 0, 0);
        }
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
     * An item requirement for an item that is not a compile-time constant - a
     * pack mod's book, for example. The item is resolved from the registry
     * when the task is created, so the owning mod must be present (declared in
     * the chapter's required mods) or the lookup yields nothing usable.
     *
     * @param id the quest-file object id
     * @param itemId the registry name of the required item
     * @param count how many the player must hold
     * @return an item-requirement spec
     */
    public static TaskSpec item(long id, ResourceLocation itemId, int count) {
        return new TaskSpec(id, Kind.ITEM, ItemStack.EMPTY, count, itemId, null);
    }

    /**
     * A built-in dimension check: completes when the player is in the named
     * dimension. This is how the Depths' entrance beat is authored - the
     * dungeon is a dimension reached from the overworld, and its id is
     * authoring data validated against the pack, never hardcoded.
     *
     * @param id the quest-file object id
     * @param dimensionId the dimension's registry name
     * @return a dimension-check spec
     */
    public static TaskSpec dimension(long id, ResourceLocation dimensionId) {
        return new TaskSpec(id, Kind.DIMENSION, ItemStack.EMPTY, 0, null, Builtin.dimension(dimensionId), null);
    }

    /**
     * An advancement check: completes when the player earns the named
     * advancement. The id is pack data - the advancement must exist in the
     * pack (or its owning mod) or the task can never complete.
     *
     * @param id the quest-file object id
     * @param advancementId the advancement's registry name
     * @return an advancement-check spec
     */
    public static TaskSpec advancement(long id, ResourceLocation advancementId) {
        return new TaskSpec(id, Kind.ADVANCEMENT, ItemStack.EMPTY, 0, null,
            Builtin.advancement(advancementId), null);
    }

    /**
     * A statistic threshold: completes when the named statistic reaches
     * {@code value}. The stat may be a vanilla one or a mod's own custom stat;
     * either way the id is pack data, and a custom stat that is not registered
     * leaves the task uncompletable.
     *
     * @param id the quest-file object id
     * @param statId the statistic's registry name
     * @param value the threshold
     * @return a statistic spec
     */
    public static TaskSpec statistic(long id, ResourceLocation statId, int value) {
        return new TaskSpec(id, Kind.STAT, ItemStack.EMPTY, value, null, Builtin.stat(statId), null);
    }

    /**
     * A kill count on one entity type, read by FTB's own kill task - the
     * native kill infra, never rebuilt. The entity id is pack data, validated
     * against the live registry; attribution of indirect kills is the kill
     * mod's job, not this layer's.
     *
     * @param id the quest-file object id
     * @param entityId the entity type's registry name
     * @param count how many kills the task requires
     * @return a kill-count spec
     */
    public static TaskSpec kill(long id, ResourceLocation entityId, int count) {
        return new TaskSpec(id, Kind.KILL, ItemStack.EMPTY, count, null, Builtin.kill(entityId), null);
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
        return new TaskSpec(id, Kind.ABILITY, ItemStack.EMPTY, 0, null,
            new Tensura(skill, status, null, 0L, null, null, null, null));
    }

    /**
     * A native existence threshold: completes when the chosen counter reaches
     * {@code value}. Counters that track peaks (the {@code MAX_*} kinds) never
     * fall back, so the beat stays earned once reached.
     *
     * @param id the quest-file object id
     * @param existence the counter to watch
     * @param value the threshold
     * @return an existence-threshold spec
     */
    public static TaskSpec existence(long id, ExistenceTask.ExistenceType existence, long value) {
        return new TaskSpec(id, Kind.EXISTENCE, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, existence, value, null, null, null, null));
    }

    /**
     * A native awakening check.
     *
     * @param id the quest-file object id
     * @param awakening the awakening required, or {@code ANY}
     * @return an awakening-check spec
     */
    public static TaskSpec awakening(long id, AwakeningTask.AwakeningStatus awakening) {
        return new TaskSpec(id, Kind.AWAKENING, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, awakening, null, null, null));
    }

    /**
     * A native Sympathetic Jobs participation check: completes once the player
     * has genuinely worked the named job. The job name must be one of the
     * pack's twelve jobs, or the task can never complete (the type warns about
     * unknown names rather than failing).
     *
     * @param id the quest-file object id
     * @param job the job name, e.g. {@code miner}
     * @return a job-participation spec
     */
    public static TaskSpec jobParticipation(long id, String job) {
        return new TaskSpec(id, Kind.JOB_PARTICIPATION, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, null, Job.participation(job), null, null));
    }

    /**
     * A native job mastery threshold: completes when the named job's level
     * reaches {@code level}. The type is job-specific, so a ladder carries one
     * job per rung; the pack caps levels at 25, which the ladder must respect.
     *
     * @param id the quest-file object id
     * @param job the job name, e.g. {@code miner}
     * @param level the level threshold
     * @return a job-mastery spec
     */
    public static TaskSpec jobMastery(long id, String job, int level) {
        return new TaskSpec(id, Kind.JOB_MASTERY, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, null, Job.mastery(job, level), null, null));
    }

    /**
     * A native multi-job mastery threshold: completes when every listed job
     * reaches its listed level.
     *
     * @param id the quest-file object id
     * @param requirements the per-job level requirements
     * @return a multi-job mastery spec
     */
    public static TaskSpec multiJobMastery(long id, List<JobRequirement> requirements) {
        return new TaskSpec(id, Kind.MULTI_JOB_MASTERY, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, null, Job.multi(requirements), null, null));
    }

    /**
     * A native trade-count check: completes when the player has taken part in
     * {@code target} completed trades, as customer or as trader owner.
     *
     * @param id the quest-file object id
     * @param target the number of completed trades
     * @return a player-transaction spec
     */
    public static TaskSpec playerTransaction(long id, long target) {
        return new TaskSpec(id, Kind.PLAYER_TRANSACTION, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, null, null, Economy.perPlayer(target), null));
    }

    /**
     * A native sales check: completes when the coin value of the player's
     * completed sales reaches {@code target}. The unit is Lightman's
     * Currency's own value scale.
     *
     * @param id the quest-file object id
     * @param target the coin value threshold
     * @return a sales-milestone spec
     */
    public static TaskSpec salesMilestone(long id, long target) {
        return new TaskSpec(id, Kind.SALES_MILESTONE, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, null, null, Economy.perPlayer(target), null));
    }

    /**
     * A native partner check: completes when the player has completed trades
     * with {@code target} distinct counterparties. Repeat trades with the same
     * partner never inflate the count.
     *
     * @param id the quest-file object id
     * @param target the number of distinct partners
     * @return a trading-partner spec
     */
    public static TaskSpec tradingPartner(long id, long target) {
        return new TaskSpec(id, Kind.TRADING_PARTNER, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, null, null, Economy.perPlayer(target), null));
    }

    /**
     * A native floor-visit check: completes when the player enters the exact
     * layer and floor of the dungeon. The dimension list is authoring data,
     * validated against the pack's registry.
     *
     * @param id the quest-file object id
     * @param dimensions the dungeon dimension ids
     * @param layer the exact layer
     * @param floor the exact floor
     * @return a floor-completion spec
     */
    public static TaskSpec floorCompletion(long id, List<String> dimensions, int layer, int floor) {
        return new TaskSpec(id, Kind.FLOOR_COMPLETION, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, null, null, null, Dungeon.floor(dimensions, layer, floor)));
    }

    /**
     * A native depth check: completes when the player reaches
     * {@code requiredLayer} or deeper. The threshold is at-least semantics, so
     * it also completes for someone who goes straight past it.
     *
     * @param id the quest-file object id
     * @param dimensions the dungeon dimension ids
     * @param requiredLayer the depth threshold
     * @return a dungeon-milestone spec
     */
    public static TaskSpec dungeonMilestone(long id, List<String> dimensions, int requiredLayer) {
        return new TaskSpec(id, Kind.DUNGEON_MILESTONE, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, null, null, null, Dungeon.milestone(dimensions, requiredLayer)));
    }

    /**
     * A native raid check: completes when the player is credited with a
     * miniboss defeat at {@code requiredLayer} or deeper. Attribution is the
     * type's own job.
     *
     * @param id the quest-file object id
     * @param dimensions the dungeon dimension ids
     * @param requiredLayer the layer the defeat must happen in or deeper
     * @return a miniboss-defeat spec
     */
    public static TaskSpec minibossDefeat(long id, List<String> dimensions, int requiredLayer) {
        return minibossDefeat(id, dimensions, requiredLayer, 1L);
    }

    /**
     * A native raid-count check: completes when the player is credited with
     * {@code target} miniboss defeats at the exact {@code requiredLayer}. The
     * layer matcher is exact (the type's own rule), so deeper kills do not
     * count; credited kills accumulate across descents.
     *
     * @param id the quest-file object id
     * @param dimensions the dungeon dimension ids
     * @param requiredLayer the layer whose minibosses count
     * @param target how many credited defeats the task requires
     * @return a miniboss-defeat spec
     */
    public static TaskSpec minibossDefeat(long id, List<String> dimensions, int requiredLayer, long target) {
        return new TaskSpec(id, Kind.MINIBOSS_DEFEAT, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, null, null, null, Dungeon.raid(dimensions, requiredLayer, target)));
    }

    /**
     * A native layer-count check: completes when the player is credited with
     * {@code target} completed layers.
     *
     * @param id the quest-file object id
     * @param dimensions the dungeon dimension ids
     * @param target how many layers must be completed
     * @return a layer-completion spec
     */
    public static TaskSpec layerCompletion(long id, List<String> dimensions, long target) {
        return new TaskSpec(id, Kind.LAYER_COMPLETION, ItemStack.EMPTY, 0, null,
            new Tensura(null, null, null, 0L, null, null, null, Dungeon.layers(dimensions, target)));
    }

    /**
     * Creates the task, or refreshes the existing one where the type allows
     * it. Native types without setters are create-only: their configuration is
     * part of the content revision, not a value delivery can drift.
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
        Task task = kind.create(this, quest);
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

    private AdvancementTask configuredAdvancement(Quest quest) {
        AdvancementTask advancement = new AdvancementTask(id, quest);
        CompoundTag tag = new CompoundTag();
        tag.putString("advancement", builtin.advancement().toString());
        tag.putString("criterion", builtin.criterion());
        advancement.readData(tag, quest.getQuestFile().holderLookup());
        return advancement;
    }

    private StatTask configuredStat(Quest quest) {
        StatTask stat = new StatTask(id, quest);
        CompoundTag tag = new CompoundTag();
        tag.putString("stat", builtin.stat().toString());
        tag.putInt("value", count);
        stat.readData(tag, quest.getQuestFile().holderLookup());
        return stat;
    }

    private KillTask configuredKill(Quest quest) {
        KillTask kill = new KillTask(id, quest);
        CompoundTag tag = new CompoundTag();
        tag.putString("entity", builtin.entity().toString());
        tag.putLong("value", count);
        kill.readData(tag, quest.getQuestFile().holderLookup());
        return kill;
    }

    private JobParticipationTask configuredJobParticipation(Quest quest) {
        JobParticipationTask participation = new JobParticipationTask(id, quest);
        participation.setJob(tensura.job().name());
        return participation;
    }

    private JobMasteryTask configuredJobMastery(Quest quest) {
        JobMasteryTask mastery = new JobMasteryTask(id, quest);
        mastery.setJob(tensura.job().name());
        mastery.setRequiredLevel(tensura.job().level());
        return mastery;
    }

    private MultiJobMasteryTask configuredMultiJobMastery(Quest quest) {
        MultiJobMasteryTask multi = new MultiJobMasteryTask(id, quest);
        multi.setRequirements(tensura.job().requirements());
        return multi;
    }

    private PlayerTransactionTask configuredPlayerTransaction(Quest quest) {
        PlayerTransactionTask transaction = new PlayerTransactionTask(id, quest);
        applyEconomy(transaction);
        return transaction;
    }

    private SalesMilestoneTask configuredSalesMilestone(Quest quest) {
        SalesMilestoneTask sales = new SalesMilestoneTask(id, quest);
        applyEconomy(sales);
        return sales;
    }

    private TradingPartnerTask configuredTradingPartner(Quest quest) {
        TradingPartnerTask partner = new TradingPartnerTask(id, quest);
        applyEconomy(partner);
        return partner;
    }

    private void applyEconomy(EconomyTask task) {
        task.setTarget(tensura.economy().target());
        task.setAllowTeamTrades(tensura.economy().allowTeamTrades());
    }

    private FloorCompletionTask configuredFloorCompletion(Quest quest) {
        FloorCompletionTask floor = new FloorCompletionTask(id, quest);
        applyDungeon(floor);
        floor.setLayer(tensura.dungeon().layer());
        floor.setFloor(tensura.dungeon().floor());
        return floor;
    }

    private DungeonMilestoneTask configuredDungeonMilestone(Quest quest) {
        DungeonMilestoneTask milestone = new DungeonMilestoneTask(id, quest);
        applyDungeon(milestone);
        milestone.setRequiredLayer(tensura.dungeon().requiredLayer());
        return milestone;
    }

    private MinibossDefeatTask configuredMinibossDefeat(Quest quest) {
        MinibossDefeatTask miniboss = new MinibossDefeatTask(id, quest);
        applyDungeon(miniboss);
        miniboss.setRequiredLayer(tensura.dungeon().requiredLayer());
        miniboss.setTarget(tensura.dungeon().target());
        return miniboss;
    }

    private LayerCompletionTask configuredLayerCompletion(Quest quest) {
        LayerCompletionTask layers = new LayerCompletionTask(id, quest);
        applyDungeon(layers);
        layers.setTarget(tensura.dungeon().target());
        return layers;
    }

    private void applyDungeon(DungeonTask task) {
        task.setDimensions(tensura.dungeon().dimensions());
    }

    /**
     * Resolves the required stack, deferring a mod item's registry lookup to
     * creation time so building the spec never touches a registry the mod's
     * absence could leave unusable.
     *
     * @return the required stack
     */
    private ItemStack requiredStack() {
        if (itemId == null) {
            return stack;
        }
        return new ItemStack(BuiltInRegistries.ITEM.get(itemId), count);
    }

    private boolean update(Task existing) {
        if (kind != Kind.ITEM || !(existing instanceof ItemTask task)) {
            return false;
        }
        ItemStack required = requiredStack();
        ItemStack current = task.getItemStack();
        if (current.getItem() == required.getItem() && current.getCount() == count) {
            return false;
        }
        task.setStackAndCount(required, count);
        return true;
    }
}
