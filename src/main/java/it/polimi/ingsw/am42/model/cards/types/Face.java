package it.polimi.ingsw.am42.model.cards.types;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

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

    public List<Resource >getListResource() {
        return null;
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

    public String firstRow(String sep) {
        if (sep == null)
            sep = "\n";

        String colorStr = color.toString();
        String to_print = colorStr;

        to_print += "+---+---------------+---+" + sep;

        return  to_print;
    }

    public String secondRow(String sep) {
        if (sep == null)
            sep = "\n";

        String colorStr = color.toString();
        String to_print = colorStr;

        // 2
        if (getCorner(Direction.UPLEFT).getState().equals(CornerState.CLOSED))
            to_print += "|    ";
        else
            to_print += "| " + (getCorner(Direction.UPLEFT).getResource() == null ? " " : getCorner(Direction.UPLEFT).getResource().toString()+colorStr) + " |";

        if (getEvaluator() == null)
            to_print += "               ";
        else
            to_print += "     " + getEvaluator().toString(color) + "     ";
        if (getCorner(Direction.UPRIGHT).getState().equals(CornerState.CLOSED))
            to_print += "    |" + sep;
        else
            to_print += "| " + (getCorner(Direction.UPRIGHT).getResource() == null ? " " : getCorner(Direction.UPRIGHT).getResource().toString()+colorStr) + " |" + sep;

        return to_print;
    }

    public String thirdRow(String sep) {
        if (sep == null)
            sep = "\n";

        String colorStr = color.toString();
        String to_print = colorStr;


        // 3
        if (getCorner(Direction.UPLEFT).getState().equals(CornerState.CLOSED))
            to_print += "|    ";
        else
            to_print += "+---+";
        to_print += "               ";
        if (getCorner(Direction.UPRIGHT).getState().equals(CornerState.CLOSED))
            to_print += "    |" + sep;
        else
            to_print += "+---+" + sep;

        to_print += ColorChooser.RESET;
        return to_print;
    }

    private String upperPart(String sep) {
        String to_print = "";

        to_print += firstRow(sep);
        to_print += secondRow(sep);
        to_print += thirdRow(sep);

        return to_print;
    }

    public String fourthRow(String sep) {
        if (sep == null)
            sep = "\n";

        String colorStr = color.toString();
        String to_print = colorStr;

        to_print += "|                       |" + sep;

        return  to_print;
    }

    public String fifthRow(String sep) {
        if (sep == null)
            sep = "\n";

        String colorStr = color.toString();
        String to_print = colorStr;

        to_print += "|                       |" + sep;

        return  to_print;
    }

    public String sixthRow(String sep) {
        if (sep == null)
            sep = "\n";

        String colorStr = color.toString();
        String to_print = colorStr;

        to_print += "|                       |" + sep;

        return  to_print;
    }


    private String middlePart(String sep) {
        String to_print = "";

        to_print += fourthRow(sep);
        to_print += fifthRow(sep);
        to_print += sixthRow(sep);

        return  to_print;
    }

    public String seventhRow(String sep) {
        if (sep == null)
            sep = "\n";

        String colorStr = color.toString();
        String to_print = colorStr;

        if (getCorner(Direction.DOWNLEFT).getState().equals(CornerState.CLOSED))
            to_print += "|    ";
        else
            to_print += "+---+";
        to_print += "               ";
        if (getCorner(Direction.DOWNRIGHT).getState().equals(CornerState.CLOSED))
            to_print += "    |" + sep;
        else
            to_print += "+---+" + sep;

        return to_print;
    }

    public String eighthRow(String sep) {
        if (sep == null)
            sep = "\n";

        String colorStr = color.toString();
        String to_print = colorStr;

        if (getCorner(Direction.DOWNLEFT).getState().equals(CornerState.CLOSED))
            to_print += "|    ";
        else
            to_print += "| " + (getCorner(Direction.DOWNLEFT).getResource() == null ? " " : getCorner(Direction.DOWNLEFT).getResource().toString()+colorStr) + " |";
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
                    to_print += r.toString() + colorStr + "|";
            to_print += " ".repeat(7 - totalValues);
        }
        if (getCorner(Direction.DOWNRIGHT).getState().equals(CornerState.CLOSED))
            to_print += "    |" + sep;
        else
            to_print += "| " + (getCorner(Direction.DOWNRIGHT).getResource() == null ? " " : getCorner(Direction.DOWNRIGHT).getResource().toString()+colorStr) + " |" + sep;

        return to_print;
    }

    public String ninthRow(String sep) {
        // Same String
        return firstRow(sep);
    }



    private String finalPart(String sep) {

        String to_print = "";
        to_print += seventhRow(sep);
        to_print += eighthRow(sep);
        to_print += ninthRow(sep);

        return to_print;
    }



    @Override
    public String toString() {

        String to_print = "";

        to_print += upperPart("\n");
        to_print += middlePart("\n");
        to_print += finalPart("\n");

        to_print += ColorChooser.RESET;


        return to_print;
    }
}

