package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

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
        String to_print = ColorChooser.YELLOW;

        to_print += "+-----------------------+\n";

        for (int i=0; i<4; i++)
            to_print += "|                       |\n";
        to_print += "|            " + numPoints + "          |\n";
        for (int i=0; i<4; i++)
            to_print += "|                       |\n";

        to_print += "+-----------------------+";


        to_print += ColorChooser.RESET;
        return to_print;
    }


}
