package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.structure.Position;

import java.util.List;
import java.util.Set;

public abstract class Client {
    public abstract String getGameInfo();

    public abstract String createGame(MessageListener l, String nickname, int numPlayers);

    public abstract boolean connect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException;

    public abstract boolean reconnect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException;

    public abstract Set<Position> getAvailablePositions(String p);

    public abstract boolean place(String p, Face face, Position pos);

    public abstract List<Color> getAvailableColors(String p);

    public abstract void chooseColor(String p, Color color);

    public abstract List<GoalCard> getGoals(String p);

    public abstract void chooseGoal(String p, GoalCard goal);

    public abstract void pick(String p, PlayableCard card);

    public abstract List<Player> getWinner();

}
