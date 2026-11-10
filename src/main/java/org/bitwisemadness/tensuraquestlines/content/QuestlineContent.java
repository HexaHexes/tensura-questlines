package org.bitwisemadness.tensuraquestlines.content;

import java.util.List;
import org.bitwisemadness.tensuraquestlines.delivery.AuthoredChapter;

/**
 * The registry of authored chapters - everything this mod delivers.
 *
 * <p>Content is code. A chapter becomes part of the pack by being listed
 * here and by existing as a spec factory in its line's content class; there
 * are no content files anywhere. New lines append their chapters to this
 * list, and the delivery layer picks them up on the next boot.</p>
 */
public final class QuestlineContent {

    private static final List<AuthoredChapter> CHAPTERS = List.of(
        ReincarnationContent.firstSteps(),
        ReincarnationContent.raceAndNature(),
        ReincarnationContent.firstSkills(),
        ReincarnationContent.awakenings(),
        ProfessionsContent.firstJob(),
        ProfessionsContent.competence(),
        ProfessionsContent.widerBench(),
        ProfessionsContent.doubleLife(),
        ProfessionsContent.masterOfTrades(),
        CommerceContent.firstCoin(),
        CommerceContent.openForBusiness(),
        CommerceContent.repeatCustomers(),
        CommerceContent.ledgerGrows(),
        CommerceContent.tycoon(),
        DepthsContent.theOpening(),
        DepthsContent.raiders(),
        DepthsContent.guardians(),
        TrialsContent.spellcraft()
    );

    private QuestlineContent() {
    }

    /**
     * @return every authored chapter, in delivery order
     */
    public static List<AuthoredChapter> chapters() {
        return CHAPTERS;
    }
}
