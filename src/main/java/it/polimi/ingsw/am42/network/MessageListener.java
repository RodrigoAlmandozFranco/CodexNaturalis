package it.polimi.ingsw.am42.network;


import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.rmi.Remote;


/**
 * This interface is used for the observer design pattern.
 * Implemented by the classes that want to observe the observer
 *
 * @author Tommaso Crippa
 */
public interface MessageListener {

        public String getId();

        public void update(Change diff);

        public void receiveMessage(Message message);

        public boolean heartbeat();
}
