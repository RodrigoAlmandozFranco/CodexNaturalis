package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.structure.Position;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Position
 * @see Position
 * @author Rodrigo Almandoz Franco
 */

class PositionTest {

        @org.junit.jupiter.api.Test
        void getX() {
            Position position = new Position(1, 2);
            assertEquals(1, position.getX());
        }

        @org.junit.jupiter.api.Test
        void getY() {
            Position position = new Position(1, 2);
            assertEquals(2, position.getY());
        }

        @org.junit.jupiter.api.Test
        void testEquals() {
            Position position1 = new Position(1, 2);
            Position position2 = new Position(1, 2);
            assertEquals(position1, position2);
        }

        @org.junit.jupiter.api.Test
        void testHashCode() {
            Position position1 = new Position(1, 2);
            Position position2 = new Position(1, 2);
            assertEquals(position1.hashCode(), position2.hashCode());
        }

}