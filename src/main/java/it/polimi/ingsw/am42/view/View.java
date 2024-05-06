package it.polimi.ingsw.am42.view;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.controller.state.State;
import it.polimi.ingsw.am42.view.gameview.GameView;

public abstract class View {
    protected String nickname;
    protected String currentPlayer;
    protected State currentState;

    protected GameView gameview;

    public void update(Change diff){
        gameview.update(currentPlayer, diff);
        currentPlayer = diff.getFuturePlayer();
        currentState = diff.getCurrentState();
    }

}
