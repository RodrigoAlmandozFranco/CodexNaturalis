package it.polimi.ingsw.am42.view.clientModel;

import it.polimi.ingsw.am42.view.app.TUIApplication;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

/**
 * Enum of all the possible methods that can be called by TUI client
 * @author Tommaso Crippa
 */
public enum MethodChoice {
    PLACESTARTING,
    CHOOSECOLOR,
    CHOOSEGOAL,
    SEEBOARD,
    SEECARDS,
    SEESTANDINGS,
    SEEGOALS,
    SEEPICKABLE,
    PLACE,
    PICK,
    SEECHAT,
    SENDMESSAGE,
    DISCONNECT,
    RELOAD;

    public String getChoice() {
        switch (this) {
            case RELOAD -> {
                return "Reload choices";
            }
            case PLACESTARTING -> {
                return ColorChooser.YELLOW + "Place starting card" + ColorChooser.RESET;
            }
            case CHOOSECOLOR -> {
                return ColorChooser.YELLOW + "Choose your color" + ColorChooser.RESET;
            }
            case CHOOSEGOAL -> {
                return ColorChooser.YELLOW + "Choose your personal goal" + ColorChooser.RESET;
            }
            case SEEBOARD -> {
                return "See the board";
            }
            case SEECARDS -> {
                return "See your cards";
            }
            case SEEPICKABLE -> {
                return  "See pickable cards";
            }
            case SEESTANDINGS -> {
                return "See the standings";
            }
            case SEEGOALS -> {
                return "See your objectives";
            }
            case PLACE -> {
                return ColorChooser.YELLOW + "Place your card" + ColorChooser.RESET;
            }
            case PICK -> {
                return ColorChooser.YELLOW + "Draw a card" + ColorChooser.RESET;
            }
            case SEECHAT -> {
                return "Check the chat";
            }
            case SENDMESSAGE -> {
                return "Send a message in the chat";
            }
            case DISCONNECT -> {
                return "Exit the game";
            }
        }
        return "Not a valid method";
    }

    public Runnable selectChoice() {
        switch (this) {

            case RELOAD -> {
                return TUIApplication::selectChoice;
            }
            case PLACESTARTING -> {
                return TUIApplication::placeStarting;
            }
            case CHOOSECOLOR -> {
                return TUIApplication::chooseColor;
            }
            case CHOOSEGOAL -> {
                return TUIApplication::chooseGoal;
            }
            case SEEBOARD -> {
                return TUIApplication::seeBoard;
            }
            case SEECARDS -> {
                return TUIApplication::seeCards;
            }
            case SEESTANDINGS -> {
                return TUIApplication::seeStandings;
            }
            case SEEGOALS -> {
                return TUIApplication::seeGoals;
            }
            case SEEPICKABLE ->  {
                return TUIApplication::seePickableCards;
            }
            case PLACE -> {
                return TUIApplication::place;
            }
            case PICK -> {
                return TUIApplication::pick;
            }
            case SEECHAT -> {
                return TUIApplication::seeChat;
            }
            case SENDMESSAGE -> {
                return TUIApplication::sendMessage;
            }
            case DISCONNECT -> {
                return TUIApplication::disconnect;
            }
        }
        return TUIApplication::selectChoice;
    }
}
