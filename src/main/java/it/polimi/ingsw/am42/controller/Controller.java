package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.state.InitialState;
import it.polimi.ingsw.am42.controller.state.State;
import it.polimi.ingsw.am42.model.Game;

public class Controller {
    private final Game g;
    private State currentState;
    public Controller (Game g){
        this.g = g;
        currentState = new InitialState(g);

    }

    /**
     * This method is called by main and it changes the currentState
     * @param currentState
     * @author Mattia Brandi
     */
    public void setState(State currentState){
        this.currentState = currentState;
    }

    /**
     * This method is called by main, and it executes the method handleAction on the right
     * currentState
     * @author Mattia Brandi
     */
    public void executionOfState (){
        currentState.handleAction();

    }
}
