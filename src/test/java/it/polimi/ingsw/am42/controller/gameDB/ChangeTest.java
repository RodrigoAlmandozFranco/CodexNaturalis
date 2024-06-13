package it.polimi.ingsw.am42.controller.gameDB;

import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException;
import it.polimi.ingsw.am42.model.structure.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangeTest {

    @Test
    void changeTest() {
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

        Change change = new Change(game, false);

        assertFalse(change.isGameStarted());

        assertEquals(change.getPointsPlayer(), game.getCurrentPlayer().getPoints());
        assertEquals(change.getNumberGoalsAchieved(), game.getCurrentPlayer().getGoalsAchieved());
        assertEquals(change.getCurrentPlayer(), game.getCurrentPlayer().getNickname());
        assertEquals(change.getFuturePlayer(), game.getCurrentPlayer().getNickname());

        for(int i = 0; i < change.getHand().size(); i++)
            assertEquals(change.getHand().get(i).getId(), game.getCurrentPlayer().getHand().get(i).getId());

        if (game.getCurrentPlayer().getBoard().getLastPlacedFace() != null)
            assertEquals(change.getLastPlacedFace().getId(), game.getCurrentPlayer().getBoard().getLastPlacedFace().getId());
        else
            assertNull(change.getLastPlacedFace());

        assertEquals(change.getFirstResourceCard().getId(), game.getFirstResourceCard().getId());
        assertEquals(change.getFirstGoldCard().getId(), game.getFirstGoldCard().getId());

        for(int i = 0; i < change.getPickableResourceCards().size(); i++)
            assertEquals(change.getPickableResourceCards().get(i).getId(), game.getPickableResourceCards().get(i).getId());

        for(int i = 0; i < change.getPickableGoldCards().size(); i++)
            assertEquals(change.getPickableGoldCards().get(i).getId(), game.getPickableGoldCards().get(i).getId());


        assertEquals(change.getCurrentState(), game.getCurrentState());

        if(game.getCurrentPlayer().getColor() != null)
            assertEquals(change.getColor(), game.getCurrentPlayer().getColor());
        else
            assertNull(change.getColor());

        assertNotNull(change.getPlayers());
        assertNotNull(change.getGlobalGoals());
        assertEquals(change.getNumberPlayers(), game.getNumberPlayers());
        assertEquals(change.isTurnFinal(), game.getTurnFinal());

        for(int i = 0; i < change.getPlayers().size(); i++)
            assertEquals(change.getPlayers().get(i).getNickname(), game.getPlayers().get(i).getNickname());

        for(int i = 0; i < change.getGlobalGoals().size(); i++)
            assertEquals(change.getGlobalGoals().get(i).getId(), game.getGoals().get(i).getId());


        game.getCurrentPlayer().placeCard(new Position(0,3), game.getCurrentPlayer().getHand().getFirst().getFront());

        change = new Change(game, true);

        assertEquals(change.getPointsPlayer(), game.getCurrentPlayer().getPoints());
        assertEquals(change.getNumberGoalsAchieved(), game.getCurrentPlayer().getGoalsAchieved());
        assertEquals(change.getCurrentPlayer(), game.getCurrentPlayer().getNickname());
        assertEquals(change.getFuturePlayer(), game.getCurrentPlayer().getNickname());

        for(int i = 0; i < change.getHand().size(); i++)
            assertEquals(change.getHand().get(i).getId(), game.getCurrentPlayer().getHand().get(i).getId());

        if (game.getCurrentPlayer().getBoard().getLastPlacedFace() != null)
            assertEquals(change.getLastPlacedFace().getId(), game.getCurrentPlayer().getBoard().getLastPlacedFace().getId());
        else
            assertNull(change.getLastPlacedFace());

        assertEquals(change.getFirstResourceCard().getId(), game.getFirstResourceCard().getId());
        assertEquals(change.getFirstGoldCard().getId(), game.getFirstGoldCard().getId());

        for(int i = 0; i < change.getPickableResourceCards().size(); i++)
            assertEquals(change.getPickableResourceCards().get(i).getId(), game.getPickableResourceCards().get(i).getId());

        for(int i = 0; i < change.getPickableGoldCards().size(); i++)
            assertEquals(change.getPickableGoldCards().get(i).getId(), game.getPickableGoldCards().get(i).getId());


        assertEquals(change.getCurrentState(), game.getCurrentState());

        if(game.getCurrentPlayer().getColor() != null)
            assertEquals(change.getColor(), game.getCurrentPlayer().getColor());
        else
            assertNull(change.getColor());

        assertNull(change.getPlayers());
        assertNull(change.getGlobalGoals());
        assertEquals(change.getNumberPlayers(), 0);
        assertEquals(change.isTurnFinal(), game.getTurnFinal());


        game.getCurrentPlayer().placeCard(new Position(0, 4), game.getFirstGoldCard().getBack());
        change = new Change(game, true);

        assertEquals(game.getFirstGoldCard().getId(), change.getLastPlacedFace().getId());

        game.getCurrentPlayer().placeCard(new Position(0, 4), game.getFirstResourceCard().getFront());
        change = new Change(game, true);

        assertEquals(game.getFirstResourceCard().getId(), change.getLastPlacedFace().getId());



        change = new Change(game, true);
        game.setCurrentPlayer(game.getPlayers().getLast());
        change.setFuturePlayer(game.getCurrentPlayer().getNickname());
        assertEquals(change.getFuturePlayer(), game.getCurrentPlayer().getNickname());
    }
}
