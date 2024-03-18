package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;

import java.util.List;
import java.util.Map;

/**
 * This class represents the face of a card.
 * @author Tommaso Crippa
 */
public abstract class Face {
    private final String srcImage;
    protected final List<Corner> corners;
    private Position position;
    private final Color color;

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
}

