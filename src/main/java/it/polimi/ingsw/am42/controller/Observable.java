package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.PlayerDisconnectedMessage;

import java.rmi.RemoteException;
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

    private long HEARTBEAT_INTERVAL = 15000; // 15 seconds

    public Observable() {

        listeners = new CopyOnWriteArrayList<>(); // Thread safe list
        timer = new Timer();
        timer.scheduleAtFixedRate(new HeartbeatTask(), HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL);

    }


    /**
     * Thread to check if all players are still connected
     */
    private class HeartbeatTask extends TimerTask {
        @Override
        public void run() {
            for (MessageListener l : listeners) {
                try {
                    boolean result = l.heartbeat();
                    if(!result) {
                        listeners.remove(l);
                        handleDisconnection();
                    }
                } catch (Exception e) {
                    listeners.remove(l);
                    handleDisconnection();
                }
            }
        }
    }

    /**
     * Adds a listener that can be notified
     * @param l Reference to listener
     */
    public void addListener(MessageListener l) {
        listeners.add(l);
    }

    /**
     * Get reference to all the listeners
     * @return list of listeners
     */
    public List<MessageListener> getListeners() {
        return listeners;
    }

    /**
     * Notify every listener with the following Change message
     * @param message diff of the effects caused by an action
     */
    protected void updateAll(Change message){
        System.out.println("Updating listeners");
        for(MessageListener l : listeners) {
            try {
                l.update(message);
            } catch (Exception e) {
                listeners.remove(l);
                handleDisconnection();
            }
        }
    }

    /**
     * Notify everyone with a general message
     * @param message unspecified message
     */
    protected void sendMessageAll(Message message) {
        for(MessageListener l : listeners) {
            try {
                l.receiveMessage(message);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Send a message to a specific listener
     * @param message generic message
     * @param id identifier of the listener
     */
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



    /**
     * Method called by heartbeat task to handle disconnections
     */
    protected void handleDisconnection() {
        sendMessageAll(new PlayerDisconnectedMessage());
    }


}
