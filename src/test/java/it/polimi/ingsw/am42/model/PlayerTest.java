package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.Card;
import it.polimi.ingsw.am42.model.cards.types.*;
import it.polimi.ingsw.am42.model.cards.types.playables.GoldCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.EvaluatorPoints;
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

        p.setHandAndGoal(hand, null);

        assertEquals(hand, p.getHand());

    }

    @Test
    void getColor() {
        Player p = new Player("Tommy", Color.CYAN);

        assertEquals(p.getColor(), Color.CYAN);
    }

    @Test
    void getPersonalGoal() {

        Player p = new Player("Tommy");

        GoalCard c = new GoalCard(1, new EvaluatorPoints(0));

        p.setHandAndGoal(null, c);

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

        GoalCard c = new GoalCard(1, new EvaluatorPoints(0));

        p.setHandAndGoal(hand, c);


        assertEquals(p.getHand(), hand);

        assertEquals(p.getPersonalGoal(), c);


    }

    @Test
    void checkRequirements() {
        Player p = new Player("Tommy");

        Map<Resource, Integer> requirements = new HashMap<>();
        requirements.put(Resource.ANIMALKINGDOM, 1);
        Front f = new Front("C://", null, null, requirements, null);
        Back f2 = new Back("C://", null, null, Resource.ANIMALKINGDOM);

        PlayableCard c = new GoldCard(1, f, f2);


        assertFalse(p.checkRequirements(f));
        assertTrue(p.checkRequirements(f2));

        p.placeCard(new Position(0, 0), f2);

        assertTrue(p.checkRequirements(f));

    }

    @Test
    void placeCard() {
        Player p = new Player("Tommy");


        Front f = new Front("C://", new ArrayList<Corner>(), Color.GREEN, new HashMap<>(), new EvaluatorPoints(0));
        Back f2 = new Back("C://", new ArrayList<Corner>(), Color.CYAN, Resource.ANIMALKINGDOM);

        PlayableCard c = new GoldCard(1, f, f2);

        p.placeCard(new Position(0, 0), f2);

        assertTrue(p.getBoard().getFaces().contains(f2));


    }

    @Test
    void calculatePoint() {
        Player p = new Player("Tommy");


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
    void removeCardFromHand() {
        Player p = new Player("Tommy");

        Front f = new Front("C://", new ArrayList<Corner>(), Color.GREEN, new HashMap<>(), new EvaluatorPoints(0));
        Back f2 = new Back("C://", new ArrayList<Corner>(), Color.CYAN, Resource.ANIMALKINGDOM);

        PlayableCard c = new GoldCard(1, f, f2);

        p.placeCard(new Position(0, 0), f2);

        assertTrue(p.getBoard().getFaces().contains(f2));

        p.removeCardFromHand(f2);

        assertFalse(p.getBoard().getFaces().contains(f2));

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

        p.removeCardFromHand(f2);

        assertFalse(p.getBoard().getFaces().contains(f2));
    }
}
