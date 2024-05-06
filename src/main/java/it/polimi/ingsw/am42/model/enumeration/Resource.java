package it.polimi.ingsw.am42.model.enumeration;

/**
 * Enumeration of the resources in the game
 * @author Rodrigo Almandoz Franco
 */

public enum Resource {
    PLANTKINGDOM,
    ANIMALKINGDOM,
    FUNGIKINGDOM,
    INSECTKINGDOM,
    QUILLOBJECT,
    INKWELLOBJECT,
    MANUSCRIPTOBJECT;


    public String toString() {
        return switch (this) {
            case PLANTKINGDOM -> "P";
            case ANIMALKINGDOM -> "A";
            case FUNGIKINGDOM -> "F";
            case INSECTKINGDOM -> "I";
            case QUILLOBJECT -> "Q";
            case INKWELLOBJECT -> "I";
            case MANUSCRIPTOBJECT -> "M";
            default -> "?";
        };
    }
}

