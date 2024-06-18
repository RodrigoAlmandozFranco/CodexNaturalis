package it.polimi.ingsw.am42.controller.gameDB;

import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDBTest {
    Game game = null;


    @BeforeEach
    void setup() {
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
    }

    @Test
    void saveGameTest() {
        GameDB gameDB = new GameDB();
        gameDB.setGame(game);
        assertNotNull(gameDB.saveGame(true));
    }


    @Test
    void afterLoadTest() {
        GameDB gameDB = new GameDB();
        gameDB.setGame(game);
        assertNotNull(gameDB.saveGame(false));
        assertNotNull(gameDB.afterLoad());
    }

    @Test
    void loadGameTest() {
        GameDB gameDB = new GameDB();
        gameDB.setGame(game);
        assertNotNull(gameDB.saveGame(true));
        Game game2 = gameDB.loadGame();
        assertNotNull(game2);

        for(int i = 0; i < game.getNumberPlayers(); i++) {
            assertEquals(game.getPlayers().get(i).getNickname(), game2.getPlayers().get(i).getNickname());
            assertEquals(game.getPlayers().get(i).getPoints(), game2.getPlayers().get(i).getPoints());
            assertEquals(game.getPlayers().get(i).getGoalsAchieved(), game2.getPlayers().get(i).getGoalsAchieved());
            assertEquals(game.getPlayers().get(i).getHand().size(), game2.getPlayers().get(i).getHand().size());
        }

        assertEquals(game.getPickableGoldCards().size(), game2.getPickableGoldCards().size());
        assertEquals(game.getPickableResourceCards().size(), game2.getPickableResourceCards().size());
        assertEquals(game.getFirstGoldCard().getId(), game2.getFirstGoldCard().getId());
        assertEquals(game.getFirstResourceCard().getId(), game2.getFirstResourceCard().getId());
        assertEquals(game.getCurrentState(), game2.getCurrentState());
        assertEquals(game.getCurrentPlayer().getNickname(), game2.getCurrentPlayer().getNickname());
        assertEquals(game.getPlayers().size(), game2.getPlayers().size());
        assertEquals(game.getNumberPlayers(), game2.getNumberPlayers());
    }
}
