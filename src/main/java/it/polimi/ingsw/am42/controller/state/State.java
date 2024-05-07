package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;

public enum State {

    INITIAL,

    SETHAND,

    SETCOLOR,

    SETGOAL,

    PLACE,

    PICK,
    LAST;

    public State changeState(Game game){
         switch (this){
             case INITIAL:
                 if(game.getCurrentPlayer().equals(game.getPlayers().getLast()))
                     return SETHAND;
                 else return INITIAL;

             case SETHAND:
                 return SETCOLOR;

             case SETCOLOR:
                 return SETGOAL;

             case SETGOAL:
                 if(game.getCurrentPlayer().equals(game.getPlayers().getLast()))
                     return SETHAND;
                 else return PLACE;

             case PLACE:
                 if(game.getTurnFinal() && game.getCurrentPlayer().equals(game.getPlayers().getLast()))
                     return LAST;
                 if(game.getCurrentPlayer().equals(game.getPlayers().getLast())) {
                     for (Player p : game.getPlayers())
                         if(p.getPoints() >= 20){
                             game.setTurnFinal(true);
                         }
                 }
                 if(game.getTurnFinal())
                     return PLACE;
                 else  return PICK;

             case PICK:
                 return PLACE;

             case LAST:
                 return LAST;

         }
        return null;
    }
}
