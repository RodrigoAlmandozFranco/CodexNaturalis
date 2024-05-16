package it.polimi.ingsw.am42.view.gui.utils.points;

public class Points {
    private int point;

    public Points(int point) {
        this.point = point;
    }


    public ScreenPosition getScreenPosition() {
        return new ScreenPosition().execute(point);
    }


}
