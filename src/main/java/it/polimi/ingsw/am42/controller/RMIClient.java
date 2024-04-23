package it.polimi.ingsw.am42.controller;


import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.structure.Position;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class RMIClient extends Client implements MessageListener{

    private View view;
    Registry registry;
    RMISpeaker stub;
    private RMIClient(String host) {
        //TODO da riga di comando ricevo se volgio un view GUI o TUI
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

    public boolean createGame(MessageListener l, String nickname, int numPlayers){
        return stub.createGame(MessageListener l, String nickname, int numPlayers);
    }

    public boolean connect(MessageListener l, String nickname, int gameId){
        return stub.connect(MessageListener l, String nickname, int gameId);
    }

    public boolean reconnect(MessageListener l, String nickname, int gameId){
        return stub.reconnect(MessageListener l, String nickname, int gameId);
    }

    public List<Position> getAvailablePositions(String p){
        return stub.List<Position> getAvailablePositions(String p);
    }

    public boolean place(String p, Face face, Position pos){
        return stub.place(String p, Face face, Position pos);
    }

    public List<Color> getAvailableColors(String p){
        return stub.getAvailableColors(String p);
    }

    public boolean chooseColor(String p, Color color){
        return stub.chooseColor(String p, Color color);
    }

    public List<GoalCard> getGoals(String p){
        return stub.getGoals(String p);
    }

    public boolean chooseGoal(String p, GoalCard goal){
        return stub.chooseGoal(String p, GoalCard goal);
    }

    public boolean pick(String p, PlayableCard card){
        return stub.pick(String p, PlayableCard card);
    }


}