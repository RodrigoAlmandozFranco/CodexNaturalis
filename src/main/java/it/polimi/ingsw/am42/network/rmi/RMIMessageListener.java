package it.polimi.ingsw.am42.network.rmi;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * Auxiliary class needed for RMI clients to be called by the controller
 * @see Controller
 * @see RMIClient
 * Transforms custom exceptions into RemoteExceptions
 *
 * @author Tommaso Crippa
 * @author Alessandro Di Maria
 */
public interface RMIMessageListener extends MessageListener, Remote {

    public String getId() throws RemoteException;

    public void update(Change diff) throws RemoteException;

    public void receiveMessage(Message message) throws RemoteException;

    public boolean heartbeat() throws RemoteException;
}
