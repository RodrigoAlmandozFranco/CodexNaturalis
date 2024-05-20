package it.polimi.ingsw.am42.network;


import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * This interface is used for the observer design pattern.
 * Implemented by the classes that want to observe the observer
 *
 * @author Tommaso Crippa
 */
public interface MessageListener {

        public String getId() throws RemoteException;

        public void update(Change diff) throws RemoteException;

        public void receiveMessage(Message message) throws RemoteException;

        public boolean heartbeat() throws RemoteException;
}
