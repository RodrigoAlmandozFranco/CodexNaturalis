package it.polimi.ingsw.am42.model;


import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;
import it.polimi.ingsw.am42.model.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.model.structure.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.util.Map;




/**
 * This class represents the player, containing all the attributes and methods to play his game
 * @author Tommaso Crippa
 */
public class Player implements Serializable {
    private int points;
    private final String nickname;
    private List<PlayableCard> hand;
    private Color color;
    private final Board board;
    private GoalCard personalGoal;
    private int numberGoalsAchieved;

    /**
     * Constructor of the class Player for the reconstruction
     * of the game after network disconnections.
     * @param nickname
     * @param points
     * @param hand
     * @param color
     * @param board
     * @param personalGoal
     * @param numberGoalsAchieved
     *
     * @author Rodrigo Almandoz Franco
     */

    public Player(String nickname, int points, List<PlayableCard> hand, Color color, Board board,
                  GoalCard personalGoal, int numberGoalsAchieved) {

        this.nickname = nickname;
        this.points = points;
        this.hand = hand;
        this.color = color;
        this.board = board;
        this.personalGoal = personalGoal;
        this.numberGoalsAchieved = numberGoalsAchieved;
    }


    public Player(String nickname) {
        this.nickname = nickname;
        this.points = 0;
        this.hand = new ArrayList<PlayableCard>();
        Random random = new Random();
        this.color = null;
        this.board = new Board();
        this.personalGoal = null;
        this.numberGoalsAchieved = 0;
    }

    /**
     * Sets the player's personal goal.
     * The player chooses the goal at the beginning of the game and then here it is registered.
     * @param goal the goal to set
     *
     * @author Rodrigo Almandoz Franco
     */

    public void setPersonalGoal(GoalCard goal) {
        this.personalGoal = goal;
    }

    /**
     * Returns the number of points the player has
     *
     * @author Tommaso Crippa
     * @return the player's points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns the player's nickname
     *
     * @author Tommaso Crippa
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }
    /**
     * Returns the player's hand, containing the three cards
     *
     * @author Tommaso Crippa
     * @return the player's hand
     */
    public List<PlayableCard> getHand() {
        return hand;
    }

    /**
     * Returns the Color of the player
     *
     * @author Tommaso Crippa
     * @return the player's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the board of the player, containing all the cards already placed
     *
     * @author Tommaso Crippa
     * @return the player's board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the private goal of the player
     *
     * @author Tommaso Crippa
     * @return the player's personal goal
     */
    public GoalCard getPersonalGoal() {
        return personalGoal;
    }

    /**
     * Returns the number of goals the player has currently achieved
     *
     * @author Tommaso Crippa
     * @return the number of goals achieved
     */
    public int getGoalsAchieved() {return numberGoalsAchieved;}

    /**
     * Sets the initial hand of the player, used at the start of the game
     *
     * @author Rodrigo Almandoz Franco
     * @param hand the initial starting hand
     */

    public void setHand(List<PlayableCard> hand) {
        this.hand = hand;
    }

    /**
     * Sets the initial goal of the player
     *
     * @author Mattia Brandi
     * @param goal the player's private goal
     */
    public void setGoal(GoalCard goal) {
        this.personalGoal = goal;
    }


    /**
     * Sets the player's color
     *
     * @author Tommaso Crippa
     * @param color the player's color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Checks if the card can be placed on the board, given its requirements and current board status
     *
     * @author Tommaso Crippa
     * @param face the face that we are checking if it can be inserted
     * @return boolean value, if true the face can be placed, otherwise it cannot
     */
    public void checkRequirements(Face face) throws RequirementsNotMetException{
        Map<Resource, Integer> requirements = face.getRequirements();
        for (Resource r : requirements.keySet()) {
            if (requirements.get(r) > board.getTotalResources().get(r))
                throw new RequirementsNotMetException("Requirements not met");
        }
    }

    /**
     * Places the face at the given position in the board
     * IT MAKES THE HYPOTHESIS THAT THE POSITION IS CORRECT AND REQUIREMENTS ARE MET
     *
     * @author Tommaso Crippa
     * @param position the position to put the face in
     * @param face the face to insert inside the board
     */
    public void placeCard(Position position, Face face) {

        face.setPosition(position);
        board.addFace(face);
        removeCardFromHand(face);
    }

    /**
     * Given a face, it returns the total points it would add if placed on the current board
     *
     * @author Tommaso Crippa
     * @param face the face that needs to be inserted
     * @return the number of points the player could receive
     */
    public int calculatePoint(Face face) {
        Evaluator e = face.getEvaluator();
        return e.getPoints(board);
    }

    /**
     * Adds points to the player's score
     *
     * @author Tommaso Crippa
     * @param points the number of points won by the player
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * Removes the card with the given face from the hand
     *
     * @author Tommaso Crippa
     * @param face the face of the card that needs to be removed
     */
    private void removeCardFromHand(Face face) {
        for (PlayableCard c : hand) {
            if (face.getId() == c.getId()) {
                hand.remove(c);
                break;
            }
        }
    }

    /**
     * Inserts the given card inside the hand
     *
     * @author Tommaso Crippa
     * @param card the card that is inserted in the hand
     */
    public void insertPickedCard(PlayableCard card) {
        hand.add(card);
    }


    /**
     * When a goal is met, this method is called to increment the number of goals achieved
     *
     * @author Tommaso Crippa
     */
    public void addGoalAchieved() {
        numberGoalsAchieved += 1;
    }
}
