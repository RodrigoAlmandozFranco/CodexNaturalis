package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;

import java.util.List;

public class FinalState implements State{

    Game g;

    public FinalState(Game g){
        this.g = g;
    }

    @Override
    public void handleAction() {
        List<Player> winner = g.getWinner();
        //TODO it shows the winner and it communicates to the others that the game is ended
    }
}
