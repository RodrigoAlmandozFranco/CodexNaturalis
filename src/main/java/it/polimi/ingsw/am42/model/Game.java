package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.Card;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.ResourceCard;
import it.polimi.ingsw.am42.model.decks.GoalDeck;
import it.polimi.ingsw.am42.model.decks.PlayableDeck;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException;
import it.polimi.ingsw.am42.model.structure.Board;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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


public class Game implements GameInterface {

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
            players = new ArrayList<>();
            resourceDeck = new PlayableDeck();
            goldDeck = new PlayableDeck();
            startingDeck = new PlayableDeck();
            goalDeck = new GoalDeck();
            pickableResourceCards = new ArrayList<>();
            pickableGoldCards = new ArrayList<>();
            globalGoals = new ArrayList<>();
            this.numberPlayers = numberPlayers;
        }
    }

    /**
     * This method initializes all the Decks
     */
    //TODO
    public void initializeDecks() {
        /*this method uses the json library*/

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
            return checkOtherConditions(tmp.subList(0, numWinners));
        }
    }


    /**
     * This method adds the new Player to the game calling the private method setPlayer
     * It checks if the game is full
     * It checks if the nickname could be used
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
        return currentPlayer;
    }

    /**
     * This method returns the list of the visible cards and the top cards of resource and gold deck
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
        }
        if(!goldDeck.finished()) {
            PlayableCard g = goldDeck.getTop();
            l.add(g);
        }
        return l;
    }


    /**
     * This method inserts the chosen card in the player's hand, and it updates the decks and the
     * lists of pickable card.
     *
     * @param c the card chosen by the player
     */
    public void chosenCardToAddInHand(PlayableCard c) {
        PlayableCard p;
        currentPlayer.insertPickedCard(c);
        if (!c.getVisibility()) {
            if (c instanceof ResourceCard) {
                resourceDeck.remove();
            } else {
                goldDeck.remove();
            }
        } else {
            if (c instanceof ResourceCard) {
                pickableResourceCards.remove(c);
                if(!resourceDeck.finished()) {
                    p = resourceDeck.getTop();
                    pickableResourceCards.add(resourceDeck.getTop());
                    resourceDeck.remove();
                    p.setVisibility(true);
                }
            } else {
                pickableGoldCards.remove(c);
                if(!goldDeck.finished()) {
                    p = goldDeck.getTop();
                    pickableGoldCards.add(goldDeck.getTop());
                    goldDeck.remove();
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
        return players.stream().sorted(Comparator.comparingInt(Player::getPoints))
                .collect(Collectors.toList());
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
        return players.stream().sorted(Comparator.comparingInt(Player::getGoalsAchieved))
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
    private List<Player> checkOtherConditions(List<Player> possibleWinners) {
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

