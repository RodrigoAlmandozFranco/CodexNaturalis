package it.polimi.ingsw.am42.view.App;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.StartingCard;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.view.App.App;
import it.polimi.ingsw.am42.view.IOHandler;
import it.polimi.ingsw.am42.view.gameview.GameView;
import it.polimi.ingsw.am42.view.gameview.MethodChoice;
import it.polimi.ingsw.am42.view.gameview.PlayerView;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TUIApplication extends App {

    private static String nickname;
    private static final IOHandler io = new IOHandler();


    public TUIApplication(Client client) {
        super(client);

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


            nickname = io.getString("What will your nickname be?");


            client.getView().setNickname(nickname);
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


            TUIApplication.nickname = nickname;
            client.getView().setNickname(TUIApplication.nickname);
            client.connect(nickname);
        }
        catch (Exception e) {
            io.print(e.getMessage());
            connect();
        }
    }

    private void reconnect() {

        try {

            nickname = io.getString("What will your nickname be?");

            client.reconnect(nickname);
        }
        catch (Exception e) {
            io.print(e.getMessage());
            reconnect();
            client.getView().setNickname(nickname);
        }
    }




    public static void placeStarting() {
        seeCards();
        PlayableCard startingCard = null;
        PlayerView p = client.getView().getPlayer(nickname);
        for(PlayableCard c: p.getHand()){
            startingCard = c;
        }
        boolean faceSide = io.getBoolean("Would you like to place the starting card face up??");
        if(startingCard != null) {
            if (faceSide)
                client.placeStarting(nickname, startingCard.getFront());
            else client.placeStarting(nickname, startingCard.getBack());
        }
        else io.print("Starting card not found!!!");
    }

    public static void chooseColor() {
        // TODO
    }

    public static void chooseGoal() {
        // TODO
    }

    public static void seeBoard() {
        // TODO
    }

    public static void seeCards() {
        PlayerView p = client.getView().getPlayer(nickname);
            for (PlayableCard c : p.getHand()) {
                c.setVisibility(true);
                io.print(c.toString());
            }
    }

    public static void seeStandings() {
        // TODO
    }

    public static void seePickableCards() {
        // TODO
    }
    public static void seeGoals() {
        // TODO
    }

    public static void place() {
        // TODO
    }

    public static void pick() {
        // TODO
    }

    public static void seeChat() {
        // TODO
    }

    public static void sendMessage() {
        // TODO
    }

    public static void disconnect() {
        if (client.getView().getCurrentState() != null
            && !client.getView().getCurrentState().equals(State.LAST)
            && !client.getView().getCurrentState().equals(State.DISCONNECTED)) {

            boolean sure = io.getBoolean("Are you sure?");
            if (!sure)
                return;
        }
        io.print("Thanks for Playing!");
        System.exit(0);
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

    public static void selectChoice() {
        List<MethodChoice> choices = client.getView().getUsableMethods();
        String question = "What do you want to do?\n";
        for (int i=0; i<choices.size(); i++)
            question += i + " - " + choices.get(i) + "\n";

        int choice = io.getInt(question);
        if (choice < 0 || choice >= choices.size()) {
            io.print("Invalid choice");
            return;
        }
        choices.get(choice).selectChoice().run();
    }



    @Override
    public void start() {

        handleConnection();

        while (true)
            selectChoice();

    }
}
