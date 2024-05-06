package it.polimi.ingsw.am42.network.tcp.server;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.GenericException;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.network.rmi.RMISpeaker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class is the server that listens for incoming connections from clients
 * and creates a new thread and a new ClientHandler for each client.
 * The ClientHandler will handle the communication with the client.
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class Server {
    public static void main( String[] args ) throws GenericException {
        System.out.println( "Hello from Server!" );
        int port = 4203;

        Controller controller = new Controller();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p") && i + 1 < args.length) {
                port = Integer.parseInt(args[i + 1]);
                i++;
            }
        }
        printIpAddress();

        startServerRMI(controller, port);

        startServerTCP(controller, port);
    }

    public static void printIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("IP Address: " + inetAddress.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Could not find IP address: " + e.getMessage());
        }
    }

    public static void startServerRMI(Controller controller, int port) throws GenericException {
        RMISpeaker stub = null;
        try {
            stub = (RMISpeaker) UnicastRemoteObject.exportObject(
                    controller, port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Bind the remote object's stub in the registry
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            registry.bind("RMISpeaker", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("Server RMI ready");
    }

    public static void startServerTCP(Controller controller, int port) throws GenericException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new GenericException("Error while creating the server socket");
        }
        System.err.println("Server TCP ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new ClientHandler(socket, controller));
            } catch (IOException e) {
                break;
            }
        }
        executor.shutdown();
    }
}