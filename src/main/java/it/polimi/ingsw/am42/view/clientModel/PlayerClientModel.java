package it.polimi.ingsw.am42.view.clientModel;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.structure.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to store the simplified player model in the client
 * The information are sent one by one with the Change messages from the server
 */
public class PlayerClientModel {

    private int points;
    private  String nickname;
    private List<PlayableCard> hand;
    private PlayersColor color;
    private Board board;
    private GoalCard personalGoal;
    private int numberGoalsAchieved;
    private List<PlayersColor> avColors;
    private List<GoalCard> avGoals;

    public PlayerClientModel() {
        this.points = 0;
        this.nickname = "";
        this.hand = new ArrayList<>();
        this.color = null;
        this.board = new Board();
        this.personalGoal = null;
        this.numberGoalsAchieved = 0;
    }

    public PlayerClientModel(Player p) {
        this.points = p.getPoints();
        this.nickname = p.getNickname();
        this.hand = new ArrayList<>();
        this.hand.addAll(p.getHand());
        this.color = p.getColor();
        this.board = p.getBoard();
        this.personalGoal = p.getPersonalGoal();
        this.numberGoalsAchieved = p.getGoalsAchieved();
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    public String getNickname() {
        return nickname;
    }
    public List<PlayableCard> getHand() {
        return hand;
    }
    public void setHand(List<PlayableCard> hand) {
        this.hand = hand;
    }

    public PlayersColor getColor() {
        return color;
    }

    public Board getBoard() {
        return board;
    }

    public GoalCard getPersonalGoal() {
        return personalGoal;
    }

    public int getNumberGoalsAchieved() {
        return numberGoalsAchieved;
    }

    public void setNumberGoalsAchieved(int numberGoalsAchieved) {
        this.numberGoalsAchieved = numberGoalsAchieved;
    }

    public List<PlayersColor> getAvColors() {
        return avColors;
    }

    public void setAvColors(List<PlayersColor> avColors) {
        this.avColors = avColors;
    }

    public void setColor(PlayersColor color) {
        this.color = color;
    }

    public void setPersonalGoal(GoalCard personalGoal) {
        this.personalGoal = personalGoal;
    }

    public List<GoalCard> getAvGoals() {
        return avGoals;
    }

    public void setAvGoals(List<GoalCard> avGoals) {
        this.avGoals = avGoals;
    }

    @Override
    public String toString() {
        return nickname;
    }
    /**
     * Main method to update the contents of the game
     * @param diff the Change message received by the server
     */
    public void update(Change diff){
        points = diff.getPointsPlayer();
        numberGoalsAchieved = diff.getNumberGoalsAchieved();
        color = diff.getColor();
        hand.clear();
        hand.addAll(diff.getHand());
        if (diff.getLastPlacedFace() != null)
            if(board.getLastPlacedFace() == null||board.getLastPlacedFace().getId()!=diff.getLastPlacedFace().getId())
                board.addFace(diff.getLastPlacedFace());
    }
}
