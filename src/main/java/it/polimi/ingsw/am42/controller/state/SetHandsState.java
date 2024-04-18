package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;

import java.util.List;

public class SetHandsState implements State {
    private Game g;
    public SetHandsState(Game g){
        this.g = g;
    }

    @Override
    public void handleAction() {
        g.initializeGameForPlayers();
        for(Player p : g.getPlayers()){
            List<GoalCard> goals = g.choosePersonalGoal();
            //TODO Now I have to send this list to the single client, he/she has to choose
            //TODO and I must receive back only one personal goal
            GoalCard goal;
            p.setGoal(goal);
        }

    }
}
