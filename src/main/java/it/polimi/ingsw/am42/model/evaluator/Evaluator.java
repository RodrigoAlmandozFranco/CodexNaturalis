package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

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

    public String toString(Color color) {
        if (color != null)
            return "  " + numPoints + "  ";
        else
            return toString();
    }
    @Override
    public String toString() {
        String to_print = ColorChooser.YELLOW;

        to_print += "+-----------------------+\n";
        for (int i=0; i<8; i++)
            to_print += "|                       |\n";
        to_print += "+-----------------------+";


        to_print += ColorChooser.RESET;
        return to_print;
    }
}
