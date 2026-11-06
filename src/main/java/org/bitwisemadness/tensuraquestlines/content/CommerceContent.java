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
    private static final int OPEN_FOR_BUSINESS_CHAPTER = 11;
    private static final int REPEAT_CUSTOMERS_CHAPTER = 12;
    private static final int LEDGER_GROWS_CHAPTER = 13;
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

    /**
     * Sub-questline 2, Open for Business (HexaHexes/tensura-questlines#12):
     * a shop of one's own, and the first sale through it.
     *
     * <p>Placing and stocking a trader has no native signal, so that beat is a
     * checkmark, exactly as the sub-issue allows; the first sale is the real
     * signal and the sales milestone carries it. The milestone's value is
     * first-pass - Lightman's Currency defines coin values in its own
     * data-driven coin list, so the unit is its base coin and the number is
     * tuned in the game-testing round. The closing beat counts trades rather
     * than value, which needs no scale at all.</p>
     *
     * @return the Open for Business chapter spec
     */
    public static AuthoredChapter openForBusiness() {
        int chapter = OPEN_FOR_BUSINESS_CHAPTER;
        return AuthoredChapter.requiring(LIGHTMANS_CURRENCY, ChapterIds.chapter(chapter), "Open for Business", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "For Rent",
                "Find a spot and place a trader. The count starts at the first sale.",
                List.of(TaskSpec.checkmark(ChapterIds.task(chapter, 0, 0))),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 4))),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "Open Sign",
                "Stock the shelves: one real sale opens the shop.",
                List.of(TaskSpec.salesMilestone(ChapterIds.task(chapter, 1, 0), 100L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.EMERALD, 6))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "Grand Opening",
                "Five trades either way, then on to Repeat Customers.",
                List.of(TaskSpec.playerTransaction(ChapterIds.task(chapter, 2, 0), 5L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.EMERALD, 8)))
        ));
    }

    /**
     * Sub-questline 3, Repeat Customers (HexaHexes/tensura-questlines#13):
     * trading-partner tiers.
     *
     * <p>The type counts <em>distinct</em> counterparties and records a
     * partner only on an accepted credit, so repeat trades with the same
     * partner never inflate a tier - the thresholds mean what they say. Every
     * tier reads that one fact at a higher count, so the chapter needs no new
     * machinery and no value scale.</p>
     *
     * @return the Repeat Customers chapter spec
     */
    public static AuthoredChapter repeatCustomers() {
        int chapter = REPEAT_CUSTOMERS_CHAPTER;
        return AuthoredChapter.requiring(LIGHTMANS_CURRENCY, ChapterIds.chapter(chapter), "Repeat Customers", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "A Familiar Face",
                "One partner who came back is the start of a market.",
                List.of(TaskSpec.tradingPartner(ChapterIds.task(chapter, 0, 0), 2L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 6))),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "Regulars",
                "Five distinct counterparties: the shop has a name now.",
                List.of(TaskSpec.tradingPartner(ChapterIds.task(chapter, 1, 0), 5L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.EMERALD, 10))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "The Usual Order",
                "Ten partners deep. Next: The Ledger Grows.",
                List.of(TaskSpec.tradingPartner(ChapterIds.task(chapter, 2, 0), 10L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.EMERALD, 14)))
        ));
    }

    /**
     * Sub-questline 4, The Ledger Grows (HexaHexes/tensura-questlines#14):
     * mid milestones and the income layer.
     *
     * <p>Two authoring checks this chapter owed, both answered against the
     * pack rather than assumed:</p>
     *
     * <p><b>Bounty payouts are not observable.</b> The LC-bounty content is
     * pure data (bounty pools and decrees), and neither it nor Bountiful
     * references Lightman's Currency's classes at all - rewards arrive as
     * items, coins included, and item grants are not trades. So the income
     * beat counts sustained trade volume on our own types instead, and
     * Bountiful remains the system of record for bounties: complement, never
     * duplicated.</p>
     *
     * <p><b>Repeatables exist but stay unused here.</b> FTB Quests carries a
     * real repeatable flag (the {@code can_repeat} and {@code repeat_cooldown}
     * keys, authorable through {@code readData}); a repeatable economy quest
     * would repeat its rewards, which is an inflation loop in a line about
     * money, so the ladder shape stands deliberately.</p>
     *
     * <p>Milestone values are first-pass on Lightman's Currency's base coin,
     * tuned in the game-testing round.</p>
     *
     * @return the Ledger Grows chapter spec
     */
    public static AuthoredChapter ledgerGrows() {
        int chapter = LEDGER_GROWS_CHAPTER;
        return AuthoredChapter.requiring(LIGHTMANS_CURRENCY, ChapterIds.chapter(chapter), "The Ledger Grows", List.of(
            new QuestSpec(ChapterIds.quest(chapter, 0), 0.0D, 0.0D,
                "In The Black",
                "The shop pays for itself, several times over.",
                List.of(TaskSpec.salesMilestone(ChapterIds.task(chapter, 0, 0), 1000L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 0, 0), Items.EMERALD, 12))),
            new QuestSpec(ChapterIds.quest(chapter, 1), 1.5D, 0.0D,
                "Volume",
                "Trade stops being occasional and starts being a ledger.",
                List.of(TaskSpec.salesMilestone(ChapterIds.task(chapter, 1, 0), 10000L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 1, 0), Items.DIAMOND, 2))),
            new QuestSpec(ChapterIds.quest(chapter, 2), 3.0D, 0.0D,
                "Daily Trade",
                "Twenty-five trades in: the routine itself is the income. Bounties stay the board's business; next comes Tycoon.",
                List.of(TaskSpec.playerTransaction(ChapterIds.task(chapter, 2, 0), 25L)),
                List.of(RewardSpec.item(ChapterIds.reward(chapter, 2, 0), Items.EMERALD, 16)))
        ));
    }
}
