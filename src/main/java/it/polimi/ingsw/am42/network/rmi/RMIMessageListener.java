package it.polimi.ingsw.am42.network.rmi;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIMessageListener extends MessageListener, Remote {

    public String getId();

    public void update(Change diff);

    public void receiveMessage(Message message);

    public boolean heartbeat();
}
