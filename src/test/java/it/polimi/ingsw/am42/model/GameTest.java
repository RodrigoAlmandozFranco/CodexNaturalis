package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.GoldCard;
import it.polimi.ingsw.am42.model.cards.types.playables.ResourceCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.evaluator.EvaluatorPoints;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException;
import it.polimi.ingsw.am42.model.structure.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @org.junit.jupiter.api.Test
    void initializeGame() {
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();
        assertEquals(2, game.getPlayers().size());

        List<PlayableCard> cards = game.getPickableCards();
        assertEquals(6, cards.size());
        assertFalse(cards.contains(null));
        int resource = 0;
        int gold = 0;
        for(PlayableCard c : cards) {
            assertNotNull(c);
            assertTrue(c.getVisibility());
            if(c instanceof ResourceCard)
                resource++;
            else gold++;

            assertTrue(c instanceof ResourceCard || c instanceof GoldCard);
        }
        assertEquals(3, resource);
        assertEquals(3, gold);
    }

    @org.junit.jupiter.api.Test
    void checkEndGamePoints() {
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();
        assertFalse(game.checkEndGamePoints());


        try{
            game.addToGame("Rodri");
            game.addToGame("Matti");

            game.getPlayers().getFirst().addPoints(10);
            assertFalse(game.checkEndGamePoints());
            game.getPlayers().getFirst().addPoints(11);
            assertTrue(game.checkEndGamePoints());


        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void checkEndGameDecks(){
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();

        assertFalse(game.checkEndGameDecks());

        Front front = new Front("front", null, Color.RED, null, null);
        Back back = new Back("back", null, Color.RED, null);
        PlayableCard card = new GoldCard(1, front, back);

        PlayableCard c = new ResourceCard(2, front, back);

        for(int i = 0; i < 38; i++){
            game.chosenCardToAddInHand(card);
        }
        assertFalse(game.checkEndGameDecks());
        for(int i = 0; i < 38; i++){
            game.chosenCardToAddInHand(c);
        }
        assertTrue(game.checkEndGameDecks());
    }

    @org.junit.jupiter.api.Test
    void getWinner(){
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();
        try{
            try {
                game.addToGame("Rodri");
                game.addToGame("Matti");
            } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                     it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                     it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
                throw new RuntimeException(e);
            }

            game.getPlayers().getFirst().addPoints(10);
            game.getPlayers().getLast().addPoints(1);

            assertEquals(1, game.getWinner().size());
            List<Player> winner = new ArrayList<>();
            winner.add(game.getPlayers().getFirst());
            assertEquals(game.getWinner(), winner);

            game.getPlayers().getLast().addPoints(9);

            game.getPlayers().getLast().addGoalAchieved();

            game.getPlayers().getFirst().addGoalAchieved();
            game.getPlayers().getFirst().addGoalAchieved();

            assertEquals(game.getWinner(), winner);

            game.getPlayers().getLast().addGoalAchieved();
            winner.add(game.getPlayers().getLast());
            assertEquals(game.getWinner(), winner);

        } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
    }

    @org.junit.jupiter.api.Test
    void addToGame() {
        Game game = null;
        try {
            game = new Game(6);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            assertInstanceOf(NumberPlayerWrongException.class, e);
        }
        assertNull(game);

        try {
            game = new Game(-6);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            assertInstanceOf(NumberPlayerWrongException.class, e);
        }
        assertNull(game);

        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(game);

        game.initializeGame();
        try{
            game.addToGame("");
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            assertInstanceOf(NicknameInvalidException.class, e);
        }
        try{
            game.addToGame("    ");
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            assertInstanceOf(NicknameInvalidException.class, e);
        }

        assertEquals(0, game.getPlayers().size());

        try{
            game.addToGame("Rodri");
            assertEquals(1, game.getPlayers().size());
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        try{
            game.addToGame("Rodri");
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            assertInstanceOf(NicknameAlreadyInUseException.class, e);
        }assertEquals(1, game.getPlayers().size());

        try{
            game.addToGame("Matti");
            assertEquals(2, game.getPlayers().size());
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        try{
            game.addToGame("Tommi");
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            assertInstanceOf(GameFullException.class, e);
        }
        assertEquals(2, game.getPlayers().size());
    }

    @org.junit.jupiter.api.Test
    void getPlayers() {
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();
        try{
            game.addToGame("Rodri");
            game.addToGame("Matti");
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        Player p1 = game.getPlayers().getFirst();
        Player p2 = game.getPlayers().getLast();

        assertEquals("Rodri", p1.getNickname());
        assertEquals("Matti", p2.getNickname());

        List<Player> players = game.getPlayers();
        assertEquals(2, players.size());
    }

    @org.junit.jupiter.api.Test
    void getCurrentPlayer() {
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();
        try{
            game.addToGame("Rodri");
            game.addToGame("Matti");
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        assertEquals(game.getCurrentPlayer(), game.getPlayers().getFirst());
    }

    @org.junit.jupiter.api.Test
    void getNextPlayer() {
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();
        try{
            game.addToGame("Rodri");
            game.addToGame("Matti");
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            throw new RuntimeException(e);
        }


        assertEquals(2, game.getPlayers().size());
        assertEquals("Rodri", game.getCurrentPlayer().getNickname());
        assertEquals("Matti", game.getNextPlayer().getNickname());
    }

    @org.junit.jupiter.api.Test
    void getPickableCards(){
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();
        List<PlayableCard> cards = game.getPickableCards();
        assertEquals(6, cards.size());
        assertFalse(cards.contains(null));
        int resource = 0;
        int gold = 0;
        int rNotVisible = 0;
        int gNotVisible = 0;
        for(PlayableCard c : cards) {
            assertNotNull(c);
            if(c instanceof ResourceCard && c.getVisibility())
                resource++;
            else if (c instanceof GoldCard && c.getVisibility())
                gold++;
            else if (c instanceof ResourceCard && !c.getVisibility())
                rNotVisible++;
            else if (c instanceof GoldCard && !c.getVisibility())
                gNotVisible++;

            assertTrue(c instanceof ResourceCard || c instanceof GoldCard);
        }
        assertEquals(2, resource);
        assertEquals(2, gold);
        assertEquals(1, rNotVisible);
        assertEquals(1, gNotVisible);

        Front front = new Front("front", null, Color.RED, null, null);
        Back back = new Back("back", null, Color.RED, null);
        PlayableCard card = new GoldCard(1, front, back);

        PlayableCard c = new ResourceCard(2, front, back);

        for(int i = 0; i < 38; i++){
            game.chosenCardToAddInHand(c);
        }
        cards = game.getPickableCards();
        assertEquals(6, cards.size());
        resource = 0;
        gold = 0;
        rNotVisible = 0;
        gNotVisible = 0;

        for( PlayableCard cd : cards) {
            assertNotNull(cd);
            if(cd instanceof ResourceCard && cd.getVisibility())
                resource++;
            else if (cd instanceof GoldCard && cd.getVisibility())
                gold++;
            else if (cd instanceof ResourceCard && !cd.getVisibility())
                rNotVisible++;
            else if (cd instanceof GoldCard && !cd.getVisibility())
                gNotVisible++;
            assertTrue(cd instanceof ResourceCard || cd instanceof GoldCard);
        }
        assertEquals(2, resource);
        assertEquals(2, gold);
        assertEquals(0, rNotVisible);
        assertEquals(2, gNotVisible);

        for(int i = 0; i < 38; i++){
            game.chosenCardToAddInHand(card);
        }
        cards = game.getPickableCards();
        assertEquals(4, cards.size());
        for( PlayableCard s : cards) {
            assertNotNull(s);
            if(s instanceof ResourceCard && s.getVisibility())
                resource++;
            else if (s instanceof GoldCard && s.getVisibility())
                gold++;
            else if (s instanceof ResourceCard && !s.getVisibility())
                rNotVisible++;
            else if (s instanceof GoldCard && !s.getVisibility())
                gNotVisible++;
            assertTrue(s instanceof ResourceCard || s instanceof GoldCard);
        }

        assertEquals(2, resource);
        assertEquals(2, gold);
        assertEquals(0, rNotVisible);
        assertEquals(0, gNotVisible);
    }

    @org.junit.jupiter.api.Test
    void chosenCardToAddInHand(){
        //TODO
    }

    @org.junit.jupiter.api.Test
    void getGoals(){
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();

        game.initializeGameForPlayers();
        List<GoalCard> goals = game.getGoals();
        assertEquals(2, goals.size());
        assertFalse(goals.contains(null));
        for(GoalCard g : goals) {
            assertNotNull(g);
            assertInstanceOf(GoalCard.class, g);
        }
    }

    @org.junit.jupiter.api.Test
    void getStandings() {
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();

        try {
            game.addToGame("Rodri");
            game.addToGame("Matti");
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        game.getPlayers().getFirst().addPoints(10);
        game.getPlayers().getLast().addPoints(15);

        List<Player> standings = game.getStandings();

        assertEquals(2, standings.size());
        assertTrue(standings.get(0).getPoints() > standings.get(1).getPoints());
        assertEquals("Matti", standings.get(0).getNickname());
        assertEquals("Rodri", standings.get(1).getNickname());
        assertEquals(15, standings.get(0).getPoints());
        assertEquals(10, standings.get(1).getPoints());
    }

    @org.junit.jupiter.api.Test
    void choosePersonalGoal() {
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();

        game.initializeGameForPlayers();
        List<GoalCard> goals = game.choosePersonalGoal();
        assertEquals(2, goals.size());
        assertFalse(goals.contains(null));
        for(GoalCard g : goals) {
            assertNotNull(g);
            assertInstanceOf(GoalCard.class, g);
        }
    }

    @org.junit.jupiter.api.Test
    void initializeGameForPlayers() {
        Game game = null;
        try {
            game = new Game(2);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();

        try {
            game.addToGame("Rodri");
            game.addToGame("Matti");
        } catch (it.polimi.ingsw.am42.model.exceptions.GameFullException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException |
                 it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        int gold = 0;
        int resource = 0;

        game.initializeGameForPlayers();
        List<Player> players = game.getPlayers();
        for (Player p : players) {
            assertEquals(0, p.getPoints());
            assertEquals(0, p.getGoalsAchieved());
            for (PlayableCard card : p.getHand()) {
                assertNotNull(card);
                if (card instanceof ResourceCard) {
                    resource++;
                } else gold++;
                assertEquals(2, resource);
                assertEquals(1, gold);
            }
        }
        assertEquals(2, game.getGoals().size());
        for(GoalCard g : game.getGoals()) {
            assertNotNull(g);
            assertInstanceOf(GoalCard.class, g);
        }
    }

}

