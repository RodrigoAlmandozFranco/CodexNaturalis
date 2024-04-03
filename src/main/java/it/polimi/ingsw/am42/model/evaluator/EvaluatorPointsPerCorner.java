package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.model.cards.types.Face;

public class EvaluatorPointsPerCorner extends Evaluator{
    public EvaluatorPointsPerCorner(int numPoints) {
        super(numPoints);
    }

    @Override
    public int getPoints(Board board) {
        Face lastFace = board.getLastPlacedFace();
        if (lastFace == null)
            return 0;
        int cornerCovered = board.getNearbyFaces(lastFace.getPosition()).size();

        return cornerCovered*numPoints;
    }
}
