package it.polimi.ingsw.am42.model.structure;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a position in the game board.
 * @author Rodrigo Almandoz Franco
 */

public class Position implements Serializable {
    private final int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * This method returns the x coordinate of the position.
     * @return x coordinate of the position.
     */
    public int getX() {
        return x;
    }
    /**
     * This method returns the y coordinate of the position.
     * @return y coordinate of the position.
     */
    public int getY() {
        return y;
    }
    /**
     * This method checks if two positions are equal.
     * @param obj the object to compare with.
     * @return true if the positions are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;

        Position other = (Position) obj;
        return this.x == other.x && this.y == other.y;
    }

    /**
     * This method returns the hash code of the position.
     * @return the hash code of the position.
     */
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
