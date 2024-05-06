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


}
