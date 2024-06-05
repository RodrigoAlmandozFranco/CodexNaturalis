package it.polimi.ingsw.am42.model.decks;

import it.polimi.ingsw.am42.model.cards.types.GoalCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a deck
 * It can be shuffled
 * It can be drawn from the top
 * It can be checked if it's finished
 * @see GoalCard
 * @see it.polimi.ingsw.am42.model.cards.types.PlayableCard
 * @see GoalDeck
 * @see PlayableDeck
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */


public abstract class Deck<T> implements Serializable, Iterable<T>{
    private List<T> deck;

    public Deck() {
        deck = new ArrayList<>();
    }

    /**
     * Shuffles the deck
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * Returns the top card of the deck
     * @return the top card of the deck
     */
    public T getTop() {
        return deck.getFirst();
    }

    /**
     * Draws the top card of the deck
     * @return the top card of the deck
     */
    public T pop() {
        T c = deck.getFirst();
        remove();
        return c;
    }

    /**
     * Checks if the deck is finished
     * @return true if the deck is empty, false otherwise
     */
    public boolean finished() {
        return deck.isEmpty();
    }

    /**
     * Removes the top card of the deck
     */
    public void remove() {
        deck.removeFirst();
    }

    /**
     * Adds a card to the deck
     * @param card the card to be added
     */
    public void addCard(T card) {
        deck.add(card);
    }

    public Iterator<T> iterator() {
        return deck.iterator();
    }
}