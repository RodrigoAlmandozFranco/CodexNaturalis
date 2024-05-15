package it.polimi.ingsw.am42.view.gameview;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.GoldCard;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class GameView {
    private List<PlayerView> players;
    private List<GoalCard> globalGoals;
    private State currentState;

    private List<PlayableCard> pickableResourceCards;
    private List<PlayableCard> pickableGoldCards;
    private int numberPlayers;
    protected PlayerView currentPlayer;
    protected PlayerView modifiedPlayer;

    private List<Runnable> usableMethods;

    public GameView() {
        this.players = new ArrayList<>();
        this.globalGoals = new ArrayList<>();
        this.currentState = null;

        this.pickableResourceCards = new ArrayList<>();
        this.pickableGoldCards = new ArrayList<>();
        this.numberPlayers = 0;
        this.currentPlayer = null;

    }


    public void handleState() {
        usableMethods.clear();

        switch (currentState) {
            case INITIAL -> {}
        }
    }

    public void updateMessage(ChatMessage chatMessage) {

    }

    public void connectionClosed() {
    }

    public void update(Change diff){
            System.out.println(diff);

            // First diff
            if (numberPlayers == 0) {
                for (Player p : diff.getPlayers())
                     players.add(new PlayerView(p));
                currentPlayer = players.getFirst();
                globalGoals = diff.getGlobalGoals();
                numberPlayers = diff.getNumberPlayers();
                }

            currentPlayer.setPoints(diff.getPointsPlayer());
            currentPlayer.setNumberGoalsAchieved(diff.getNumberGoalsAchieved());
            currentPlayer.setHand(diff.getHand());
            if (diff.getLastPlacedFace() != null)
                currentPlayer.getBoard().addFace(diff.getLastPlacedFace());

            pickableGoldCards.clear();
            pickableGoldCards.addAll(diff.getPickableGoldCards());
            pickableGoldCards.add(diff.getFirstGoldCard());

            pickableResourceCards.clear();
            pickableResourceCards.addAll(diff.getPickableResourceCards());
            pickableResourceCards.add(diff.getFirstResourceCard());

            currentState = diff.getCurrentState();

            modifiedPlayer = currentPlayer;
            currentPlayer = getPlayer(diff.getFuturePlayer());

            handleState();
    }


    @Override
    public String toString() {
        return "GameView{" +
                "players=" + players +
                ", globalGoals=" + globalGoals +
                ", pickableResourceCards=" + pickableResourceCards +
                ", pickableGoldCards=" + pickableGoldCards +
                ", numberPlayers=" + numberPlayers +
                '}';
    }

    private PlayerView getPlayer(String nickname) {
        for (PlayerView p : players)
            if (p.getNickname() == nickname)
                return p;
        return null;
    }

    public List<PlayableCard> getPickableResourceCards() {
        return pickableResourceCards;
    }

    public void setPickableResourceCards(List<PlayableCard> pickableResourceCards) {
        this.pickableResourceCards = pickableResourceCards;
    }

    public List<PlayableCard> getPickableGoldCards() {
        return pickableGoldCards;
    }

    public void setPickableGoldCards(List<PlayableCard> pickableGoldCards) {
        this.pickableGoldCards = pickableGoldCards;
    }

    public List<PlayerView> getPlayers() {
        return players;
    }

    private void setPlayers(List<PlayerView> players) {
        this.players = players;
    }

    public List<GoalCard> getGlobalGoals() {
        return globalGoals;
    }

    public void setGlobalGoals(List<GoalCard> globalGoals) {
        this.globalGoals = globalGoals;
    }

    public void setNumberPlayers(int numberPlayers) {
        this.numberPlayers = numberPlayers;
    }

    public int getNumberPlayers() {
        return numberPlayers;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public String getModifiedPlayer(){
        return modifiedPlayer.getNickname();
    }
}