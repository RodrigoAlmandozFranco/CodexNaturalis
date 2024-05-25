package it.polimi.ingsw.am42.network.tcp.client;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.PingMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.ChangeMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.PlayerDisconnectedMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GoodMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


//TODO disconnected, close socket, while loop
public class ServerHandler implements Runnable {
    private volatile Message message;
    private Socket socket;
    private ClientTCP client;
    private boolean isRunning = true;

    private ObjectInputStream input;

    public ServerHandler(Socket socket, ClientTCP client) {
        this.socket = socket;
        this.client = client;
        message = null;
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            while(isRunning) {
                if (socket.getInputStream().available() > 0) {
                    Message m = (Message) input.readObject();
                    if(m instanceof ChangeMessage){
                        client.update(((ChangeMessage) m).getChange());
                    } else if (m instanceof ChatMessage) {
                        client.receiveMessage(m);
                    } else if(m instanceof PlayerDisconnectedMessage) {
                        client.updateDisconnection();
                    } else if(!(m instanceof PingMessage)){
                        synchronized (this) {
                            message = m;
                            notify();
                        }
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            closeAll();
            client.serverDown();
        }
    }

    public void closeAll() {
        try{
            input.close();
            isRunning = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Message getMessage() throws InterruptedException {
        synchronized (this) {
            while (message == null) {
                wait();
            }
            Message msg = message;
            message = null;

            return msg;
        }
    }
}

