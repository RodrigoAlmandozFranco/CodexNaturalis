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
 * @see Deck
 * @see PlayableCard
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class PlayableDeck extends Deck<PlayableCard> {
    public PlayableDeck() {
        super();
    }
}
