package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.controller.Hub;
import it.polimi.ingsw.am42.model.Game;
import org.controlsfx.control.action.Action;

import java.util.List;

public class InitialState implements State {

    private Game g;
    private Hub hub;
    private List<Integer> tmp;
    private Action a;


    public InitialState(Game g, Hub hub) {
        this.g = g;
        this.hub = hub;
        tmp = null;
    }

    public void handleAction() throws RuntimeException {
        g.initializeGame();
        int num = g.getNumberPlayers();
        while (tmp.size() < num) {
            try {
                a = hub.askAction();  //I assume that will be a specific ask for each Action
                //askNicknameAction();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!tmp.contains(a.getId())) {
                try {
                    g.addToGame(a.getNickname());
                    tmp.add(a.getId());
                } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                         it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                         it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
