package it.polimi.ingsw.am42.view.App;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.view.App.App;
import it.polimi.ingsw.am42.view.IOHandler;
import it.polimi.ingsw.am42.view.gameview.GameView;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TUIApplication extends App {

    private String nickname;
    private final IOHandler io;


    public TUIApplication(Client client) {
        super(client);
        this.io = new IOHandler();

        printLogo();

    }

    private void printLogo() {
        io.print( "\u001B[33m" +
                "\n" +
                "                ▄████████  ▄██████▄  ████████▄     ▄████████ ▀████    ▐████▀                        \n" +
                "               ███    ███ ███    ███ ███   ▀███   ███    ███   ███▌   ████▀                         \n" +
                "               ███    █▀  ███    ███ ███    ███   ███    █▀     ███  ▐███                           \n" +
                "               ███        ███    ███ ███    ███  ▄███▄▄▄        ▀███▄███▀                           \n" +
                "               ███        ███    ███ ███    ███ ▀▀███▀▀▀        ████▀██▄                            \n" +
                "               ███    █▄  ███    ███ ███    ███   ███    █▄    ▐███  ▀███                           \n" +
                "               ███    ███ ███    ███ ███   ▄███   ███    ███  ▄███     ███▄                         \n" +
                "               ████████▀   ▀██████▀  ████████▀    ██████████ ████       ███▄                        \n" +
                "                                                                                                    \n" +
                "███▄▄▄▄      ▄████████     ███     ███    █▄     ▄████████    ▄████████  ▄█        ▄█     ▄████████ \n" +
                "███▀▀▀██▄   ███    ███ ▀█████████▄ ███    ███   ███    ███   ███    ███ ███       ███    ███    ███ \n" +
                "███   ███   ███    ███    ▀███▀▀██ ███    ███   ███    ███   ███    ███ ███       ███▌   ███    █▀  \n" +
                "███   ███   ███    ███     ███   ▀ ███    ███  ▄███▄▄▄▄██▀   ███    ███ ███       ███▌   ███        \n" +
                "███   ███ ▀███████████     ███     ███    ███ ▀▀███▀▀▀▀▀   ▀███████████ ███       ███▌ ▀███████████ \n" +
                "███   ███   ███    ███     ███     ███    ███ ▀███████████   ███    ███ ███       ███           ███ \n" +
                "███   ███   ███    ███     ███     ███    ███   ███    ███   ███    ███ ███▌    ▄ ███     ▄█    ███ \n" +
                " ▀█   █▀    ███    █▀     ▄████▀   ████████▀    ███    ███   ███    █▀  █████▄▄██ █▀    ▄████████▀  \n" +
                "                                                ███    ███              ▀                           \n"

                + "\u001B[0m"
        );
    }

    private void createGame() {

        int numPlayers = 0;
        try {

            numPlayers = io.getInt("How many players should join this game?");


            String nickname = io.getString("What will your nickname be?");



            int gameid = client.createGame(nickname, numPlayers);
        }
        catch (Exception e) {
            io.print(e.getMessage());
            createGame();
        }
    }

    private void connect() {
        String nickname;
        io.print("Connecting to game:");

        try {
            nickname = io.getString("What will your nickname be?");


            client.connect(nickname);
        }
        catch (Exception e) {
            io.print(e.getMessage());
            connect();
        }
    }

    private void reconnect() {

        try {

            String nickname = io.getString("What will your nickname be?");

            client.reconnect(nickname);
        }
        catch (Exception e) {
            io.print(e.getMessage());
            reconnect();
        }
    }

    private void seeBoard() {
        Board board = client.getView().getPlayers().getFirst().getBoard();
        System.out.println(board);

    }

    private void handleConnection() {
        ConnectionState c;
        try {
            c = client.getGameInfo();
        } catch (NullPointerException e) {
            io.print("could not connect to server");
            System.exit(0);
            return;
        }
        if (c.equals(ConnectionState.CONNECT)) {
            connect();
        }
        else if (c.equals(ConnectionState.LOAD)) {
            boolean choice = io.getBoolean("An unfinished game was found in the Server, do you want to continue it?");
            if (choice)
                reconnect();
            else {
                io.print("Creating new game...");
                createGame();
            }
        }
        else {
            io.print("Creating new game...");
            createGame();
        }
    }


    @Override
    public void start() {

        handleConnection();


        while (true) {



            int choice = io.getInt(
                    "0 - exit\n" +
                    "1 - createGame\n" +
                    "2 - connect\n" +
                    "3 - reconnect\n" +
                    "4 - see board\n" +
                    "5 - place");


            switch (choice) {
                case (0) -> {
                    System.out.println("Thanks for Playing!");
                    return;
                }
                case (1) -> createGame();
                case (2) -> connect();
                case (3) -> reconnect();
                case (4) -> seeBoard();
                default -> {
                    System.out.println("Invalid choice");
                }

            }





        }

    }
}
