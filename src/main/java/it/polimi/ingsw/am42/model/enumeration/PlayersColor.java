package it.polimi.ingsw.am42.model.enumeration;

public enum PlayersColor {

    RED,
    BLUE,
    GREEN,
    YELLOW;

    @Override
    public String toString() {
        switch (this) {
            case RED -> {
                return "\033[0;31m" + "RED" + "\033[0m";
            }
            case BLUE -> {
                return  "\033[0;34m" + "BLUE" + "\033[0m";
            }
            case GREEN -> {
                return  "\033[0;32m" + "GREEN" + "\033[0m";
            }
            case YELLOW -> {
                return  "\033[0;33m" + "YELLOW" + "\033[0m";
            }
        }
        return "";
    }
}
