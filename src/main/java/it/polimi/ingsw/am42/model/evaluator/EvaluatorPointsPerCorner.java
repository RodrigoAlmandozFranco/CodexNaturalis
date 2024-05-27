package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

public class EvaluatorPointsPerCorner extends Evaluator{
    public EvaluatorPointsPerCorner(int numPoints) {
        super(numPoints);
    }

    /**
     * Returns the number of points proportionate to the nymber of points covered by lastPlacedFace
     * @param board the current board
     * @return numPoints proportionate to the number of corners near lastPlacedFace
     * @author Tommaso Crippa
     */
    @Override
    public int getPoints(Board board) {
        Face lastFace = board.getLastPlacedFace();
        if (lastFace == null)
            return 0;
        int cornerCovered = board.getNearbyFaces(lastFace.getPosition()).size();

        return cornerCovered*numPoints;
    }

    public String toString(Color color) {
        if (color != null)
            return "" + numPoints + color + " | C" ;
        else
            return toString();
    }

    public String toString() {
        String to_print = ColorChooser.YELLOW;

        to_print += "+-----------------------+\n";

        for (int i=0; i<4; i++)
            to_print += "|                       |\n";
        to_print += "|          C |" + numPoints + "          |\n";
        for (int i=0; i<4; i++)
            to_print += "|                       |\n";

        to_print += "+-----------------------+";


        to_print += ColorChooser.RESET;
        return to_print;
    }

}
