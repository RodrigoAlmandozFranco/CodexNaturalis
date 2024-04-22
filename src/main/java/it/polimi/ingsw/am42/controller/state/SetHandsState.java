package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.controller.Hub;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import org.controlsfx.control.action.Action;

import java.util.List;

public class SetHandsState implements State {
    private Hub hub;
    private Action a;
    private Game g;

    public SetHandsState(Game g, Hub hub) {
        this.g = g;
        this.hub = hub;
    }

    @Override
    public void handleAction() throws RuntimeException  {
        g.initializeGameForPlayers();
        for(Player p : g.getPlayers()){
            List<GoalCard> goals = g.choosePersonalGoal();
            //TODO Now I have to send this list to the single client, he/she has to choose
            do{
                try {
                    a = hub.askAction();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }while(!(a.getNickname()==p.getNickname()));
            p.setGoal(a.getPersonalGoal());
        }
    }
}
