package it.polimi.ingsw.am42.view;

import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.rmi.RMIClient;
import it.polimi.ingsw.am42.network.tcp.client.ClientTCP;
import it.polimi.ingsw.am42.view.App.App;
import it.polimi.ingsw.am42.view.App.GUIApplication;
import it.polimi.ingsw.am42.view.App.TUIApplication;
import it.polimi.ingsw.am42.view.gameview.GameView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 Executable for Client
 Parameters to add when launching file:
 --tui | -t: use TUI instead of GUI
 --rmi | -r : use RMI connection instead of TCP connection
 --port | -p : set port of server to next int
 --ip | -a : set address of server to next string
 **/
public class ClientMain extends UnicastRemoteObject {

    static private int PORT = 4203;

    static private String ADDRESS = "localhost";

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

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--ip") || args[i].equals("-a")) {
                ADDRESS = args[i + 1];
                break;
            }
        }

        Client client;


        if (rmiParam) {
            client = new RMIClient(ADDRESS, PORT);
        }
        else {
            try {
                client = new ClientTCP(ADDRESS, PORT);
            } catch (Exception e) {
                System.out.println("could not connect to server");
                System.exit(1);
                return;
            }
        }

        GameView view = new GameView();

        client.setView(view);

        App app;

        if (tuiParam)
                app = new TUIApplication(client);
        else
            app = new GUIApplication(client);

        app.start();
    }

}
