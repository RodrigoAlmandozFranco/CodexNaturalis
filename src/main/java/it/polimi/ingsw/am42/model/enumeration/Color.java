package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

public enum Color {
    GREEN,
    CYAN,
    RED,
    PURPLE,
    WHITE;


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

    public String toString(boolean literal) {
        if (literal) {
            return switch (this) {
                case RED -> "RED";
                case CYAN -> "CYAN";
                case PURPLE -> "PURPLE";
                case GREEN -> "GREEN";
                case WHITE -> "WHITE";
                default -> "WHAT";
            };
        } else return toString();
    }


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
