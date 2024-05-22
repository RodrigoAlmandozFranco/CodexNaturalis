package it.polimi.ingsw.am42.network.tcp.client;

import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
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
            while(true) {
                if (socket.getInputStream().available() > 0) {
                    Message m = (Message) input.readObject();
                    if(m instanceof ChangeMessage){
                        client.update(((ChangeMessage) m).getChange());
                    } else if (m instanceof ChatMessage) {
                        client.receiveMessage(m);
                    } else if(m instanceof PlayerDisconnectedMessage) {
                        client.updateDisconnection();
                    } else {
                        synchronized (this) {
                            message = m;
                            notify();
                        }
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            //If everything ok, when the server stops the connection
            //I will receive this Exception
            try{
                socket.close();
                input.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            client.connectionClosed();
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

