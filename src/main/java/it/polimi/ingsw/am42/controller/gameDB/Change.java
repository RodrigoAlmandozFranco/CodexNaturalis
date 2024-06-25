package it.polimi.ingsw.am42.controller.gameDB;

import it.polimi.ingsw.am42.model.GameInterface;
import it.polimi.ingsw.am42.model.cards.types.*;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the changes made by the current player.
 * In order to notify the changes, this class is sent to the client.
 * If the connection is based on TCP, this class is serialized and sent.
 * It has all the getters needed to get the changes.
 *
 * @author Rodrigo Almandoz Franco
 */

public class Change implements Serializable {
    private int pointsPlayer;
    private int numberGoalsAchieved;
    private String currentPlayer;
    private String futurePlayer;
    private List<Player> players;
    private List<GoalCard> globalGoals;
    private int numberPlayers;
    private List<PlayableCard> hand;
    private Face lastPlacedFace;
    private PlayableCard firstResourceCard;
    private PlayableCard firstGoldCard;
    private List<PlayableCard> pickableResourceCards;
    private List<PlayableCard> pickableGoldCards;
    private State currentState;
    private boolean gameStarted;
    private PlayersColor color;
    private boolean isTurnFinal;


    public Change(GameInterface game, boolean gameStarted){
        if(game.getCurrentState().equals(State.DISCONNECTED)){
            return;
        }
        pointsPlayer = game.getCurrentPlayer().getPoints();
        numberGoalsAchieved = game.getCurrentPlayer().getGoalsAchieved();
        currentPlayer = game.getCurrentPlayer().getNickname();
        futurePlayer = game.getCurrentPlayer().getNickname();
        hand = new ArrayList<>(game.getCurrentPlayer().getHand());

        if(game.getCurrentPlayer().getBoard().getFaces().size() == 1){
            lastPlacedFace = game.getCurrentPlayer().getBoard().getLastPlacedFace();
        } else {
            if (game.getCurrentPlayer().getBoard().getLastPlacedFace() != null) {
                if (game.getCurrentPlayer().getBoard().getLastPlacedFace() instanceof Front)
                    lastPlacedFace = new Front(game.getCurrentPlayer().getBoard().getLastPlacedFace().getSrcImage(),
                            game.getCurrentPlayer().getBoard().getLastPlacedFace().getCorners(),
                            game.getCurrentPlayer().getBoard().getLastPlacedFace().getColor(),
                            game.getCurrentPlayer().getBoard().getLastPlacedFace().getRequirements(),
                            game.getCurrentPlayer().getBoard().getLastPlacedFace().getEvaluator());
                else
                    lastPlacedFace = new Back(game.getCurrentPlayer().getBoard().getLastPlacedFace().getSrcImage(),
                            game.getCurrentPlayer().getBoard().getLastPlacedFace().getCorners(),
                            game.getCurrentPlayer().getBoard().getLastPlacedFace().getColor(),
                            game.getCurrentPlayer().getBoard().getLastPlacedFace().getListResource());

            }
        }
        if(lastPlacedFace !=  null){
            lastPlacedFace.setPosition(game.getCurrentPlayer().getBoard().getLastPlacedFace().getPosition());
            lastPlacedFace.setId(game.getCurrentPlayer().getBoard().getLastPlacedFace().getId());
        }





        firstResourceCard = new PlayableCard(game.getFirstResourceCard().getId(), game.getFirstResourceCard().getFront(), game.getFirstResourceCard().getBack());
        firstGoldCard = new PlayableCard(game.getFirstGoldCard().getId(), game.getFirstGoldCard().getFront(), game.getFirstGoldCard().getBack());

        pickableResourceCards = new ArrayList<>(game.getPickableResourceCards());

        pickableGoldCards = new ArrayList<>(game.getPickableGoldCards());

        currentState = game.getCurrentState();
        color = game.getCurrentPlayer().getColor();
        players = null;
        globalGoals = null;
        numberPlayers = 0;
        this.gameStarted = gameStarted;

        isTurnFinal = game.getTurnFinal();

        if(!gameStarted){
            players = new ArrayList<>(game.getPlayers());
            globalGoals = new ArrayList<>(game.getGoals());
            numberPlayers = game.getNumberPlayers();
        }
    }


    public void setFuturePlayer(String futurePlayer) {
        this.futurePlayer = futurePlayer;
    }

    public boolean isTurnFinal() {
        return isTurnFinal;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<GoalCard> getGlobalGoals() {
        return globalGoals;
    }

    public int getNumberPlayers() {
        return numberPlayers;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }


    public int getPointsPlayer() {
        return pointsPlayer;
    }

    public int getNumberGoalsAchieved() {
        return numberGoalsAchieved;
    }

    public String getFuturePlayer() {
        return futurePlayer;
    }

    public PlayersColor getColor() {
        return color;
    }

    public List<PlayableCard> getHand() {
        return hand;
    }

    public Face getLastPlacedFace() {
        return lastPlacedFace;
    }

    public PlayableCard getFirstResourceCard() {
        return firstResourceCard;
    }

    public PlayableCard getFirstGoldCard() {
        return firstGoldCard;
    }

    public List<PlayableCard> getPickableResourceCards() {
        return pickableResourceCards;
    }

    public List<PlayableCard> getPickableGoldCards() {
        return pickableGoldCards;
    }

    public State getCurrentState() {
        return currentState;
    }


    @Override
    public String toString() {
        return "Change{" +
                "pointsPlayer=" + pointsPlayer +
                ", numberGoalsAchieved=" + numberGoalsAchieved +
                ", currentPlayer=" + currentPlayer +
                ", futurePlayer='" + futurePlayer + '\'' +
                ", players=" + players +
                ", globalGoals=" + globalGoals +
                ", numberPlayers=" + numberPlayers +
                ", hand=" + hand +
                ", lastPlacedFace=" + lastPlacedFace +
                ", color=" + color +
                ", firstResourceCard=" + firstResourceCard +
                ", firstGoldCard=" + firstGoldCard +
                ", pickableResourceCards=" + pickableResourceCards +
                ", pickableGoldCards=" + pickableGoldCards +
                ", currentState=" + currentState +
                ", gameStarted=" + gameStarted +
                '}';
    }


}

