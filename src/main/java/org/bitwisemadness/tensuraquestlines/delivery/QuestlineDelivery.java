package org.bitwisemadness.tensuraquestlines.delivery;

import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
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
        if (delivered) {
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
        try {
            boolean changed = false;
            for (AuthoredChapter chapter : QuestlineContent.chapters()) {
                changed |= chapter.apply(file);
            }
            if (changed) {
                file.markDirty();
            }
            TensuraQuestlines.LOGGER.info("Delivered {} authored chapter(s), changed={}",
                QuestlineContent.chapters().size(), changed);
        } catch (RuntimeException error) {
            TensuraQuestlines.LOGGER.error("Authored chapter delivery failed; content may be missing this session", error);
        }
    }
}
