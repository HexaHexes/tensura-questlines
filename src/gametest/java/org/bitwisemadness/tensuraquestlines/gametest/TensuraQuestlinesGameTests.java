package org.bitwisemadness.tensuraquestlines.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.bitwisemadness.tensuraquestlines.TensuraQuestlines;

/**
 * Smoke test: proves the mod loads and the GameTest harness runs.
 *
 * <p>Replace this with real tests for your mod's behaviour. Keep the
 * {@code expectedTests} count in {@code build.gradle} in sync with the number
 * of {@code @GameTest} methods here, or the harness fails the run.</p>
 */
@GameTestHolder(TensuraQuestlines.MOD_ID)
@PrefixGameTestTemplate(false)
public final class TensuraQuestlinesGameTests {

    private TensuraQuestlinesGameTests() {
    }

    /** The mod is loaded and the empty structure template resolves. */
    @GameTest(template = "empty")
    public static void mod_loads(GameTestHelper helper) {
        helper.succeed();
    }
}
