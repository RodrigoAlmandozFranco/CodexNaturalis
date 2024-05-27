package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.controller.gameDB.GameDB;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.GameInterface;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.rmi.RMISpeaker;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.PlayerDisconnectedMessage;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * This class acts as the controller of the model
 * It transforms the requests made by the client into actions on the game
 * Extends from the Observable class for the observer design pattern
 * @see Observable
 *
 * @author Tommaso Crippa
 * @author Alessandro Di Maria
 */
public class Controller extends Observable{
    private GameInterface game;
    private final GameDB gameDB;

    public Controller() {
        super();
        this.gameDB = new GameDB();
        this.game = null;
    }


    public ConnectionState getGameInfo() {

        System.out.println("sending game info");


        if (this.game == null) {
            if (this.gameDB.fileExists())
                return ConnectionState.LOAD;
            return ConnectionState.CREATE;
        }
        if(this.gameDB.fileExists()) return ConnectionState.LOADING;
        return ConnectionState.CONNECT;
    }


    public int createGame(MessageListener l, String nickname, int numPlayers) throws NumberPlayerWrongException, GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {

        gameDB.fileDelete();

        this.game = new Game(numPlayers);
        gameDB.setGame(this.game);

        this.game.addToGame(nickname);
        this.addListener(l);

        System.out.println(nickname + " created new game");

        //TODO return gameid;
        return 0;
    }


    public boolean connect(MessageListener l, String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        this.game.addToGame(nickname);
        this.addListener(l);

        System.out.println(nickname + " connected to the game");

        if(listeners.size() == game.getNumberPlayers()) {
            game.changeState();

            Change change = gameDB.saveGame(false);
            updateAll(change);
            System.out.println("Everybody joined -> starting game");
        }


        return true;
    }


    public boolean reconnect(MessageListener l, String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        boolean wasInPrevGame = false;

        if(listeners.isEmpty()) {
            this.game = this.gameDB.loadGame();
            System.out.println(nickname + " loaded up new game");
        }
        for (MessageListener lis : listeners) {
            try {
                if (lis.getId().equals(nickname))
                    throw new NicknameAlreadyInUseException(nickname + "is already connected");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        for (Player p : game.getPlayers())
            if (p.getNickname().equals(nickname)) {
                wasInPrevGame = true;
                break;
            }

        if(!wasInPrevGame)
            throw new NicknameInvalidException(nickname + " was not in the previous game");

        this.addListener(l);

        if(listeners.size() == game.getNumberPlayers()) {
            Change change = gameDB.afterLoad();
            updateAll(change);
        }

        System.out.println(nickname + " reconncted");

        return true;
    }

    public Set<Position> getAvailablePositions(String p) {
        //if (p.equals(game.getCurrentPlayer().getNickname()))

        System.out.println(p + " requested available positions");
        return game.getCurrentPlayer().getBoard().getPossiblePositions();
    }


    public boolean place(String p, Face face, Position position) throws RequirementsNotMetException {
        game.getCurrentPlayer().checkRequirements(face);

        Change change;
        game.getCurrentPlayer().placeCard(position, face);
        game.changeState();
        change = gameDB.saveGame(true);
        if(game.getTurnFinal())
            game.setCurrentPlayer(game.getNextPlayer());
        change.setFuturePlayer(game.getCurrentPlayer().getNickname());
        updateAll(change);

        gameDB.saveGame(true);
        System.out.println(p + " placed a card in position " + position);
        return true;
    }


    public void pick(String p, PlayableCard card) {
        game.chosenCardToAddInHand(card);
        game.changeState();
        Change change = gameDB.saveGame(true);
        game.setCurrentPlayer(game.getNextPlayer());
        change.setFuturePlayer(game.getCurrentPlayer().getNickname());
        updateAll(change);

        gameDB.saveGame(true);
        System.out.println(p + "picked a card");
    }

    public List<PlayersColor> placeStarting(String p, Face face){
        Change change;
        game.getCurrentPlayer().placeCard(new Position(0,0), face);
        game.changeState();
        change = gameDB.saveGame(true);
        updateAll(change);

        System.out.println(p + " placed starting card");
        return game.getAvailableColors();
    }


    public List<GoalCard> chooseColor(String p, PlayersColor color) {
        game.getCurrentPlayer().setColor(color);
        game.removeColor(color);
        game.changeState();
        //game.initializeHandCurrentPlayer();
        Change change = gameDB.saveGame(false);
        updateAll(change);
        System.out.println(p + " chose color" + color);

        return game.choosePersonalGoal();
    }



    public void chooseGoal(String p, GoalCard goal) {
        game.getCurrentPlayer().setPersonalGoal(goal);
        game.changeState();
        Change change = gameDB.saveGame(false);
        game.setCurrentPlayer(game.getNextPlayer());
        change.setFuturePlayer(game.getCurrentPlayer().getNickname());
        updateAll(change);

        gameDB.saveGame(true);
        System.out.println(p + " chose his personal goal");
    }


    public List<Player> getWinner(){
        System.out.println("sending final results");
        return game.getWinner();
    }

    @Override
    protected void handleDisconnection() {
        playerDisconnected();
    }

    public void playerDisconnected() {
        System.out.println("someone disconnected to the game");
        game.setCurrentState(State.DISCONNECTED);
        updateAll(gameDB.saveGame(true));
        gameDB.fileDelete();
        System.exit(0);
    }

    public void sendChatMessage(ChatMessage chatMessage) {
        System.out.println("delivering chat message");
        if (chatMessage.getReceiver().equals("all"))
            sendMessageAll(chatMessage);
        else {
            sendMessage(chatMessage, chatMessage.getReceiver());
            sendMessage(chatMessage, chatMessage.getSender());
        }
    }
}

