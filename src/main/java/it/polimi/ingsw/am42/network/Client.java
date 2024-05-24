package it.polimi.ingsw.am42.network;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.view.gameview.GameView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;

public interface Client {

    ConnectionState getGameInfo();

    public  int createGame(String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException;

    boolean connect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException;
    boolean reconnect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException;

    Set<Position> getAvailablePositions(String p);

    boolean place(String p, Face face, Position pos) throws RequirementsNotMetException;

    List<GoalCard> chooseColor(String p, PlayersColor color);

    void chooseGoal(String p, GoalCard goal);

    void pick(String p, PlayableCard card);

    List<Player> getWinner();

    List<PlayersColor> placeStarting(String p, Face face);

    void sendChatMessage(ChatMessage message);
    void receiveMessage(Message message);
    void setView(GameView view);

    void playerDisconnected();

    void updateDisconnection();
    GameView getView();
}
