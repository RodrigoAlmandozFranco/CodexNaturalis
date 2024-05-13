package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.controller.gameDB.GameDB;
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
        if (this.game == null) {
            if (this.gameDB.fileExists())
                return ConnectionState.LOAD;
            return ConnectionState.CONNECT;
        }
        return ConnectionState.CREATE;
    }


    public int createGame(MessageListener l, String nickname, int numPlayers) throws NumberPlayerWrongException, GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {

        this.game = new Game(numPlayers);
        gameDB.setGame(this.game);

        this.addListener(l);

        this.game.addToGame(nickname);

        //TODO return gameid;
        return 0;
    }


    public boolean connect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        this.addListener(l);

        this.game.addToGame(nickname);

        if(listeners.size() == game.getNumberPlayers()) {
            game.changeState();
            Change change = gameDB.saveGame(false);
            updateAll(change);
        }

        return true;
    }


    public boolean reconnect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        if(listeners.isEmpty())
            this.game = this.gameDB.loadGame();
        this.addListener(l);
        this.game.addToGame(nickname);

        if(listeners.size() == game.getNumberPlayers()) {
            Change change = gameDB.afterLoad();
            updateAll(change);
        }

        return true;
    }


    public Set<Position> getAvailablePositions(String p) {
        //if (p.equals(game.getCurrentPlayer().getNickname()))
        return game.getCurrentPlayer().getBoard().getPossiblePositions();
    }


    public boolean place(String p, Face face, Position position) throws RequirementsNotMetException {
        game.getCurrentPlayer().checkRequirements(face);

        Change change;
        game.getCurrentPlayer().placeCard(position, face);
        game.changeState();
        if(game.getTurnFinal())
            game.setCurrentPlayer(game.getNextPlayer());
        change = gameDB.saveGame(true);
        updateAll(change);

        return true;
    }


    public void pick(String p, PlayableCard card) {
        game.chosenCardToAddInHand(card);
        game.changeState();
        game.setCurrentPlayer(game.getNextPlayer());
        Change change = gameDB.saveGame(true);
        updateAll(change);
    }

    public List<Color> placeStarting(String p, Face face){
        Change change;
        game.getCurrentPlayer().placeCard(new Position(0,0), face);
        game.changeState();
        change = gameDB.saveGame(true);
        updateAll(change);

        return game.getAvailableColors();
    }


    public List<GoalCard> chooseColor(String p, Color color) {
        game.getCurrentPlayer().setColor(color);
        game.removeColor(color);
        game.changeState();
        game.initializeHandCurrentPlayer();
        Change change = gameDB.saveGame(false);
        updateAll(change);
        return game.choosePersonalGoal();
    }



    public void chooseGoal(String p, GoalCard goal) {
        game.getCurrentPlayer().setPersonalGoal(goal);
        game.changeState();
        game.setCurrentPlayer(game.getNextPlayer());
        Change change = gameDB.saveGame(false);
        updateAll(change);
    }


    public List<Player> getWinner(){
        return game.getWinner();
    }

    @Override
    protected void handleDisconnection() {
        playerDisconnected();
    }

    public void playerDisconnected() {
        game.setCurrentState(State.DISCONNECTED);
        sendMessageAll(new PlayerDisconnectedMessage());
    }

    public void sendChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getReceiver().equals("all"))
            sendMessageAll(chatMessage);
        else
            sendMessage(chatMessage, chatMessage.getReceiver());
    }
}

