package it.polimi.ingsw.am42.model.enumeration;


import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;


import it.polimi.ingsw.am42.model.GameInterface;
import it.polimi.ingsw.am42.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.quality.MockitoHint;

import java.util.LinkedList;

class StateTest {

    @Mock
    GameInterface game;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangeState_INITIAL_NotEnoughPlayers() {
        when(game.getPlayers()).thenReturn(new LinkedList<>());
        when(game.getNumberPlayers()).thenReturn(4);

        State initialState = State.INITIAL;
        State newState = initialState.changeState(game);

        assertEquals(State.INITIAL, newState);
    }

    @Test
    void testChangeState_INITIAL_EnoughPlayers() {
        LinkedList<Player> players = new LinkedList<>();
        Player mockPlayer = Mockito.mock(Player.class);
        players.add(mockPlayer);
        players.add(mockPlayer);
        players.add(mockPlayer);
        players.add(mockPlayer);

        when(game.getPlayers()).thenReturn(players);
        when(game.getNumberPlayers()).thenReturn(4);

        State initialState = State.INITIAL;
        State newState = initialState.changeState(game);

        verify(game, times(1)).initializeGame();
        assertEquals(State.SETHAND, newState);
    }

    @Test
    void testChangeState_SETHAND() {
        State setHandState = State.SETHAND;
        State newState = setHandState.changeState(game);

        assertEquals(State.SETCOLOR, newState);
    }

    @Test
    void testChangeState_SETCOLOR() {
        State setColorState = State.SETCOLOR;
        State newState = setColorState.changeState(game);

        verify(game, times(1)).initializeHandCurrentPlayer();
        assertEquals(State.SETGOAL, newState);
    }

    @Test
    void testChangeState_SETGOAL_NotLastPlayer() {
        Player mockPlayer = Mockito.mock(Player.class);
        Player mockPlayer_2 = Mockito.mock(Player.class);
        Player currentPlayer = mockPlayer;
        LinkedList<Player> players = new LinkedList<>();
        players.add(currentPlayer);
        players.add(mockPlayer_2);

        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(game.getPlayers()).thenReturn(players);

        State setGoalState = State.SETGOAL;
        State newState = setGoalState.changeState(game);

        assertEquals(State.SETHAND, newState);
    }

    @Test
    void testChangeState_SETGOAL_LastPlayer() {
        Player mockPlayer = Mockito.mock(Player.class);
        Player lastPlayer = mockPlayer;
        LinkedList<Player> players = new LinkedList<>();
        players.add(mockPlayer);
        players.add(lastPlayer);

        when(game.getCurrentPlayer()).thenReturn(lastPlayer);
        when(game.getPlayers()).thenReturn(players);

        State setGoalState = State.SETGOAL;
        State newState = setGoalState.changeState(game);

        assertEquals(State.PLACE, newState);
    }

    @Test
    void testChangeState_PLACE_TurnNotFinal_NotLastPlayer() {
        Player mockPlayer = Mockito.mock(Player.class);
        Player currentPlayer = mockPlayer;
        LinkedList<Player> players = new LinkedList<>();
        players.add(currentPlayer);
        players.add(mockPlayer);

        when(game.getTurnFinal()).thenReturn(false);
        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(game.getPlayers()).thenReturn(players);

        State placeState = State.PLACE;
        State newState = placeState.changeState(game);

        assertEquals(State.PICK, newState);
    }

    @Test
    void testChangeState_PLACE_TurnFinal_NotLastPlayer() {
        Player mockPlayer = Mockito.mock(Player.class);
        Player mockPlayer_1 = Mockito.mock(Player.class);
        Player currentPlayer = mockPlayer;
        LinkedList<Player> players = new LinkedList<>();
        players.add(currentPlayer);
        players.add(mockPlayer_1);

        when(game.getTurnFinal()).thenReturn(true);
        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(game.getPlayers()).thenReturn(players);

        State placeState = State.PLACE;
        State newState = placeState.changeState(game);

        assertEquals(State.PLACE, newState);
    }

    @Test
    void testChangeState_PLACE_TurnFinal_LastPlayer() {
        Player mockPlayer = Mockito.mock(Player.class);
        Player lastPlayer = new Player("ALE");
        LinkedList<Player> players = new LinkedList<>();
        players.add(mockPlayer);
        players.add(lastPlayer);

        when(game.getTurnFinal()).thenReturn(true);
        when(game.getCurrentPlayer()).thenReturn(lastPlayer);
        when(game.getPlayers()).thenReturn(players);

        State placeState = State.PLACE;
        State newState = placeState.changeState(game);

        assertEquals(State.LAST, newState);
    }

    @Test
    void testChangeState_PICK_NotLastPlayer() {
        Player mockPlayer = Mockito.mock(Player.class);
        Player currentPlayer = mockPlayer;
        LinkedList<Player> players = new LinkedList<>();
        players.add(currentPlayer);
        players.add(mockPlayer);

        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(game.getPlayers()).thenReturn(players);

        State pickState = State.PICK;
        State newState = pickState.changeState(game);

        assertEquals(State.PLACE, newState);
    }

    @Test
    void testChangeState_PICK_LastPlayer_EndGameDecks() {
        Player mockPlayer = Mockito.mock(Player.class);
        Player lastPlayer = mockPlayer;
        LinkedList<Player> players = new LinkedList<>();
        players.add(mockPlayer);
        players.add(lastPlayer);

        when(game.getCurrentPlayer()).thenReturn(lastPlayer);
        when(game.getPlayers()).thenReturn(players);
        when(game.checkEndGameDecks()).thenReturn(true);
        when(game.checkEndGamePoints()).thenReturn(false);

        State pickState = State.PICK;
        State newState = pickState.changeState(game);

        verify(game, times(1)).setTurnFinal(true);
        assertEquals(State.PLACE, newState);
    }

    @Test
    void testChangeState_PICK_LastPlayer_EndGamePoints() {
        Player mockPlayer = Mockito.mock(Player.class);
        Player lastPlayer = mockPlayer;
        LinkedList<Player> players = new LinkedList<>();
        players.add(mockPlayer);
        players.add(lastPlayer);

        when(game.getCurrentPlayer()).thenReturn(lastPlayer);
        when(game.getPlayers()).thenReturn(players);
        when(game.checkEndGameDecks()).thenReturn(false);
        when(game.checkEndGamePoints()).thenReturn(true);

        State pickState = State.PICK;
        State newState = pickState.changeState(game);

        verify(game, times(1)).setTurnFinal(true);
        assertEquals(State.PLACE, newState);
    }

    @Test
    void testChangeState_LAST() {
        State lastState = State.LAST;
        State newState = lastState.changeState(game);

        assertEquals(State.LAST, newState);
    }

    @Test
    void testChangeState_DISCONNECTED() {
        State disconnectedState = State.DISCONNECTED;
        State newState = disconnectedState.changeState(game);

        assertEquals(State.DISCONNECTED, newState);
    }
}
