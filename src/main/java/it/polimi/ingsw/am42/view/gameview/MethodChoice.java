package it.polimi.ingsw.am42.view.gameview;

import it.polimi.ingsw.am42.view.App.TUIApplication;

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
    SEEPICKABLECARDS,
    SEEGOALS,
    PLACE,
    PICK,
    SEECHAT,
    SENDMESSAGE,
    DISCONNECT;

    public String getChoice() {
        switch (this) {
            case PLACESTARTING -> {
                return "Place starting card";
            }
            case CHOOSECOLOR -> {
                return "Choose your color";
            }
            case CHOOSEGOAL -> {
                return "Choose your personal goal";
            }
            case SEEBOARD -> {
                return "See the board";
            }
            case SEECARDS -> {
                return "See your cards";
            }
            case SEESTANDINGS -> {
                return "See the standings";
            }
            case SEEPICKABLECARDS -> {
                return "See drawable cards";
            }
            case SEEGOALS -> {
                return "See your objectives";
            }
            case PLACE -> {
                return "Place your card";
            }
            case PICK -> {
                return "Draw a card";
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
            case SEEPICKABLECARDS -> {
                return TUIApplication::seePickableCards;
            }
            case SEEGOALS -> {
                return TUIApplication::seeGoals;
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
