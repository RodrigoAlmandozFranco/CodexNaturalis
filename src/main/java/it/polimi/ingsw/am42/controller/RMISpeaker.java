package it.polimi.ingsw.am42.controller;

import java.rmi.Remote;

public interface RMISpeaker extends Remote {

    public void addAction(Action a);

}
