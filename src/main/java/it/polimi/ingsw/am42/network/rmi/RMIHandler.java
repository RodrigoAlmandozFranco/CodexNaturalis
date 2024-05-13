package it.polimi.ingsw.am42.network.rmi;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
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

    @Override
    public ConnectionState getGameInfo() {
        return controller.getGameInfo();
    }

    @Override
    public int createGame(MessageListener l, String nickname, int numPlayers) throws RemoteException{

        try {
            return controller.createGame(l, nickname, numPlayers);
        }
        catch (NumberPlayerWrongException | GameFullException | NicknameInvalidException | NicknameAlreadyInUseException e) {

            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public boolean connect(MessageListener l, String nickname) throws RemoteException {
        try {
            return controller.connect(l, nickname);
        }
        catch (GameFullException | NicknameInvalidException | NicknameAlreadyInUseException e) {
            throw new RemoteException(e.getMessage(), e);
        }

    }

    @Override
    public boolean reconnect(MessageListener l, String nickname) throws RemoteException {
        try {
            return controller.reconnect(l, nickname);
        }
        catch (GameFullException | NicknameInvalidException | NicknameAlreadyInUseException e) {

            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public Set<Position> getAvailablePositions(String p) {
        return controller.getAvailablePositions(p);
    }

    @Override
    public boolean place(String p, Face face, Position position) throws RemoteException {
        try {
            return controller.place(p, face, position);
        }
        catch (RequirementsNotMetException e) {
            throw new RemoteException(e.getMessage(), e);
        }

    }

    @Override
    public void pick(String p, PlayableCard card) {
        controller.pick(p, card);
    }

    @Override
    public List<GoalCard> chooseColor(String p, Color color) {
        return controller.chooseColor(p, color);
    }

    @Override
    public void chooseGoal(String p, GoalCard goal) {
        controller.chooseGoal(p, goal);
    }

    @Override
    public List<Player> getWinner() {
        return controller.getWinner();
    }

    @Override
    public List<Color> placeStarting(String p, Face face) {
        return controller.placeStarting(p, face);
    }

    @Override
    public void playerDisconnected() {
        controller.playerDisconnected();
    }

    @Override
    public void sendChatMessage(ChatMessage chatMessage) {
        controller.sendChatMessage(chatMessage);

    }
}
