package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.structure.Board;

public class EvaluatorPoints extends Evaluator{

    public EvaluatorPoints(int numPoints) {
        super(numPoints);
    }

    @Override
    public int getPoints(Board board) {
        return numPoints;
    }
}
