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
    void initializeGame();
    void initializeHandCurrentPlayer();
    boolean checkEndGamePoints();
    void changeState();
    void setCurrentState(State currentState);
    boolean checkEndGameDecks();
    List<Player> getWinner();
    void addToGame(String nickname) throws   GameFullException,
                                                    NicknameAlreadyInUseException,
                                                    NicknameInvalidException;
    List<Player> getPlayers();
    Player getCurrentPlayer();
    Player getNextPlayer();
    void setCurrentPlayer(Player p);
    int getNumberPlayers();
    List<PlayableCard> getPickableCards();
    List<GoalCard> getGoals();
    List<Player> getStandings();
    void chosenCardToAddInHand(PlayableCard c);
    List<GoalCard> choosePersonalGoal();
    void setTurnFinal(boolean v);
    boolean getTurnFinal();
    List<PlayableCard> getPickableResourceCards();
    List<PlayableCard> getPickableGoldCards();
    PlayableCard getFirstResourceCard();
    PlayableCard getFirstGoldCard();
    List<PlayersColor> getAvailableColors();
    void removeColor(PlayersColor c);
    State getCurrentState();
    Player getPlayer(String nickname);
}
