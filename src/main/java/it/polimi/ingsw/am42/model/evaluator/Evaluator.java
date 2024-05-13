package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.structure.Board;

import java.io.Serializable;

/**
 * Subclass of evaluator, returning numPoints
 * @author Tommaso Crippa
 */
public abstract class Evaluator implements Serializable {
    protected final int numPoints;

    public Evaluator(int numPoints) {
        this.numPoints = numPoints;
    }

    public int getPoints(Board board){
        return 0;
    }


    public String toString(boolean small) {
        if (small)
            return "  " + numPoints + "  ";
        else
            return toString();
    }
    @Override
    public String toString() {
        String to_print = "\u001B[33m";

        to_print += "+-----------------------+\n";
        for (int i=0; i<7; i++)
            to_print += "|                       |\n";
        to_print += "+-----------------------+";


        to_print += Color.WHITE.toString();
        return to_print;
    }
}
