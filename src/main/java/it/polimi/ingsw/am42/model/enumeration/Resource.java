package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.view.tui.ColorChooser;

import java.io.Serializable;

/**
 * Enumeration of the resources in the game
 * @author Rodrigo Almandoz Franco
 */

public enum Resource implements Serializable {
    PLANTKINGDOM,
    ANIMALKINGDOM,
    FUNGIKINGDOM,
    INSECTKINGDOM,
    QUILLOBJECT,
    INKWELLOBJECT,
    MANUSCRIPTOBJECT;


    public String toString() {
        return switch (this) {
            case PLANTKINGDOM -> ColorChooser.GREEN + "P" + ColorChooser.RESET;
            case ANIMALKINGDOM -> ColorChooser.CYAN + "A" + ColorChooser.RESET;
            case FUNGIKINGDOM -> ColorChooser.RED +"F" + ColorChooser.RESET;
            case INSECTKINGDOM -> ColorChooser.PURPLE +"I" + ColorChooser.RESET;
            case QUILLOBJECT -> ColorChooser.WHITE + "Q" + ColorChooser.RESET;
            case INKWELLOBJECT -> ColorChooser.WHITE + "I" + ColorChooser.RESET;
            case MANUSCRIPTOBJECT -> ColorChooser.WHITE + "M" + ColorChooser.RESET;
            default -> "?";
        };
    }

    public String fullName() {
        return switch (this) {
            case PLANTKINGDOM -> ColorChooser.GREEN + "PLANTKINGDOM" + ColorChooser.RESET;
            case ANIMALKINGDOM -> ColorChooser.CYAN +"ANIMALKINGDOM" + ColorChooser.RESET;
            case FUNGIKINGDOM -> ColorChooser.RED +"FUNGIKINGDOM" + ColorChooser.RESET;
            case INSECTKINGDOM -> ColorChooser.PURPLE +"INSECTKINGDOM" + ColorChooser.RESET;
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

