package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

import java.util.Map;
/**
 * Subclass of evaluator, returns the number of points given the requested resources the number of resources on the board
 */
public class EvaluatorPointsPerResource extends Evaluator{

    private final Map<Resource, Integer> resourceMap;

    /**
     * Constructor of the class
     * @param numPoints the number of points given by the card
     * @param resourceMap the number of resources needed to award the points
     */
    public EvaluatorPointsPerResource(int numPoints, Map<Resource, Integer> resourceMap) {
        super(numPoints);

        this.resourceMap = resourceMap;
    }

    /**
     * Returns the number of points given the requested resources the number of resources on the board
     * @param board the current board
     * @return numPoints proportionate to the requested resources and the number of resources on the board
     * @author Tommaso Crippa
     */
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



    public String toString(Color color) {
        if (color != null)
            return "" + numPoints + color + " │ " + resourceMap.keySet().toArray()[0] + color;
        else
            return toString();
    }


    public String threeOfAKind() {
        Resource resource = resourceMap.keySet().stream().toList().getFirst();
        String color = resource.resourceToColor().toString();
        String to_print = color;


        to_print += "┌――――――――――――――┐\n";
        to_print += "│   ___                 │\n";
        to_print += "│  /   \\        " + numPoints + "       │\n";
        to_print += "│  │ " + resource+color + " │                │\n";

        to_print += "│  │   │        " + resource+color+ "       │\n";
        to_print += "│  │   │      /   \\     │\n";
        to_print += "│  │   │     " + resource+color+ "  -  " + resource+color+ "    │\n";

        to_print += "│  │   │                │\n";
        to_print += "└――――――――――――――┘";
        to_print += "\n";

        to_print += ColorChooser.RESET;
        return to_print;
    }

    public String pair() {
        String color = ColorChooser.YELLOW;
        String to_print = color;

        Resource res = resourceMap.keySet().stream().toList().get(0);


        to_print += "┌――――――――――――――┐\n";
        to_print += "│   ___                 │\n";
        to_print += "│  /   \\        " + numPoints + "       │\n";
        to_print += "│  │ " + res + color + " │                │\n";

        to_print += "│  │   │                │\n";
        to_print += "│  │   │      " + res+color + " │ " + res+color +"     │\n";
        to_print += "│  │   │                │\n";

        to_print += "│  │   │                │\n";
        to_print += "└――――――――――――――┘";

        to_print += ColorChooser.RESET;
        return to_print;
    }

    public String oneOfEach() {
        String color = ColorChooser.YELLOW;
        String to_print = color;

        Resource first = resourceMap.keySet().stream().toList().get(0);
        Resource second = resourceMap.keySet().stream().toList().get(1);
        Resource third = resourceMap.keySet().stream().toList().get(2);


        to_print += "┌――――――――――――――┐\n";
        to_print += "│   ___                 │\n";
        to_print += "│  /   \\        " + numPoints + "       │\n";
        to_print += "│  │ " + ColorChooser.WHITE + "X" + color + " │                │\n";
        to_print += "│  │   │                │\n";
        to_print += "│  │   │     " + first+color + " │ " + second+color + " │ " + third+color + "  │\n";
        to_print += "│  │   │                │\n";

        to_print += "│  │   │                │\n";
        to_print += "└――――――――――――――┘";

        to_print += ColorChooser.RESET;

        return to_print;
    }



    @Override
    public String toString() {

        if (resourceMap == null || resourceMap.isEmpty())
            return super.toString();
        if (resourceMap.keySet().size() == 1) {
            Resource res = resourceMap.keySet().stream().toList().get(0);
            if (resourceMap.get(res) == 3)
                return threeOfAKind();
            else
                return pair();
        }
        if (resourceMap.keySet().size() == 3)
            return oneOfEach();
        return super.toString();



    }
}

