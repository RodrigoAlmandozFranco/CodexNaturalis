package it.polimi.ingsw.am42.model.decks;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a deck of goal cards
 * It has a list of goal cards
 * It can be shuffled
 * It can be drawn from the top
 * It can be checked if it's finished
 * @see GoalCard
 * @see Deck
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class GoalDeck extends Deck<GoalCard> {
    public GoalDeck() {
        super();
    }
}
