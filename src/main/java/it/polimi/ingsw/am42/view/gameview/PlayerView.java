package it.polimi.ingsw.am42.view.gameview;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.structure.Board;

import java.util.ArrayList;
import java.util.List;

public class PlayerView {

    private int points;
    private  String nickname;
    private List<PlayableCard> hand;
    private Color color;
    private Board board;
    private GoalCard personalGoal;
    private int numberGoalsAchieved;

    public PlayerView() {
        this.points = 0;
        this.nickname = "";
        this.hand = new ArrayList<>();
        this.color = null;
        this.board = new Board();
        this.personalGoal = null;
        this.numberGoalsAchieved = 0;
    }

    public PlayerView(Player p) {
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

    public void setColor(Color color){
        this.color = color;
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

    public Color getColor() {
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

    @Override
    public String toString() {
        return nickname;
    }

    public void update(Change diff){
        points = diff.getPointsPlayer();
        numberGoalsAchieved = diff.getNumberGoalsAchieved();
        hand.clear();
        hand.addAll(diff.getHand());
        if (diff.getLastPlacedFace() != null)
            board.addFace(diff.getLastPlacedFace());
    }
}
