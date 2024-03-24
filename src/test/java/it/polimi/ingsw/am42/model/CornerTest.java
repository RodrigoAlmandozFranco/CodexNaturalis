package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CornerTest {

    @Test
    void getResource() {
        Corner c = new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT);

        assertEquals(Resource.ANIMALKINGDOM, c.getResource());

        c = new Corner(Resource.ANIMALKINGDOM, CornerState.CLOSED, Direction.UPLEFT);

        assertNull(c.getResource());
    }

    @Test
    void getState() {
        Corner c = new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT);

        assertEquals(CornerState.OPEN, c.getState());
    }

    @Test
    void closeCorner() {
        Corner c = new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT);

        c.closeCorner();

        assertEquals(CornerState.CLOSED, c.getState());
    }

    @Test
    void getDirection() {
        Corner c = new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT);

        assertEquals(Direction.UPLEFT, c.getDirection());

    }
}