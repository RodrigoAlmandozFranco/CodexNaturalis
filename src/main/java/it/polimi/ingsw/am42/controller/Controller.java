package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.controller.gameDB.GameDB;
import it.polimi.ingsw.am42.controller.state.State;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.rmi.RMISpeaker;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.util.List;
import java.util.Set;

public class Controller extends Observable implements RMISpeaker {
    private Game game;
    private State currentState;
    private final GameDB gameDB;

    public Controller() {
        this.gameDB = new GameDB();
        this.currentState = State.INITIAL;
    }

    @Override
    public String getGameInfo() {
        return null;
    }

    //TODO
    @Override
    public int createGame(MessageListener l, String nickname, int numPlayers) throws NumberPlayerWrongException, GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {

        this.game = new Game(numPlayers);
        gameDB.setGame(this.game);

        this.addListener(l);

        this.game.addToGame(nickname);

        this.currentState = this.currentState.changeState(this.game);

        //TODO return gameid;
        return 1;
    }

    @Override
    public boolean connect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        this.addListener(l);

        this.game.addToGame(nickname);

        this.currentState = this.currentState.changeState(this.game);

        return true;
    }

    @Override
    public boolean reconnect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        this.game = this.gameDB.loadGame();
        this.addListener(l);

        this.game.addToGame(nickname);

        this.currentState = this.currentState.changeState(this.game);

        return true;
    }

    @Override
    public Set<Position> getAvailablePositions(String p) {
        if (p.equals(game.getCurrentPlayer().getNickname()))
            return game.getCurrentPlayer().getBoard().getPossiblePositions();
        return null;
    }

    @Override
    public boolean place(String p, Face face, Position position) throws RequirementsNotMetException {
        game.getCurrentPlayer().checkRequirements(face);

        Message change;
        game.getCurrentPlayer().placeCard(position, face);
        this.currentState = this.currentState.changeState(this.game);
        if(game.getTurnFinal())
            game.setCurrentPlayer(game.getNextPlayer());
        change = gameDB.saveGame(true, this.currentState);
        updateAll(change);

        return true;
    }

    @Override
    public void pick(String p, PlayableCard card) {
        game.chosenCardToAddInHand(card);
        this.currentState = this.currentState.changeState(this.game);
        game.setCurrentPlayer(game.getNextPlayer());
        Message change = gameDB.saveGame(true, this.currentState);
        updateAll(change);
    }

    @Override
    public void chooseColor(String p, Color color) {
        game.getCurrentPlayer().setColor(color);
        game.removeColor(color);
        this.currentState = this.currentState.changeState(this.game);
        Message change = gameDB.saveGame(false, this.currentState);
        updateAll(change);
    }

    @Override
    public List<GoalCard> getGoals(String p) {
        return game.choosePersonalGoal();
    }

    @Override
    public void chooseGoal(String p, GoalCard goal) {
        game.getCurrentPlayer().setPersonalGoal(goal);
        this.currentState = this.currentState.changeState(game);
        game.setCurrentPlayer(game.getNextPlayer());
        Message change = gameDB.saveGame(false, this.currentState);
        updateAll(change);
    }

    @Override
    public List<Color> getAvailableColors(String p) {
        return game.getAvailableColors();
    }

    public List<Player> getWinner(){
        return game.getWinner();
    }
}

