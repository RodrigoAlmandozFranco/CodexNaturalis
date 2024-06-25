package it.polimi.ingsw.am42.network.tcp.client;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.PingMessage;
import it.polimi.ingsw.am42.network.tcp.messages.clientToServer.*;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.*;
import it.polimi.ingsw.am42.view.clientModel.GameClientModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This class represents the client
 * It contains the internal information useful for the client
 * and the actions it can do to interact with the other players and with the server
 *
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */


public class ClientTCP implements Client {

    private static String ip = "";
    private static int port;
    private ServerHandler serverHandler;
    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private GameClientModel clientModel;
    private boolean isRunning = true;

    public ClientTCP(String ip, int port) throws IOException {
        ClientTCP.ip = ip;
        ClientTCP.port = port;
        socket = new Socket(ip, port);

        output = new ObjectOutputStream(socket.getOutputStream());
        Thread thread = new Thread(() -> {
                try {
                    startClient();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        );
        thread.start();
    }

    /**
     * This method starts the client, it initializes the serverHandler to receive messages
     * It starts two parallel threads, one for the ServerHandler and one to check the ServerStatus
     *
     * @throws NoSuchElementException if there are mistakes during the initializing phase
     */
    public void startClient() throws IOException {
        serverHandler = new ServerHandler(socket, this);

        try {
            new Thread(serverHandler).start();
            new Thread(this::checkServerStatus).start();
            while (isRunning) {}
        } catch (final NoSuchElementException e) {
            serverDown();
        }
    }

    /**
     * This method send the message to the server
     * If there are mistakes during the sending process it manages the fact that the server is down
     *
     * @param message message to be sent
     */
    public void sendMessage(Message message) {
        if(isRunning) {
            try {
                synchronized (output) {
                    output.writeObject(message);
                    output.flush();
                }
            } catch (final IOException e) {
                serverDown();
            }
        }
    }

    /**
     * This method sends a message to the server to ask for game information
     *
     * @throws RuntimeException if the thread is interrupted
     * @return the current state of the Game
     */
    @Override
    public ConnectionState getGameInfo() {
        Message message = new GetGameInfoMessage();
        sendMessage(message);
        ConnectionStateMessage answer;
        try {
            answer = (ConnectionStateMessage) serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return answer.getConnectionState();
    }

    /**
     * This method sends a message to the server to create a new Game with the specified number of players
     * It waits for the server's answer and manages the exceptions based on the message's type
     *
     * @param nickname nickname of the Player
     * @param numPlayers number of Players chosen by the First Player
     * @throws GameFullException if the Game is full
     * @throws NicknameInvalidException if the chosen nickname is invalid
     * @throws NicknameAlreadyInUseException if the chosen nickname is already used by another player
     * @throws NumberPlayerWrongException if the chosen number is wrong
     * @throws GameAlreadyCreatedException if tha game has been already created
     * @return 1 the creation was ok
     */
    @Override
    public int createGame(String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException, GameAlreadyCreatedException {
        Message message = new FirstConnectionMessage(nickname, numPlayers);
        sendMessage(message);


        Message answer;
        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return switch (answer) {
            case NumberPlayersWrongErrorMessage numberPlayersWrongErrorMessage ->
                    throw new NumberPlayerWrongException(numberPlayersWrongErrorMessage.getMessage());
            case NicknameInvalidErrorMessage nicknameInvalidErrorMessage ->
                    throw new NicknameInvalidException(nicknameInvalidErrorMessage.getMessage());
            case NicknameAlreadyInUseErrorMessage nicknameAlreadyInUseErrorMessage ->
                    throw new NicknameAlreadyInUseException(nicknameAlreadyInUseErrorMessage.getMessage());
            case GameAlreadyCreatedErrorMessage gameAlreadyCreatedErrorMessage ->
                    throw new GameAlreadyCreatedException("Game is already created");
            default -> 1;
        };
    }

    /**
     * This method sends a message to the server to connect a player to an existing Game
     * It waits for the server's answer and manages the exceptions based on the message's type
     *
     * @param nickname nickname of the Player
     * @throws GameFullException if the Game is full
     * @throws NicknameInvalidException if the chosen nickname is invalid
     * @throws NicknameAlreadyInUseException if the chosen nickname is already used by another player
     * @return true if the connection was ok
     */
    @Override
    public boolean connect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        Message message = new ConnectMessage(nickname);
        sendMessage(message);
        Message answer;

        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return switch (answer) {
            case GameFullErrorMessage gameFullErrorMessage -> throw new GameFullException(gameFullErrorMessage.getMessage());
            case NicknameInvalidErrorMessage nicknameInvalidErrorMessage ->
                    throw new NicknameInvalidException(nicknameInvalidErrorMessage.getMessage());
            case NicknameAlreadyInUseErrorMessage nicknameAlreadyInUseErrorMessage ->
                    throw new NicknameAlreadyInUseException(nicknameAlreadyInUseErrorMessage.getMessage());
            default -> true;
        };
    }

    /**
     * This method sends a message to the server to reconnect the player to a loaded Game
     * It waits for the server's answer and manages the exceptions based on the message's type
     *
     * @param nickname nickname of the player
     * @throws GameFullException if the game is full
     * @throws NicknameInvalidException if the chosen nickname is invalid
     * @throws NicknameAlreadyInUseException if the chosen nickname is already used by another player
     * @throws GameAlreadyCreatedException if tha game has been already created
     * @return true if the reconnection was ok
     */
    @Override
    public boolean reconnect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, GameAlreadyCreatedException {
        Message message = new ReconnectMessage(nickname);
        sendMessage(message);

        Message answer;

        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return switch (answer) {
            case GameFullErrorMessage gameFullErrorMessage -> throw new GameFullException(gameFullErrorMessage.getMessage());
            case NicknameInvalidErrorMessage nicknameInvalidErrorMessage ->
                    throw new NicknameInvalidException(nicknameInvalidErrorMessage.getMessage());
            case NicknameAlreadyInUseErrorMessage nicknameAlreadyInUseErrorMessage ->
                    throw new NicknameAlreadyInUseException(nicknameAlreadyInUseErrorMessage.getMessage());
            case GameAlreadyCreatedErrorMessage gameAlreadyCreatedErrorMessage ->
                    throw new GameAlreadyCreatedException("Game is already created");
            default -> true;
        };
    }

    /**
     * This method sends a message to the server asking for the available positions
     * It waits for the server's answer and manages the exceptions if something goes wrong
     *
     * @param p identifier of the Player
     * @throws WrongTurnException if the player can't do this action
     * @return Set of available positions
     */
    @Override
    public Set<Position> getAvailablePositions(String p) throws WrongTurnException {
        Message message = new GetAvailablePositionMessage(p);
        sendMessage(message);
        SendAvailablePositionMessage answer;

        try {
            answer = (SendAvailablePositionMessage) serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            throw new WrongTurnException("You can't do this action now!");
        }
        return answer.getPositions();
    }

    /**
     * This method sends a message to the server to place the StartingCard
     * It waits for the server's answer and manages the exceptions if something goes wrong
     *
     * @param p identifier of the player
     * @param face face of the Starting Card
     * @throws WrongTurnException if the player can't do this action
     * @return List of available Colors
     */
    public List<PlayersColor> placeStarting(String p, Face face) throws WrongTurnException {
        Message message = new PlaceStartingMessage(p, face);
        sendMessage(message);

        SendAvailableColorsMessage answer;
        try {
            answer = (SendAvailableColorsMessage) serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            throw new WrongTurnException("You can't do this action now!");
        }
        return answer.getColors();
    }

    /**
     * This method sends a message to the server to place a Card in the chosen position
     * It waits for the server's answer and manages the exceptions if something goes wrong
     *
     * @param p identifier of the player
     * @param face face of the Card
     * @param pos position chosen by the Player
     * @throws WrongTurnException if the player can't do this action
     * @throws RequirementsNotMetException if the chosen Card doesn't satisfy the requirements
     * @return true if the placement was successful
     */
    @Override
    public boolean place(String p, Face face, Position pos) throws RequirementsNotMetException, WrongTurnException {
        Message message = new PlaceMessage(p, face, pos);
        sendMessage(message);

        Message answer;

        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(answer instanceof NoRequirementsErrorMessage)
            throw new RequirementsNotMetException("Requirements are not met");
        if(answer instanceof WrongTurnErrorMessage)
            throw new WrongTurnException("You can't do this action now!");

        return true;
    }

    /**
     * This method sends a message to the server to set the chosen Color
     * It waits for the server's answer and manages the exceptions if something goes wrong
     *
     * @param p identifier of the player
     * @param color Color chosen by the player
     * @throws WrongTurnException if the player can't do this action
     * @return List of Goal Card
     */
    @Override
    public List<GoalCard> chooseColor(String p, PlayersColor color) throws WrongTurnException {
        Message message = new ChosenColorMessage(p, color);
        sendMessage(message);
        SendPossibleGoalsMessage answer;
        try {
            answer = (SendPossibleGoalsMessage) serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            throw new WrongTurnException("You can't do this action now!");
        }

        return answer.getGoals();
    }

    /**
     * This method sends a message to the server to set the chosen Goal Card
     * It waits for the server's answer and manages the exceptions if something goes wrong
     *
     * @param p identifier of the player
     * @param goal Goal Card chosen by the Player
     * @throws WrongTurnException if the player can't do this action
     */
    @Override
    public void chooseGoal(String p, GoalCard goal) throws WrongTurnException {
        Message message = new ChosenGoalMessage(p, goal);
        sendMessage(message);

        Message answer;

        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(answer instanceof WrongTurnErrorMessage)
            throw new WrongTurnException("You can't do this action now!");
    }

    /**
     * This method sends a message to the server to sets the picked Card from the Playable Cards
     * It waits for the server's answer and manages the exceptions if something goes wrong
     *
     * @param p identifier of the card
     * @param card Playable Card chosen by the Player
     * @throws WrongTurnException if the player can't do this action
     */
    @Override
    public void pick(String p, PlayableCard card) throws WrongTurnException {
        Message message = new PickMessage(p, card);
        sendMessage(message);

        Message answer;

        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(answer instanceof WrongTurnErrorMessage)
            throw new WrongTurnException("You can't do this action now!");
    }

    /**
     * This method sends a message to the server to get the List of Winners
     * It waits for the server's answer and manages the exceptions if something goes wrong
     *
     * @throws WrongTurnException if the player can't do this action
     * @return List of Players
     */
    @Override
    public List<Player> getWinner() throws WrongTurnException {
        Message message = new GetWinnerMessage();
        sendMessage(message);

        SendWinnerMessage answer;

        try {
            answer = (SendWinnerMessage) serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            throw new WrongTurnException("You can't do this action now!");
        }

        return answer.getWinners();
    }

    /**
     * This method updates the view with the Change sent by the server
     * The Change object is given to the view
     *
     * @param change Change with the updated information
     */
    public void update(Change change) {
        clientModel.update(change);
    }

    /**
     * This method sends a ChatMessage to the server
     *
     * @param message ChatMessage to be sent
     */
    @Override
    public void sendChatMessage(ChatMessage message) {
        sendMessage(message);
    }

    /**
     * This method receives a message from the server
     *
     * @param message Message received from the server
     */
    public void receiveMessage(Message message){
        clientModel.updateMessage(message);
    }

    /**
     * This method updates the view because a Player has disconnected
     */
    public void updateDisconnection(){
        clientModel.setGameAborted(true);
    }

    /**
     * This method sends a message to the server to notify that the Player has disconnected
     */
    public void playerDisconnected() {
        sendMessage(new PlayerDisconnectedMessage());
        updateDisconnection();
    }

    /**
     * This method checks the Server Status in order to know if the server's connection is still open
     * If the server has disconnected it handles the disconnection
     */
    public void checkServerStatus() {
        while (isRunning) {
            try {
               sendMessage(new PingMessage());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                serverDown();
            }
        }
    }

    /**
     * This method manages the server's disconnection
     * It updates the view
     * It sets the flag to stop running the Thread
     * It closes all the streams and connections
     * If something goes wrong during these closing process it waits before closing the program
     */
    public void serverDown() {
        clientModel.setServerDown(true);
        isRunning = false;
        serverHandler.closeAll();
        try {
            output.close();
            socket.close();
        } catch (IOException e) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
                System.exit(0);
            }
        }
    }

    /**
     * This method sets the Client Model
     *
     * @param clientModel GameClientModel associated to the client
     */
    public void setClientModel(GameClientModel clientModel) {
        this.clientModel = clientModel;
    }
    /**
     * This method returns the client model
     *
     * @return the GameClientModel of the player
     */
    public GameClientModel getClientModel() {
        return clientModel;
    }
}