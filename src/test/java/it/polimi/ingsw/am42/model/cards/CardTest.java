package it.polimi.ingsw.am42.model.cards;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class represents a Card
 * It has an id
 * @see it.polimi.ingsw.am42.model.cards.Card
 * @author Rodrigo Almandoz Franco
 */

class CardTest {

        @org.junit.jupiter.api.Test
        void getId() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            Card card = new PlayableCard(1, front, back);
            assertEquals(1, card.getId());
        }
}