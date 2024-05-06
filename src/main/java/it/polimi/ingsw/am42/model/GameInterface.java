package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
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
    public void initializeGameForPlayers();
    public boolean checkEndGamePoints();
    public boolean checkEndGameDecks();
    public List<Player> getWinner();
    public void addToGame(String nickname) throws   GameFullException,
                                                    NicknameAlreadyInUseException,
                                                    NicknameInvalidException;
    public List<Player> getPlayers();
    public Player getCurrentPlayer();
    public Player getNextPlayer();
    public void setCurrentPlayer(Player p);
    public int getNumberPlayers();
    public List<PlayableCard> getPickableCards();
    public List<GoalCard> getGoals();
    public List<Player> getStandings();
    public void chosenCardToAddInHand(PlayableCard c);
    public List<GoalCard> choosePersonalGoal();
    public void setTurnFinal(boolean v);
    public boolean getTurnFinal();
    public List<PlayableCard> getPickableResourceCards();
    public List<PlayableCard> getPickableGoldCards();
    public PlayableCard getFirstResourceCard();
    public PlayableCard getFirstGoldCard();
    public List<Color> getAvailableColors();
    public void removeColor(Color c);
    public Face getFace(String srcImage);
    public PlayableCard getPlayableCard(int id);
    public GoalCard getGoalCard(int id);
}
