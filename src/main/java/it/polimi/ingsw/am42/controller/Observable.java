package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    List<MessageListener> listeners = new ArrayList<>();
    public void addListener(MessageListener l) {
        listeners.add(l);
    }
    public void removeListener(MessageListener l) {
        listeners.remove(l);
    }
    protected void updateAll(Change message){
        for(MessageListener l : listeners)
            l.update(message);
    }

    protected void sendMessageAll(Message message) {
        for(MessageListener l : listeners)
            // TODO make custom update message different from Change
            l.receiveMessage(smessage);
    }

    protected void sendMessage(Message message, String id) {
        for(MessageListener l : listeners)
            if (l.getId().equals(id))
                // TODO make custom update message different from Change
                l.receiveMessage(smessage);
    }


}
