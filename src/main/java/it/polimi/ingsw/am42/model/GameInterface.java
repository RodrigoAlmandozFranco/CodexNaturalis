package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.Card;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException;

import java.util.List;

/**
 * This interface is used to define the methods that game exposes to the controller
 * @author Rodrigo Almandoz Franco
 */

public interface GameInterface {

    public void initializeDecks();
    public boolean checkEndGamePoints();
    public boolean checkEndGameDecks();
    public List<Player> getWinner();

    public void addToGame(String nickname) throws   GameFullException,
                                                    NicknameAlreadyInUseException,
                                                    NicknameInvalidException;
    public List<Player> getPlayers();
    public Player getCurrentPlayer();
    public List<PlayableCard> getPickableCards();
    public void chosenCardToAddInHand (PlayableCard c);
    public List<GoalCard> getGoals();
    public List<Player> getStandings();
    
}
