package it.polimi.ingsw.am42.model.cards.types;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;
import it.polimi.ingsw.am42.model.structure.Position;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class represents the face of a card.
 * @author Tommaso Crippa
 */
public abstract class Face implements Serializable {
    private final String srcImage;
    protected final List<Corner> corners;
    protected int id;
    private Position position;
    protected final Color color;

    public Face(String src, List<Corner> corners, Color color){
        this.srcImage = src;
        this.corners = corners;
        this.position = null;
        this.color = color;
    }

    /**
     * Returns the corners of the face
     *
     * @author Tommaso Crippa
     * @return a list of corners
     */
    public List<Corner> getCorners() {
        return corners;
    }

    /**
     * Returns the corner in the given direction
     *
     * @author Tommaso Crippa
     * @param dir the direction of the corner
     * @return a corner, with the same direction as dir
     */
    public Corner getCorner(Direction dir) {
        for (Corner c : corners) {
            if (c.getDirection().equals(dir))
                    return c;
        }
        return null;
    }

    public Evaluator getEvaluator() {
        return null;
    }

    public Map<Resource, Integer> getRequirements() {
        return null;
    }

    public Map<Resource, Integer> getResources() {
        return null;
    }

    /**
     * Returns the position of the card
     *
     * @author Tommaso Crippa
     * @return If placed in the board, the position of the card, else is returns null
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of the card
     *
     * @author Tommaso Crippa
     * @param position the position to put the card in
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Sets the position of the card
     *
     * @author Tommaso Crippa
     * @param x the x coordinates to put the card in
     * @param y the y coordinates to put the card in
     */
    public void setPosition(int x, int y) {
        this.position = new Position(x, y);
    }
    /**
     * Returns color of the face
     *
     * @author Tommaso Crippa
     * @return color of the face
     */
    public Color getColor() {
        return color;
    }
    /**
     * Returns the source of the image
     *
     * @author Tommaso Crippa
     * @return srcImage
     */
    public String getSrcImage() {
        return srcImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String upperPart() {

        String to_print = color.toString();

        // 1
        to_print += "+---+---------------+---+\n";

        // 2
        if (getCorner(Direction.UPLEFT).getState().equals(CornerState.CLOSED))
            to_print += "|    ";
        else
            to_print += "| " + (getCorner(Direction.UPLEFT).getResource() == null ? " " : getCorner(Direction.UPLEFT).getResource().toString()) + " |";

        if (getEvaluator() == null)
            to_print += "               ";
        else
            to_print += "     " + getEvaluator().toString(true) + "     ";
        if (getCorner(Direction.UPRIGHT).getState().equals(CornerState.CLOSED))
            to_print += "    |\n";
        else
            to_print += "| " + (getCorner(Direction.UPRIGHT).getResource() == null ? " " : getCorner(Direction.UPRIGHT).getResource().toString()) + " |\n";

        // 3
        if (getCorner(Direction.UPLEFT).getState().equals(CornerState.CLOSED))
            to_print += "|    ";
        else
            to_print += "+---+";
        to_print += "               ";
        if (getCorner(Direction.UPRIGHT).getState().equals(CornerState.CLOSED))
            to_print += "    |\n";
        else
            to_print += "+---+\n";

        to_print += Color.WHITE.toString();
        return to_print;
    }


    protected String middlePart() {
        String to_print = color.toString();
        for (int i=0; i<3; i++)
            to_print += "|                       |\n";

        to_print += Color.WHITE.toString();
        return to_print;
    }

    private String finalPart() {


        String to_print = color.toString();

        // 1
        if (getCorner(Direction.DOWNLEFT).getState().equals(CornerState.CLOSED))
            to_print += "|    ";
        else
            to_print += "+---+";
        to_print += "               ";
        if (getCorner(Direction.DOWNRIGHT).getState().equals(CornerState.CLOSED))
            to_print += "    |\n";
        else
            to_print += "+---+\n";


        // 2
        if (getCorner(Direction.DOWNLEFT).getState().equals(CornerState.CLOSED))
            to_print += "|    ";
        else
            to_print += "| " + (getCorner(Direction.DOWNLEFT).getResource() == null ? " " : getCorner(Direction.DOWNLEFT).getResource().toString()) + " |";
        if (getRequirements().equals(null))
            to_print += "               ";
        else {
            int totalValues = 0;
            for (int value : getRequirements().values()) {
                totalValues += value;
            }

            to_print += " ".repeat(7 - totalValues);

            to_print += totalValues != 0 ? "|" : " ";
            for (Resource r : getRequirements().keySet())
                for (int i=0; i< getRequirements().get(r); i++)
                    to_print += r.toString() + "|";
            to_print += " ".repeat(7 - totalValues);
        }
        if (getCorner(Direction.DOWNRIGHT).getState().equals(CornerState.CLOSED))
            to_print += "    |\n";
        else
            to_print += "| " + (getCorner(Direction.DOWNRIGHT).getResource() == null ? " " : getCorner(Direction.DOWNRIGHT).getResource().toString()) + " |\n";


        // 3
        to_print += "+---+---------------+---+\n";

        to_print += Color.WHITE.toString();
        return to_print;

    }

    @Override
    public String toString() {

        String to_print = "";

        to_print += upperPart();
        to_print += middlePart();
        to_print += finalPart();


        return to_print;
    }
}

