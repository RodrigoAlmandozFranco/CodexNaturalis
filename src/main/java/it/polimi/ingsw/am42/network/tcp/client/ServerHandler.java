package it.polimi.ingsw.am42.network.tcp.client;

import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.ChangeMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GoodMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.PlayerDisconnectedMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


//TODO disconnected, close socket, while loop
public class ServerHandler implements Runnable {
    private Message message;
    private Socket socket;
    private boolean newMessage = false;
    private boolean isRunning = true;
    private ClientTCP client;

    public ServerHandler(Socket socket, ClientTCP client) {
        this.socket = socket;
        this.client = client;
        message = null;
    }

    @Override
    public void run() {
        try {
            //System.out.println("ServerHandler ready!");
            while(isRunning) {
                if (socket.getInputStream().available() > 0 && !newMessage) {
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    Message m = (Message) input.readObject();
                    if(m instanceof ChangeMessage){
                        client.update(((ChangeMessage) m).getChange());
                    } else if (m instanceof ChatMessage) {
                        client.updateMessage((ChatMessage) m);
                    } else if(m instanceof PlayerDisconnectedMessage) {
                        client.updateDisconnection();
                    } else if (!(m instanceof GoodMessage)) {
                        message = m;
                        newMessage = true;
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopThread() {
        isRunning = false;
    }

    public Message getMessage () {

        while(message == null || !newMessage) {}
        Message msg = message;
        newMessage = false;
        message = null;

        return msg;
    }
}

