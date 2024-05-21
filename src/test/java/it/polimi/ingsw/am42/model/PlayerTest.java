package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.Card;
import it.polimi.ingsw.am42.model.cards.types.*;
import it.polimi.ingsw.am42.model.cards.types.playables.GoldCard;
import it.polimi.ingsw.am42.model.enumeration.*;
import it.polimi.ingsw.am42.model.evaluator.EvaluatorPoints;
import it.polimi.ingsw.am42.model.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.am42.model.structure.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getPoints() {

        Player p = new Player("Tommy");

        assertEquals(p.getPoints(), 0);
        p.addPoints(5);
        assertEquals(p.getPoints(), 5);
    }

    @Test
    void getNickname() {
        Player p = new Player("Tommy");

        assertEquals(p.getNickname(), "Tommy");
    }

    @Test
    void getHand() {
        Player p = new Player("Tommy");

        assertEquals(p.getHand(), new ArrayList<PlayableCard>());

        List<PlayableCard> hand = new ArrayList<PlayableCard>();

        hand.add(new GoldCard(1, null, null));

        p.setHand(hand);

        assertEquals(hand, p.getHand());

    }

    @Test
    void getColor() {
        Player p = new Player("Tommy");

        p.setColor(PlayersColor.RED);

        assertEquals(p.getColor(), PlayersColor.RED);
    }

    @Test
    void getPersonalGoal() {

        Player p = new Player("Tommy");

        GoalCard c = new GoalCard(1, null, new EvaluatorPoints(0));

        p.setGoal(c);

        assertEquals(p.getPersonalGoal(), c);

    }

    @Test
    void getGoalsAchieved() {
        Player p = new Player("Tommy");

        assertEquals(p.getGoalsAchieved(), 0);

        p.addGoalAchieved();

        assertEquals(p.getGoalsAchieved(), 1);
    }

    @Test
    void setHandAndGoal() {
        Player p = new Player("Tommy");


        List<PlayableCard> hand = new ArrayList<PlayableCard>();

        hand.add(new GoldCard(1, null, null));

        GoalCard c = new GoalCard(1, null, new EvaluatorPoints(0));

        p.setHand(hand);
        p.setGoal(c);


        assertEquals(p.getHand(), hand);

        assertEquals(p.getPersonalGoal(), c);


    }

    @Test
    void checkRequirements() {
        Player p = new Player("Tommy");

        Map<Resource, Integer> requirements = new HashMap<>();
        requirements.put(Resource.ANIMALKINGDOM, 1);
        List<Resource> lst = new ArrayList<>();
        lst.add(Resource.ANIMALKINGDOM);

        List<Corner> cornerList = new ArrayList<Corner>();
        for (Direction d : Direction.values()) {
            cornerList.add(new Corner(Resource.FUNGIKINGDOM, CornerState.OPEN, d));
        }

        Front f = new Front("C://", cornerList, null, requirements, null);
        Back f2 = new Back("C://", cornerList, null, lst);

        PlayableCard c = new GoldCard(1, f, f2);

        try {
            p.checkRequirements(f);
        }
        catch (RequirementsNotMetException e) {}

        try {
            p.checkRequirements(f2);

            p.placeCard(new Position(0, 0), f2);

            p.checkRequirements(f);
        } catch (RequirementsNotMetException e) {}


    }

    @Test
    void placeCard() {
        Player p = new Player("Tommy");

        List<Resource> lst = new ArrayList<>();
        lst.add(Resource.ANIMALKINGDOM);

        Front f = new Front("C://", new ArrayList<Corner>(), Color.GREEN, new HashMap<>(), new EvaluatorPoints(0));
        Back f2 = new Back("C://", new ArrayList<Corner>(), Color.CYAN, lst);

        PlayableCard c = new GoldCard(1, f, f2);

        List<PlayableCard> hand = new ArrayList<PlayableCard>();

        hand.add(c);

        p.setHand(hand);

        assertTrue(p.getHand().contains(c));

        p.placeCard(new Position(0, 0), f2);

        assertTrue(p.getBoard().getFaces().contains(f2));

        assertFalse(p.getHand().contains(c));

        assertTrue(p.getHand().isEmpty());

    }

    @Test
    void calculatePoint() {
        Player p = new Player("Ale");


        Front f = new Front("C://", null, null, null, new EvaluatorPoints(1));

        assertEquals(p.calculatePoint(f), 1);

    }

    @Test
    void addPoints() {
        Player p = new Player("Tommy");

        assertEquals(p.getPoints(), 0);
        p.addPoints(5);
        assertEquals(p.getPoints(), 5);
    }

    @Test
    void insertPickedCard() {
        Player p = new Player("Tommy");


        Front f = new Front("C://", null, null, null, null);
        Back f2 = new Back("C://", null, null, null);

        PlayableCard c = new GoldCard(1, f, f2);


        assertFalse(p.getHand().contains(c));
        p.insertPickedCard(c);

        assertTrue(p.getHand().contains(c));
    }
}
