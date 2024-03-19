package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a deck of cards
 * It has a list of cards
 * It can be shuffled
 * It can be drawn from the top
 * It can be checked if it's finished
 * @see Card
 * @author Rodrigo Almandoz Franco
 */

public class Deck {
    private final List<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
    }
    public void shuffle() {
        Collections.shuffle(deck);
    }
    public Card getTop() {
        return deck.getFirst();
    }
    public boolean finished() {
        return deck.isEmpty();
    }

    public void remove() {
        deck.removeFirst();
    }

    public void addCard(Card card) {
        deck.add(card);
    }
}
