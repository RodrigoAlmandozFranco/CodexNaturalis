package it.polimi.ingsw.am42.model.decks;

import it.polimi.ingsw.am42.model.cards.Card;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a deck of playable cards
 * It has a list of playable cards
 * It can be shuffled
 * It can be drawn from the top
 * It can be checked if it's finished
 * @see PlayableCard
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class PlayableDeck implements Serializable, Iterable<PlayableCard>{
    private List<PlayableCard> deck;

    public PlayableDeck() {
        deck = new ArrayList<>();
    }
    public void shuffle() {
        Collections.shuffle(deck);
    }
    public PlayableCard getTop() {
        return deck.getFirst();
    }
    public boolean finished() {
        return deck.isEmpty();
    }

    public void remove() {
        deck.removeFirst();
    }

    public void addCard(PlayableCard card) {
        deck.add(card);
    }

    public PlayableCard pop() {
        PlayableCard c = deck.getFirst();
        remove();
        return c;
    }

    @Override
    public Iterator<PlayableCard> iterator() {
        return deck.iterator();
    }
}