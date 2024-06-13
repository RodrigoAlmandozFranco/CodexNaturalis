package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.GoldCard;
import it.polimi.ingsw.am42.model.cards.types.playables.ResourceCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;
import it.polimi.ingsw.am42.model.evaluator.EvaluatorPoints;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @org.junit.jupiter.api.Test
    void initializeGame() {
        Game game = null;
        try {
            game = new Game(2);
        } catch (NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeDecks();
        try{
            try {
                game.addToGame("Rodri");
                game.addToGame("Matti");
            } catch (GameFullException |
                     NicknameAlreadyInUseException |
                     NicknameInvalidException e) {
                throw new RuntimeException(e);
            }
            game.initializeGame();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, game.getPlayers().size());

        List<PlayableCard> pr = game.getPickableResourceCards();
        List<PlayableCard> pg = game.getPickableGoldCards();
        List<GoalCard> gg = game.getGoals();
        List<PlayableCard> cards = game.getPickableCards();
        assertEquals(6, cards.size());
        assertFalse(cards.contains(null));
        int resource = 0;
        int gold = 0;
        for(PlayableCard c : cards) {
            assertNotNull(c);
            if(pr.contains(c) || pg.contains(c))
                assertTrue(c.getVisibility());
            else assertFalse(c.getVisibility());

            if(c instanceof ResourceCard)
                resource++;
            else gold++;

            assertTrue(c instanceof ResourceCard || c instanceof GoldCard);
        }
        assertEquals(3, resource);
        assertEquals(3, gold);
    }

    @org.junit.jupiter.api.Test
    void setCurrentState() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        game.setCurrentState(State.SETHAND);
        assertEquals(State.SETHAND, game.getCurrentState());
    }

    @org.junit.jupiter.api.Test
    void getCurrentState() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        State state = game.getCurrentState();
        assertNotNull(state);
        assertEquals(State.INITIAL, state);
    }

    @org.junit.jupiter.api.Test
    void changeState() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Tommy");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        State state = game.getCurrentState();
        assertNotNull(state);
        assertEquals(State.INITIAL, state);
        game.changeState();
        assertEquals(State.SETHAND, game.getCurrentState());
        assertNotEquals(State.INITIAL, game.getCurrentState());
    }

    @org.junit.jupiter.api.Test
    void checkEndGamePoints() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
        } catch (NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }

        try{
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
            assertFalse(game.checkEndGamePoints());
            game.getPlayers().getFirst().addPoints(10);
            assertFalse(game.checkEndGamePoints());
            game.getPlayers().getFirst().addPoints(11);
            assertTrue(game.checkEndGamePoints());

        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void checkEndGameDecks(){
        Game game = null;
        try {
            game = new Game(2);
        } catch (NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        try{
            game.addToGame("Rodri");
            game.addToGame("Matti");
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        game.initializeGame();

        assertFalse(game.checkEndGameDecks());

        PlayableCard s = game.getFirstGoldCard();

        Front front = new Front("front", s.getFront().getCorners(), s.getFront().getColor(), s.getFront().getRequirements(), s.getFront().getEvaluator());
        Back back = new Back("back", s.getBack().getCorners(), s.getBack().getColor(), s.getBack().getListResource());
        PlayableCard card = new GoldCard(150, front, back);
        card.setVisibility(false);
        PlayableCard c = new ResourceCard(150, front, back);
        c.setVisibility(false);

        for(int i = 0; i < 38; i++){
            game.chosenCardToAddInHand(card);
            card.setVisibility(false);
        }
        assertFalse(game.checkEndGameDecks());
        for(int i = 0; i < 38; i++){
            game.chosenCardToAddInHand(c);
            c.setVisibility(false);
        }
        assertTrue(game.checkEndGameDecks());
    }

    @org.junit.jupiter.api.Test
    void getWinner(){
        Game game = null;
        try {
            game = new Game(2);
        } catch (NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        game.initializeDecks();
        try{
            try {
                game.addToGame("Rodri");
                game.addToGame("Matti");
            } catch (GameFullException |
                     NicknameAlreadyInUseException |
                     NicknameInvalidException e) {
                throw new RuntimeException(e);
            }
            game.initializeGame();

            game.getPlayers().getFirst().addPoints(10);
            game.getPlayers().getFirst().setGoal(new GoalCard(1, ",", new EvaluatorPoints(0)));
            game.getPlayers().getLast().addPoints(1);
            game.getPlayers().getLast().setGoal(new GoalCard(1, ",", new EvaluatorPoints(0)));

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
        } catch (NumberPlayerWrongException e) {
            assertInstanceOf(NumberPlayerWrongException.class, e);
        }
        assertNull(game);

        try {
            game = new Game(-6);
        } catch (NumberPlayerWrongException e) {
            assertInstanceOf(NumberPlayerWrongException.class, e);
        }
        assertNull(game);

        try {
            game = new Game(2);
            game.initializeDecks();
        } catch (NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(game);


        try{
            game.addToGame("");
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            assertInstanceOf(NicknameInvalidException.class, e);
        }
        try{
            game.addToGame("    ");
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            assertInstanceOf(NicknameInvalidException.class, e);
        }

        assertEquals(0, game.getPlayers().size());

        try{
            game.addToGame("Rodri");
            assertEquals(1, game.getPlayers().size());
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        try{
            game.addToGame("Rodri");
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            assertInstanceOf(NicknameAlreadyInUseException.class, e);
        }assertEquals(1, game.getPlayers().size());

        try{
            game.addToGame("Matti");
            assertEquals(2, game.getPlayers().size());
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        try{
            game.addToGame("Tommi");
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            assertInstanceOf(GameFullException.class, e);
        }
        game.initializeGame();
        assertEquals(2, game.getPlayers().size());
    }

    @org.junit.jupiter.api.Test
    void getPlayers() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
        } catch (NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }

        try{
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        List<Player> players = game.getPlayers();
        assertEquals(2, players.size());
    }

    @org.junit.jupiter.api.Test
    void getCurrentPlayer() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
        } catch (NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }

        try{
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        assertEquals(game.getCurrentPlayer(), game.getPlayers().getFirst());
    }

    @org.junit.jupiter.api.Test
    void setCurrentPlayer() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
        } catch (NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }

        try{
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        assertEquals(game.getCurrentPlayer(), game.getPlayers().getFirst());
        game.setCurrentPlayer(game.getPlayers().getLast());
        assertEquals(game.getCurrentPlayer(), game.getPlayers().getLast());
    }

    @org.junit.jupiter.api.Test
    void getNextPlayer() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
        } catch (NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }

        try{
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (GameFullException |
                 NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }

        assertEquals(game.getCurrentPlayer(), game.getPlayers().getFirst());
        game.setCurrentPlayer(game.getNextPlayer());
        assertEquals(game.getCurrentPlayer(), game.getPlayers().getLast());
    }

    @org.junit.jupiter.api.Test
    void getNumberPlayers() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(game.getPlayers());
        assertEquals(2, game.getNumberPlayers());
        assertEquals(2, game.getPlayers().size());
    }

    @org.junit.jupiter.api.Test
    void getPickableCards() throws GameFullException, NicknameAlreadyInUseException, NicknameInvalidException {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        List<PlayableCard> pr = game.getPickableResourceCards();
        List<PlayableCard> pg = game.getPickableGoldCards();
        List<GoalCard> gg = game.getGoals();
        List<PlayableCard> cards = game.getPickableCards();
        assertEquals(6, cards.size());
        assertFalse(cards.contains(null));
        int resource = 0;
        int gold = 0;
        for(PlayableCard c : cards) {
            assertNotNull(c);
            if(pr.contains(c) || pg.contains(c))
                assertTrue(c.getVisibility());
            else assertFalse(c.getVisibility());

            if(c instanceof ResourceCard)
                resource++;
            else gold++;

            assertTrue(c instanceof ResourceCard || c instanceof GoldCard);
        }
        assertEquals(3, resource);
        assertEquals(3, gold);
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
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | NicknameInvalidException | NicknameAlreadyInUseException |
                 GameFullException e) {
            throw new RuntimeException(e);
        }

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
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | NicknameInvalidException | NicknameAlreadyInUseException |
                 GameFullException e) {
            throw new RuntimeException(e);
        }

        Player p1 = game.getPlayers().getFirst();
        p1.addPoints(10);
        Player p2 = game.getPlayers().getLast();
        p2.addPoints(15);

        List<Player> standings = game.getStandings();

        assertEquals(2, standings.size());
        assertTrue(standings.get(0).getPoints() > standings.get(1).getPoints());
        assertEquals(p2.getNickname(), standings.get(0).getNickname());
        assertEquals(p1.getNickname(), standings.get(1).getNickname());
        assertEquals(15, standings.get(0).getPoints());
        assertEquals(10, standings.get(1).getPoints());
    }

    @org.junit.jupiter.api.Test
    void choosePersonalGoal() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | NicknameInvalidException | NicknameAlreadyInUseException |
                 GameFullException e) {
            throw new RuntimeException(e);
        }

        List<GoalCard> goals = game.choosePersonalGoal();
        assertEquals(2, goals.size());
        assertFalse(goals.contains(null));
        for(GoalCard g : goals) {
            assertNotNull(g);
            assertInstanceOf(GoalCard.class, g);
        }
    }

    @org.junit.jupiter.api.Test
    void setTurnFinal() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        boolean b = game.getTurnFinal();
        assertFalse(b);
        game.setTurnFinal(true);
        assertTrue(game.getTurnFinal());
    }

    @org.junit.jupiter.api.Test
    void getTurnFinal() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        boolean b = game.getTurnFinal();
        assertFalse(b);
        game.setTurnFinal(true);
        assertTrue(game.getTurnFinal());
    }


    @org.junit.jupiter.api.Test
    void initializeHandCurrentPlayer(){
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        game.initializeHandCurrentPlayer();
        assertEquals(3, game.getCurrentPlayer().getHand().size());
        assertNotNull(game.getCurrentPlayer().getHand());
        int resource = 0, gold = 0;
        for(PlayableCard c : game.getCurrentPlayer().getHand()){
            assertNotNull(c);
            if(c instanceof ResourceCard)
                resource++;
            else gold++;
        }

        assertEquals(2, resource);
        assertEquals(1, gold);
    }

    @org.junit.jupiter.api.Test
    void getPickableResourceCards() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        List<PlayableCard> cards = game.getPickableResourceCards();
        assertNotNull(cards);
        for(PlayableCard card : cards){
            assertNotNull(card);
            assertInstanceOf(ResourceCard.class, card);
        }
        assertEquals(2, cards.size());
    }

    @org.junit.jupiter.api.Test
    void getPickableGoldCards() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        List<PlayableCard> cards = game.getPickableGoldCards();
        assertNotNull(cards);
        for(PlayableCard card : cards){
            assertNotNull(card);
            assertInstanceOf(GoldCard.class, card);
        }
        assertEquals(2, cards.size());
    }

    @org.junit.jupiter.api.Test
    void getFirstResourceCard() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        PlayableCard card = game.getFirstResourceCard();
        assertNotNull(card);
        assertInstanceOf(ResourceCard.class, card);
    }

    @org.junit.jupiter.api.Test
    void getFirstGoldCard() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        PlayableCard card = game.getFirstGoldCard();
        assertNotNull(card);
        assertInstanceOf(GoldCard.class, card);
    }

    @org.junit.jupiter.api.Test
    void getAvailableColors() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        List<PlayersColor> colors = game.getAvailableColors();
        assertNotNull(colors);
        assertEquals(4, colors.size());
        for(PlayersColor c : colors){
            assertNotNull(c);
        }
    }

    @org.junit.jupiter.api.Test
    void removeColor() {
        Game game = null;
        try {
            game = new Game(2);
            game.initializeDecks();
            game.addToGame("Rodri");
            game.addToGame("Matti");
            game.initializeGame();
        } catch (NumberPlayerWrongException | GameFullException | NicknameAlreadyInUseException |
                 NicknameInvalidException e) {
            throw new RuntimeException(e);
        }
        List<PlayersColor> colors = game.getAvailableColors();
        assertNotNull(colors);
        assertEquals(4, colors.size());
        PlayersColor c = colors.getFirst();
        game.removeColor(c);
        for(PlayersColor cc : colors){
            assertNotNull(cc);
        }
        assertEquals(3, colors.size());
        assertFalse(colors.contains(c));
    }


}

