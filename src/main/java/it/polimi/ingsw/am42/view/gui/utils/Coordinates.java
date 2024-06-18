package it.polimi.ingsw.am42.view.gui.utils;

public class Coordinates {
    private double x;
    private double y;

    /**
     * This class helps BoardController to work with the coordinates of
     * all the JavaFx elements
     * @author Mattia Brandi
     */
    public Coordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
