package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.model.Position;
/**
 * Enumeration of the directions of the cards in the game
 * @author Rodrigo Almandoz Franco
 */
public enum Direction {
    UPRIGHT, // (+1, 0)
    UPLEFT, // (0, +1)
    DOWNRIGHT, // (0, -1)
    DOWNLEFT; // (-1, 0)


    /**
     * Returns the new position for every direction.
     *
     * @author Tommaso Crippa
     * @return Position, which corresponds to the new position for every direction.
     */
    public Position getPosition() {
        return switch (this) {
            case UPRIGHT -> new Position(1, 0);
            case UPLEFT -> new Position(0, 1);
            case DOWNRIGHT -> new Position(0, -1);
            case DOWNLEFT -> new Position(-1, 0);
        };
    }

    /**
     * Returns the new position starting from a position and going to a direction.
     *
     * @author Alessandro Di Maria
     * @return Position, which corresponds to the new position for every direction.
     * @param p the starting position
     */
    public Position getPosition(Position p ) {
        return switch (this) {
            case UPRIGHT -> new Position(p.getX() + 1, p.getY() );
            case UPLEFT -> new Position(p.getX() , p.getY() + 1);
            case DOWNRIGHT -> new Position(p.getX() , p.getY() - 1);
            case DOWNLEFT -> new Position(p.getX() - 1, p.getY() );
        };
    }

    /**
     * Returns the opposite direction of the current one
     *
     * @author Tommaso Crippa
     * @return opposite direction of the current one
     */
    public Direction opposite() {
        return switch (this) {
            case UPRIGHT -> DOWNLEFT;
            case UPLEFT -> DOWNRIGHT;
            case DOWNRIGHT -> UPLEFT;
            case DOWNLEFT -> UPRIGHT;
        };
    }
}
