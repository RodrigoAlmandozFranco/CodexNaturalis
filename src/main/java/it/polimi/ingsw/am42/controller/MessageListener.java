package it.polimi.ingsw.am42.controller;

public interface MessageListener {

        public String getId();

        public void update(Message message);
}
