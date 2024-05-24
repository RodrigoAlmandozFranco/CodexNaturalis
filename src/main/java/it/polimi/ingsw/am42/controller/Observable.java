package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.PlayerDisconnectedMessage;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * This class acts as the observer in the observer design pattern
 *
 * @author Tommaso Crippa
 */
public abstract class Observable {

    protected List<MessageListener> listeners;
    private Timer timer;

    private long HEARTBEAT_INTERVAL = 30000; // 30 seconds

    public Observable() {

        listeners = new CopyOnWriteArrayList<>(); // Thread safe list
        timer = new Timer();
        timer.scheduleAtFixedRate(new HeartbeatTask(), HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL);

    }



    private class HeartbeatTask extends TimerTask {
        @Override
        public void run() {
            for (MessageListener l : listeners) {
                try {
                    l.heartbeat();
                } catch (Exception e) {
                    handleDisconnection();
                }
            }
        }
    }

    public void addListener(MessageListener l) {
        listeners.add(l);
    }

    public List<MessageListener> getListeners() {
        return listeners;
    }

    protected void updateAll(Change message){
        System.out.println("Updating listeners");
        for(MessageListener l : listeners) {
            try {
                l.update(message);
            } catch (RemoteException e) {
                //TODO qui un client si potrebbe essere disconesso quindi si potrebbe pensare di passare allo stato DISCONNECTED
                throw new RuntimeException(e);
            }
        }
    }

    protected void sendMessageAll(Message message) {
        for(MessageListener l : listeners) {
            try {
                l.receiveMessage(message);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void sendMessage(Message message, String id) {
        for(MessageListener l : listeners) {
            try {
                if (l.getId().equals(id))
                    l.receiveMessage(message);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    protected void handleDisconnection() {
        sendMessageAll(new PlayerDisconnectedMessage());
    }


}
