package it.polimi.ingsw.am42.model.cards.types;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;
import it.polimi.ingsw.am42.model.evaluator.EvaluatorPoints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the back face of a card.
 * @author Tommaso Crippa
 */
public class Back extends Face {

    private final Resource resource;


    public Back(String src, List<Corner> corners, Color color, Resource resource) {
        super(src, corners, color);

        this.resource = resource;
    }

    /**
     * Returns the evaluator of the given face.
     *
     * @author Tommaso Crippa
     * @return an evaluator that always gives 0 points
     */
    @Override
    public Evaluator getEvaluator() {
        return new EvaluatorPoints(0);
    }

    /**
     * Returns the requirements of the given face.
     *
     * @author Tommaso Crippa
     * @return an empty map, as the requirements are always satisfied
     */
    @Override
    public Map<Resource, Integer> getRequirements() {
        return new HashMap<Resource, Integer>();
    }

    /**
     * Returns the resources of the given face.
     *
     * @author Tommaso Crippa
     * @return  a map with a single key, the resource in the center, with a value of 1
     */
    @Override
    public Map<Resource, Integer> getResources() {

        Map<Resource, Integer> resources = new HashMap<Resource, Integer>();
        resources.put(resource, 1);

        return resources;

    }
}

