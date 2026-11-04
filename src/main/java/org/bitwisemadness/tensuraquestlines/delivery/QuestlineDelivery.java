package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import net.minecraft.gametest.framework.GameTestServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.bitwisemadness.tensuraquestlines.TensuraQuestlines;
import org.bitwisemadness.tensuraquestlines.content.QuestlineContent;

/**
 * Delivers the authored chapters into the live quest file.
 *
 * <p>FTB Quests 2101 has no datapack ingestion - verified for
 * HexaHexes/tensura-quests#27 - so chapters are built programmatically with
 * the same sequence FTB's own {@code /ftbquests generate_chapter} uses. The
 * first tick after the quest file exists is the delivery point: FTB creates
 * and loads that file during server startup, so a tick-based hook needs no
 * assumptions about listener ordering. Delivery runs once per server session
 * and is idempotent across sessions.</p>
 *
 * <p>Persistence goes through FTB's normal save cycle: delivery only marks
 * the file dirty, exactly as an editor edit does, and the world save or
 * server stop writes it. Writing mid-session is deliberately avoided - a
 * fresh instance has no quest folder yet, and a startup-time write fights
 * FTB's own file watching while the server is still coming up. Because
 * content is code and delivery re-creates anything missing, nothing is ever
 * lost to an early crash.</p>
 *
 * <p>GameTest servers are skipped: the harness is not a game. Its batch loop
 * runs unthrottled against the live quest file, and a delivery that creates
 * objects there wedges the batch with no error (observed twice, with the
 * no-mutation run passing) - so the suite exercises delivery through its own
 * isolated quest file instead. Production servers deliver normally.</p>
 *
 * <p>Delivery is per-chapter fault-isolated and availability-gated: a chapter
 * whose optional content system is absent is skipped (dormant content), and a
 * chapter that throws is logged and skipped so one broken or unregistered
 * family can never take the rest of the questlines down with it.</p>
 */
@EventBusSubscriber(modid = TensuraQuestlines.MOD_ID)
public final class QuestlineDelivery {

    /** Latches per server session; cleared on stop so single-player worlds redeliver. */
    private static boolean delivered;

    private QuestlineDelivery() {
    }

    /**
     * @param event the server tick
     */
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (delivered || event.getServer() instanceof GameTestServer) {
            return;
        }
        ServerQuestFile.getInstance().ifPresent(QuestlineDelivery::deliver);
    }

    /**
     * @param event the server stopping
     */
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        delivered = false;
    }

    private static void deliver(ServerQuestFile file) {
        delivered = true;
        boolean changed = false;
        int applied = 0;
        int skipped = 0;
        for (AuthoredChapter chapter : QuestlineContent.chapters()) {
            if (!chapter.isAvailable()) {
                skipped++;
                continue;
            }
            try {
                changed |= chapter.apply(file);
                applied++;
            } catch (RuntimeException error) {
                TensuraQuestlines.LOGGER.error("Chapter {} failed to deliver; the rest continue",
                    chapter.id(), error);
            }
        }
        if (changed) {
            file.markDirty();
        }
        TensuraQuestlines.LOGGER.info(
            "Delivered {} of {} authored chapter(s), {} skipped (optional system absent), changed={}",
            applied, QuestlineContent.chapters().size(), skipped, changed);
    }
}
