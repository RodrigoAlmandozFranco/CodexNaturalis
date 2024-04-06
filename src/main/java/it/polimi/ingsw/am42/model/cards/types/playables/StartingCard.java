package it.polimi.ingsw.am42.model.cards.types.playables;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Resource;

import java.util.List;

/**
 * This class represents a Starting Card
 * It extends PlayableCard
 * @see PlayableCard
 * @author Rodrigo Almandoz Franco
 */

public class StartingCard extends PlayableCard {
    List<Resource> resources;


    public StartingCard(int id, Front front, Back back, List<Resource> res) {
        super(id, front, back);
        resources = res;
    }

    public List<Resource> getResources() {
        return resources;
    }
}
