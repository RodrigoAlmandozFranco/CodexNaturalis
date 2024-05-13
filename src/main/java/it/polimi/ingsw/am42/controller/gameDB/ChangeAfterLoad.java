package it.polimi.ingsw.am42.controller.gameDB;

import it.polimi.ingsw.am42.model.GameInterface;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.State;

import java.util.List;

public class ChangeAfterLoad extends Change {
    private List<Player> players;
    private int numberPlayers;
    private String currentPlayer;
    private String nextPlayer;
    private List<GoalCard> goals;
    private PlayableCard firstResourceCard;
    private PlayableCard firstGoldCard;
    private List<PlayableCard> pickableResourceCards;
    private List<PlayableCard> pickableGoldCards;
    private State currentState;


    public ChangeAfterLoad(GameInterface game) {
        players = game.getPlayers();
        numberPlayers = game.getNumberPlayers();
        currentPlayer = game.getCurrentPlayer().getNickname();
        nextPlayer = game.getNextPlayer().getNickname();
        goals = game.getGoals();
        firstResourceCard = game.getFirstResourceCard();
        firstGoldCard = game.getFirstGoldCard();
        pickableResourceCards = game.getPickableResourceCards();
        pickableGoldCards = game.getPickableGoldCards();
        currentState = game.getCurrentState();
    }

    @Override
    public int getNumberPlayers() {
        return numberPlayers;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getNextPlayer() {
        return nextPlayer;
    }

    public List<GoalCard> getGoals() {
        return goals;
    }

    @Override
    public PlayableCard getFirstResourceCard() {
        return firstResourceCard;
    }

    @Override
    public PlayableCard getFirstGoldCard() {
        return firstGoldCard;
    }

    @Override
    public List<PlayableCard> getPickableResourceCards() {
        return pickableResourceCards;
    }

    @Override
    public List<PlayableCard> getPickableGoldCards() {
        return pickableGoldCards;
    }

    @Override
    public State getCurrentState() {
        return currentState;
    }
}
