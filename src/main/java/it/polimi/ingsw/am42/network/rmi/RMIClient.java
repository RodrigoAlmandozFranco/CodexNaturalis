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
    private GameClientModel view;
    private String nickname;
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

    public ConnectionState getGameInfo(){
        try {
            return stub.getGameInfo();
        } catch (RemoteException e) {
            serverDown();
            return null;
        }

    }

    public int createGame(String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException, GameAlreadyCreatedException {
        try {
            int gameID = stub.createGame(this, nickname, numPlayers);
            this.nickname = nickname;
            return gameID;
        } catch (GameFullException | NicknameInvalidException | NicknameAlreadyInUseException | NumberPlayerWrongException | GameAlreadyCreatedException e) {
            throw e;
        }
        catch (RemoteException e) {
            serverDown();
            return -1;
        }
    }

    public boolean connect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        try {
            this.nickname = nickname;
            return stub.connect(this, nickname);
        } catch (GameFullException | NicknameInvalidException | NicknameAlreadyInUseException e) {
            throw e;
        }
        catch (RemoteException e) {
            serverDown();
            return false;
        }
    }

    public boolean reconnect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, GameAlreadyCreatedException {
        try {
            this.nickname = nickname;
            return stub.reconnect(this, nickname);
        } catch (GameFullException | NicknameAlreadyInUseException | NicknameInvalidException | GameAlreadyCreatedException e) {
            throw e;
        } catch (RemoteException e) {
            serverDown();
            return false;
        }
    }

    public Set<Position> getAvailablePositions(String p) throws WrongTurnException {
        try {
            return stub.getAvailablePositions(p);
        } catch (WrongTurnException e) {
            throw e;
        }
        catch (RemoteException e) {
            serverDown();
            return new TreeSet<Position>();
        }
    }

    public boolean place(String p, Face face, Position pos) throws RequirementsNotMetException, WrongTurnException {
        try {
            return stub.place( p, face, pos);
        } catch (WrongTurnException | RequirementsNotMetException e) {
            throw e;
        }
        catch (RemoteException e) {

            serverDown();
            return false;
        }
    }

    public List<PlayersColor> placeStarting(String p, Face face) throws WrongTurnException {
        try {
            return stub.placeStarting(p, face);
        } catch (WrongTurnException e) {
            throw e;
        }
        catch (RemoteException e) {
            serverDown();
            return null;
        }
    }

    public List<GoalCard> chooseColor(String p, PlayersColor color) throws WrongTurnException {
        try {
            return stub.chooseColor(p, color);
        } catch (WrongTurnException e) {
            throw e;
        }
        catch (RemoteException e) {
            serverDown();
            return null;
        }
    }

    public void chooseGoal(String p, GoalCard goal) throws WrongTurnException{
        try {
            stub.chooseGoal( p, goal);
        } catch (WrongTurnException e) {
            throw e;
        } catch (RemoteException e) {
            serverDown();
        }
    }

    public void pick(String p, PlayableCard card) throws WrongTurnException {
        try {
            stub.pick(p, card);
        } catch (WrongTurnException e) {
            throw e;
        }catch (RemoteException e) {
            serverDown();
        }
    }

    @Override
    public String getId() {
        return this.nickname;
    }

    public List<Player> getWinner() throws WrongTurnException {
        try {
            return stub.getWinner();
        } catch (WrongTurnException e) {
            throw e;
        }
        catch (RemoteException e) {
            serverDown();
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
            serverDown();
        }
    }

    @Override
    public void receiveMessage(Message message) {
        view.updateMessage(message);
    }

    public boolean heartbeat() throws RemoteException {
        return true;
    }

    public void checkServerStatus() {
        while (true) {
            try {
                stub.getStatus();
                Thread.sleep(10000);
            } catch (InterruptedException | RemoteException e) {
                serverDown();
                break;
            }
        }
    }

    public void serverDown() {
        view.setServerDown(true);
    }

    public void updateDisconnection(){
         view.setGameAborted(true);
    }


    public void setView(GameClientModel view) {
        this.view = view;
    }

    @Override
    public void playerDisconnected() {
        try {
            stub.playerDisconnected();
        } catch (RemoteException e) {
            serverDown();
        }
        updateDisconnection();
    }

    public GameClientModel getView() {
        return view;
    }
}