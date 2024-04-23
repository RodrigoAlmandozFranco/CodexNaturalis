package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.structure.Position;

import java.util.List;

public abstract class Client {
    public String getGameInfo();

    public boolean createGame(MessageListener l, String nickname, int numPlayers);

    public boolean connect(MessageListener l, String nickname, int gameId);

    public boolean reconnect(MessageListener l, String nickname, int gameId);

    public List<Position> getAvailablePositions(String p);

    public boolean place(String p, Face face, Position pos);

    public List<Color> getAvailableColors(String p);

    public boolean chooseColor(String p, Color color);

    public List<GoalCard> getGoals(String p);

    public boolean chooseGoal(String p, GoalCard goal);

    public boolean pick(String p, PlayableCard card);

}
