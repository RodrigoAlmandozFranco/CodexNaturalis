package it.polimi.ingsw.am42.network.rmi;


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

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Set;

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

    public String getGameInfo(){return stub.getGameInfo();}

    public int createGame(MessageListener l, String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException {
        return stub.createGame(l,  nickname,  numPlayers);
    }

    public boolean connect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        return stub.connect(l, nickname, gameId);
    }

    public boolean reconnect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        return stub.reconnect( l, nickname, gameId);
    }

    public Set<Position> getAvailablePositions(String p){
        return stub.getAvailablePositions(p);
    }

    public boolean place(String p, Face face, Position pos) throws RequirementsNotMetException {
        return stub.place( p, face, pos);
    }

    public List<Color> getAvailableColors(String p){
        return stub.getAvailableColors( p);
    }

    public void chooseColor(String p, Color color){
        stub.chooseColor(p, color);
    }

    public List<GoalCard> getGoals(String p){
        return stub.getGoals( p);
    }

    public void chooseGoal(String p, GoalCard goal){stub.chooseGoal( p, goal);
    }

    public void pick(String p, PlayableCard card){
         stub.pick(p, card);
    }


    @Override
    public String getId() {
        return null;
    }

    public List<Player> getWinner() {
        return stub.getWinner();
    }

    @Override
    public void update(Message message) {
        view.update(message);
    }
}