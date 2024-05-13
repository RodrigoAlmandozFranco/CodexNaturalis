package it.polimi.ingsw.am42.view;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.view.gameview.GameView;

public abstract class View {
    protected String nickname;
    protected String currentPlayer;

    protected GameView gameview;

    public void update(Change diff){
        gameview.update(currentPlayer, diff);
        currentPlayer = diff.getFuturePlayer();
    }

    public void handleState() {
    }

    public void updateMessage(ChatMessage chatMessage) {

    }

    public void connectionClosed() {
    }
}
