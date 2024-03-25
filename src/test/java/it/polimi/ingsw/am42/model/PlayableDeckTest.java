package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.ResourceCard;
import it.polimi.ingsw.am42.model.decks.PlayableDeck;
import it.polimi.ingsw.am42.model.enumeration.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the PlayableDeck class
 * @see PlayableDeck
 * @author Rodrigo Almandoz Franco
*/


class PlayableDeckTest {

    @org.junit.jupiter.api.Test
    void getTop() {
        Front front = new Front("front", null, Color.RED, null, null);
        Back back = new Back("back", null, Color.RED, null);
        PlayableCard card = new ResourceCard(1, front, back);
        PlayableDeck deck = new PlayableDeck();
        deck.addCard(card);
        assertEquals(card, deck.getTop());
    }

    @org.junit.jupiter.api.Test
    void Remove() {
        Front front = new Front("front", null, Color.RED, null, null);
        Back back = new Back("back", null, Color.RED, null);
        PlayableCard card = new ResourceCard(1, front, back);
        PlayableDeck deck = new PlayableDeck();
        deck.addCard(card);
        deck.remove();
        assertTrue(deck.finished());
    }
}
