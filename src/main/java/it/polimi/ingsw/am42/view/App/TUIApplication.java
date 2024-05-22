package it.polimi.ingsw.am42.view.App;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.view.tui.ColorChooser;
import it.polimi.ingsw.am42.view.tui.IOHandler;
import it.polimi.ingsw.am42.view.gameview.MethodChoice;
import it.polimi.ingsw.am42.view.gameview.PlayerView;

import java.util.*;

public class TUIApplication extends App {

    private static String nickname;
    private static final IOHandler io = new IOHandler();

    //ONLY FOR TESTING
    public TUIApplication() {
        super();
    }

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

            client.getView().setNickname(nickname);
            client.reconnect(nickname);
        }
        catch (Exception e) {
            io.print(e.getMessage());
            reconnect();

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
                p.setAvColors(client.placeStarting(nickname, startingCard.getFront()));
            else p.setAvColors(client.placeStarting(nickname, startingCard.getBack()));
        }
        else io.print("Starting card not found!!!");
    }

    public static void chooseColor() {
        String question = "Choose one of the available colors\n";
        PlayerView p = client.getView().getPlayer(nickname);
        List<PlayersColor> avcolors = p.getAvColors();
        for (int i=0; i<avcolors.size(); i++)
            question += i + " - " + avcolors.get(i).toString() + "\n";

        int choice = io.getInt(question);
        while (choice < 0 || choice >= avcolors.size()) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }
        p.setColor(avcolors.get(choice));
        p.setAvGoals(client.chooseColor(nickname,avcolors.get(choice)));
    }

    public static void chooseGoal() {
        PlayerView p = client.getView().getPlayer(nickname);
        List<GoalCard> goals = p.getAvGoals();
        String question = "Choose one of the following goals\n";
        for (int i=0; i<goals.size(); i++)
            question += i + " - \n" + goals.get(i) + "\n";

        int choice = io.getInt(question);
        while (choice < 0 || choice >= goals.size()) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }
        p.setPersonalGoal(goals.get(choice));
        client.chooseGoal(nickname,goals.get(choice));

    }

    /**
     * Called by player during selection, select which player's board and its board will be seen.
     * Calls seeBoard(String nickname)
     * @author Alessandro Di Maria
     * @author Tommaso Crippa
     */
    public static void seeBoard() {
        String question = "Which player's board?\n";

        List<String> names = new ArrayList<>();
        names.add(nickname);
        for(PlayerView p:client.getView().getPlayers())
            if(!p.getNickname().equals(nickname))
                names.add(p.getNickname());

        for (int i=0; i<names.size(); i++)
            question += (i) + " - " + names.get(i) + "\n";

        int choice = io.getInt(question);
        while (choice < 0 || choice >= names.size()) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }
        seeBoard(names.get(choice));
    }

    /**
     * Shows board of selected player
     * @author Alessandro Di Maria
     * @author Tommaso Crippa
     */
    private static void seeBoard(String nickname) {
        Board board = client.getView().getPlayer(nickname).getBoard();
        seeBoard(board.getFaces());
        seeResources(nickname);
    }


    /**
     * Called by App for placement, shows the board and its position
     * Calls seeBoard(List<Face> faces)
     * @author Alessandro Di Maria
     * @author Tommaso Crippa
     */
    private static void seeBoard(String nickname, List<Position> positions) {
        List<Face> allFaces = new ArrayList<>();


        for (Position pos : positions) {
            Face f = new Back(null, null, null, null);
            f.setPosition(pos);
            allFaces.add(f);
        }

        allFaces.addAll(client.getView().getPlayer(nickname).getBoard().getFaces());
        seeBoard(allFaces);
        seeResources(nickname);
    }

    /**
     * Shows board
     * @author Alessandro Di Maria
     * @author Tommaso Crippa
     */
    public static void seeBoard(List<Face> faces) {
        List<Face> sameHighness = new ArrayList<>();
        int leftest = faces.stream().map(x -> x.getPosition().leftness()).reduce((x, y) -> x > y ? x : y).orElse(0);
        int highest = faces.stream().map(x -> x.getPosition().highness()).reduce((x, y) -> x > y ? x : y).orElse(0);
        int lowest = faces.stream().map(x -> x.getPosition().highness()).reduce((x, y) -> x < y ? x : y).orElse(0);
        for(int i = highest; i >= lowest; i--){
            sameHighness.clear();
            for(Face f: faces)
                if (f.getPosition().highness() == i)
                    sameHighness.add(f);
            sameHighness.sort(Comparator.comparingInt(a -> -a.getPosition().leftness()));

            int lastLeft = leftest;
            String to_print = "";
            for(Face f: sameHighness) {

                for(int j=f.getPosition().leftness(); j<lastLeft; j++)
                    to_print += "   ";
                if(f.getColor() == null)
                    to_print += " "+ faces.indexOf(f)+" ";
                else
                    to_print += f.getColor() + "███";
                lastLeft = f.getPosition().leftness()-1;
            }
            to_print += ColorChooser.RESET;
            io.print(to_print);

        }

    }

    private static void seeResources(String nickname){
        Map<Resource, Integer> resources = client.getView().getPlayer(nickname).getBoard().getTotalResources();
        String message = "These are your resources and objects\n";
        for (Resource r: Resource.values())
            message += r.fullName() + " (" +r+") = "+ resources.get(r) + "\n";
        io.print(message);
    }

    public static void seeCards() {
        PlayerView p = client.getView().getPlayer(nickname);
            for (PlayableCard c : p.getHand()) {
                c.setVisibility(true);
                io.print(c.toString());
            }
    }

    public static void seeHand() {
        PlayerView p = client.getView().getPlayer(nickname);
        int i = 0;
        for (PlayableCard c : p.getHand()) {
            c.setVisibility(true);
            io.print(i + " : \n" + c.getFront());
            i++;
            io.print(i + " : \n" + c.getBack());
            i++;
        }
    }

    public static void seeStandings() {
        String message = "Scoreboard\n";
        for (PlayerView p: client.getView().getPlayers())
            message +=  p.getNickname()  + (p.getNickname().equals(nickname)?" (You)":"")+" = "+ p.getPoints() + "\n";
        io.print(message);
    }

    public static void seeGoals() {
        io.print("Global goals:");
        for (GoalCard g : client.getView().getGlobalGoals())
            io.print(""+g);

        io.print("Personal goal:");
        io.print(""+client.getView().getPlayer(nickname).getPersonalGoal());
    }

    public static void place() {
        seeBoard(nickname);
        //Selection of the position
        List<Position> availablePositions = client.getAvailablePositions(nickname).stream().toList();
        if(availablePositions.isEmpty()) {
            io.print("No position available");
            return;
        }
        Position pos = availablePositions.getFirst();
        seeBoard(nickname, availablePositions);
        String question = "Choose one of the following positions\n";
        for (int i=0; i<availablePositions.size(); i++)
            question += i + " - " + availablePositions.get(i) + "\n";

        int choice = io.getInt(question);
        while (choice < 0 || choice >= availablePositions.size()) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }
        pos = availablePositions.get(choice);

        //Selection of the face
        seeHand();
        PlayerView p = client.getView().getPlayer(nickname);

        question = "Choose one of the face\n";

        choice = io.getInt(question);
        while (choice < 0 || choice >= p.getHand().size()*2) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }

        boolean faceSide = (choice%2 == 0) ? true : false;
        try {
            if (faceSide)
                client.place(nickname, p.getHand().get(choice/2).getFront(), pos);
            else
                client.place(nickname, p.getHand().get(choice/2).getBack(), pos);
        } catch (RequirementsNotMetException e) {
            io.print(e.getMessage());
            place();
        }
    }

    public static void seePickableCards(List<PlayableCard> cards) {
        int i = 0;
        for (PlayableCard c : cards) {
            if(c.getVisibility())
                io.print("\n"+ i + " : " + c.getFront());
            else
                io.print("\n" + i + " : " + c.getBack());
            i++;
        }
    }
    public static void pick() {
        List<PlayableCard> cards = new ArrayList<>();
        cards.addAll(client.getView().getPickableResourceCards());
        cards.addAll(client.getView().getPickableGoldCards());
        seePickableCards(cards);

        String question = "Select which card to pick\n";

        int choice = io.getInt(question);
        while (choice < 0 || choice >= cards.size()) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }
        client.pick(nickname,cards.get(choice));
    }

    public static void seeChat() {
        List<ChatMessage> chat = client.getView().getAllMessages();
        String to_print = "";
        for (ChatMessage message : chat) {
            if (message.getReceiver().equals( "all"))
                to_print += ColorChooser.ORANGE;
            else
                to_print += ColorChooser.PINK;
            to_print += "<" + message.getSender() + ">: ";
            to_print += message.getMessage();
            to_print += ColorChooser.RESET + "\n";
        }

        io.print(to_print);
    }

    public static void sendMessage() {
        String receiver = "all";
        String question = "Who do you want to write to?\n";

        if (client.getView().getPlayers().isEmpty()) {

            return;
        }
        List<String> names = new ArrayList<>();
        names.add("all");
        for(PlayerView p:client.getView().getPlayers())
            if(!p.getNickname().equals(nickname))
                names.add(p.getNickname());

        for (int i=0; i<names.size(); i++)
            question += (i) + " - " + names.get(i) + "\n";

        int choice = io.getInt(question);
        while (choice < 0 || choice >= client.getView().getPlayers().size()) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }
        receiver = names.get(choice);

        String content = io.getString("What do you want to write?");
        client.sendChatMessage(new ChatMessage(content, nickname, receiver));
    }

    public static void disconnect() {
        if (client.getView().getCurrentState() != null
            && !client.getView().getCurrentState().equals(State.LAST)
            && !client.getView().getCurrentState().equals(State.DISCONNECTED)) {

            boolean sure = io.getBoolean("Are you sure?");
            if (!sure)
                return;
        }
        // TODO: call client.playerDisconnected()
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
        List<ChatMessage> newMessage = client.getView().getTmpMessages();
        String newMessages = "";
        if(newMessage != null && !newMessage.isEmpty())
            newMessages += " ("+newMessage.size()+" not read)";
        List<MethodChoice> choices = client.getView().getUsableMethods();
        String question = "What do you want to do?\n";
        for (int i=0; i<choices.size(); i++)
            question += i + " - " + choices.get(i).getChoice() + (choices.get(i).equals(MethodChoice.SEECHAT)?newMessages:"") + "\n";

        int choice = io.getInt(question);
        if (choice < 0 || choice >= choices.size()) {
            io.print("Invalid choice");
            return;
        }
        choices.get(choice).selectChoice().run();
    }


    private void  updatePlayer() {
        while (true) {
            if (client.getView().getNewUpdate() && client.getView().getCurrentPlayer()!=null){
                String current = client.getView().getCurrentPlayer().getNickname();
                if (current.equals(nickname)) {
                    System.out.println("\n" +
                            "-------------------------------------------------\n" +
                            "                              __                 \n" +
                            "   __  ______  __  _______   / /___  ___________ \n" +
                            "  / / / / __ \\/ / / / ___/  / __/ / / / ___/ __ \\\n" +
                            " / /_/ / /_/ / /_/ / /     / /_/ /_/ / /  / / / /\n" +
                            " \\__, /\\____/\\__,_/_/      \\__/\\__,_/_/  /_/ /_/ \n" +
                            "/____/                                           \n" +
                            "-------------------------------------------------\n");
                    System.out.println("Press 0 to reload your choices");
                }
                else {
                    PlayersColor c = client.getView().getPlayer(current).getColor();
                    if (c != null)
                        current = c + current + ColorChooser.RESET;
                    System.out.println(client.getView().getCurrentState().showState(current));
                }

            }
            else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void start() {

        handleConnection();

        Thread threadGame = new Thread(this::updatePlayer);
        threadGame.start();

        while (true)
            selectChoice();

    }


}
