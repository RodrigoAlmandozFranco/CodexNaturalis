package it.polimi.ingsw.am42.network.tcp.server;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.GenericException;
import it.polimi.ingsw.am42.model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is the server that listens for incoming connections from clients
 * and creates a new thread and a new ClientHandler for each client.
 * The ClientHandler will handle the communication with the client.
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class ServerTCP {
    private int port;
    private Controller controller;
    private Game game;

    public ServerTCP(int port) {
        this.port = port;
    }


    /**
     * This method sets the controller and the game for the server
     * @param controller
     * @param game
     */
    public void setComponents(Controller controller, Game game){
        this.controller = controller;
        this.game = game;
    }


    public void startServer() throws GenericException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new GenericException("Error while creating the server socket");
        }

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new ClientHandler(socket, controller, game));
            } catch (IOException e) {
                break;
            }
        }
        executor.shutdown();
    }
}

