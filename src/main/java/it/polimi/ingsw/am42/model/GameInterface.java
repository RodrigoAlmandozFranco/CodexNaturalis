package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;


import java.util.List;

/**
 * This interface is used to define the methods that game exposes to the controller
 * @author Rodrigo Almandoz Franco
 */

public interface GameInterface {
    /**
     * This method initializes the game.
     * It creates the decks and initializes the pickable cards.
     */
    void initializeGame();
    /**
     * This method initializes the hand of the current player.
     */
    void initializeHandCurrentPlayer();
    /**
     * This method checks if the game has to end in the next turn
     *
     * @return true if the game has to end, false otherwise
     */
    boolean checkEndGamePoints();
    /**
     * This method changes the state of the game
     */
    void changeState();
    /**
     * This method sets the current state of the game
     * @param currentState the state of the game
     */
    void setCurrentState(State currentState);
    /**
     * This method checks if the game has to end now because the decks are empty.
     *
     * @return true if the game has to end, false otherwise
     */
    boolean checkEndGameDecks();
    /**
     * This method decides the winner trying to find the player with the maximum number of points,
     * otherwise it calls the private method checkOtherConditions
     *
     * @return the list of the winner/winners
     */
    List<Player> getWinner();
    /**
     * This method adds the new Player to the game calling the private method setPlayer
     * It checks if the game is full
     * It checks if the nickname could be used
     * It also gives the player his starting card
     *
     * @param nickname the nickname of the Player
     */
    void addToGame(String nickname) throws   GameFullException,
                                                    NicknameAlreadyInUseException,
                                                    NicknameInvalidException;
    /**
     * This method returns the list of Players
     *
     * @return the list of Players
     */
    List<Player> getPlayers();
    /**
     * This method returns the player that is playing his turn
     *
     * @return the current Player
     */
    Player getCurrentPlayer();
    /**
     * This method returns the next player that has to play after the current player
     * @return the next player, the one that has to play now
     */

    Player getNextPlayer();
    /**
     *
     * With this method Controller sets the current Player
     * @param p
     */
    void setCurrentPlayer(Player p);
    /**
     * This method returns the number of Players
     * @return number of players
     */
    int getNumberPlayers();
    /**
     * this method returns the list of the global goals
     *
     * @return the list of global goals
     */
    List<GoalCard> getGoals();
    /**
     * This method returns the list of players sorted by the number of points achieved.
     *
     * @return the list of players sorted by the number of goals achieved
     */
    List<Player> getStandings();
    /**
     * This method inserts the chosen card in the player's hand, and it updates the decks.
     *
     * @param c the card chosen by the player
     */
    void chosenCardToAddInHand(PlayableCard c);
    /**
     * This method returns the list containing two goals from which a single player must choose his own goal.
     * @return the list of two goals
     */
    List<GoalCard> choosePersonalGoal();
    /**
     * This method sets the turnFinal.
     * In this way the controller can understand when to switch from the normal turn
     * state to the final turn state
     * @param v
     */
    void setTurnFinal(boolean v);
    /**
     * This method returns the turnFinal
     * @return the boolean turn Final
     */
    boolean getTurnFinal();
    /**
     * This method returns the pickable resource cards
     * @return the list of pickable resource cards
     */
    List<PlayableCard> getPickableResourceCards();
    /**
     * This method returns the pickable gold cards
     * @return the list of pickable gold cards
     */
    List<PlayableCard> getPickableGoldCards();
    /**
     * This method returns the first resource card of the resource deck
     * @return the first resource card
     */
    PlayableCard getFirstResourceCard();
    /**
     * This method returns the first gold card of the gold deck
     * @return the first gold card
     */
    PlayableCard getFirstGoldCard();
    /**
     * This method returns the list of available colors
     * @return the list of available colors
     */
    List<PlayersColor> getAvailableColors();
    /**
     * This method removes the color from the list of available colors
     * @param c the color to remove
     */
    void removeColor(PlayersColor c);
    /**
     * This method returns the current state of the game
     * @return the current state of the game
     */
    State getCurrentState();
    /**
     * This method returns the player with the corresponding nickname
     * @param nickname the nickname of the player
     * @return the player with the corresponding nickname
     */
    Player getPlayer(String nickname);
}
