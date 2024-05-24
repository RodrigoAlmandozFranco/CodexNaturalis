package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.model.GameInterface;
import it.polimi.ingsw.am42.model.Player;

import java.io.Serializable;

public enum State implements Serializable {

    INITIAL,

    SETHAND,

    SETCOLOR,

    SETGOAL,

    PLACE,

    PICK,
    LAST,
    DISCONNECTED;

    public State changeState(GameInterface game){
         switch (this){
             case INITIAL:
                 if(game.getPlayers().size() == game.getNumberPlayers()) {
                     game.initializeGame();
                     return SETHAND;
                 }
                 else return INITIAL;

             case SETHAND:
                 return SETCOLOR;

             case SETCOLOR:
                 game.initializeHandCurrentPlayer();
                 return SETGOAL;

             case SETGOAL:
                 if(game.getCurrentPlayer().equals(game.getPlayers().getLast()))
                     return PLACE;
                 else return SETHAND;


             case PLACE:
                 if(game.getTurnFinal() && game.getCurrentPlayer().equals(game.getPlayers().getLast()))
                     return LAST;

                 if(game.getTurnFinal())
                     return PLACE;
                 else  return PICK;


             case PICK:
                 if(game.getCurrentPlayer().equals(game.getPlayers().getLast()))
                     if (game.checkEndGameDecks() || game.checkEndGamePoints())
                         game.setTurnFinal(true);
                 return PLACE;

             case LAST:
                 return LAST;

             case DISCONNECTED:
                 return DISCONNECTED;

         }
        return null;
    }

    /**
     * This method is used by TUIApplication to show what is currently happening
     * @param currentPlayer nickname of acting player
     * @return
     */
    public String showState(String currentPlayer){
        switch (this){
            case INITIAL -> {
                return "Setting up";
            }
            case SETHAND -> {
                return currentPlayer + " is placing their starting card";
            }
            case SETCOLOR -> {
                return currentPlayer + " is choosing a color";
            }
            case SETGOAL -> {
                return currentPlayer + " is choosing the personal goal";
            }
            case PLACE -> {
                return currentPlayer + " is placing a card";
            }
            case PICK -> {
                return currentPlayer + " is drawing a card";
            }
            case LAST -> {
                return "The game has finished";
            }
            case DISCONNECTED -> {
                return "Someone disconnected";
            }
        }
        return  null;
    }
}
