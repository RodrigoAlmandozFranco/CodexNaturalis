package it.polimi.ingsw.am42.network;

import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Message;

public interface MessageListener {

        public String getId();

        public void update(Message message);
}
