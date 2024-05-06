package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.structure.Board;


public class EvaluatorPointsPerChair extends Evaluator{

    private final Color color1;
    private final Color color2;
    private final Direction direction;

    public EvaluatorPointsPerChair(int numPoints, Color color1, Color color2, Direction direction) {
        super(numPoints);
        this.color1 = color1;
        this.color2 = color2;
        this.direction = direction;
    }

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


    public String toString(boolean small) {
        if (small)
            return numPoints + " | L" ;
        else
            return toString();
    }
    @Override
    public String toString() {
        String to_print = "\u001B[33m";

        to_print += "+-----------------------+\n";

        // TODO

        to_print += "+-----------------------+";


        to_print += Color.WHITE.toString();
        return to_print;
    }

}
