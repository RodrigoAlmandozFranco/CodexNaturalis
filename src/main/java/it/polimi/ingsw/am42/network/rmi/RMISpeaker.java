package it.polimi.ingsw.am42.network.rmi;

import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.chat.ChatMessage;

import java.rmi.Remote;
import java.util.List;
import java.util.Set;

public interface RMISpeaker extends Remote {


    public String getGameInfo();

    public int createGame(MessageListener l, String nickname, int numPlayers) throws NumberPlayerWrongException,
                                                                                        GameFullException,
                                                                                        NicknameInvalidException,
                                                                                        NicknameAlreadyInUseException;

    public boolean connect(MessageListener l, String nickname, int gameId)
                                 throws GameFullException,
                                        NicknameInvalidException,
                                        NicknameAlreadyInUseException;

    public boolean reconnect(MessageListener l, String nickname, int gameId)
                                 throws GameFullException,
                                        NicknameInvalidException,
                                        NicknameAlreadyInUseException;


    public Set<Position> getAvailablePositions(String p);

    public boolean place(String p, Face face, Position position) throws RequirementsNotMetException;

    public void pick(String p, PlayableCard card);

    public void chooseColor(String p, Color color);

    public List<GoalCard> getGoals(String p);

    public void chooseGoal(String p, GoalCard goal);

    public List<Player> getWinner();

    public List<Color> getAvailableColors(String p);

    public void playerDisconnected();

    public void sendChatMessage(ChatMessage chatMessage);
}
