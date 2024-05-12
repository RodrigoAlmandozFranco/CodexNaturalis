package it.polimi.ingsw.am42.view;

import com.sun.javafx.logging.jfr.JFRInputEvent;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.rmi.RMIClient;
import it.polimi.ingsw.am42.network.tcp.client.ClientTCP;
import javafx.application.Application;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientMain extends UnicastRemoteObject {

    static private int PORT = 4203;

    protected ClientMain() throws RemoteException {
    }

    public static void main(String[] args) {
        boolean tuiParam = false;
        boolean rmiParam = false;


        for (String arg : args) {
            if (arg.equals("--tui") || arg.equals("-t")) {
                tuiParam = true;
                break;
            }
        }

        for (String arg : args) {
            if (arg.equals("--rmi") || arg.equals("-r")) {
                rmiParam = true;
                break;
            }
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--port") || args[i].equals("-p")) {
                PORT = Integer.parseInt(args[i+1]);
                break;
            }
        }

        if (tuiParam) {
            connectToIP(rmiParam);

        } else {
            // Application.launch(Gui.class);
        }
    }

    private static void connectToIP(boolean rmiParam) {
        Client client;

        System.out.println("Insert server IP address:");

        Scanner scanner = new Scanner(System.in);
        System.out.print("IP: ");

        // Read the input provided by the user
        String address = scanner.nextLine();
        if (address.equals("")) address = "localhost";

        if (rmiParam) {
            client = new RMIClient(address, PORT);
        }
        else {
            try {
                client = new ClientTCP(address, PORT);
            } catch (Exception e) {
                System.out.println("could not connect to server");
                System.exit(1);
            }
        }


    }
}
