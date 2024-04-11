package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;

public class CheckState implements State{
    private Game g;

    public CheckState(Game g){
        this.g = g;
    }

    @Override
    public void handleAction() {
        boolean endGamePoints = g.checkEndGamePoints();
        if(endGamePoints){
            if(g.getCurrentPlayer() == g.getPlayers().getLast()){
                g.setTurnFinal(true);
            }
        }
        if(g.checkEndGameDecks()){
            //TODO
        }

        Player p = g.getNextPlayer();
        g.setCurrentPlayer(p);
    }
}
