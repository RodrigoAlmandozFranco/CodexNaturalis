package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.structure.Board;

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



    public String toString(boolean small) {
        if (small)
            return numPoints + " | /" ;
        else
            return toString();
    }

    public String toString() {
        String to_print = color.toString();

        to_print += "+-----------------------+\n";
        to_print += "|   ___                 |\n";
        to_print += "|  /   \\        " + numPoints + "       |\n";
        to_print += "|  | " + color.colorToResource() + " |                |\n";

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

        to_print += Color.WHITE.toString();
        return to_print;
    }
}

