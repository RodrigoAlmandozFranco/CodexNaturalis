package it.polimi.ingsw.am42.view.clientModel;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to store the simplified game model in the client
 * The information are sent one by one with the Change messages from the server
 */
public class GameClientModel {

    private String nickname;
    private List<PlayerClientModel> players;
    private List<GoalCard> globalGoals;
    private State currentState;
    private boolean newUpdate = false;

    private List<PlayableCard> pickableResourceCards;
    private List<PlayableCard> pickableGoldCards;
    private int numberPlayers;
    protected PlayerClientModel currentPlayer;
    private List<MethodChoice> usableMethods;
    private List<ChatMessage> allMessages;
    private List<ChatMessage> tmpMessages;
    private boolean isGameAborted = false;
    private boolean startGame;
    private boolean isTurnFinal;
    private boolean isServerDown = false;

    /**
     * Constructor of the class
     */
    public GameClientModel() {
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

    /**
     * Method to decide which methods can be used by the (thick) client in the current state
     */
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
                usableMethods.add(MethodChoice.SEEPICKABLE);
                if (currentPlayer.getNickname().equals(nickname))
                    usableMethods.add(MethodChoice.PLACE);

            }
            case PICK -> {
                usableMethods.add(MethodChoice.SEESTANDINGS);
                usableMethods.add(MethodChoice.SEECARDS);
                usableMethods.add(MethodChoice.SEEBOARD);
                usableMethods.add(MethodChoice.SEEGOALS);
                usableMethods.add(MethodChoice.SEEPICKABLE);
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

    /**
     * Adds the chat messages to the pile
     * @param message the last message received
     */
    public void updateMessage(Message message) {
        if (message instanceof ChatMessage) {
            allMessages.add((ChatMessage) message);
            tmpMessages.add((ChatMessage) message);
        }
        else {
            System.out.println(message);
        }
    }

    /**
     * Method used by app to check if the server is down
     * @return
     */
    public boolean getServerDown(){
        return isServerDown;
    }

    public void setServerDown(boolean serverDown){
        isServerDown = serverDown;
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

    /**
     * Main method to update the contents of the game
     * @param diff the Change message received by the server
     */
    public void update(Change diff){

        if(diff.getCurrentState().equals(State.DISCONNECTED)){
            isGameAborted = true;
            return;
        }


        // First diff
        if (numberPlayers == 0) {
            for (Player p : diff.getPlayers())
                 players.add(new PlayerClientModel(p));
            currentPlayer = getPlayer(diff.getCurrentPlayer());
            globalGoals = diff.getGlobalGoals();
            numberPlayers = diff.getNumberPlayers();
        }

        currentPlayer = getPlayer(diff.getCurrentPlayer());

        currentPlayer.setColor(diff.getColor());

        currentPlayer.update(diff);

        isTurnFinal = diff.isTurnFinal();

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

    public boolean isGameAborted(){
        return isGameAborted;
    }

    public void setGameAborted(boolean gameAborted){
        isGameAborted = gameAborted;
    }

    public boolean isTurnFinal() {
        return isTurnFinal;
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

    public PlayerClientModel getPlayer(String nickname) {
        for (PlayerClientModel p : players)
            if (p.getNickname().equals(nickname))
                return p;
        return null;
    }

    public PlayerClientModel getCurrentPlayer() {
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

    public List<PlayerClientModel> getPlayers() {
        return players;
    }

    private void setPlayers(List<PlayerClientModel> players) {
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