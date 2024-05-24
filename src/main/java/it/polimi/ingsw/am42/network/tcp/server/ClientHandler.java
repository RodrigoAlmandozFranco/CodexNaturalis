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
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.PlayerDisconnectedMessage;
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
        new Thread(this::checkIsRunning).start();

        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run() {
        try {
            while(isRunning) {
                if(socket.getInputStream().available() > 0) {
                    Message message = (Message) input.readObject();
                    Message answer;

                    if(message instanceof ChatMessage) {
                        controller.sendChatMessage((ChatMessage) message);
                    } else if (message instanceof PlayerDisconnectedMessage) {
                        playerDisconnected();

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
        if(isRunning) {
            synchronized (output) {
                output.writeObject(answer);
                output.flush();
            }
        }
    }

    public void receiveMessage(Message message) {
        if(isRunning) {
            try {
                sendMessage(message);
            } catch (IOException e) {
                isRunning = false;
                playerDisconnected();
            }
        }
    }


    private void playerDisconnected() {
        if (controller.getListeners().contains(this))
            controller.playerDisconnected();
        isRunning = false;
    }

    public void update (Change change) {
        if(isRunning) {
            Message message = new ChangeMessage(change);
            try {
                sendMessage(message);
            } catch (IOException e) {
                isRunning = false;
                playerDisconnected();
            }
        }
    }


    public boolean heartbeat() {
        try {
            sendMessage(new PingMessage());
            return true;
        } catch (IOException e) {
            playerDisconnected();
            isRunning = false;
            closeAll();
            return false;
        }
    }

    public String getId(){
        return nickname;
    }

    private void checkIsRunning() {
        while(isRunning)
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        System.exit(1);
    }
}
