package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.model.cards.types.Face;

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
}
