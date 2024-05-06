package it.polimi.ingsw.am42.controller.gameDB;

import it.polimi.ingsw.am42.controller.state.State;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
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

public class Change extends Message implements Serializable {
    private int pointsPlayer;
    private int numberGoalsAchieved;
    private String futurePlayer;
    private List<PlayableCard> hand;
    private Face lastPlacedFace;
    private PlayableCard firstResourceCard;
    private PlayableCard firstGoldCard;
    private List<PlayableCard> pickableResourceCards;
    private List<PlayableCard> pickableGoldCards;
    private State currentState;


    public Change(Game game, State state){

        pointsPlayer = game.getCurrentPlayer().getPoints();
        numberGoalsAchieved = game.getCurrentPlayer().getGoalsAchieved();
        futurePlayer = game.getNextPlayer().getNickname();
        hand = game.getCurrentPlayer().getHand();
        lastPlacedFace = game.getCurrentPlayer().getBoard().getLastPlacedFace();
        firstResourceCard = game.getFirstResourceCard();
        firstGoldCard = game.getFirstGoldCard();
        pickableResourceCards = game.getPickableResourceCards();
        pickableGoldCards = game.getPickableGoldCards();
        currentState = state;
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
}

