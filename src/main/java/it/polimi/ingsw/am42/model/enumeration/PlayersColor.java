package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.view.tui.ColorChooser;

import java.io.Serializable;

public enum PlayersColor implements Serializable {
    RED,
    BLUE,
    GREEN,
    YELLOW;

    @Override
    public String toString() {
        switch (this) {
            case RED -> {
                return ColorChooser.RED;
            }
            case BLUE -> {
                return ColorChooser.BLUE;
            }
            case GREEN -> {
                return ColorChooser.GREEN;
            }
            case YELLOW -> {
                return ColorChooser.YELLOW;
            }
        }
        return "";
    }

    public String toString(boolean literal) {
        if (literal) {
            switch (this) {
                case RED -> {
                    return ColorChooser.RED + "RED" + ColorChooser.RESET;
                }
                case BLUE -> {
                    return ColorChooser.BLUE + "BLUE" + ColorChooser.RESET;
                }
                case GREEN -> {
                    return ColorChooser.GREEN + "GREEN" + ColorChooser.RESET;
                }
                case YELLOW -> {
                    return ColorChooser.YELLOW + "YELLOW" + ColorChooser.RESET;
                }
                default -> {
                    return "";
                }
            }
        }
        else
            return toString();
    }

}
