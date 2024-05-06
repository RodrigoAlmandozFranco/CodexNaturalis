package it.polimi.ingsw.am42.network;


import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

public interface MessageListener {

        public String getId();

        public void update(Change diff);
}
