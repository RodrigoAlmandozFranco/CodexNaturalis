package it.polimi.ingsw.am42.model.evaluator;

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

}
