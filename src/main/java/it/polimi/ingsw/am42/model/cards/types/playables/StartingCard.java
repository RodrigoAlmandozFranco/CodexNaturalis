package it.polimi.ingsw.am42.model.cards.types.playables;

import it.polimi.ingsw.am42.model.Back;
import it.polimi.ingsw.am42.model.Front;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;

/**
 * This class represents a Starting Card
 * It extends PlayableCard
 * @see PlayableCard
 * @author Rodrigo Almandoz Franco
 */

public class StartingCard extends PlayableCard {
    public StartingCard(int id, Front front, Back back) {
        super(id, front, back);
    }
}
