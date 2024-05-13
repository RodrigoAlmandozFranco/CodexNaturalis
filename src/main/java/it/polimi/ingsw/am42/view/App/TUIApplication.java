package it.polimi.ingsw.am42.view.App;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.view.App.App;
import it.polimi.ingsw.am42.view.gameview.BoardView;
import it.polimi.ingsw.am42.view.gameview.GameView;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TUIApplication extends App {

    private String nickname;
    private final Scanner in;
    private final PrintStream out;


    public TUIApplication(Client client) {
        super(client);
        in = new Scanner(System.in);
        out = new PrintStream(System.out, true);

        printLogo();

    }

    private void printLogo() {
        System.out.println( "\u001B[33m" +
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
            out.println("How many players should join this game?");

            numPlayers = in.nextInt();

            out.println("What will your nickname be?");

            String nickname = in.nextLine();
            nickname = in.nextLine();



            int gameid = client.createGame(null, nickname, numPlayers);

            System.out.println("GameID :" + gameid);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            createGame();
        }
    }

    private void connect() {

        try {
            out.println("What is the gameID?");
            int gameID = in.nextInt();

            out.println("What will your nickname be?");
            String nickname = in.nextLine();


            client.connect((MessageListener) client, nickname, gameID);
        }
        catch (Exception e) {
            createGame();
        }
    }

    private void reconnect() {

        try {
            out.println("What is the gameID?");
            int gameID = in.nextInt();

            out.println("What will your nickname be?");
            String nickname = in.nextLine();


            client.reconnect((MessageListener) client, nickname, gameID);
        }
        catch (Exception e) {
            createGame();
        }
    }

    private void seeBoard() {
        BoardView board = client.getView().getPlayers().getFirst().getBoard();
        System.out.println(board);

    }


    @Override
    public void start() {


        while (true) {

            System.out.println( "0 - exit\n" +
                                "1 - createGame\n" +
                                "2 - connect\n" +
                                "3 - reconnect\n" +
                                "4 - check board");

            int choice = in.nextInt();


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
