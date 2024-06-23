package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

import java.io.Serializable;

/**
 * Enumeration that stores the game's cards color
 */
public enum Color implements Serializable {
    GREEN,
    CYAN,
    RED,
    PURPLE,
    WHITE; // For starting card

    /**
     * Returns the terminal's color to display
     */
    public String toString() {
        return  switch (this) {
            case RED -> ColorChooser.RED;
            case CYAN -> ColorChooser.CYAN;
            case PURPLE -> ColorChooser.PURPLE;
            case GREEN -> ColorChooser.GREEN;
            case WHITE -> ColorChooser.WHITE;
            default -> ColorChooser.RESET;
        };
    }

    /**
     * Method used to select how to display the color
     * @param literal if true, get the literal name of the color, otherwise get only the terminal's color
     */
    public String toString(boolean literal) {
        if (literal) {
            return switch (this) {
                case RED -> ColorChooser.RED + "RED" + ColorChooser.RESET;
                case CYAN -> ColorChooser.CYAN + "CYAN" + ColorChooser.RESET;
                case PURPLE -> ColorChooser.PURPLE + "PURPLE" + ColorChooser.RESET;
                case GREEN -> ColorChooser.GREEN + "GREEN" + ColorChooser.RESET;
                case WHITE -> ColorChooser.WHITE + "WHITE" + ColorChooser.RESET;
                default -> ColorChooser.WHITE +"WHAT" + ColorChooser.RESET;
            };
        } else return toString();
    }

    /**
     * For each color (except white) get its respective associated resource
     */
    public Resource colorToResource() {
        return  switch (this) {
            case RED -> Resource.FUNGIKINGDOM;
            case CYAN -> Resource.ANIMALKINGDOM;
            case PURPLE -> Resource.INSECTKINGDOM;
            case GREEN -> Resource.PLANTKINGDOM;
            default -> null;
        };
    }
}
