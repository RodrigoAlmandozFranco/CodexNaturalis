package it.polimi.ingsw.am42.network.rmi;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public interface RMIMessageListener extends MessageListener, Remote {

    public String getId() throws RemoteException;

    public void update(Change diff) throws RemoteException;

    public void receiveMessage(Message message) throws RemoteException;

    public boolean heartbeat() throws RemoteException;
}
