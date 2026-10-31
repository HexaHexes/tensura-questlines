package org.bitwisemadness.tensuraquestlines;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/**
 * Tensura Questlines entry point.
 *
 * <p>The mod ships authored FTB Quests chapters for the pack; it registers no
 * gameplay of its own. Keep {@link #MOD_ID} equal to {@code mod_id} in
 * {@code gradle.properties} (template rename checklist).</p>
 */
@Mod(TensuraQuestlines.MOD_ID)
public final class TensuraQuestlines {

    public static final String MOD_ID = "tensura_questlines";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TensuraQuestlines(IEventBus modBus) {
        LOGGER.info("Tensura Questlines initialized");
    }
}
