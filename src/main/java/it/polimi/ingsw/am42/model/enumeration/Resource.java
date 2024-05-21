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

    public String fullName() {
        return switch (this) {
            case PLANTKINGDOM -> "PLANTKINGDOM";
            case ANIMALKINGDOM -> "ANIMALKINGDOM";
            case FUNGIKINGDOM -> "FUNGIKINGDOM";
            case INSECTKINGDOM -> "INSECTKINGDOM";
            case QUILLOBJECT -> "QUILLOBJECT";
            case INKWELLOBJECT -> "INKWELLOBJECT";
            case MANUSCRIPTOBJECT -> "MANUSCRIPTOBJECT";
            default -> "?";
        };
    }

    public Color resourceToColor() {
        return switch (this) {
            case PLANTKINGDOM -> Color.GREEN;
            case ANIMALKINGDOM -> Color.CYAN;
            case FUNGIKINGDOM -> Color.RED;
            case INSECTKINGDOM -> Color.PURPLE;
            default -> Color.WHITE;
        };
    }

}

