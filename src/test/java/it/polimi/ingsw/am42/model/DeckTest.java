package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.Card;
import it.polimi.ingsw.am42.model.cards.types.playables.ResourceCard;
import it.polimi.ingsw.am42.model.enumeration.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the Deck class
 * @see Deck
 * @author Rodrigo Almandoz Franco
*/


class DeckTest {

        @org.junit.jupiter.api.Test
        void getTop() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            Card card = new ResourceCard(1, front, back);
            Deck deck = new Deck();
            deck.addCard(card);
            assertEquals(card, deck.getTop());
        }

    @org.junit.jupiter.api.Test
    void Remove() {
        Front front = new Front("front", null, Color.RED, null, null);
        Back back = new Back("back", null, Color.RED, null);
        Card card = new ResourceCard(1, front, back);
        Deck deck = new Deck();
        deck.addCard(card);
        deck.remove();
        assertTrue(deck.finished());
    }
}
