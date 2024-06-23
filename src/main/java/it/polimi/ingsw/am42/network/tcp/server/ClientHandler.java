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

    /**
     * This method waits for messages from the client and managed them based on their type
     * This method continues to listen for incoming messages from the client until the connection is open
     * It always sends back a response message to the client
     *
     * @throws RuntimeException if the message's class is not known
     */
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

    /**
     * This method closes the input and the output streams
     * It closes the socket connection
     *
     * @throws RuntimeException if something goes wrong during the closing process
     */
    private void closeAll() {
        try{
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * This method sets the nickname
     *
     * @param nickname nickname of the Player
     */
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     * This method sends message to the client using the output stream
     *
     * @param answer message to be sent
     * @throws IOException if something goes wring during the sending process
     */
    private void sendMessage(Message answer) throws IOException {
        if(isRunning) {
            synchronized (output) {
                output.writeObject(answer);
                output.flush();
            }
        }
    }

    /**
     * This method receives a message and sends it to the client if the connection is still open
     * If occurs an error the server sets the flag to stop running and manages the player disconnection
     *
     * @param message message to be sent to the client
     *
     */
    public void receiveMessage(Message message) {
        if (isRunning) {
            try {
                sendMessage(message);
            } catch (IOException e) {
                isRunning = false;
                playerDisconnected();
            }
        }
    }

    /**
     * This method manages the Player's disconnection
     * It controls if the ClientHandler is contained in the list of listeners and manages the disconnection
     * It sets a flag to stop running
     *
     */
    private void playerDisconnected() {
        if (controller.getListeners().contains(this))
            controller.playerDisconnected();
        isRunning = false;
    }

    /**
     * This method is used to notify the client that it has to update its internal values
     * with those one contained in the Change
     * If occurs an error the server sets the flag to stop running and manages the player disconnection
     *
     * @param change Change object made by the server that contains all the update values
     *
     */
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

    /**
     * This method controls if the client is still connected and manages
     * the player's disconnection if the player has closed the connection
     * If everything is ok it returns. If it catches an IOException it sets
     * the flag to stop running the server and manages the player's disconnection
     */
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

    /**
     * This method returns the nickname of the player associated to the ClientHandler
     *
     * @return nickname of the Player
     */
    public String getId(){
        return nickname;
    }

    /**
     * This method controls if the server is running
     *
     * @throws RuntimeException when the Thread isn't running
     */
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
