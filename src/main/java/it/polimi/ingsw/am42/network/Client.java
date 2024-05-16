package it.polimi.ingsw.am42.network;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.view.gameview.GameView;

import java.util.List;
import java.util.Set;

public abstract class Client {

    protected GameView view;

    public abstract ConnectionState getGameInfo();

    public abstract int createGame(String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException;

    public abstract boolean connect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException;

    public abstract boolean reconnect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException;

    public abstract boolean connectAfterLoad(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException;

    public abstract Set<Position> getAvailablePositions(String p);

    public abstract boolean place(String p, Face face, Position pos) throws RequirementsNotMetException;

    public abstract List<GoalCard> chooseColor(String p, Color color);

    public abstract void chooseGoal(String p, GoalCard goal);

    public abstract void pick(String p, PlayableCard card);

    public abstract List<Player> getWinner();

    public abstract List<Color> placeStarting(String p, Face face);

    public abstract void update(Change diff);

    public abstract  void sendChatMessage(ChatMessage message);

    public void setView(GameView view) {
        this.view = view;
    }

    public GameView getView() {
        return view;
    }
}
