package it.polimi.ingsw.am42.network.rmi;

import it.polimi.ingsw.am42.controller.ConnectionState;
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
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;




/**
 * Interface used by RMI clients to interact with the model
 *
 * @author Tommaso Crippa
 * @author Alessandro Di Maria
 */
public interface RMISpeaker extends Remote {


    public ConnectionState getGameInfo() throws RemoteException;

    public int createGame(MessageListener l, String nickname, int numPlayers) throws RemoteException;

    public boolean connect(MessageListener l, String nickname, int gameId)
                                  throws RemoteException;

    public boolean reconnect(MessageListener l, String nickname, int gameId)
                                 throws RemoteException;


    public Set<Position> getAvailablePositions(String p) throws RemoteException;

    public boolean place(String p, Face face, Position position) throws RemoteException;

    public void pick(String p, PlayableCard card) throws RemoteException;

    public List<GoalCard> chooseColor(String p, Color color) throws RemoteException;

    public void chooseGoal(String p, GoalCard goal) throws RemoteException;

    public List<Player> getWinner() throws RemoteException;

    public List<Color> placeStarting(String p, Face face) throws RemoteException;

    public void playerDisconnected() throws RemoteException;

    public void sendChatMessage(ChatMessage chatMessage) throws RemoteException;
}
