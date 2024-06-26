package it.polimi.ingsw.am42.network.rmi;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.chat.ChatMessage;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;


/**
 * Auxiliary class needed for RMI clients to interact with controller
 * @see Controller
 * @see RMIClient
 * Transforms custom exceptions into RemoteExceptions
 *
 * @author Tommaso Crippa
 * @author Alessandro Di Maria
 */
public class RMIHandler implements RMISpeaker{

    private final Controller controller;
    public RMIHandler(Controller controller) {
        this.controller = controller;
    }

    /**
     * Method to request which kind of way to connect to the game
     *
     * @return ConnectionState
     */
    @Override
    public ConnectionState getGameInfo() {
        return controller.getGameInfo();
    }
    /**
     * Method to create new game
     *
     * @param l Reference to the calling object, used to receive notification for observer pattern
     * @param nickname Name of the player
     * @param numPlayers Number of players that are going to play the game
     * @return 0 if game successfully created, otherwise an exception is raised
     *
     * @throws NumberPlayerWrongException if the numPlayers provided is not in the possible interval
     * @throws GameFullException if the game has already all players connected
     * @throws NicknameInvalidException if the nickname doesn't have a valid format
     * @throws NicknameAlreadyInUseException if the nickname has already been chosen by another player
     */
    @Override
    public int createGame(MessageListener l, String nickname, int numPlayers) throws RemoteException{
        return controller.createGame(l, nickname, numPlayers);
    }
    /**
     *
     * Standard way of connecting to newly created game
     *
     * @param l Reference to the calling object, used to receive notification for observer pattern
     * @param nickname Name of the player
     * @return true if the connection works without problems, otherwise it throws an exception
     *
     * @throws GameFullException if the game has already all players connected
     * @throws NicknameInvalidException if the nickname doesn't have a valid format
     * @throws NicknameAlreadyInUseException if the nickname has already been chosen by another player
     */
    @Override
    public boolean connect(MessageListener l, String nickname) throws RemoteException {
        return controller.connect(l, nickname);
    }
    /**
     *
     * Standard way of connecting to saved game AND loading up saved game
     *
     * @param l Reference to the calling object, used to receive notification for observer pattern
     * @param nickname Name of the player
     * @return true if the connection works without problems, otherwise it throws an exception
     *
     * @throws GameFullException if the game has already all players connected
     * @throws NicknameInvalidException if the nickname doesn't have a valid format
     * @throws NicknameAlreadyInUseException if the nickname has already been chosen by another player
     */
    @Override
    public boolean reconnect(MessageListener l, String nickname) throws RemoteException {
        return controller.reconnect(l, nickname);
    }
    /**
     * Method to get the available positions to place of the current player
     * @param p Nickname of method caller
     *
     * @return set with available positions
     */
    @Override
    public Set<Position> getAvailablePositions(String p) throws RemoteException {
        return controller.getAvailablePositions(p);
    }
    /**
     * Method called by the current player to place a specific face on a specific position
     *
     * @param p Nickname of method caller
     * @param face Selected Face
     * @param position Selected Position
     * @return true if placed successfully, otherwise throws an exception
     *
     * @throws RequirementsNotMetException if the player's board doesn't fulfill the face requirements
     */
    @Override
    public boolean place(String p, Face face, Position position) throws RemoteException {
        return controller.place(p, face, position);
    }

    /**
     * Method called by the current player to pick one of the pickable cards from the deck
     *
     * @param p Nickname of method caller
     * @param card Selected Card to add to hand
     */
    @Override
    public void pick(String p, PlayableCard card) throws RemoteException {
        controller.pick(p, card);
    }
    /**
     * Method called by the current player to select which color to identify itself
     * @param p Nickname of method caller
     * @param color the Selected Color
     * @return The list of objectives, the player now has to select which one it should choose
     */
    @Override
    public List<GoalCard> chooseColor(String p, PlayersColor color) throws RemoteException {
        return controller.chooseColor(p, color);
    }

    /**
     * Method called by the current player to select which objective to try and fulfill during the game
     * @param p Nickname of method caller
     * @param goal Selected Goal
     */
    @Override
    public void chooseGoal(String p, GoalCard goal) throws RemoteException {
        controller.chooseGoal(p, goal);
    }
    /**
     * Returns the list of winners (more than one if there is a tie)
     * @return list of winners
     */
    @Override
    public List<Player> getWinner() throws RemoteException {
        return controller.getWinner();
    }
    /**
     * Method called by the current player to place THE FIRST face (of the starting card) on the board
     * @param p Nickname of method caller
     * @param face Selected face
     * @return The list of available colors, the player now has to choose which one to use
     */
    @Override
    public List<PlayersColor> placeStarting(String p, Face face) throws RemoteException {
        return controller.placeStarting(p, face);
    }

    /**
     * Method called by the players to disconnect
     */
    @Override
    public void playerDisconnected() {
        controller.playerDisconnected();
    }

    /**
     * Method to send messages to other players
     * @param chatMessage the message to send
     */
    @Override
    public void sendChatMessage(ChatMessage chatMessage) {
        controller.sendChatMessage(chatMessage);

    }

    @Override
    public void getStatus() throws RemoteException{
    };
}
