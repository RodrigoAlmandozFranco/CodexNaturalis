package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.model.Game;

public class InitialState implements State {

    Game g;

    public InitialState(Game g) {
        this.g = g;
    }

    public void handleAction() {
        g.initializeGame();
        int num = g.getNumberPlayers();
        int i = 0;
        while (i < num) {
            //TODO I think that here should be a getter from the view
            try {
                g.addToGame(nickname);
            } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                     it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                     it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
                throw new RuntimeException(e);
            }
            i++;
        }

    }
}
