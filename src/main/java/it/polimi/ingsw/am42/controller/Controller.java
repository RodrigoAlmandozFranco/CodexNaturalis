package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.state.InitialState;
import it.polimi.ingsw.am42.controller.state.State;
import it.polimi.ingsw.am42.model.Game;

public class Controller {
    private final Game g;
    private final Hub hub;
    private State currentState;

    public Controller(Game g, Hub hub) {
        this.g = g;
        this.hub = hub;

    }

    /**
     * This method is called by main and it changes the currentState
     *
     * @param currentState
     * @author Mattia Brandi
     */
    public void setState(State currentState) {
        this.currentState = currentState;
    }

    /**
     * This method is called by main, and it executes the method handleAction on the right
     * currentState
     *
     * @author Mattia Brandi
     */
    public void executionOfState () throws RuntimeException {
        try {
            currentState.handleAction();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

