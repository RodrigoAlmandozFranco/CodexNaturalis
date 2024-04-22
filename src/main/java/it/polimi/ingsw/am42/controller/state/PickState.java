package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.controller.Hub;
import it.polimi.ingsw.am42.controller.state.State;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.structure.Position;
import org.controlsfx.control.action.Action;

import java.util.List;
import java.util.Set;

public class PickState implements State {
    private Game g;
    private Hub hub;
    private Action a;

    public PickState(Game g, Hub hub){
        this.g = g;
        this.hub = hub;
    }

    @Override
    public void handleAction() throws RuntimeException{
        do{
            try {
                a = hub.askAction();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }while(!(a.getNickname()==g.getCurrentPlayer().getNickname()));
        g.chosenCardToAddInHand(a.getChosenCard());
    }
}