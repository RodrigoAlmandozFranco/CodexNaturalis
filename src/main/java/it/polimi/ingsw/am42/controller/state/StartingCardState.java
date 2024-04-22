package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.controller.Hub;
import it.polimi.ingsw.am42.model.Game;
import org.controlsfx.control.action.Action;

public class StartingCardState implements State {
    private Game g;
    private Hub hub;
    private Action a;

    public StartingCardState(Game g, Hub hub){
        this.g = g;
        this.hub = hub;
    }

    @Override
    public void handleAction() throws RuntimeException{


        //TODO add starting
        //TODO choose color

    }
}
