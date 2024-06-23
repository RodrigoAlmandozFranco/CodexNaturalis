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

public class Server extends UnicastRemoteObject {

    static private int PORT = 4203;

    protected Server() throws RemoteException {
    }

    public static void main( String[] args ) throws GenericException {
        System.out.println( "Hello from Server!" );
        for (int i = 0; i < args.length; i++) {
            if ((args[i].equals("-p") || (args[i].equals("--port"))) && i + 1 < args.length) {
                PORT = Integer.parseInt(args[i + 1]);
                i++;
            }
        }
        Controller controller = new Controller();
        System.out.println("IP Address: " + getIpAddress());
        System.out.println("PortRMI: " + PORT);
        System.out.println("PortTCP: " + (PORT + 1));
        startServerRMI(controller);

        startServerTCP(controller);
    }

    private static String getIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }


    public static void startServerRMI(Controller controller) throws GenericException {

        // System.setProperty("java.rmi.server.hostname",getIpAddress());

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
            registry.bind("RMIHandler", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.out.println("Server RMI ready");
    }

    /**
     * This method starts the TCP connection for the server
     * It initializes a server Socket on a specified port and waits for the clients' connections
     * It manages each connection using a separate thread
     *
     * @param controller passed to each ClientHandler created
     * @throws GenericException if there is an error creating a new ServerSocket
     */

    public static void startServerTCP(Controller controller) throws GenericException {

        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(PORT + 1);
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


