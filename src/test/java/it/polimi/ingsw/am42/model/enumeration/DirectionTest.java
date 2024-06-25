package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.model.structure.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void getPosition() {
        for (Direction d: Direction.values()) {
            if (d.equals(Direction.UPRIGHT))
                assertEquals(new Position(1, 0), d.getPosition());
            if (d.equals(Direction.UPLEFT))
                assertEquals(new Position(0, 1), d.getPosition());
            if (d.equals(Direction.DOWNRIGHT))
                assertEquals(new Position(0, -1), d.getPosition());
            if (d.equals(Direction.DOWNLEFT))
                assertEquals(new Position(-1, 0), d.getPosition());
        }
    }

    @Test
    void testGetPosition() {
        Position p = new Position(100, 100);
        for (Direction d: Direction.values()) {
            if (d.equals(Direction.UPRIGHT))
                assertEquals(new Position(p.getX()+1, p.getY()), d.getPosition(p));
            if (d.equals(Direction.UPLEFT))
                assertEquals(new Position(p.getX(), p.getY()+1), d.getPosition(p));
            if (d.equals(Direction.DOWNRIGHT))
                assertEquals(new Position(p.getX(), p.getY()-1), d.getPosition(p));
            if (d.equals(Direction.DOWNLEFT))
                assertEquals(new Position(p.getX()-1, p.getY()), d.getPosition(p));
        }
    }

    @Test
    void opposite() {
        for (Direction d: Direction.values()) {
            if (d.equals(Direction.UPRIGHT))
                assertEquals(Direction.DOWNLEFT,  d.opposite());
            if (d.equals(Direction.UPLEFT))
                assertEquals(Direction.DOWNRIGHT,  d.opposite());
            if (d.equals(Direction.DOWNRIGHT))
                assertEquals(Direction.UPLEFT,  d.opposite());
            if (d.equals(Direction.DOWNLEFT))
                assertEquals(Direction.UPRIGHT,  d.opposite());
        }
    }
}