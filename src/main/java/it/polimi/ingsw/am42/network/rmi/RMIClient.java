package it.polimi.ingsw.am42.network.rmi;


import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.view.clientModel.GameClientModel;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * This class acts as the client when using RMI connection
 *
 * @author Alessandro Di Maria
 */
public class RMIClient extends UnicastRemoteObject implements Client, RMIMessageListener, Serializable {
    private Registry registry;
    private RMISpeaker stub;
    private GameClientModel clientModel;
    private String nickname;

    /**
     * This constructor creates a new RMI Client
     *
     * @param host address of the host
     * @param port
     * @throws RemoteException if the connection to the server fails
     */
    public RMIClient(String host, int port) throws RemoteException {
        try {
            this.registry = LocateRegistry.getRegistry(host, port);// si potrebbe mettere anche la porta
            this.stub = (RMISpeaker) registry.lookup("RMIHandler");
            System.out.println("Connected to Server!!!");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        this.nickname = "Still missing";
        new Thread(this::checkServerStatus).start();
    }

    /**
     * Retrieves the current state of the game.
     *
     */
    public ConnectionState getGameInfo(){
        try {
            return stub.getGameInfo();
        } catch (RemoteException e) {
            serverDown();
            return null;
        }

    }

    /**
     * Creates a new game with the specified nickname and number of players.
     * <p>
     * This method initializes a new game session using the provided nickname and
     * number of players. It validates the inputs and may throw exceptions if the
     * conditions are not met.
     *
     * @param nickname the nickname of the player creating the game. It must be a valid, unique nickname.
     * @param numPlayers the number of players in the game. It must be a positive number within the allowed range.
     * @throws GameFullException if the game cannot accommodate more players.
     * @throws NicknameInvalidException if the provided nickname is invalid.
     * @throws NicknameAlreadyInUseException if the provided nickname is already in use.
     * @throws NumberPlayerWrongException if the number of players is outside the allowed range.
     */
    public int createGame(String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException, GameAlreadyCreatedException {
        try {
            int gameID = stub.createGame(this, nickname, numPlayers);
            this.nickname = nickname;
            return gameID;
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof GameAlreadyCreatedException)
                throw (GameAlreadyCreatedException) originalException;
            if (originalException instanceof GameFullException)
                throw (GameFullException) originalException;
            if (originalException instanceof NicknameAlreadyInUseException)
                throw (NicknameAlreadyInUseException) originalException;
            if (originalException instanceof NicknameInvalidException)
                throw (NicknameInvalidException) originalException;
            serverDown();
            return -1;
        }
    }

    /**
     * Connects a player to the game using the specified nickname.
     *
     * <p>This method attempts to connect a player to the game session using the provided
     * nickname. It validates the nickname and ensures that the game can accommodate the new player.
     * If the conditions are not met, appropriate exceptions are thrown.
     *
     * @param nickname the nickname of the player attempting to connect. It must be a valid, unique nickname.
     * @return true if the connection is successful, false otherwise.
     * @throws GameFullException if the game cannot accommodate more players.
     * @throws NicknameInvalidException if the provided nickname is invalid.
     * @throws NicknameAlreadyInUseException if the provided nickname is already in use.
     */
    public boolean connect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        try {
            this.nickname = nickname;
            return stub.connect(this, nickname);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof GameFullException)
                throw (GameFullException) originalException;
            if (originalException instanceof NicknameAlreadyInUseException)
                throw (NicknameAlreadyInUseException) originalException;
            if (originalException instanceof NicknameInvalidException)
                throw (NicknameInvalidException) originalException;
            serverDown();
            return false;
        }
    }

    /**
     * Reconnects a player to the game using the specified nickname.
     *
     * <p>This method attempts to reconnect a player to the game session using the provided
     * nickname. Or to load a previous game session after a fail of the server.
     * It validates the nickname and ensures that the game can accommodate the player.
     * If the conditions are not met, appropriate exceptions are thrown.
     *
     * @param nickname the nickname of the player attempting to reconnect. It must be a valid, unique nickname.
     * @return true if the reconnection is successful, false otherwise.
     * @throws GameFullException if the game cannot accommodate more players.
     * @throws NicknameInvalidException if the provided nickname is invalid, or it was not in the previous game.
     * @throws NicknameAlreadyInUseException if the provided nickname is already in use.
     */
    public boolean reconnect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, GameAlreadyCreatedException {
        try {
            this.nickname = nickname;
            return stub.reconnect(this, nickname);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof GameAlreadyCreatedException)
                throw (GameAlreadyCreatedException) originalException;
            if (originalException instanceof GameFullException)
                throw (GameFullException) originalException;
            if (originalException instanceof NicknameAlreadyInUseException)
                throw (NicknameAlreadyInUseException) originalException;
            if (originalException instanceof NicknameInvalidException)
                throw (NicknameInvalidException) originalException;
            serverDown();
            return false;
        }
    }

    /**
     * Retrieves the available positions for the player.
     *
     * <p>This method returns a set of positions where the specified player can place a card.
     * It validates whether it is the correct turn for the player and may throw an exception
     * if it is not the player's turn.
     *
     * @param p the identifier or nickname of the player whose available positions are to be retrieved.
     * @return a set of positions representing the available positions for the player.
     * @throws WrongTurnException if it is not the specified player's turn.
     */
    public Set<Position> getAvailablePositions(String p) throws WrongTurnException {
        try {
            return stub.getAvailablePositions(p);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof WrongTurnException)
                throw (WrongTurnException) originalException;
            serverDown();
            return new TreeSet<Position>();
        }
    }

    /**
     * Places a card for the specified player on the given position on the specified face.
     *
     * <p>This method attempts to place a card for the player identified by the given nickname
     * at the specified position with the specified face orientation. It checks if the requirements
     * for placing the card are met and whether it is the player's turn. Appropriate exceptions
     * are thrown if the conditions are not satisfied.
     *
     * @param p the nickname of the player placing the card.
     * @param face the face orientation of the card to be placed.
     * @param pos the position where the card is to be placed.
     * @return true if the card is successfully placed, false otherwise.
     * @throws RequirementsNotMetException if the requirements for placing the piece are not met.
     * @throws WrongTurnException if it is not the specified player's turn.
     */
    public boolean place(String p, Face face, Position pos) throws RequirementsNotMetException, WrongTurnException {
        try {
            return stub.place( p, face, pos);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof WrongTurnException)
                throw (WrongTurnException) originalException;
            if (originalException instanceof RequirementsNotMetException)
                throw (RequirementsNotMetException) originalException;
            serverDown();
            return false;
        }
    }

    /**
     * Places the starting card for the specified player on the specified face.
     *
     * <p>This method attempts to place the starting card for the player identified by the given
     * nickname with the specified face orientation. It validates whether it is the correct turn
     * for the player and may throw an exception if it is not the player's turn.
     * The method returns a list of PlayerColor from which the player one in the next turn.
     *
     * @param p the nickname of the player placing the card.
     * @param face the face orientation of the card to be placed.
     * @return a list of PlayerColor.
     * @throws WrongTurnException if it is not the specified player's turn.
     */
    public List<PlayersColor> placeStarting(String p, Face face) throws WrongTurnException {
        try {
            return stub.placeStarting(p, face);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof WrongTurnException)
                throw (WrongTurnException) originalException;
            serverDown();
            return null;
        }
    }

    /**
     * Allows the specified player to choose a color.
     *
     * <p>This method lets the player identified by the given nickname choose a color.
     * It checks if it is the player's turn and may throw an exception if it is not the player's turn.
     * The method returns a list of goalCards from which the player one in the next turn.
     *
     * @param p the nickname of the player choosing the color.
     * @param color chosen by the player.
     * @return a list of goalCards.
     * @throws WrongTurnException if it is not the specified player's turn.
     */
    public List<GoalCard> chooseColor(String p, PlayersColor color) throws WrongTurnException {
        try {
            return stub.chooseColor(p, color);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof WrongTurnException)
                throw (WrongTurnException) originalException;
            serverDown();
            return null;
        }
    }

    /**
     * Allows the specified player to choose a goal card.
     *
     * <p>This method lets the player identified by the given nickname choose his personal goal.
     * It checks if it is the player's turn and may throw an exception if it is not the player's turn.
     *
     * @param p the nickname of the player choosing the goal card.
     * @param goal the GoalCard chosen by the player.
     * @throws WrongTurnException if it is not the specified player's turn.
     */
    public void chooseGoal(String p, GoalCard goal) throws WrongTurnException{
        try {
            stub.chooseGoal( p, goal);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof WrongTurnException)
                throw (WrongTurnException) originalException;
            serverDown();
        }
    }

    /**
     * Allows the specified player to pick a playable card.
     *
     * <p>This method lets the player identified by the given nickname pick a playable card.
     * It checks if it is the player's turn and may throw an exception if it is not the player's turn.
     *
     * @param p the nickname of the player picking the card.
     * @param card the PlayableCard chosen by the player.
     * @throws WrongTurnException if it is not the specified player's turn.
     */
    public void pick(String p, PlayableCard card) throws WrongTurnException {
        try {
            stub.pick(p, card);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof WrongTurnException)
                throw (WrongTurnException) originalException;
            serverDown();
        }
    }

    /**
     * Returns the unique identifier (nickname) of the player.
     *
     * <p>This method retrieves the unique identifier, which is the nickname, of the player.
     * It is used to identify the player within the game.
     *
     * @return the nickname of the player.
     */
    @Override
    public String getId() {
        return this.nickname;
    }

    /**
     * Retrieves the list of players who have won the game.
     *
     * <p>This method returns a list of players who are considered winners based on the game's
     * winning conditions. It checks if the method is called at the appropriate game state and
     * may throw an exception if it is called at the wrong turn.
     *
     * @return a list of Players representing the winners of the game.
     * @throws WrongTurnException if the method is called at the wrong turn or game state.
     */
    public List<Player> getWinner() throws WrongTurnException {
        try {
            return stub.getWinner();
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof WrongTurnException)
                throw (WrongTurnException) originalException;
            serverDown();
            return null;
        }
    }

    /**
     * Updates the game state with the specified change.
     *
     * <p>This method applies the given change to the game state and updates the view accordingly.
     * It may throw a {@link RemoteException} if a communication-related error occurs during the update.
     *
     * @param diff the Change object representing the difference or update to be applied.
     * @throws RemoteException if a communication-related error occurs during the update.
     */
    @Override
    public void update(Change diff) throws RemoteException{
        clientModel.update(diff);
    }

    /**
     * Sends a chat message.
     *
     * <p>This method sends a chat message to the server that then delivers it to the
     * players contained in receiver.
     * The message is represented by a ChatMessage object.
     *
     * @param message the ChatMessage object representing the chat message to be sent.
     */
    @Override
    public void sendChatMessage(ChatMessage message) {
        try {
            stub.sendChatMessage(message);
        } catch (RemoteException e) {
            serverDown();
        }
    }

    /**
     * Delivers a received message to the clientModel.
     * <p>
     * This method processes a received message represented by a Message object.
     * It handles the necessary actions based on the content of the message.
     *
     * @param message the Message object representing the received message.
     */
    @Override
    public void receiveMessage(Message message) {
        clientModel.updateMessage(message);
    }

    /**
     * Checks the connectivity status.
     *
     * <p>This method is used to check if the remote connection is still active.
     * It returns true if the connection is active. It may throw a
     * RemoteException if a communication-related error occurs.
     *
     * @return true if the connection is active.
     * @throws RemoteException if a communication-related error occurs.
     */
    public boolean heartbeat() throws RemoteException {
        return true;
    }

    /**
     * Checks the connectivity status with the server periodically.
     *
     * <p>This method is used to check if the remote connection is still active.
     */
    public void checkServerStatus() {
        while (true) {
            try {
                stub.getStatus();
                Thread.sleep(5000);
            } catch (InterruptedException | RemoteException e) {
                serverDown();
                break;
            }
        }
    }

    /**
     * Updates the client model to indicate that the server is down.
     *
     * <p>This method sets the server status to down in the client model,
     * allowing the client application to handle the server down scenario appropriately.</p>
     */
    public void serverDown() {
        clientModel.setServerDown(true);
    }

    /**
     * Updates the client model to indicate that the game has been aborted due
     * to disconnection of another player.
     *
     * <p>This method sets the game aborted status to true in the client model,
     * allowing the client application to handle the disconnection scenario appropriately.</p>
     */
    public void updateDisconnection(){
         clientModel.setGameAborted(true);
    }

    public void setClientModel(GameClientModel clientModel) {
        this.clientModel = clientModel;
    }

    /**
     * Handles a player disconnection.
     *
     * <p>This method attempts to notify the server about a player disconnection via the stub.
     * If a RemoteException occurs, it handles the server being down by calling serverDown().
     * Regardless of the exception, it updates the client model to indicate that the game has been aborted
     * due to disconnection.</p>
     */
    @Override
    public void playerDisconnected() {
        try {
            stub.playerDisconnected();
        } catch (RemoteException e) {
            serverDown();
        }
        updateDisconnection();
    }

    public GameClientModel getClientModel() {
        return clientModel;
    }
}