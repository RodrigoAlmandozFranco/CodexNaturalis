package it.polimi.ingsw.am42.model.decks;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a deck of goal cards
 * It has a list of goal cards
 * It can be shuffled
 * It can be drawn from the top
 * It can be checked if it's finished
 * @see GoalCard
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class GoalDeck {
    private final List<GoalCard> deck;

    public GoalDeck() {
        deck = new ArrayList<>();
    }
    public void shuffle() {
        Collections.shuffle(deck);
    }
    public GoalCard getTop() {
        return deck.getFirst();
    }
    public boolean finished() {
        return deck.isEmpty();
    }

    public void remove() {
        deck.removeFirst();
    }

    public void addCard(GoalCard card) {
        deck.add(card);
    }
}