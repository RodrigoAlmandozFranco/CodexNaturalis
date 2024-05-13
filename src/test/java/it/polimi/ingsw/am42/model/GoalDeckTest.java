package it.polimi.ingsw.am42.model;


import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.decks.GoalDeck;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the GoalDeck class
 * @see GoalDeck
 * @author Rodrigo Almandoz Franco
 */


class GoalDeckTest {

    @org.junit.jupiter.api.Test
    void getTop() {
        GoalCard card = new GoalCard(1,null, null);
        GoalDeck deck = new GoalDeck();
        deck.addCard(card);
        assertEquals(card, deck.getTop());
    }

    @org.junit.jupiter.api.Test
    void Remove() {
        GoalCard card = new GoalCard(1, null, null);
        GoalDeck deck = new GoalDeck();
        deck.addCard(card);
        deck.remove();
        assertTrue(deck.finished());
    }
}
