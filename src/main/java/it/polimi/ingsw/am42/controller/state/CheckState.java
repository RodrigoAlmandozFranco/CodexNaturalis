package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.controller.Hub;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;

public class CheckState implements State {
    private Game g;
    private Hub hub;

    public CheckState(Game g, Hub hub){
        this.g = g;
        this.hub = hub;
    }

    @Override
    public void handleAction() throws RuntimeException {
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
