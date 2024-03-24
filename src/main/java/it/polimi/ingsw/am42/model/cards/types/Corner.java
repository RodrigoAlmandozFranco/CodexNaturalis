package it.polimi.ingsw.am42.model.cards.types;

import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;


/**
 * This class represents a corner of a face.
 * @author Tommaso Crippa
 */

public class Corner {
    private final Resource resource;
    private CornerState state;
    private final Direction direction;

    public Corner(Resource resource, CornerState state, Direction direction) {
        this.resource = resource;
        this.state = state;
        this.direction = direction;
    }

    /**
     * Returns the resource inside the corner, if visible.
     *
     * @author Tommaso Crippa
     * @return resource, if state is open. If state is closed returns null
     */
    public Resource getResource() {
        if (state.equals(CornerState.OPEN))
            return resource;
        else
            return null;
    }

    /**
     * Returns the state of the corner
     *
     * @author Tommaso Crippa
     * @return the state of the corner
     */
    public CornerState getState() {
        return state;
    }

    /**
     * Makes the resource unavailable by closing the corner
     *
     * @author Tommaso Crippa
     */
    public void closeCorner() {
        this.state = CornerState.CLOSED;
    }

    /**
     * Returns the direction of the corner
     *
     * @author Tommaso Crippa
     * @return the direction of the corner
     */
    public Direction getDirection() {
        return direction;
    }

}
