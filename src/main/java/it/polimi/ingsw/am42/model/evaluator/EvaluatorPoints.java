package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.structure.Board;

public class EvaluatorPoints extends Evaluator{

    public EvaluatorPoints(int numPoints) {
        super(numPoints);
    }

    /**
     * Returns the number of points given by the card
     * @param board the current board
     * @return numPoints
     * @author Tommaso Crippa
     */
    @Override
    public int getPoints(Board board) {
        return numPoints;
    }


    public String toString(boolean small) {
        if (small)
            return "  " + (numPoints != 0 ? numPoints : " ") + "  ";
        else
            return toString();
    }
    @Override
    public String toString() {
        String to_print = "\u001B[33m";

        to_print += "+-----------------------+\n";

        // TODO

        to_print += "+-----------------------+";


        to_print += Color.WHITE.toString();
        return to_print;
    }
}
