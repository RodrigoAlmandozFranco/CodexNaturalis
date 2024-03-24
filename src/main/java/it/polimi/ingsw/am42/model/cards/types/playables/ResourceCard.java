package it.polimi.ingsw.am42.model.cards.types.playables;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;

/**
 * This class represents a Resource Card
 * It extends PlayableCard
 * @see PlayableCard
 * @author Rodrigo Almandoz Franco
 */

public class ResourceCard extends PlayableCard {

    public ResourceCard(int id, Front front, Back back) {
        super(id,front, back);
    }

}
