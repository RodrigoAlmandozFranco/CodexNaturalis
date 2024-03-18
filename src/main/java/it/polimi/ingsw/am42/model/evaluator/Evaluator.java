package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.Board;

public abstract class Evaluator {
    protected final int numPoints;

    public Evaluator(int numPoints) {
        this.numPoints = numPoints;
    }

    public int getPoints(Board board){
        return 0;
    }

}
