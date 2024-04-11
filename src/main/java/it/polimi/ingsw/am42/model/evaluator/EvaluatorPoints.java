package it.polimi.ingsw.am42.model.evaluator;

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
}
