package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Message;

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
    protected void updateAll(Message message){
        for(MessageListener l : listeners)
            l.update(message);
    }
    protected void update(Message message, String id){
        for(MessageListener l : listeners)
            if (l.getId().equals(id))
                l.update(message);
    }
}
