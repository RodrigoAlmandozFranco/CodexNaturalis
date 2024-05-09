package it.polimi.ingsw.am42.network.rmi;


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
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.view.View;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Set;


/**
 * This class acts as the client when using a RMI connection
 *
 * @author Alessandro Di Maria
 */
public class RMIClient extends Client implements MessageListener {
    Registry registry;
    RMISpeaker stub;
    private RMIClient(String host) {
        //TODO da riga di comando ricevo se voglio un view GUI o TUI
        //this.view = new View(this);
        try {
            this.registry = LocateRegistry.getRegistry(host);
            this.stub = (RMISpeaker) registry.lookup("Controller");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    //TODO metodo che il server chiama sul RMIClient
    //public void update(diff) {view.update(diff);}

    public String getGameInfo(){
        try {
            return stub.getGameInfo();
        } catch (RemoteException e) {
            //TODO notify the view that the server is dead
            return null;
        }

    }

    public int createGame(MessageListener l, String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException {
        try {
            return stub.createGame(l, nickname, numPlayers);
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

    public boolean connect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        try {
            return stub.connect(l, nickname, gameId);
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

    public boolean reconnect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        try {
            return stub.reconnect(l, nickname, gameId);
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
        // TODO give nickname to RMIClient
        //return nickname;
        return null;
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
    public void update(Change diff) {
        view.update(diff);
    }

    @Override
    public void receiveMessage(Message message) {
        // TODO: implement receive Message
        // view.receiveMessage(message)
    }

    public boolean heartbeat() {
        return true;
    }
}