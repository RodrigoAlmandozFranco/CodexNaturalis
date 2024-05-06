package it.polimi.ingsw.am42.view;

import it.polimi.ingsw.am42.network.tcp.messages.Message;

public abstract class View {

    public abstract void update(Message message);

}
