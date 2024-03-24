package it.polimi.ingsw.am42.model.cards.types.playables;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.enumeration.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the gold card
 * @see GoldCard
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

class GoldCardTest {

        @org.junit.jupiter.api.Test
        void getFront() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            GoldCard card = new GoldCard(1, front, back);
            assertEquals(front, card.getFront());
        }

        @org.junit.jupiter.api.Test
        void getBack() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            GoldCard card = new GoldCard(1, front, back);
            assertEquals(back, card.getBack());
        }

        @org.junit.jupiter.api.Test
        void setVisibility() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            GoldCard card = new GoldCard(1, front, back);
            card.setVisibility(true);
            assertTrue(card.getVisibility());
        }

        @org.junit.jupiter.api.Test
        void getVisibility() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            GoldCard card = new GoldCard(1, front, back);
            assertFalse(card.getVisibility());
        }
}
