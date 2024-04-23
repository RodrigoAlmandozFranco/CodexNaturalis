package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.gameDB.GameDB;
import it.polimi.ingsw.am42.controller.state.State;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.structure.Position;

import java.util.List;

public class Controller extends Observable implements RMISpeaker  {
    private final Game game;
    private State currentState;
    private GameDB gameDB;

    public Controller(Game game) {
        this.game = game;
    }

    @Override
    public String getGameInfo() {
        return null;
    }

    @Override
    public String createGame(MessageListener l, String nickname, int numPlayers) {
        return null;
    }

    @Override
    public boolean connect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        return false;
    }

    @Override
    public boolean reconnect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        return false;
    }

    @Override
    public List<Position> getAvailablePosition(String p) {
        return null;
    }

    @Override
    public boolean place(String p, Face face, Position position) throws NoRequirementsException {
        return false;
    }

    @Override
    public void pick(String p, PlayableCard card) {

    }

    @Override
    public void chooseColor(String p, Color color) {

    }

    @Override
    public List<GoalCard> getGoals(String p) {
        return null;
    }

    @Override
    public void chooseGoal(String p, GoalCard goal) {

    }
}

