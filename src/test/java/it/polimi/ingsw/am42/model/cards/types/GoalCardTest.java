package it.polimi.ingsw.am42.model.cards.types;

import static org.junit.jupiter.api.Assertions.*;
/**
 * This class tests the GoalCard
 * @see GoalCard
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

class GoalCardTest {

        @org.junit.jupiter.api.Test
        void getEvaluator() {
            GoalCard goalCard = new GoalCard(1, null, null);
            assertNull(goalCard.getEvaluator());
        }
}