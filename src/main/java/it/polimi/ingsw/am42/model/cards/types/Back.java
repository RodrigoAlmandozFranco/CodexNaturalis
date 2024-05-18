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

    private final List<Resource> resource;


    public Back(String src, List<Corner> corners, Color color, List<Resource> resource) {
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

        Resource r;
        for (Corner c: corners) {
            r = c.getResource();
            if (r != null) {
                if (!resources.containsKey(r))
                    resources.put(r, 0);
                resources.put(r, resources.get(r)+1);
            }
        }

        for (Resource res : resource){
            if (!resources.containsKey(res))
                resources.put(res, 0);
            resources.put(res, resources.get(res)+1);
        }

        return resources;
    }

    /*
    @Override
    protected String middlePart() {
        String to_print = color.toString();
        if (resource.isEmpty())
            for (int i=0; i<3; i++)
                to_print += "|                       |\n";
        else {
            // 1
            to_print += "|" + " ".repeat(10 - resource.size())
                    + "+-" + "--".repeat(resource.size()) + "+"
                    + " ".repeat(10 - resource.size()) + "|\n";
            // 2
            to_print += "|" + " ".repeat(10 - resource.size())
                    + "| ";
            for (Resource r : resource)
                to_print += r + " ";
            to_print += "|"+ " ".repeat(10 - resource.size()) + "|\n";
            // 3
            to_print += "|" + " ".repeat(10 - resource.size())
                    + "+-" + "--".repeat(resource.size()) + "+"
                    + " ".repeat(10 - resource.size()) + "|\n";
        }



        to_print += Color.WHITE.toString();
        return to_print;


    }

     */

}

