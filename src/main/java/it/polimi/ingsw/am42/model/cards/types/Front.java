package it.polimi.ingsw.am42.model.cards.types;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class represents the front face of a card.
 * @author Tommaso Crippa
 */
public class Front extends Face {

    private final Map<Resource, Integer> requirements;
    private final Evaluator evaluator;


    public Front(String src, List<Corner> corners, Color color, Map<Resource, Integer> requirements, Evaluator evaluator) {
        super(src, corners, color);

        this.requirements = requirements;
        this.evaluator = evaluator;
    }

    /**
     * Returns the evaluator of the given face.
     *
     * @author Tommaso Crippa
     * @return evaluator
     */
    @Override
    public Evaluator getEvaluator() {
        return evaluator;
    }

    /**
     * Returns the requirements of the given face.
     *
     * @author Tommaso Crippa
     * @return a map, where the keys are the resources and the values are the required number of resources
     */
    @Override
    public Map<Resource, Integer> getRequirements() {
        return requirements;
    }


    /**
     * Returns the resources of the given face.
     *
     * @author Tommaso Crippa
     * @return  a map, where the keys are the resources and the values are the number of times that resource is shown
     */
    @Override
    public Map<Resource, Integer> getResources() {

        Map<Resource, Integer> resources = new HashMap<Resource, Integer>();
        Resource r;

        for (Corner c: corners) {
            r = c.getResource();
            if (r != null) {
                if (!resources.containsKey(r))
                    resources.put(r, 0);
                resources.put(r, resources.get(r)+1);
            }
        }
        return resources;

    }
}
