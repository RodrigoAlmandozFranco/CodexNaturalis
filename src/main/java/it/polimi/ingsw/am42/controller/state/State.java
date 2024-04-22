package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.model.Game;

public interface State {
    public void handleAction () throws RuntimeException;
}
