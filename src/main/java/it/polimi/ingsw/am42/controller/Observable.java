package it.polimi.ingsw.am42.controller;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    List<MessageListener> listeners = new ArrayList<>();
    public addListener(MessageListener l) {
        listeners.add(l);
    }
    public removeListener(MessageListener l) {
        listeners.remove(l);
    }
    protected updateAll(Message message){
        for(MessageListener l : listeners)
            l.update(message);
    }
    protected update(Message message, String id){
        for(MessageListener l : listeners)
            if (l.getId().equals(id))
                l.update(message);
    }
}
