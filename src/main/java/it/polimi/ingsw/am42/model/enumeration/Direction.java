package it.polimi.ingsw.am42.model.enumeration;
/**
 * Enumeration of the directions of the cards in the game
 * @author Rodrigo Almandoz Franco
 */
public enum Direction {
    UPRIGHT ("UPRIGHT"),
    UPLEFT ("UPLEFT"),
    DOWNRIGHT ("DOWNRIGHT"),
    DOWNLEFT ("DOWNLEFT");

    private final String direction;

    Direction(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
