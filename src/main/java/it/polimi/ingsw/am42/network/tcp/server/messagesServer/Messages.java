package it.polimi.ingsw.am42.network.tcp.server.messagesServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.Game;

public class Messages {
    protected Controller controller;
    protected Game game;

    public Messages(Controller controller, Game game) {
        this.controller = controller;
        this.game = game;
    }


    public String execute(){
        return null;
    }

    protected Messages(){}
}
