package it.polimi.ingsw.am42.model.cards.types;

import it.polimi.ingsw.am42.model.Back;
import it.polimi.ingsw.am42.model.Front;
import it.polimi.ingsw.am42.model.enumeration.Color;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {
    /**
     * This class represents a Card
     * It has an id
     * @see it.polimi.ingsw.am42.model.cards.Card
     * @author Rodrigo Almandoz Franco
     */

        @org.junit.jupiter.api.Test
        void getFront() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            PlayableCard card = new PlayableCard(1, front, back);
            assertEquals(front, card.getFront());
        }

        @org.junit.jupiter.api.Test
        void getBack() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            PlayableCard card = new PlayableCard(1, front, back);
            assertEquals(back, card.getBack());
        }

        @org.junit.jupiter.api.Test
        void setVisibility() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            PlayableCard card = new PlayableCard(1, front, back);
            card.setVisibility(true);
            assertTrue(card.getVisibility());
        }

        @org.junit.jupiter.api.Test
        void getVisibility() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            PlayableCard card = new PlayableCard(1, front, back);
            assertFalse(card.getVisibility());
        }

}