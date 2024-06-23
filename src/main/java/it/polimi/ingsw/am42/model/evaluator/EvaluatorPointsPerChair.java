package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

/**
 * Subclass of evaluator, returns the number of points given by the card by counting the number of chairs in the board
 *
 * Example of chair:
 *           |>  #
 * backrest -|     .
 *           |>  #
 *      seat ->    #
 *
 *  This particular chair is pointed in the UPLEFT Direction (0, +1)
 */
public class EvaluatorPointsPerChair extends Evaluator{

    private final Color color1;
    private final Color color2;
    private final Direction direction;

    /**
     *
     * @param numPoints the number of points given by the card
     * @param color1 the seat of the chair
     * @param color2 the backrest of the chair
     * @param direction the direction of the backrest
     */
    public EvaluatorPointsPerChair(int numPoints, Color color1, Color color2, Direction direction) {
        super(numPoints);
        this.color1 = color1;
        this.color2 = color2;
        this.direction = direction;
    }

    /**
     * Returns the number of points given by the card by counting the number of chairs in the board
     * @param board the current board
     * @return numPoints
     */
    @Override
    public int getPoints(Board board) {

        int total = 0;
        boolean upward = direction.equals(Direction.UPLEFT) || direction.equals(Direction.UPRIGHT);
        Face head;
        Face backRest;
        Face seat;

        for (Face f : board.getFaces()) {
            if (!f.getColor().equals(color1))
                continue;

            seat = board.getNearbyFaces(f.getPosition()).get(direction);
            if (!upward) //If the face with the color2 is in an "upward" position the second face to check will be the face up (->^)+(<-^)= ^
                head = board.getNearbyFaces(Direction.UPRIGHT.getPosition(f.getPosition())).get(Direction.UPLEFT);
            else //If the face with the color2 is in a "downward" position the second face to check will be the face down (->v)+(<-v)= v
                head = board.getNearbyFaces(Direction.DOWNRIGHT.getPosition(f.getPosition())).get(Direction.DOWNLEFT);

            if(head!=null && seat!=null && head.getColor().equals(color1) && seat.getColor().equals(color2))
                continue;

            // here there is a face with the correct color but is no the central part of another chair
            head = f;
            while (head != null && head.getColor().equals(color1)) {
                if (upward) //If the face with the color2 is in an "upward" position the second face to check will be the face up (->^)+(<-^)= ^
                    backRest = board.getNearbyFaces(Direction.UPRIGHT.getPosition(head.getPosition())).get(Direction.UPLEFT);
                else //If the face with the color2 is in a "downward" position the second face to check will be the face down (->v)+(<-v)= v
                    backRest = board.getNearbyFaces(Direction.DOWNRIGHT.getPosition(head.getPosition())).get(Direction.DOWNLEFT);

                if (backRest!=null && backRest.getColor().equals(color1)) {
                    seat = board.getNearbyFaces(backRest.getPosition()).get(direction);
                    if (seat!=null && seat.getColor().equals(color2)) {
                        total += numPoints;

                        //assigning the new possible head
                        if (upward) //If the face with the color2 is in an "upward" position the second face to check will be the face up (->^)+(<-^)= ^
                            head = board.getNearbyFaces(Direction.UPRIGHT.getPosition(backRest.getPosition())).get(Direction.UPLEFT);
                        else //If the face with the color2 is in a "downward" position the second face to check will be the face down (->v)+(<-v)= v
                            head = board.getNearbyFaces(Direction.DOWNRIGHT.getPosition(backRest.getPosition())).get(Direction.DOWNLEFT);

                    }else break;
                }
                else break;
            }
        }

        return total;

    }


    public String toString(Color color) {
        if (color != null)
            return "" + numPoints + color + " │ L";
        else
            return toString();
    }

    public String toString() {
        String colorStr = color1.toString();
        String to_print = colorStr;

        to_print += "┌――――――――――――――┐\n";
        to_print += "│   ___                 │\n";
        to_print += "│  /   \\        " + numPoints + "       │\n";
        to_print += "│  │ " + color1.colorToResource()+colorStr + " │                │\n";

        if (direction.equals(Direction.UPRIGHT)) {
            to_print += "│  │   │         " + color2.toString() + "██" + color1.toString() + "     │\n";
            to_print += "│  │   │       ██       │\n";
            to_print += "│  │   │       ██       │\n";
        }
        else if (direction.equals(Direction.UPLEFT)) {
            to_print += "│  │   │      " + color2.toString() + "██" + color1.toString() + "        │\n";
            to_print += "│  │   │        ██      │\n";
            to_print += "│  │   │        ██      │\n";
        }
        else if (direction.equals(Direction.DOWNLEFT)) {
            to_print += "│  │   │        ██      │\n";
            to_print += "│  │   │        ██      │\n";
            to_print += "│  │   │      " + color2.toString() + "██" + color1.toString() + "        │\n";
        }
        else {
            to_print += "│  │   │       ██       │\n";
            to_print += "│  │   │       ██       │\n";
            to_print += "│  │   │         " + color2.toString() + "██" + color1.toString() + "     │\n";
        }
        to_print += "│  │   │                │\n";
        to_print += "└――――――――――――――┘";

        to_print += ColorChooser.RESET;
        return to_print;
    }

}
