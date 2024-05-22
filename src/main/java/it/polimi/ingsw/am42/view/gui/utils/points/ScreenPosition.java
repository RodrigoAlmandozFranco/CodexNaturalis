package it.polimi.ingsw.am42.view.gui.utils.points;

import javafx.stage.Screen;

public class ScreenPosition {
    private int x;
    private int y;

    public ScreenPosition() {}

    public ScreenPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    // First Player:    x += -18    y+= -16
    // Second Player:   x += 18     y+= -16
    // Third Player:    x += -18    y+= 16
    // Fourth Player:   x += 18     y+= 18

    public ScreenPosition getScreenPosition(int number, int points) {
        this.execute(points);
        switch (number) {
            case 0:
                x = x - 18;
                y = y - 16;
                break;
            case 1:
                x = x + 18;
                y = y - 16;
                break;
            case 2:
                x = x - 18;
                y = y + 16;
                break;
            case 3:
                x = x + 18;
                y = y + 16;
                break;
        }
        return new ScreenPosition(x, y);
    }


    private ScreenPosition execute(int point) {
        switch(point) {
            case 0:
                x = 574;
                y = 507;
                break;
            case 1:
                x = 641;
                y = 507;
                break;
            case 2:
                x = 707;
                y = 507;
                break;
            case 3:
                x = 741;
                y = 446;
                break;
            case 4:
                x = 674;
                y = 446;
                break;
            case 5:
                x = 607;
                y = 446;
                break;
            case 6:
                x = 541;
                y = 446;
                break;
            case 7:
                x = 541;
                y = 385;
                break;
            case 8:
                x = 607;
                y = 385;
                break;
            case 9:
                x = 674;
                y = 385;
                break;
            case 10:
                x = 741;
                y = 385;
                break;
            case 11:
                x = 741;
                y = 324;
                break;
            case 12:
                x = 674;
                y = 324;
                break;
            case 13:
                x = 607;
                y = 324;
                break;
            case 14:
                x = 541;
                y = 324;
                break;
            case 15:
                x = 541;
                y = 263;
                break;
            case 16:
                x = 607;
                y = 263;
                break;
            case 17:
                x = 674;
                y = 263;
                break;
            case 18:
                x = 741;
                y = 263;
                break;
            case 19:
                x = 741;
                y = 202;
                break;
            case 20:
                x = 641;
                y = 172;
                break;
            case 21:
                x = 541;
                y = 202;
                break;
            case 22:
                x = 541;
                y = 141;
                break;
            case 23:
                x = 541;
                y = 80;
                break;
            case 24:
                x = 580;
                y = 30;
                break;
            case 25:
                x = 641;
                y = 19;
                break;
            case 26:
                x = 702;
                y = 30;
                break;
            case 27:
                x = 741;
                y = 80;
                break;
            case 28:
                x = 741;
                y = 141;
                break;
            case 29:
                x = 641;
                y = 93;
                break;
        }

        return this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
