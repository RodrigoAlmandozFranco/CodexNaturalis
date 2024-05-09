package it.polimi.ingsw.am42.network;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.GenericException;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.network.rmi.RMIHandler;
import it.polimi.ingsw.am42.network.rmi.RMISpeaker;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

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

    static private int PORT = 4203;
    public static void main( String[] args ) throws GenericException {
        System.out.println( "Hello from Server!" );
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p") && i + 1 < args.length) {
                PORT = Integer.parseInt(args[i + 1]);
                i++;
            }
        }
        Controller controller = new Controller();
        printIpAddress();

        startServerRMI(controller);

        startServerTCP(controller);
    }

    public static void printIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("IP Address: " + inetAddress.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Could not find IP address: " + e.getMessage());
        }
    }

    public static void startServerRMI(Controller controller) throws GenericException {

        RMIHandler obj = new RMIHandler(controller);

        RMISpeaker stub = null;
        try {
            stub = (RMISpeaker) UnicastRemoteObject.exportObject(
                    obj, PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Bind the remote object's stub in the registry
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(PORT);
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
        System.out.println("Server RMI ready");
    }

    public static void startServerTCP(Controller controller) throws GenericException {

        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(PORT+1);
        } catch (IOException e) {
            throw new GenericException(e.getMessage());
        }
        System.out.println("Server TCP ready");
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