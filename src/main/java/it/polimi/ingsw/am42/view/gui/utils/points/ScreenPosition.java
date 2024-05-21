package it.polimi.ingsw.am42.view.gui.utils.points;

import javafx.stage.Screen;

public class ScreenPosition {
    private int x;
    private int y;

    public ScreenPosition() {}



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
        return this;
    }


    private ScreenPosition execute(int point) {
        switch(point) {
            case 0:
                x = 244;
                y = 506;
                break;
            case 1:
                x = 311;
                y = 506;
                break;
            case 2:
                x = 378;
                y = 506;
                break;
            case 3:
                x = 411;
                y = 445;
                break;
            case 4:
                x = 344;
                y = 445;
                break;
            case 5:
                x = 278;
                y = 445;
                break;
            case 6:
                x = 211;
                y = 445;
                break;
            case 7:
                x = 211;
                y = 384;
                break;
            case 8:
                x = 278;
                y = 384;
                break;
            case 9:
                x = 344;
                y = 384;
                break;
            case 10:
                x = 411;
                y = 384;
                break;
            case 11:
                x = 411;
                y = 323;
                break;
            case 12:
                x = 344;
                y = 323;
                break;
            case 13:
                x = 278;
                y = 323;
                break;
            case 14:
                x = 211;
                y = 323;
                break;
            case 15:
                x = 211;
                y = 262;
                break;
            case 16:
                x = 278;
                y = 262;
                break;
            case 17:
                x = 344;
                y = 262;
                break;
            case 18:
                x = 411;
                y = 262;
                break;
            case 19:
                x = 411;
                y = 201;
                break;
            case 20:
                x = 311;
                y = 171;
                break;
            case 21:
                x = 211;
                y = 201;
                break;
            case 22:
                x = 211;
                y = 140;
                break;
            case 23:
                x = 211;
                y = 79;
                break;
            case 24:
                x = 249;
                y = 29;
                break;
            case 25:
                x = 311;
                y = 18;
                break;
            case 26:
                x = 372;
                y = 29;
                break;
            case 27:
                x = 411;
                y = 79;
                break;
            case 28:
                x = 411;
                y = 140;
                break;
            case 29:
                x = 311;
                y = 92;
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
