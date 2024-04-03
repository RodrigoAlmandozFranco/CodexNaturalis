package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.structure.Board;

import java.util.Map;

public class EvaluatorPointsPerResource extends Evaluator{

    private final Map<Resource, Integer> resourceMap;
    public EvaluatorPointsPerResource(int numPoints, Map<Resource, Integer> resourceMap) {
        super(numPoints);

        this.resourceMap = resourceMap;
    }

    @Override
    public int getPoints(Board board) {

        Map<Resource, Integer> boardResources = board.getTotalResources();
        int minFactor = Integer.MAX_VALUE;

        if (resourceMap == null || resourceMap.isEmpty())
            return 0;

        for (Resource r : resourceMap.keySet())
            if (boardResources.containsKey(r) && resourceMap.get(r) != 0)
                minFactor = Math.min(minFactor, boardResources.get(r) / resourceMap.get(r));
            else
                minFactor = 0;

        return minFactor*numPoints;
    }
}

