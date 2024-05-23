package it.polimi.ingsw.am42.network.tcp.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.PingMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.ChangeMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendWinnerMessage;

/**
 * This class is responsible for handling the connection with the client.
 * It receives messages from the client and sends them to the controller.
 * It also sends messages to the client.
 * The answers of the ClientHandler could be to a singleClient or to all the clients
 * thanks to the updateAll() in MessageListener.
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class ClientHandler implements Runnable, MessageListener {
    private Socket socket;
    private Controller controller;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String nickname;
    private boolean isRunning = true;


    public ClientHandler(Socket socket, Controller controller) throws IOException {
        this.socket = socket;
        this.controller = controller;

        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run() {
        try {
            new Thread(this::heartbeatTCP).start();
            while(isRunning) {
                if(socket.getInputStream().available() > 0) {
                    Message message = (Message) input.readObject();
                    Message answer;

                    if(message instanceof ChatMessage) {
                        controller.sendChatMessage((ChatMessage) message);
                    } else if (!(message instanceof PingMessage)) {
                        answer = message.execute(this, controller);
                        sendMessage(answer);
                    }
                }
            }
        } catch (IOException e) {
            playerDisconnected();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void heartbeatTCP() {
        while (isRunning) {
            try {
                sendMessage(new PingMessage());
                Thread.sleep(15000);
            } catch (InterruptedException | IOException e) {
                playerDisconnected();
                isRunning = false;
                closeAll();
            }
        }
    }

    private void closeAll() {
        try{
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    private void sendMessage(Message answer) throws IOException {
        synchronized (output) {
            output.writeObject(answer);
            output.flush();
        }
    }

    public void receiveMessage(Message message) {
        try {
            sendMessage(message);
        } catch (IOException e) {
            playerDisconnected();
        }
    }


    private void playerDisconnected() {
        controller.playerDisconnected();
    }

    public void update (Change change) {
        Message message = new ChangeMessage(change);
        try {
            sendMessage(message);
        } catch (IOException e) {
            playerDisconnected();
        }
    }


    public boolean heartbeat() {
        return true;
    }

    public String getId(){
        return nickname;
    }

}
