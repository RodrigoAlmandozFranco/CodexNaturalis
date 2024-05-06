package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.model.structure.Position;

public enum Color {
    GREEN,
    CYAN,
    RED,
    PURPLE,
    WHITE;


    public String toString() {
        return  switch (this) {
            case RED -> "\u001B[31m";
            case CYAN -> "\u001B[36m";
            case PURPLE -> "\u001B[35m";
            case GREEN -> "\u001B[32m";
            case WHITE -> "\u001B[37m";
            default -> "\u001B[0m";
        };
    }
}
