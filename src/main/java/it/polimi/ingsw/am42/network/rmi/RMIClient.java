package it.polimi.ingsw.am42.network.rmi;


import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.view.gameview.GameView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;


/**
 * This class acts as the client when using RMI connection
 *
 * @author Alessandro Di Maria
 */
public class RMIClient extends UnicastRemoteObject implements Client, RMIMessageListener, Serializable {
    private Registry registry;
    private RMISpeaker stub;
    private GameView view;
    private String nickname;
    public RMIClient(String host, int port) throws RemoteException {
        //TODO da riga di comando ricevo se voglio un view GUI o TUI
        //this.view = new View(this);
        try {
            this.registry = LocateRegistry.getRegistry(host, port);// si potrebbe mettere anche la porta
            this.stub = (RMISpeaker) registry.lookup("RMIHandler");
            System.out.println("Connected to Server!!!");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        this.nickname = "Still missing";
    }

    public ConnectionState getGameInfo(){
        try {
            return stub.getGameInfo();
        } catch (RemoteException e) {
            //TODO notify the view that the server is dead
            return null;
        }

    }

    public int createGame(String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException {
        try {
            int gameID = stub.createGame(this, nickname, numPlayers);
            this.nickname = nickname;
            return gameID;
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof GameFullException)
                throw (GameFullException) originalException;
            if (originalException instanceof NicknameInvalidException)
                throw (NicknameInvalidException) originalException;
            if (originalException instanceof NicknameAlreadyInUseException)
                throw (NicknameAlreadyInUseException) originalException;
            if (originalException instanceof NumberPlayerWrongException)
                throw (NumberPlayerWrongException) originalException;

            //TODO notify the view that the server is dead
            else return -1;
        }
    }

    public boolean connect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        try {
            return stub.connect(this, nickname);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof GameFullException)
                throw (GameFullException) originalException;
            if (originalException instanceof NicknameInvalidException)
                throw (NicknameInvalidException) originalException;
            if (originalException instanceof NicknameAlreadyInUseException)
                throw (NicknameAlreadyInUseException) originalException;

            //TODO notify the view that the server is dead
            else return false;
        }
    }

    public boolean reconnect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        try {
            return stub.reconnect(this, nickname);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof GameFullException)
                throw (GameFullException) originalException;
            if (originalException instanceof NicknameInvalidException)
                throw (NicknameInvalidException) originalException;
            if (originalException instanceof NicknameAlreadyInUseException)
                throw (NicknameAlreadyInUseException) originalException;

            //TODO notify the view that the server is dead
            else return false;
        }
    }

    @Override
    public boolean connectAfterLoad(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        return false;
    }

    public Set<Position> getAvailablePositions(String p){
        try {
            return stub.getAvailablePositions(p);
        } catch (RemoteException e) {
            //TODO notify the view that the server is dead
            return null;
        }
    }

    public boolean place(String p, Face face, Position pos) throws RequirementsNotMetException {
        try {
            return stub.place( p, face, pos);
        } catch (RemoteException e) {
            Throwable originalException = e.getCause();
            if (originalException instanceof RequirementsNotMetException)
                throw (RequirementsNotMetException) originalException;

            //TODO notify the view that the server is dead
            else return false;
        }
    }

    public List<Color> placeStarting(String p, Face face){
        try {
            return stub.placeStarting(p, face);
        } catch (RemoteException e) {
            //TODO notify the view that the server is dead
            return null;
        }
    }

    public List<GoalCard> chooseColor(String p, Color color) {
        try {
            return stub.chooseColor(p, color);
        } catch (RemoteException e) {
            //TODO notify the view that the server is dead
            return null;
        }
    }

    public void chooseGoal(String p, GoalCard goal){
        try {
            stub.chooseGoal( p, goal);
        } catch (RemoteException e) {
            //TODO notify the view that the server is dead
        }
    }

    public void pick(String p, PlayableCard card){
        try {
            stub.pick(p, card);
        } catch (RemoteException e) {
            //TODO notify the view that the server is dead
        }
    }

    @Override
    public String getId() {
        return this.nickname;
    }

    public List<Player> getWinner() {
        try {
            return stub.getWinner();
        } catch (RemoteException e) {
            //TODO notify the view that the server is dead
            return null;
        }
    }

    @Override
    public void update(Change diff) throws RemoteException{
        view.update(diff);
    }

    @Override
    public void sendChatMessage(ChatMessage message) {
        try {
            stub.sendChatMessage(message);
        } catch (RemoteException e) {
            //TODO notify the view that the server is dead
        }
    }

    @Override
    public void receiveMessage(Message message) {
        // TODO: implement receive Message
        // view.receiveMessage(message)
    }

    public boolean heartbeat() throws RemoteException {
        return true;
    }


    public void setView(GameView view) {
        this.view = view;
    }

    public GameView getView() {
        return view;
    }
}