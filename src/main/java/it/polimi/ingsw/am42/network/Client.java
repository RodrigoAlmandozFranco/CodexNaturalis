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

import java.util.List;
import java.util.Set;

public interface Client {

    ConnectionState getGameInfo();

    public  int createGame(String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException, GameAlreadyCreatedException;

    boolean connect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException;
    boolean reconnect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, GameAlreadyCreatedException;

    Set<Position> getAvailablePositions(String p) throws WrongTurnException;

    boolean place(String p, Face face, Position pos) throws RequirementsNotMetException, WrongTurnException;

    List<GoalCard> chooseColor(String p, PlayersColor color) throws WrongTurnException;

    void chooseGoal(String p, GoalCard goal) throws WrongTurnException;

    void pick(String p, PlayableCard card) throws WrongTurnException;

    List<Player> getWinner() throws WrongTurnException;

    List<PlayersColor> placeStarting(String p, Face face) throws WrongTurnException;

    void sendChatMessage(ChatMessage message);
    void receiveMessage(Message message);
    void setView(GameClientModel view);

    void playerDisconnected();

    void checkServerStatus();

    void serverDown();

    void updateDisconnection();
    GameClientModel getView();

}
