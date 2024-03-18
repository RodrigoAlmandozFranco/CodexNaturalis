package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.Board;
import it.polimi.ingsw.am42.model.Face;

public class EvaluatorPointsPerCorner extends Evaluator{
    public EvaluatorPointsPerCorner(int numPoints) {
        super(numPoints);
    }

    @Override
    public int getPoints(Board board) {
        Face lastFace = board.getLastPlacedFace();
        int cornerCovered = board.getNearbyFaces(lastFace.getPosition()).size();

        return cornerCovered*numPoints;
    }
}
