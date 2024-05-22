package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.view.tui.ColorChooser;

public enum PlayersColor {

    RED,
    BLUE,
    GREEN,
    YELLOW;

    @Override
    public String toString() {
        switch (this) {
            case RED -> {
                return ColorChooser.RED + "RED" + ColorChooser.RESET;
            }
            case BLUE -> {
                return  ColorChooser.BLUE + "BLUE" + ColorChooser.RESET;
            }
            case GREEN -> {
                return  ColorChooser.GREEN + "GREEN" + ColorChooser.RESET;
            }
            case YELLOW -> {
                return  ColorChooser.YELLOW + "YELLOW" + ColorChooser.RESET;
            }
        }
        return "";
    }
}
