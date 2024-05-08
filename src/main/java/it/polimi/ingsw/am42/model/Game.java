package it.polimi.ingsw.am42.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.gson.BackDeserializer;
import it.polimi.ingsw.am42.gson.CornerDeserializer;
import it.polimi.ingsw.am42.gson.EvaluatorDeserializer;
import it.polimi.ingsw.am42.gson.FrontDeserializer;
import it.polimi.ingsw.am42.gson.GoalCardDeserializer;
import it.polimi.ingsw.am42.gson.PlayableCardDeserializer;
import it.polimi.ingsw.am42.model.cards.Card;
import it.polimi.ingsw.am42.model.cards.types.*;
import it.polimi.ingsw.am42.model.cards.types.playables.ResourceCard;
import it.polimi.ingsw.am42.model.decks.GoalDeck;
import it.polimi.ingsw.am42.model.decks.PlayableDeck;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException;
import it.polimi.ingsw.am42.model.structure.Board;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class represents a Game
 * It implements GameInterface
 * @see GameInterface
 * @see Player
 * @see Card
 * @see GoalCard
 * @see Board
 * @author Mattia Brandi
 * @author Rodrigo Almandoz Franco
*/

public class Game implements GameInterface, Serializable {

    private List<Player> players;
    private final List<GoalCard> globalGoals;
    private PlayableDeck resourceDeck;
    private PlayableDeck goldDeck;
    private PlayableDeck startingDeck;
    private GoalDeck goalDeck;
    private List<PlayableCard> pickableResourceCards;
    private List<PlayableCard> pickableGoldCards;
    private Player currentPlayer;
    private final int numberPlayers;
    private boolean turnFinal;
    private List<Color> availableColors;
    private State currentState;



    /**
     * This constructor creates a new Game
     *
     * @param numberPlayers the number of players that will play the game
     * @throws NumberPlayerWrongException if the number of players is not between 2 and 4
     */
    public Game(int numberPlayers) throws NumberPlayerWrongException {
        if (numberPlayers < 2 || numberPlayers > 4) {
            throw new NumberPlayerWrongException("Number of players must be between 2 and 4");
        } else {
            this.numberPlayers = numberPlayers;
            players = new ArrayList<>();
            resourceDeck = new PlayableDeck();
            goldDeck = new PlayableDeck();
            startingDeck = new PlayableDeck();
            goalDeck = new GoalDeck();
            pickableResourceCards = new ArrayList<>();
            pickableGoldCards = new ArrayList<>();
            globalGoals = new ArrayList<>();
            currentPlayer = null;
            turnFinal = false;
            availableColors = new ArrayList<Color>();
            availableColors.addAll(Arrays.asList(Color.values()));
            this.currentState = State.INITIAL;
        }
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void changeState(){
        this.currentState = this.currentState.changeState(this);
    }

    /**
     * Constructor of the class Player for the reconstruction
     * of the game after network disconnections.
     * @param numberPlayers
     * @param players
     * @param globalGoals
     * @param resourceDeck
     * @param goldDeck
     * @param startingDeck
     * @param goalDeck
     * @param pickableResourceCards
     * @param pickableGoldCards
     * @param currentPlayer
     */


    public Game(int numberPlayers, List<Player> players, List<GoalCard> globalGoals, PlayableDeck resourceDeck,
                PlayableDeck goldDeck, PlayableDeck startingDeck, GoalDeck goalDeck,
                List<PlayableCard> pickableResourceCards, List<PlayableCard> pickableGoldCards, Player currentPlayer) {

        this.players = players;
        this.globalGoals = globalGoals;
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.startingDeck = startingDeck;
        this.goalDeck = goalDeck;
        this.pickableResourceCards = pickableResourceCards;
        this.pickableGoldCards = pickableGoldCards;
        this.currentPlayer = currentPlayer;
        this.numberPlayers = numberPlayers;
        this.availableColors = null;
    }

    /**
     * This method initializes the game.
     * It creates the decks and initializes the pickable cards.
     */
    @Override
    public void initializeGame() {
        initializeDecks();

        for(int i = 0; i < 2; i++) {
            PlayableCard r = resourceDeck.pop();
            PlayableCard g = goldDeck.pop();

            r.setVisibility(true);
            g.setVisibility(true);

            pickableGoldCards.add(g);
            pickableResourceCards.add(r);
        }

        Collections.shuffle(players);
        currentPlayer = players.getFirst();

        for (int i = 0; i < 2; i++) {
            globalGoals.add(goalDeck.pop());
        }
        initializeGameForPlayers();
    }

    /**
     * This method checks if the game has to end in the next turn
     *
     * @return true if the game has to end, false otherwise
     */
    public boolean checkEndGamePoints() {
        return currentPlayer.getPoints() >= 20;
    }

    /**
     * This method checks if the game has to end now because the decks are empty.
     *
     * @return true if the game has to end, false otherwise
     */
    public boolean checkEndGameDecks() {
        return resourceDeck.finished() && goldDeck.finished();
    }


    /**
     * This method decides the winner trying to find the player with the maximum number of points,
     * otherwise it calls the private method checkOtherConditions
     *
     * @return the list of the winner/winners
     */
    public List<Player> getWinner() {
        int numWinners = 1;
        int len = players.size();
        boolean flag = false;

        awardGoalPoints();

        List<Player> tmp = getStandings();

        for (int i = 0; i < len - 1 && !flag; i++) {
            if (tmp.get(i).getPoints() == tmp.get(i + 1).getPoints()) {
                numWinners++;
            } else {
                flag = true;
            }
        }

        if (numWinners == 1) {
            List<Player> p = new ArrayList<>();
            p.add(tmp.getFirst());
            return p;
        } else {
            return checkOtherWinningConditions(tmp.subList(0, numWinners));
        }
    }


    /**
     * This method adds the new Player to the game calling the private method setPlayer
     * It checks if the game is full
     * It checks if the nickname could be used
     * It also gives the player his starting card
     *
     * @param nickname the nickname of the Player
     */
    @Override
    public void addToGame(String nickname) throws GameFullException,
                                                  NicknameAlreadyInUseException,
                                                  NicknameInvalidException {
        if (players.size() == numberPlayers) {
            throw new GameFullException("Game is full");
        } else {
            if (nickname.trim().isEmpty()) {
                throw new NicknameInvalidException("Nickname has to be at least of one character long");
            } else if (checkNickname(nickname)) {
                setPlayer(nickname);
            } else {
                throw new NicknameAlreadyInUseException("Nickname already in use");
            }
        }

        Player p = players.getLast();
        List<PlayableCard> hand = new ArrayList<PlayableCard>();
        hand.add(startingDeck.getTop());
        p.setHand(hand);
    }

    /**
     * This method returns the list of Players
     *
     * @return the list of Players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * This method returns the player that is playing his turn
     *
     * @return the current Player
     */
    public Player getCurrentPlayer() {
        if(currentPlayer == null) {
            currentPlayer = players.getFirst();
        }
        return currentPlayer;
    }
    /**
     *
     * With this method Controller sets the current Player
     * @param p
     */
    public void setCurrentPlayer(Player p){
        currentPlayer = p;
    }
    /**
     * This method modifies the currentPlayer attribute to the next player
     * @return the next player, the one that has to play now
     */
    public Player getNextPlayer() {
        if(currentPlayer == null) {
            currentPlayer = players.getFirst();
        }
        return players.get((players.indexOf(currentPlayer) + 1) % players.size());
    }

    /**
     * This method returns the number of Players
     * @return number of players
     */
    public int getNumberPlayers(){
        return numberPlayers;
    }



    /**
     * This method returns the list of the visible cards and the top cards of resource and gold deck.
     * If a deck is finished, it returns the top card of the other deck.
     * If the two decks are finished, it returns only the pickable cards (pickableResourceCards, pickableGoldCards).
     *
     * @return the list of possible cards that the player can choose
     */
    public List<PlayableCard> getPickableCards() {
        List<PlayableCard> l = new ArrayList<>();
        l.addAll(pickableResourceCards);
        l.addAll(pickableGoldCards);
        if(!resourceDeck.finished()) {
            PlayableCard r = resourceDeck.getTop();
            l.add(r);
        } else {
            if(!goldDeck.finished()) {
                PlayableCard g = goldDeck.getTop();
                l.add(g);
            }
        }
        if(!goldDeck.finished()) {
            PlayableCard g = goldDeck.getTop();
            l.add(g);
        } else {
            if(!resourceDeck.finished()) {
                PlayableCard r = resourceDeck.getTop();
                l.add(r);
            }
        }
        return l;
    }


    /**
     * This method inserts the chosen card in the player's hand, and it updates the decks.
     *
     * @param c the card chosen by the player
     */
    public void chosenCardToAddInHand(PlayableCard c) {
        PlayableCard p;
        boolean visibility = c.getVisibility();
        PlayableDeck deck1, deck2;
        List<PlayableCard> list1, list2;

        if (c instanceof ResourceCard) {
            deck1 = resourceDeck;
            list1 = pickableResourceCards;
            deck2 = goldDeck;
            list2 = pickableGoldCards;
        } else {
            deck1 = goldDeck;
            list1 = pickableGoldCards;
            deck2 = resourceDeck;
            list2 = pickableResourceCards;
        }

        c.setVisibility(true);
        currentPlayer.insertPickedCard(c);

        if(!visibility){
            deck1.remove();
        } else {
            list1.remove(c);
            if(!deck1.finished()){
                p = deck1.getTop();
                list1.add(deck1.getTop());
                deck1.remove();
                p.setVisibility(true);
            } else {
                if(!deck2.finished()){
                    p = deck2.getTop();
                    list2.add(deck2.getTop());
                    deck2.remove();
                    p.setVisibility(true);
                }
            }
        }
    }


    /**
     * this method returns the list of the global goals
     *
     * @return the list of global goals
     */
    public List<GoalCard> getGoals() {
        return globalGoals;
    }


    /**
     * This method returns the list of players sorted by the number of points achieved.
     *
     * @return the list of players sorted by the number of goals achieved
     */
    public List<Player> getStandings() {
        return players.stream().sorted(Comparator.comparingInt(Player::getPoints).reversed())
                .collect(Collectors.toList());
    }


    /**
     * This method returns the list containing two goals from which a single player must choose his own goal.
     * @return the list of two goals
     */
    public List<GoalCard> choosePersonalGoal(){
        List<GoalCard> goals = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            goals.add(goalDeck.getTop());
            goalDeck.remove();
        }
        return goals;
    }

    /**
     * This method sets the turnFinal.
     * In this way the controller can understand when to switch from the normal turn
     * state to the final turn state
     * @param v
     */
    public void setTurnFinal(boolean v){
        turnFinal = v;
    }

    /**
     * This method returns the turnFinal
     * @return the boolean turn Final
     */
    public boolean getTurnFinal(){
        return turnFinal;
    }

    /**
     * This method initializes the game and the hands of each player.
     * It shuffles the players list and assigns the first player.
     * It sets the global goals for the game.
     * Controller calls this method when the number of players is equal to the size of the list of players.
     */

    public void initializeGameForPlayers() {
        Collections.shuffle(players);
        currentPlayer = players.getFirst();

        for (Player p : players) {
            List<PlayableCard> list = new ArrayList<>();
            list.add(startingDeck.pop());

            p.setHand(list);
        }
    }

    public void initializeHandCurrentPlayer() {
        List<PlayableCard> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(resourceDeck.pop());
        }
        list.add(goldDeck.pop());

        currentPlayer.setHand(list);
    }

    public List<PlayableCard> getPickableResourceCards(){
        return pickableResourceCards;
    }

    public List<PlayableCard> getPickableGoldCards(){
        return pickableResourceCards;
    }

    public PlayableCard getFirstResourceCard() {
        return resourceDeck.getTop();
    }

    public PlayableCard getFirstGoldCard() {
        return goldDeck.getTop();
    }


    public List<Color> getAvailableColors() {
        return availableColors;
    }


    public void removeColor(Color c) {
        availableColors.remove(c);
    }


    public Face getFace(String srcImage){

        for (PlayableCard card : currentPlayer.getHand()) {
            if (card.getFront().getSrcImage().equals(srcImage)) {
                return card.getFront();
            }
            if (card.getBack().getSrcImage().equals(srcImage)) {
                return card.getBack();
            }
        }
        return null;
    }

    public PlayableCard getPlayableCard(int id){
        PlayableDeck deck;
        List<PlayableCard> pickable;

        if(id <= 40){
            deck = resourceDeck;
            pickable = pickableResourceCards;
        }
        else {
            deck = goldDeck;
            pickable = pickableGoldCards;
        }

        if(deck.getTop().getId() == id) {
            return deck.getTop();
        }

        for(PlayableCard card : pickable) {
            if(card.getId() == id)
                return card;
        }

        return null;
    }

    public GoalCard getGoalCard(int id){
        for(GoalCard card : goalDeck) {
            if(card.getId() == id) {
                return card;
            }
        }
        return null;
    }



    /**
     * This method initializes the decks of the game.
     * It reads the json files and creates the cards.
     * It shuffles the decks.
     * It uses GsonBuilder with registerTypeAdapter.
     * It uses the classes in the gson package.
     */

    private void initializeDecks() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Evaluator.class, new EvaluatorDeserializer())
                .registerTypeAdapter(Front.class, new FrontDeserializer())
                .registerTypeAdapter(Back.class, new BackDeserializer())
                .registerTypeAdapter(Corner.class, new CornerDeserializer())
                .registerTypeAdapter(PlayableCard.class, new PlayableCardDeserializer())
                .registerTypeAdapter(GoalCard.class, new GoalCardDeserializer())
                .create();

        PlayableDeck d;

        String resource = "src/main/resources/it/polimi/ingsw/am42/json/resource.json";
        String gold = "src/main/resources/it/polimi/ingsw/am42/json/gold.json";
        String starting = "src/main/resources/it/polimi/ingsw/am42/json/starting.json";

        List<String> sources = new ArrayList<>();
        sources.add(resource);
        sources.add(gold);
        sources.add(starting);

        for(String src : sources) {
            try{
                if(src.contains("resource.json")) d = resourceDeck;
                else if(src.contains(("gold"))) d = goldDeck;
                else d = startingDeck;


                FileReader reader = new FileReader(src);
                JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

                for(int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    PlayableCard card = gson.fromJson(jsonObject, PlayableCard.class);
                    d.addCard(card);
                }
            } catch (FileNotFoundException e)   {
                throw new RuntimeException(e);
            }
        }

        String goal = "src/main/resources/it/polimi/ingsw/am42/json/goal.json";

        try {
            FileReader reader = new FileReader(goal);
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            for(int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                GoalCard card = gson.fromJson(jsonObject, GoalCard.class);
                goalDeck.addCard(card);
            }
        } catch (FileNotFoundException e)   {
            throw new RuntimeException(e);
        }

        resourceDeck.shuffle();
        goldDeck.shuffle();
        startingDeck.shuffle();
        goalDeck.shuffle();
    }

    /**
     * This method checks if the nickname is already in use
     *
     * @param nickname the nickname to check
     * @return true if the nickname is not in use, false otherwise
     */
    private boolean checkNickname(String nickname) {
        for (Player p : players) {
            if (p.getNickname().equals(nickname)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method adds the player into players list after all the checks
     *
     * @param nickname the nickname of the player
     */
    private void setPlayer(String nickname) {
        Player p = new Player(nickname);
        players.add(p);
    }

    /**
     * This method returns the list of players sorted by the number of goals achieved
     *
     * @param players the list of players to sort
     * @return the list of players sorted by the number of goals achieved
     */
    private List<Player> getStandingsGoals(List<Player> players) {
        return players.stream().sorted(Comparator.comparingInt(Player::getGoalsAchieved).reversed())
                .collect(Collectors.toList());
    }

    /**
     * This method adds the number of points the player has made thanks to globalGoals and
     * personalGoal and how updates the number of goals he has respected
     */
    private void awardGoalPoints() {
        for (Player p : players) {
            for (GoalCard g : globalGoals) {
                int points = g.getEvaluator().getPoints(p.getBoard());
                p.addPoints(points);
                if (points > 0) {
                    p.addGoalAchieved();
                }
            }
            int points = p.getPersonalGoal().getEvaluator().getPoints(p.getBoard());
            p.addPoints(points);
            if (points > 0) {
                p.addGoalAchieved();
            }
        }
    }

    /**
     * This method checks the other conditions for which a game could be won.
     * If there is a tie on points, the checkOtherConditions checks the number of goals achieved.
     * If it is a tie on goals achieved, the checkOtherConditions returns the list of players that have won
     *
     * @param possibleWinners the list of players that have the same number of points
     * @return the list of winner/winners
     */
    private List<Player> checkOtherWinningConditions(List<Player> possibleWinners) {
        possibleWinners = getStandingsGoals(possibleWinners);

        int numWinners = 1;
        boolean flag = false;
        for (int i = 0; i < possibleWinners.size() - 1 && !flag; i++) {
            if (possibleWinners.get(i).getGoalsAchieved() == possibleWinners.get(i + 1).getGoalsAchieved()) {
                numWinners++;
            } else {
                flag = true;
            }
        }
        return possibleWinners.subList(0, numWinners);
    }

}
