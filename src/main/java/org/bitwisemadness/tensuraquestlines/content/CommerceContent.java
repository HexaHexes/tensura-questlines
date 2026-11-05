package org.bitwisemadness.tensuraquestlines.content;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.bitwisemadness.tensuraquestlines.delivery.AuthoredChapter;
import org.bitwisemadness.tensuraquestlines.delivery.QuestSpec;
import org.bitwisemadness.tensuraquestlines.delivery.RewardSpec;
import org.bitwisemadness.tensuraquestlines.delivery.TaskSpec;

/**
 * Major questline 3, Commerce (HexaHexes/tensura-quests#31): coin is the
 * language of Tempest, and this line walks a player from buyer to merchant to
 * power broker.
 *
 * <p>The line reads the Lightman's Currency task family, so every chapter
 * declares {@code lightmanscurrency} in its required mods: the questlines are
 * dormant wherever that system is absent. The overlap audit the major asked
 * for is clean - {@code lc_ftb_hook} is entirely FTB-Chunks-claiming-via-LC
 * (claim ports, refunds, force-load takeover, marketplace access) and adds no
 * quest tasks or rewards, so nothing in this line duplicates it.</p>
 */
public final class CommerceContent {

    private static final int FIRST_COIN_CHAPTER = 10;
    private static final List<String> LIGHTMANS_CURRENCY = List.of("lightmanscurrency");
    private static final ResourceLocation BASIC_WALLET =
        ResourceLocation.parse("lightmanscurrency:wallet_leather");

    private CommerceContent() {
    }

    /**
     * Sub-questline 1, First Coin (HexaHexes/tensura-questlines#11): wallets
     * and the first trade.
     *
     * <p>The beats use the trade-count fact deliberately rather than a sales
     * value: "made your first trade" needs no knowledge of the currency's
     * value scale, and both sides of a trade credit their own player, so
     * buying counts as much as selling. The wallet item is resolved from the
     * mod's registry when the task is created.</p>
     *
     * @return the First Coin chapter spec
     */
    public static AuthoredChapter firstCoin() {
        int chapter = FIRST_COIN_CHAPTER;
        return AuthoredChapter.requiring(LIGHTMANS_CURRENCY, ChapterIds.chapter(chapter), "First Coin", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "Need Change?",
                "Nothing happens in Tempest without a wallet.",
                List.of(TaskSpec.item(ChapterIds.task(chapter, 0, 0), BASIC_WALLET, 1)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 3))),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "First Purchase",
                "Buy something from anyone, or sell something to anyone. Both count.",
                List.of(TaskSpec.playerTransaction(ChapterIds.task(chapter, 1, 0), 1L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.EMERALD, 5))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "Small Change",
                "Three trades in, the habit sticks. Next: Open for Business.",
                List.of(TaskSpec.playerTransaction(ChapterIds.task(chapter, 2, 0), 3L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.EMERALD, 8)))
        ));
    }
}
