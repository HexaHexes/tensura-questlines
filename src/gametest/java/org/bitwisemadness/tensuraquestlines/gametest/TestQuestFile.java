package org.bitwisemadness.tensuraquestlines.gametest;

import dev.architectury.utils.Env;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.Player;

/**
 * Isolated quest file standing in for ServerQuestFile in GameTests, mirroring
 * the fixture the tensura-quests suites use.
 *
 * <p>Each test builds its own instance, so no test touches the run's real
 * quest file, and nothing is ever persisted - {@code getFolder} is not
 * overridden, so the base implementation has no folder to write to.</p>
 */
final class TestQuestFile extends BaseQuestFile {

    private final Env side;
    private final HolderLookup.Provider registries;

    TestQuestFile(Env side, HolderLookup.Provider registries) {
        this.side = side;
        this.registries = registries;
    }

    @Override
    public Env getSide() {
        return side;
    }

    @Override
    public HolderLookup.Provider holderLookup() {
        return registries;
    }

    @Override
    public boolean deleteObjects(List<Long> ids) {
        return false;
    }

    @Override
    public boolean isPlayerOnTeam(Player player, TeamData data) {
        return false;
    }

    @Override
    public String getLocale() {
        return "en_us";
    }

    @Override
    public boolean isDefaultPerTeamReward() {
        return false;
    }
}
