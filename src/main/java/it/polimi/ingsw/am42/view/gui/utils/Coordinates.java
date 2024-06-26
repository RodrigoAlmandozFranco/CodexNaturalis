package it.polimi.ingsw.am42.view.gui.utils;

/**
 * This class helps BoardController to work with the screen coordinates of
 * all the JavaFx elements
 *
 * @author Mattia Brandi
 */
public class Coordinates {
    private double x;
    private double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * This method returns the x value of the coordinate
     *
     * @return the x value of the coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * This method returns the y value of the coordinate
     *
     * @return the y value of the coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * This method sets the x value of the coordinate
     *
     * @param x x value of the coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * This method sets the y value of the coordinate
     *
     * @param y y value of the coordinate
     */
    public void setY(double y) {
        this.y = y;
    }
}
