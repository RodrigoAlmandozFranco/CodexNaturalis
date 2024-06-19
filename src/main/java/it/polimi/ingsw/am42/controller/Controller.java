package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.controller.gameDB.GameDB;
import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
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


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    /**
     * Method to request which kind of way to connect to the game
     *
     * @return ConnectionState
     */
    public synchronized ConnectionState getGameInfo() {

        System.out.println("sending game info");


        if (this.game == null) {
            if (this.gameDB.fileExists())
                return ConnectionState.LOAD;
            return ConnectionState.CREATE;
        }
        if(this.gameDB.fileExists())
            return ConnectionState.LOADING;
        return ConnectionState.CONNECT;
    }

    /**
     * Method to create new game
     *
     * @param l Reference to the calling object, used to receive notification for observer pattern
     * @param nickname Name of the player
     * @param numPlayers Number of players that are going to play the game
     * @return 0 if game successfully created, otherwise an exception is raised
     *
     * @throws NumberPlayerWrongException if the numPlayers provided is not in the possible interval
     * @throws GameFullException if the game has already all players connected
     * @throws NicknameInvalidException if the nickname doesn't have a valid format
     * @throws NicknameAlreadyInUseException if the nickname has already been chosen by another player
     */
    public synchronized int createGame(MessageListener l, String nickname, int numPlayers) throws NumberPlayerWrongException, GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, GameAlreadyCreatedException {

        if(game != null) throw new GameAlreadyCreatedException("Game has already started");


        gameDB.fileDelete();

        this.game = new Game(numPlayers);
        gameDB.setGame(this.game);

        this.game.addToGame(nickname);
        this.addListener(l);

        System.out.println(nickname + " created new game");

        //TODO return gameid;
        return 0;
    }

    /**
     *
     * Standard way of connecting to newly created game
     *
     * @param l Reference to the calling object, used to receive notification for observer pattern
     * @param nickname Name of the player
     * @return true if the connection works without problems, otherwise it throws an exception
     *
     * @throws GameFullException if the game has already all players connected
     * @throws NicknameInvalidException if the nickname doesn't have a valid format
     * @throws NicknameAlreadyInUseException if the nickname has already been chosen by another player
     */
    public synchronized boolean connect(MessageListener l, String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
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

    /**
     *
     * Standard way of connecting to saved game AND loading up saved game
     *
     * @param l Reference to the calling object, used to receive notification for observer pattern
     * @param nickname Name of the player
     * @return true if the connection works without problems, otherwise it throws an exception
     *
     * @throws GameFullException if the game has already all players connected
     * @throws NicknameInvalidException if the nickname doesn't have a valid format
     * @throws NicknameAlreadyInUseException if the nickname has already been chosen by another player
     */
    public synchronized boolean reconnect(MessageListener l, String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, GameAlreadyCreatedException {
        boolean wasInPrevGame = false;

        if(!gameDB.fileExists()) throw new GameAlreadyCreatedException("Game has already started");

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
            throw new NicknameInvalidException(game.getPlayer(nickname) + " was not in the previous game");

        this.addListener(l);

        if(listeners.size() == game.getNumberPlayers()) {
            Change change = gameDB.afterLoad();
            updateAll(change);
        }

        System.out.println(nickname + " reconnected");

        return true;
    }

    /**
     * Method to get the available positions to place of the current player
     * @param p Nickname of method caller
     *
     * @return set with available positions
     */
    public Set<Position> getAvailablePositions(String p) throws WrongTurnException {
        if(!p.equals(game.getCurrentPlayer().getNickname())
                || !game.getCurrentState().equals(State.PLACE)) {
            Change change = gameDB.saveGame(true);
            updateAll(change);
            throw new WrongTurnException("You can't do this action now");
        }
        System.out.println(game.getPlayer(p) + " requested available positions");
        return game.getCurrentPlayer().getBoard().getPossiblePositions();
    }

    /**
     * Method called by the current player to place a specific face on a specific position
     *
     * @param p Nickname of method caller
     * @param face Selected Face
     * @param position Selected Position
     * @return true if placed successfully, otherwise throws an exception
     *
     * @throws RequirementsNotMetException if the player's board doesn't fulfill the face requirements
     */
    public boolean place(String p, Face face, Position position) throws RequirementsNotMetException, WrongTurnException {
        if(!p.equals(game.getCurrentPlayer().getNickname())
                || !game.getCurrentState().equals(State.PLACE)) {
            Change change = gameDB.saveGame(true);
            updateAll(change);
            throw new WrongTurnException("You can't do this action now");
        }

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
        System.out.println(game.getPlayer(p) + " placed a card in position " + position);
        return true;
    }

    /**
     * Method called by the current player to pick one of the pickable cards from the deck
     *
     * @param p Nickname of method caller
     * @param card Selected Card to add to hand
     */
    public void pick(String p, PlayableCard card) throws WrongTurnException {
        if(!p.equals(game.getCurrentPlayer().getNickname())
                || !game.getCurrentState().equals(State.PICK)) {
            Change change = gameDB.saveGame(true);
            updateAll(change);
            throw new WrongTurnException("You can't do this action now");
        }

        game.chosenCardToAddInHand(card);
        game.changeState();
        Change change = gameDB.saveGame(true);
        game.setCurrentPlayer(game.getNextPlayer());
        change.setFuturePlayer(game.getCurrentPlayer().getNickname());
        updateAll(change);

        gameDB.saveGame(true);
        System.out.println(game.getPlayer(p) + " picked a card");
    }

    /**
     * Method called by the current player to place THE FIRST face (of the starting card) on the board
     * @param p Nickname of method caller
     * @param face Selected face
     * @return The list of available colors, the player now has to choose which one to use
     */
    public List<PlayersColor> placeStarting(String p, Face face) throws WrongTurnException {
        if(!p.equals(game.getCurrentPlayer().getNickname())
        || !game.getCurrentState().equals(State.SETHAND)) {
            Change change = gameDB.saveGame(true);
            updateAll(change);
            throw new WrongTurnException("You can't do this action now");
        }
        Change change;
        game.getCurrentPlayer().placeCard(new Position(0,0), face);
        game.changeState();
        change = gameDB.saveGame(true);
        updateAll(change);

        System.out.println(game.getPlayer(p)  + " placed starting card");
        return game.getAvailableColors();
    }

    /**
     * Method called by the current player to select which color to identify itself
     * @param p Nickname of method caller
     * @param color the Selected Color
     * @return The list of objectives, the player now has to select which one it should choose
     */
    public List<GoalCard> chooseColor(String p, PlayersColor color) throws WrongTurnException {
        if(!p.equals(game.getCurrentPlayer().getNickname())
            || !game.getCurrentState().equals(State.SETCOLOR)) {
            Change change = gameDB.saveGame(true);
            updateAll(change);
            throw new WrongTurnException("You can't do this action now");
        }

        game.getCurrentPlayer().setColor(color);
        game.removeColor(color);
        game.changeState();
        //game.initializeHandCurrentPlayer();
        Change change = gameDB.saveGame(false);
        updateAll(change);
        System.out.println(game.getPlayer(p) + " chose color" + color);

        return game.choosePersonalGoal();
    }


    /**
     * Method called by the current player to select which objective to try and fulfill during the game
     * @param p Nickname of method caller
     * @param goal Selected Goal
     */
    public void chooseGoal(String p, GoalCard goal) throws WrongTurnException {
        if(!p.equals(game.getCurrentPlayer().getNickname())
        || !game.getCurrentState().equals(State.SETGOAL)){
            Change change = gameDB.saveGame(true);
            updateAll(change);
            throw new WrongTurnException("You can't do this action now");
        }
        game.getCurrentPlayer().setPersonalGoal(goal);
        game.changeState();
        Change change = gameDB.saveGame(false);
        game.setCurrentPlayer(game.getNextPlayer());
        change.setFuturePlayer(game.getCurrentPlayer().getNickname());
        updateAll(change);

        gameDB.saveGame(true);
        System.out.println(game.getPlayer(p) + " chose his personal goal");
    }

    /**
     * Returns the list of winners (more than one if there is a tie)
     * @return list of winners
     */
    public List<Player> getWinner() throws WrongTurnException{
        if (!game.getCurrentState().equals(State.LAST)) {
            Change change = gameDB.saveGame(true);
            updateAll(change);
            throw new WrongTurnException("You can't do this action now");
        }
        System.out.println("sending final results");
        return game.getWinner();
    }

    /**
     * Method called when a disconnection is detected
     */
    @Override
    protected void handleDisconnection() {
        playerDisconnected();
    }

    /**
     * Method called by the players to disconnect
     */
    public void playerDisconnected() {
        System.out.println("someone disconnected to the game");
        game.setCurrentState(State.DISCONNECTED);
        updateAll(gameDB.saveGame(true));
        gameDB.fileDelete();
        System.exit(0);
    }

    /**
     * Method to send messages to other players
     * @param chatMessage the message to send
     */
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

