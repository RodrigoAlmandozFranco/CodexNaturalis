package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.view.tui.ColorChooser;


/**
 * Subclass of evaluator, returns the number of points given by the card by counting the number of stairs in the board
 *
 * Example of stair:
 *           #
 *              #
 *                 #
 *  This particular stair is pointed in the UPLEFT Direction (0, +1)
 */
public class EvaluatorPointsPerStair extends Evaluator {

    private final Color color;
    private final Direction direction;

    /**
     * Constructor of the class
     * @param numPoints the number of points given by the card
     * @param color the color of the stair
     * @param direction the direction of the stair (only up direction allowed)
     */
    public EvaluatorPointsPerStair(int numPoints, Color color, Direction direction) {
        super(numPoints);
        this.color = color;
        this.direction = direction;
    }

    /**
     * Awards the number of points given the number of stairs in the board
     */
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
            return "" + numPoints + color + " │ /" ;
        else
            return toString();
    }

    public String toString() {
        String colorStr = color.toString();
        String to_print = colorStr;

        to_print += "┌-----------------------┐\n";
        to_print += "│   _____               │\n";
        to_print += "│  /     \\      " + numPoints + "       │\n";
        to_print += "│  │  " + color.colorToResource()+colorStr + "  │              │\n";

        if (direction.equals(Direction.UPRIGHT)) {
            to_print += "│  │     │        ██    │\n";
            to_print += "│  │     │      ██      │\n";
            to_print += "│  │     │    ██        │\n";
        }
        else {
            to_print += "│  │     │    ██        │\n";
            to_print += "│  │     │      ██      │\n";
            to_print += "│  │     │        ██    │\n";
        }
        to_print += "│  │     │              │\n";
        to_print += "└-----------------------┘";

        to_print += ColorChooser.RESET;
        return to_print;
    }
}

