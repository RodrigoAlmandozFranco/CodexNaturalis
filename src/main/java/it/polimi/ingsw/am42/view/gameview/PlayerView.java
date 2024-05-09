package it.polimi.ingsw.am42.view.gameview;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.structure.Board;

import java.util.List;

public class PlayerView {
    private int points;
    private  String nickname;
    private List<PlayableCard> hand;
    private Color color;
    private BoardView board;
    private GoalCard personalGoal;

    public int getPoints() {
        return points;
    }

    public String getNickname() {
        return nickname;
    }

    public List<PlayableCard> getHand() {
        return hand;
    }

    public Color getColor() {
        return color;
    }

    public BoardView getBoard() {
        return board;
    }

    public GoalCard getPersonalGoal() {
        return personalGoal;
    }

    public int getNumberGoalsAchieved() {
        return numberGoalsAchieved;
    }

    private int numberGoalsAchieved;

    public void update(Change diff){
        points = diff.getPointsPlayer();
        numberGoalsAchieved = diff.getNumberGoalsAchieved();
        hand = diff.getHand();
        board.update(diff);
    }
}
