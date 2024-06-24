package it.polimi.ingsw.am42.network;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.view.clientModel.GameClientModel;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;


/**
 * This interface of the client
 *
 */
public interface Client {

    /**
     * Retrieves the current state of the game.
     */
    ConnectionState getGameInfo();

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
    public  int createGame(String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException, GameAlreadyCreatedException;

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
    boolean connect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException;

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
    boolean reconnect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, GameAlreadyCreatedException;

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
    Set<Position> getAvailablePositions(String p) throws WrongTurnException;

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
    boolean place(String p, Face face, Position pos) throws RequirementsNotMetException, WrongTurnException;

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
    List<GoalCard> chooseColor(String p, PlayersColor color) throws WrongTurnException;

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
    void chooseGoal(String p, GoalCard goal) throws WrongTurnException;

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
    void pick(String p, PlayableCard card) throws WrongTurnException;

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
    List<Player> getWinner() throws WrongTurnException;

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
    List<PlayersColor> placeStarting(String p, Face face) throws WrongTurnException;

    /**
     * Sends a chat message.
     *
     * <p>This method sends a chat message to the server that then delivers it to the
     * players contained in receiver.
     * The message is represented by a ChatMessage object.
     *
     * @param message the ChatMessage object representing the chat message to be sent.
     */
    void sendChatMessage(ChatMessage message);

    /**
     * Delivers a received message to the clientModel.
     * <p>
     * This method processes a received message represented by a Message object.
     * It handles the necessary actions based on the content of the message.
     *
     * @param message the Message object representing the received message.
     */
    void receiveMessage(Message message);
    void setClientModel(GameClientModel clientModel);

    /**
     * Handles a player disconnection.
     *
     * <p>This method attempts to notify the server about a player disconnection via the stub.
     * If a RemoteException occurs, it handles the server being down by calling serverDown().
     * Regardless of the exception, it updates the client model to indicate that the game has been aborted
     * due to disconnection.</p>
     */
    void playerDisconnected();

    /**
     * Checks the connectivity status with the server periodically.
     *
     * <p>This method is used to check if the remote connection is still active.
     */
    void checkServerStatus();

    /**
     * Updates the client model to indicate that the server is down.
     *
     * <p>This method sets the server status to down in the client model,
     * allowing the client application to handle the server down scenario appropriately.</p>
     */
    void serverDown();

    /**
     * Updates the client model to indicate that the game has been aborted due
     * to disconnection of another player.
     *
     * <p>This method sets the game aborted status to true in the client model,
     * allowing the client application to handle the disconnection scenario appropriately.</p>
     */
    void updateDisconnection();
    GameClientModel getClientModel();

}
