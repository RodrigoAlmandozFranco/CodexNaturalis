package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

public class EvaluatorPointsPerStair extends Evaluator {

    private final Color color;
    private final Direction direction;

    public EvaluatorPointsPerStair(int numPoints, Color color, Direction direction) {
        super(numPoints);
        this.color = color;
        this.direction = direction;
    }

    @Override
    public int getPoints(Board board) {

        int total = 0;

        for (Face f : board.getFaces()) {
            if (!f.getColor().equals(color))
                continue;
            Face opp = board.getNearbyFaces(f.getPosition()).get(direction.opposite());
            if (opp == null || !opp.getColor().equals(color)) {
                int count = 1;
                Face fUp = board.getNearbyFaces(f.getPosition()).get(direction);
                while (fUp != null && fUp.getColor().equals(color)) {
                    fUp = board.getNearbyFaces(fUp.getPosition()).get(direction);
                    count++;
                }

                total += numPoints * (count / 3);
            }
        }

        return total;
    }



    public String toString(Color color) {
        if (color != null)
            return "" + numPoints + color + " | /" ;
        else
            return toString();
    }

    public String toString() {
        String colorStr = color.toString();
        String to_print = colorStr;

        to_print += "+-----------------------+\n";
        to_print += "|   ___                 |\n";
        to_print += "|  /   \\        " + numPoints + "       |\n";
        to_print += "|  | " + color.colorToResource()+colorStr + " |                |\n";

        if (direction.equals(Direction.UPRIGHT)) {
            to_print += "|  |   |          ██    |\n";
            to_print += "|  |   |        ██      |\n";
            to_print += "|  |   |      ██        |\n";
        }
        else {
            to_print += "|  |   |      ██        |\n";
            to_print += "|  |   |        ██      |\n";
            to_print += "|  |   |          ██    |\n";
        }
        to_print += "|  |   |                |\n";
        to_print += "+-----------------------+";

        to_print += ColorChooser.RESET;
        return to_print;
    }
}

