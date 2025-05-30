package it.polimi.ingsw.am42.view.app;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.*;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.view.tui.ColorChooser;
import it.polimi.ingsw.am42.view.tui.IOHandler;
import it.polimi.ingsw.am42.view.clientModel.MethodChoice;
import it.polimi.ingsw.am42.view.clientModel.PlayerClientModel;

import java.util.*;


/**
 * This class is responsible for starting the terminal user interface of the application.
 * It extends the App class.
 *
 * @author Tommaso Crippa
 * @author Alessandro Di Maria
 */
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

    /**
     * Prints the Codex Naturalis logo on the terminal
     */
    private void printLogo() {
        io.print(
                "\n" +
                "                ▄████████  ▄██████▄  ████████▄     ▄████████ ▀████     ████▀                        \n" +
                "               ███    ███ ███    ███ ███   ▀███   ███    ███   ███    ████▀                         \n" +
                "               ███    █▀  ███    ███ ███    ███   ███    █▀     ███   ███                           \n" +
                "               ███        ███    ███ ███    ███  ▄███▄▄▄        ▀███▄███▀                           \n" +
                "               ███        ███    ███ ███    ███ ▀▀███▀▀▀        ████▀██▄                            \n" +
                "               ███    █▄  ███    ███ ███    ███   ███    █▄     ███  ▀███                           \n" +
                "               ███    ███ ███    ███ ███   ▄███   ███    ███  ▄███     ███▄                         \n" +
                "               ████████▀   ▀██████▀  ████████▀    ██████████ ████       ███▄                        \n" +
                "                                                                                                    \n" +
                "███▄▄▄▄      ▄████████     ███     ███    █▄     ▄████████    ▄████████  ▄█        ▄█     ▄████████ \n" +
                "███▀▀▀██▄   ███    ███ ▀█████████▄ ███    ███   ███    ███   ███    ███ ███       ███    ███    ███ \n" +
                "███   ███   ███    ███    ▀███▀▀██ ███    ███   ███    ███   ███    ███ ███       ███    ███    █▀  \n" +
                "███   ███   ███    ███     ███   ▀ ███    ███  ▄███▄▄▄▄██▀   ███    ███ ███       ███    ███        \n" +
                "███   ███ ▀███████████     ███     ███    ███ ▀▀███▀▀▀▀▀   ▀███████████ ███       ███  ▀███████████ \n" +
                "███   ███   ███    ███     ███     ███    ███ ▀███████████   ███    ███ ███       ███           ███ \n" +
                "███   ███   ███    ███     ███     ███    ███   ███    ███   ███    ███ ███     ▄ ███     ▄█    ███ \n" +
                " ▀█   █▀    ███    █▀     ▄████▀   ████████▀    ███    ███   ███    █▀  █████▄▄██ █▀    ▄████████▀  \n" +
                "                                                ███    ███              ▀                           \n"

                + "\u001B[0m"
        );
    }

    /**
     * TUI method to create a game
     * if the GameAlreadyCreatedException is raised, it changes method of connection
     * if any other exception is raised, repeat the procedure
     * otherwise a game is successfully created
     */
    private static void createGame() {

        int numPlayers = 0;
        try {

            numPlayers = io.getInt("How many players should join this game?");


            nickname = io.getString("What will your nickname be?");

            client.getClientModel().setNickname(nickname);
            int gameid = client.createGame(nickname, numPlayers);
        }catch(GameAlreadyCreatedException e) {
            io.print("Someone already created a game, restarting connection");
            handleConnection();
        }
        catch (Exception e) {
            io.print(e.getMessage());
            createGame();
        }
    }
    /**
     * TUI method to join a game
     * if the GameFullException is raised, the program stops running
     * if any other exception is raised, repeat the procedure
     * otherwise the user successfully joins
     */
    private static void connect() {
        String nickname;
        io.print("Connecting to game:");

        try {
            nickname = io.getString("What will your nickname be?");


            TUIApplication.nickname = nickname;
            client.getClientModel().setNickname(TUIApplication.nickname);
            client.connect(nickname);
        }
        catch (GameFullException e){
        io.print(e.getMessage());
        System.exit(0);
        }
        catch (Exception e) {
            io.print(e.getMessage());
            connect();
        }
    }
    /**
     * TUI method to join a previously saved game
     * if the GameAlreadyCreatedException is raised, it changes method of connection
     * if the GameFullException is raised, the program stops running
     * if any other exception is raised, repeat the procedure
     * otherwise the user successfully joins the game
     */
    private static void reconnect() {


        nickname = io.getString("What was your nickname in the previous game?");

        client.getClientModel().setNickname(nickname);

        try {
            client.reconnect(nickname);
        }
        catch(GameAlreadyCreatedException e){
            io.print("Someone already created a game, restarting connection");
            handleConnection();
        }
        catch (GameFullException e){
            io.print(e.getMessage());
            System.exit(0);
        }
        catch (Exception e) {
            io.print(e.getMessage());
            reconnect();

        }
    }

    /**
     * TUI method to place the initial Starting Card
     * if any error is made during this procedure it returns to the selectChoice() method
     */
    public static void placeStarting() {
        seeCards();
        PlayableCard startingCard = null;
        PlayerClientModel p = client.getClientModel().getPlayer(nickname);
        for(PlayableCard c: p.getHand()){
            startingCard = c;
        }
        boolean faceSide = io.getBoolean("Would you like to place the starting card face up??");
        if(startingCard != null) {
            Face face = startingCard.getBack();
            if (faceSide)
                face = startingCard.getFront();
            try {
                p.setAvColors(client.placeStarting(nickname, face));
            } catch (WrongTurnException e) {
                io.print(ColorChooser.RED + e.getMessage() + ColorChooser.RESET);
                selectChoice();
            }
        }
        else io.print("Starting card not found!!!");
    }

    /**
     * TUI method to select the user's color
     * if any error is made during this procedure it returns to the selectChoice() method
     */
    public static void chooseColor() {
        String question = "Choose one of the available colors\n";
        PlayerClientModel p = client.getClientModel().getPlayer(nickname);
        List<PlayersColor> avcolors = p.getAvColors();
        for (int i=0; i<avcolors.size(); i++)
            question += i + " - " + avcolors.get(i).toString(true) + "\n";

        int choice = io.getInt(question);
        while (choice < 0 || choice >= avcolors.size()) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }
        p.setColor(avcolors.get(choice));
        try {
            p.setAvGoals(client.chooseColor(nickname,avcolors.get(choice)));
        } catch (WrongTurnException e) {
            io.print(ColorChooser.RED + e.getMessage() + ColorChooser.RESET);
            selectChoice();
        }
    }

    /**
     * TUI method to select the user's personal goal
     * if any error is made during this procedure it returns to the selectChoice() method
     */
    public static void chooseGoal() {
        PlayerClientModel p = client.getClientModel().getPlayer(nickname);
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
        try {
            client.chooseGoal(nickname,goals.get(choice));
        } catch (WrongTurnException e) {
            io.print(ColorChooser.RED + e.getMessage() + ColorChooser.RESET);
            selectChoice();
        }

    }



    /**
     * TUI method that zooms in on the board to check which corner a card would cover if placed in a given position
     * @param pos the position to zoom in
     */
    private static void zoom(Position pos) {
        Map<Direction, Face> nearby = client.getClientModel().getPlayer(nickname).getBoard().getNearbyFaces(pos);
        String cLeft = null;
        String resLeft = " ";
        if (nearby.containsKey(Direction.UPLEFT)) {
            cLeft = nearby.get(Direction.UPLEFT).getColor().toString();
            Resource res = nearby.get(Direction.UPLEFT).getCorner(Direction.DOWNRIGHT).getResource();
            if (res!=null)
                resLeft = res.toString();
        }
        String cRight = null;
        String resRight = " ";
        if (nearby.containsKey(Direction.UPRIGHT)) {
            cRight = nearby.get(Direction.UPRIGHT).getColor().toString();
            Resource res = nearby.get(Direction.UPRIGHT).getCorner(Direction.DOWNLEFT).getResource();
            if (res!=null)
                resRight = res.toString();
        }

        String to_print = "";

        // 1
        to_print += "         " + (cLeft==null ? " ": cLeft + "|");
        to_print += "               ";
        to_print += (cRight==null ? " ": cRight + "|") + "      \n"+ColorChooser.RESET;
        // 2
        to_print +=  ColorChooser.YELLOW  +"     +---+---------------+---+\n";
        // 3
        to_print += "     " + "| " + resLeft + (cLeft==null ? "  ": cLeft + " |");
        to_print += "               ";
        to_print += (cRight==null ? "  ": cRight + "| ") + resRight + ColorChooser.YELLOW + " |" + "\n";
        // 4
        to_print += "   " + (cLeft==null ? "  ": cLeft + "--") + ColorChooser.YELLOW + "+" + (cLeft==null ? "    ": cLeft + "---+");
        to_print += "               ";
        to_print += (cRight==null ? "    ": cRight + "+---") + ColorChooser.YELLOW + "+" + (cRight==null ? "": cRight + "--") + "\n";
        // 5 6 7
        for (int i = 0; i < 3; i++)
            to_print += "     " + ColorChooser.YELLOW + "|" + "                       " + "|" + ColorChooser.RESET + "\n";

        cLeft = null;
        resLeft = " ";
        if (nearby.containsKey(Direction.DOWNLEFT)) {
            cLeft = nearby.get(Direction.DOWNLEFT).getColor().toString();
            Resource res = nearby.get(Direction.DOWNLEFT).getCorner(Direction.UPRIGHT).getResource();
            if (res!=null)
                resLeft = res.toString();
        }
        cRight = null;
        resRight = " ";
        if (nearby.containsKey(Direction.DOWNRIGHT)) {
            cRight = nearby.get(Direction.DOWNRIGHT).getColor().toString();
            Resource res = nearby.get(Direction.DOWNRIGHT).getCorner(Direction.UPLEFT).getResource();
            if (res!=null)
                resRight = res.toString();
        }

        // 9
        to_print += "   " + (cLeft==null ? "  ": cLeft + "--") + ColorChooser.YELLOW + "+" + (cLeft==null ? "    ": cLeft + "---+");
        to_print += "               ";
        to_print += (cRight==null ? "    ": cRight + "+---") + ColorChooser.YELLOW + "+" + (cRight==null ? "": cRight + "--") + "\n";
        // 10
        to_print += "     " + ColorChooser.YELLOW  + "| " + resLeft + (cLeft==null ? "  ": cLeft + " |");
        to_print += "               ";
        to_print += (cRight==null ? "  ": cRight + "| ") + resRight +ColorChooser.YELLOW  + " |" + "\n";
        // 11
        to_print +=  ColorChooser.YELLOW  +"     +---+---------------+---+\n";
        // 12
        to_print += "         " + (cLeft==null ? " ": cLeft + "|");
        to_print += "               ";
        to_print += (cRight==null ? " ": cRight + "|") + "      \n"+ColorChooser.RESET;

        io.print(to_print);
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
        for(PlayerClientModel p:client.getClientModel().getPlayers())
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
        Board board = client.getClientModel().getPlayer(nickname).getBoard();
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

        allFaces.addAll(client.getClientModel().getPlayer(nickname).getBoard().getFaces());
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
            String to_print = "   ";
            for(Face f: sameHighness) {

                for(int j=f.getPosition().leftness(); j<lastLeft; j++)
                    to_print += "   ";
                if(f.getColor() == null)
                    to_print += " "+ faces.indexOf(f)+" ";
                else
                    to_print += f.getColor() + "███" + ColorChooser.RESET;
                lastLeft = f.getPosition().leftness()-1;
            }
            to_print += ColorChooser.RESET;
            io.print(to_print);

        }
    }

    /**
     * TUI method to see how much resources a player has
     * @param nickname nickname of the player's resources that the user wants to check
     */
    private static void seeResources(String nickname){
        Map<Resource, Integer> resources = client.getClientModel().getPlayer(nickname).getBoard().getTotalResources();
        String message = "These are your resources and objects\n";
        for (Resource r: Resource.values())
            message += r.fullName() + " (" +r+") = "+ resources.get(r) + "\n";
        io.print(message);
    }

    /**
     * TUI method to see the user's cards
     */
    public static void seeCards() {
        PlayerClientModel p = client.getClientModel().getPlayer(nickname);
            for (PlayableCard c : p.getHand()) {
                c.setVisibility(true);
                io.print(c.toString());
            }
    }


    /**
     * TUI method to see the user's cards WITH INDEX
     */
    public static void seeHand() {
        PlayerClientModel p = client.getClientModel().getPlayer(nickname);
        int i = 0;
        for (PlayableCard c : p.getHand()) {
            c.setVisibility(true);
            io.print(i + " : \n" + c);
            i++;
        }
    }

    /**
     * TUI method to see the current standings
     */
    public static void seeStandings() {
        String message = "Scoreboard\n";
        for (PlayerClientModel p: client.getClientModel().getPlayers())
            message +=  p.getNickname()  + (p.getNickname().equals(nickname)?" (You)":"")+" = "+ p.getPoints() + "\n";
        io.print(message);
    }

    /**
     * Method to see the user's personal goal + the global ones
     */
    public static void seeGoals() {
        io.print("Global goals:");
        for (GoalCard g : client.getClientModel().getGlobalGoals())
            io.print(""+g);

        io.print("Personal goal:");
        io.print(""+client.getClientModel().getPlayer(nickname).getPersonalGoal());
    }


    /**
     * TUI method to place a card
     * if any error is made during this procedure it returns to the selectChoice() method
     */
    public static void place() {
        //Selection of the position
        List<Position> availablePositions = null;
        try {
            availablePositions = client.getAvailablePositions(nickname).stream().toList();
        } catch (WrongTurnException e) {
            io.print(ColorChooser.RED + e.getMessage() + ColorChooser.RESET);
            selectChoice();
            return;
        }
        if(availablePositions.isEmpty()) {
            io.print("No position available");
            return;
        }
        Position pos = availablePositions.getFirst();
        boolean sure = false;
        String question;
        while (!sure) {
            seeBoard(nickname, availablePositions);
            question = "Choose one of the following positions\n";
            for (int i = 0; i < availablePositions.size(); i++)
                question += i + " - " + availablePositions.get(i) + "\n";

            int choice = io.getInt(question);
            while (choice < 0 || choice >= availablePositions.size()) {
                io.print("Invalid choice");
                choice = io.getInt(question);
            }
            pos = availablePositions.get(choice);
            zoom(pos);
            sure = io.getBoolean("Are you sure?");
        }
        //Selection of the face
        seeHand();
        PlayerClientModel p = client.getClientModel().getPlayer(nickname);

        question = "Choose one of the cards\n";

        int choice = io.getInt(question);
        while (choice < 0 || choice >= p.getHand().size()) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }

        boolean faceSide = io.getBoolean("Would you like to place the starting card face up??");

        try {
            if (faceSide)
                client.place(nickname, p.getHand().get(choice).getFront(), pos);
            else
                client.place(nickname, p.getHand().get(choice).getBack(), pos);
        } catch (RequirementsNotMetException e) {
            io.print(e.getMessage());
            place();
        } catch (WrongTurnException e) {
            io.print(ColorChooser.RED + e.getMessage() + ColorChooser.RESET);
            selectChoice();
        }
    }


    /**
     * TUI method to see the cards that can currently be picked
     */
    public static void seePickableCards() {
        List<PlayableCard> cards = new ArrayList<>();
        cards.addAll(client.getClientModel().getPickableResourceCards());
        cards.addAll(client.getClientModel().getPickableGoldCards());
        seePickableCards(cards);
    }

    /**
     * TUI method to see the cards that can currently be picked WITH Index
     * @param cards the pickable cards
     */
    private static void seePickableCards(List<PlayableCard> cards) {
        int i = 0;
        for (PlayableCard c : cards) {
            if(c.getVisibility())
                io.print("\n"+ i + " :\n" + c.getFront());
            else
                io.print("\n" + i + " :\n" + c.getBack());
            i++;
        }
    }

    /**
     * TUI method to pick a card
     * if any error is made during this procedure it returns to the selectChoice() method
     */
    public static void pick() {
        seeResources(nickname);

        List<PlayableCard> cards = new ArrayList<>();
        cards.addAll(client.getClientModel().getPickableResourceCards());
        cards.addAll(client.getClientModel().getPickableGoldCards());

        seePickableCards(cards);

        String question = "Select which card to pick\n";

        int choice = io.getInt(question);
        while (choice < 0 || choice >= cards.size()) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }
        try {
            client.pick(nickname, cards.get(choice));
        } catch (WrongTurnException e) {
            io.print(ColorChooser.RED + e.getMessage() + ColorChooser.RESET);
            selectChoice();
        }
    }
    /**
     * TUI method to check the chat messages
     */
    public static void seeChat() {
        List<ChatMessage> chat = client.getClientModel().getAllMessages();
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
    /**
     * TUI method to send a chat message
     */
    public static void sendMessage() {
        String receiver = "all";
        String question = "Who do you want to write to?\n";

        if (client.getClientModel().getPlayers().isEmpty()) {

            return;
        }
        List<String> names = new ArrayList<>();
        names.add("all");
        for(PlayerClientModel p:client.getClientModel().getPlayers())
            if(!p.getNickname().equals(nickname))
                names.add(p.getNickname());

        for (int i=0; i<names.size(); i++)
            question += (i) + " - " + names.get(i) + "\n";

        int choice = io.getInt(question);
        while (choice < 0 || choice >= client.getClientModel().getPlayers().size()) {
            io.print("Invalid choice");
            choice = io.getInt(question);
        }
        receiver = names.get(choice);

        String content = io.getString("What do you want to write?");
        client.sendChatMessage(new ChatMessage(content, nickname, receiver));
    }
    /**
     * TUI method to disconnect from the game
     */
    public static void disconnect() {
        if (client.getClientModel().getCurrentState() != null
            && !client.getClientModel().getCurrentState().equals(State.LAST)
            && !client.getClientModel().getCurrentState().equals(State.DISCONNECTED)) {

            boolean sure = io.getBoolean("Are you sure?");
            if (!sure)
                return;
        }
        client.playerDisconnected();
        io.print("Thanks for Playing!");
        System.exit(0);
    }


    /**
     * TUI method to handle the player's connection
     * it asks the server how it should connect between the
     * createGame, connect, and reconnect method
     */
    private static void handleConnection() {
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
        } else if (c.equals(ConnectionState.LOADING)) {
            reconnect();

        } else {
            io.print("Creating new game...");
            createGame();
        }
    }
    /**
     * Main method of the TUI Application
     * it gets from GameClientModel the actions it can currently do (THICK client)
     * then it makes the user select which one they want to do
     */
    public static void selectChoice() {

        if (client.getClientModel().getCurrentState() == null)
            io.print("Waiting for Players");


        List<ChatMessage> newMessage = client.getClientModel().getTmpMessages();
        String newMessages = "";
        if(newMessage != null && !newMessage.isEmpty())
            newMessages += " ("+newMessage.size()+" not read)";
        List<MethodChoice> choices = client.getClientModel().getUsableMethods();
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

    /**
     * Method to check if a player has disconnected from the game or if the server crashed,
     * exiting the game if true
     *
     */
    private void checkDisconnection(){
        if (client.getClientModel().isGameAborted()) {
            if (client.getClientModel().getCurrentState()==null || (!client.getClientModel().getCurrentState().equals(State.LAST)
                    && !client.getClientModel().getCurrentState().equals(State.DISCONNECTED))) {
                io.print("Someone disconnected to the game");
                io.print("Exiting game...");
                System.exit(0);
            }
        }
        if (client.getClientModel().getServerDown()) {
            io.print("Server has fallen");
            io.print("Exiting game...");
            System.exit(0);
        }
    }

    /**
     * Shows the final standings of the game (with suspense!!!)
     * if it isn't the end of the game it is not displayed (no spoilers)
     */
    private void printFinalStandings() {

        io.print("\n" +
                "----------------------------------------------------------------------------\n" +
                "    _______             __   _____ __                  ___                 \n" +
                "   / ____(_)___  ____ _/ /  / ___// /_____ _____  ____/ (_)___  ____ ______\n" +
                "  / /_  / / __ \\/ __ `/ /   \\__ \\/ __/ __ `/ __ \\/ __  / / __ \\/ __ `/ ___/\n" +
                " / __/ / / / / / /_/ / /   ___/ / /_/ /_/ / / / / /_/ / / / / / /_/ (__  ) \n" +
                "/_/   /_/_/ /_/\\__,_/_/   /____/\\__/\\__,_/_/ /_/\\__,_/_/_/ /_/\\__, /____/  \n" +
                "                                                             /____/        \n" +
                "----------------------------------------------------------------------------\n");
        List<Player> winners;
        int numWinners;

        try {
            winners = client.getWinner();
        } catch (WrongTurnException e) {
            io.print("There are no winners today");
            selectChoice();
            return;
        }
        numWinners = winners.size();



        List<PlayerClientModel> standings = client.getClientModel().getPlayers();
        standings.sort(Comparator.comparingInt(PlayerClientModel::getPoints));


        Stack<String> colors = new Stack<>();
        List<String> medals = Arrays.asList(ColorChooser.GOLD, ColorChooser.SILVER, ColorChooser.BRONZE, ColorChooser.PURPLE);

        for (int i = 0; i < numWinners; i++) {
            colors.push(medals.getFirst());
        }
        for(int i = numWinners; i< client.getClientModel().getNumberPlayers(); i++) {
            colors.push(medals.get(i));
        }
        long time = 1000;
        for (PlayerClientModel p : standings) {
            String message = colors.pop() + p.getNickname() + (p.getNickname().equals(nickname) ? " (You)" : "") + " = " + p.getPoints() + ColorChooser.RESET + "\n";
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            time *= (long) 1.30;
            io.print(message);

        }
    }

    /**
     * Separate thread that displays updates to the player, such as:
     * - it is your turn
     * - it is the final turn
     * - another player has done an action
     */
    private void  updatePlayer() {
        while (true) {
            checkDisconnection();

            if (client.getClientModel().getNewUpdate() && client.getClientModel().getCurrentPlayer()!=null){
                String current = client.getClientModel().getCurrentPlayer().getNickname();
                if (client.getClientModel().getCurrentState().equals(State.LAST)) {
                        printFinalStandings();
                }

                else if (current.equals(nickname)) {
                    if (client.getClientModel().isTurnFinal()) {
                        io.print(ColorChooser.RED +
                                "\n" +
                                "--------------------------------------------------\n" +
                                "    _______             __   ______               \n" +
                                "   / ____(_)___  ____ _/ /  /_  __/_  ___________ \n" +
                                "  / /_  / / __ \\/ __ `/ /    / / / / / / ___/ __ \\\n" +
                                " / __/ / / / / / /_/ / /    / / / /_/ / /  / / / /\n" +
                                "/_/   /_/_/ /_/\\__,_/_/    /_/  \\__,_/_/  /_/ /_/ \n" +
                                "--------------------------------------------------\n" + ColorChooser.RESET);
                    }
                    else {
                        io.print("\n" +
                                "-------------------------------------------------\n" +
                                "                              __                 \n" +
                                "   __  ______  __  _______   / /___  ___________ \n" +
                                "  / / / / __ \\/ / / / ___/  / __/ / / / ___/ __ \\\n" +
                                " / /_/ / /_/ / /_/ / /     / /_/ /_/ / /  / / / /\n" +
                                " \\__, /\\____/\\__,_/_/      \\__/\\__,_/_/  /_/ /_/ \n" +
                                "/____/                                           \n" +
                                "-------------------------------------------------\n");
                    }
                    io.print("Press 0 to reload your choices");
                }
                else {
                    PlayersColor c = client.getClientModel().getPlayer(current).getColor();
                    if (c != null)
                        current = c + current + ColorChooser.RESET;
                    System.out.println(client.getClientModel().getCurrentState().showState(current));
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

    /**
     * Method to start the application
     * after handling the connection, it starts the thread to update the player
     */
    @Override
    public void start() {

        handleConnection();

        new Thread(this::updatePlayer).start();


        while (true)
            selectChoice();

    }


}
