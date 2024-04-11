package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;


import java.util.List;

/**
 * This interface is used to define the methods that game exposes to the controller
 * @author Rodrigo Almandoz Franco
 */

public interface GameInterface {

    public void initializeGame();
    public boolean checkEndGamePoints();
    public boolean checkEndGameDecks();
    public List<Player> getWinner();

    public void addToGame(String nickname) throws   GameFullException,
                                                    NicknameAlreadyInUseException,
                                                    NicknameInvalidException;
    public List<Player> getPlayers();
    public Player getCurrentPlayer();
    public Player getNextPlayer();
    public int getNumberPlayers();
    public void setCurrentPlayer(Player p);
    public List<PlayableCard> getPickableCards();
    public void chosenCardToAddInHand (PlayableCard c);
    public List<GoalCard> getGoals();
    public List<Player> getStandings();
    public List<GoalCard> choosePersonalGoal();
    public void setTurnFinal(boolean v);
    public boolean getTurnFinal();
    public void initializeGameForPlayers();
    public List<Integer> getGlobalGoals();
    public List<Integer> getGoalDeck();
    public List<Integer> getResourceDeck();
    public List<Integer> getGoldDeck();
    public List<Integer> getStartingDeck();
    public List<Integer> getPickableResourceCards();
    public List<Integer> getPickableGoldCards();



}
