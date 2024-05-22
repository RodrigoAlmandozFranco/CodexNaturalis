package it.polimi.ingsw.am42.view.gameview;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.GoldCard;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameView {

    private String nickname;
    private List<PlayerView> players;
    private List<GoalCard> globalGoals;
    private State currentState;
    private boolean newUpdate = false;

    private List<PlayableCard> pickableResourceCards;
    private List<PlayableCard> pickableGoldCards;
    private int numberPlayers;
    protected PlayerView currentPlayer;
    private List<MethodChoice> usableMethods;
    private List<ChatMessage> allMessages;
    private List<ChatMessage> tmpMessages;

    private boolean startGame;


    public GameView() {
        this.players = new ArrayList<>();
        this.globalGoals = new ArrayList<>();
        this.currentState = null;

        this.pickableResourceCards = new ArrayList<>();
        this.pickableGoldCards = new ArrayList<>();
        this.numberPlayers = 0;
        this.currentPlayer = null;
        this.nickname = null;
        this.usableMethods = new ArrayList<>();
        usableMethods.add(MethodChoice.RELOAD);
        usableMethods.add(MethodChoice.DISCONNECT);
        usableMethods.add(MethodChoice.SEECHAT);
        usableMethods.add(MethodChoice.SENDMESSAGE);
        allMessages = new ArrayList<>();
        tmpMessages = new ArrayList<>();
        startGame = false;
    }

    public void handleState() {
        usableMethods.clear();

        usableMethods.add(MethodChoice.RELOAD);
        usableMethods.add(MethodChoice.DISCONNECT);
        usableMethods.add(MethodChoice.SEECHAT);
        usableMethods.add(MethodChoice.SENDMESSAGE);
        switch (currentState) {
            case INITIAL -> {}

            case SETHAND -> {
                usableMethods.add(MethodChoice.SEECARDS);
                usableMethods.add(MethodChoice.SEEBOARD);
                if (currentPlayer.getNickname().equals(nickname))
                    usableMethods.add(MethodChoice.PLACESTARTING);

            }
            case SETCOLOR -> {
                usableMethods.add(MethodChoice.SEECARDS);
                usableMethods.add(MethodChoice.SEEBOARD);
                if (currentPlayer.getNickname().equals(nickname))
                    usableMethods.add(MethodChoice.CHOOSECOLOR);
            }
            case SETGOAL -> {
                usableMethods.add(MethodChoice.SEECARDS);
                usableMethods.add(MethodChoice.SEEBOARD);
                if (currentPlayer.getNickname().equals(nickname))
                    usableMethods.add(MethodChoice.CHOOSEGOAL);
            }
            case PLACE -> {
                usableMethods.add(MethodChoice.SEESTANDINGS);
                usableMethods.add(MethodChoice.SEECARDS);
                usableMethods.add(MethodChoice.SEEBOARD);
                usableMethods.add(MethodChoice.SEEGOALS);
                if (currentPlayer.getNickname().equals(nickname))
                    usableMethods.add(MethodChoice.PLACE);

            }
            case PICK -> {
                usableMethods.add(MethodChoice.SEESTANDINGS);
                usableMethods.add(MethodChoice.SEECARDS);
                usableMethods.add(MethodChoice.SEEBOARD);
                usableMethods.add(MethodChoice.SEEGOALS);
                if (currentPlayer.getNickname().equals(nickname))
                    usableMethods.add(MethodChoice.PICK);
            }
            case LAST -> {
                usableMethods.add(MethodChoice.SEEBOARD);
                usableMethods.add(MethodChoice.SEESTANDINGS);
            }
            case DISCONNECTED -> {
            }
            default -> {}
        }
    }
    public String getNickname() {
        return nickname;
    }

    public void updateMessage(Message message) {
        if (message instanceof ChatMessage) {
            allMessages.add((ChatMessage) message);
            tmpMessages.add((ChatMessage) message);
        }
        else {
            System.out.println(message);
        }
    }

    public List<ChatMessage> getAllMessages() {
        return new ArrayList<>(allMessages);
    }

    public List<ChatMessage> getTmpMessages() {
        List<ChatMessage> tmp = new ArrayList<>(tmpMessages);
        tmpMessages.clear();
        return tmp;
    }
    

    public boolean getReady(){
        return startGame;
    }


    public void connectionClosed() {
    }

    public void update(Change diff){

        // First diff
        if (numberPlayers == 0) {
            for (Player p : diff.getPlayers())
                 players.add(new PlayerView(p));
            currentPlayer = getPlayer(diff.getCurrentPlayer());
            globalGoals = diff.getGlobalGoals();
            numberPlayers = diff.getNumberPlayers();
        }

        currentPlayer = getPlayer(diff.getCurrentPlayer());

        currentPlayer.setColor(diff.getColor());

        currentPlayer.update(diff);

        pickableGoldCards.clear();
        pickableGoldCards.addAll(diff.getPickableGoldCards());
        pickableGoldCards.add(diff.getFirstGoldCard());

        pickableResourceCards.clear();
        pickableResourceCards.addAll(diff.getPickableResourceCards());
        pickableResourceCards.add(diff.getFirstResourceCard());

        currentState = diff.getCurrentState();

        currentPlayer = getPlayer(diff.getFuturePlayer());

        startGame = true;
        newUpdate = true;
        
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

    public boolean getNewUpdate() {
        boolean tmp = newUpdate;
        newUpdate = false;
        return tmp;
    }

    public PlayerView getPlayer(String nickname) {
        for (PlayerView p : players)
            if (p.getNickname().equals(nickname))
                return p;
        return null;
    }

    public PlayerView getCurrentPlayer() {
        return currentPlayer;
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
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public List<MethodChoice> getUsableMethods() {
        return usableMethods;
    }
}