package it.polimi.ingsw.am42.controller.gameDB;

import it.polimi.ingsw.am42.model.GameInterface;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.io.Serializable;
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


    public Change(GameInterface game, boolean gameStarted){

        pointsPlayer = game.getCurrentPlayer().getPoints();
        numberGoalsAchieved = game.getCurrentPlayer().getGoalsAchieved();
        currentPlayer = game.getCurrentPlayer().getNickname();
        futurePlayer = game.getCurrentPlayer().getNickname();
        hand = game.getCurrentPlayer().getHand();
        lastPlacedFace = game.getCurrentPlayer().getBoard().getLastPlacedFace();
        firstResourceCard = game.getFirstResourceCard();
        firstGoldCard = game.getFirstGoldCard();
        pickableResourceCards = game.getPickableResourceCards();
        pickableGoldCards = game.getPickableGoldCards();
        currentState = game.getCurrentState();
        players = null;
        globalGoals = null;
        numberPlayers = 0;
        this.gameStarted = gameStarted;

        if(!gameStarted){
            players = game.getPlayers();
            globalGoals = game.getGoals();
            numberPlayers = game.getNumberPlayers();
        }
    }

    public Change() {
    }

    public void setFuturePlayer(String futurePlayer) {
        this.futurePlayer = futurePlayer;
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

    /*
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
                ", firstResourceCard=" + firstResourceCard +
                ", firstGoldCard=" + firstGoldCard +
                ", pickableResourceCards=" + pickableResourceCards +
                ", pickableGoldCards=" + pickableGoldCards +
                ", currentState=" + currentState +
                ", gameStarted=" + gameStarted +
                '}';
    }

     */
}

