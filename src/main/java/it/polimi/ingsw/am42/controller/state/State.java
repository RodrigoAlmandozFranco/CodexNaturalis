package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.model.Game;

public abstract class State {

    // TODO: make next state for every subclass
    public abstract State nextState(Game game);
}
