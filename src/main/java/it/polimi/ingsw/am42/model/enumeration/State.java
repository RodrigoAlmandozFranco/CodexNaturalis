package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.model.GameInterface;
import it.polimi.ingsw.am42.model.Player;

public enum State {

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
                 if(game.getCurrentPlayer().equals(game.getPlayers().getLast()))
                     if (game.checkEndGameDecks() || game.checkEndGamePoints())
                         game.setTurnFinal(true);

                 if(game.getTurnFinal())
                     return PLACE;
                 else  return PICK;

             case PICK:
                 return PLACE;

             case LAST:
                 return LAST;

             case DISCONNECTED:
                 return DISCONNECTED;

         }
        return null;
    }
}
