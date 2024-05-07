package it.polimi.ingsw.am42.network.tcp.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

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
    private ClientToServerMessage message;
    private PrintWriter out;
    private ObjectInputStream input;
    private ObjectOutputStream output;


    public ClientHandler(Socket socket, Controller controller) throws IOException {
        this.socket = socket;
        this.controller = controller;
        message = new ClientToServerMessage(controller, this);
        out = new PrintWriter(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run() {
        try {
            while(true) {
                if(socket.getInputStream().available() > 0) {
                    Message message = (Message) input.readObject();
                    Message answer;


                    if(message instanceof ChatMessage) {
                        controller.sendChatMessage((ChatMessage) message);
                    } else {
                        answer = message.execute();
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

    private void sendMessage(Message answer) {
        try {
            output.writeObject(answer);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playerDisconnected() {
        controller.playerDisconnected();
    }

    public void update (Change change) {
        sendMessage(change);
    }
    public String getId(){
        return null;
    }

}
