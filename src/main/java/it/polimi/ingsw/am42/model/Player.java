package it.polimi.ingsw.am42.model;


import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.model.structure.Position;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import java.util.Map;




/**
 * This class represents the player, containing all the attributes and methods to play his game
 * @author Tommaso Crippa
 */
public class Player {
    private int points;
    private final String nickname;
    private List<PlayableCard> hand;
    private final Color color;
    private final Board board;
    private GoalCard personalGoal;
    private int numberGoalsAchieved;


    public Player(String nickname) {
        this.nickname = nickname;
        this.points = 0;
        this.hand = new ArrayList<PlayableCard>();
        Random random = new Random();
        this.color = Color.values()[random.nextInt(Color.values().length)];
        this.board = new Board();
        this.personalGoal = null;
        this.numberGoalsAchieved = 0;
    }

    public Player(String nickname, Color color) {
        this.nickname = nickname;
        this.points = 0;
        this.hand = new ArrayList<PlayableCard>();
        this.color = color;
        this.board = new Board();
        this.personalGoal = null;
        this.numberGoalsAchieved = 0;
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
     * Checks if the card can be placed on the board, given its requirements and current board status
     *
     * @author Tommaso Crippa
     * @param face the face that we are checking if it can be inserted
     * @return boolean value, if true the face can be placed, otherwise it cannot
     */
    public boolean checkRequirements(Face face) {
        Map<Resource, Integer> requirements = face.getRequirements();
        for (Resource r : requirements.keySet()) {
            if (requirements.get(r) > board.getTotalResources().get(r))
                return false;
        }
        return true;
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
    public void removeCardFromHand(Face face) {
        for (PlayableCard c : hand) {
            if (face.equals((c.getFront())) || face.equals(c.getBack())) {
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
