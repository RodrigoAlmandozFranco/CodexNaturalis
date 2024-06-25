package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.*;
import it.polimi.ingsw.am42.model.cards.types.playables.GoldCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.evaluator.EvaluatorPoints;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private Change change;
    private Controller controller;
    MessageListener messageListener1, messageListener2;
    List<Player> players;
    private ChatMessage message;

    @BeforeEach
    void setup() {
        File file = new File(System.getProperty("user.dir"), "it/polimi/ingsw/gamePersistence/game.dat");
        file.delete();

        controller = new Controller();
        messageListener1 = new MessageListener() {
            @Override
            public String getId() throws RemoteException {
                return "Rodri";
            }

            @Override
            public void update(Change diff) throws RemoteException {
                change = diff;
                if(change.getPlayers() != null) {
                    players = change.getPlayers();
                }
            }

            @Override
            public void receiveMessage(Message m) throws RemoteException {
                assertNotNull(m);
                message = (ChatMessage) m;
            }

            @Override
            public boolean heartbeat() throws RemoteException {
                return false;
            }
        };
        messageListener2 = new MessageListener() {
            @Override
            public String getId() throws RemoteException {
                return "Matti";
            }

            @Override
            public void update(Change diff) throws RemoteException {
                assertNotNull(diff);
                //change = diff;
                if(change.getPlayers() != null) {
                    players = new ArrayList<>(change.getPlayers());
                }
            }

            @Override
            public void receiveMessage(Message message) throws RemoteException {
                assertNotNull(message);
            }

            @Override
            public boolean heartbeat() throws RemoteException {
                return true;
            }
        };
    }

    @Test
    void setupGameTest(){
        assertEquals(controller.getGameInfo(), ConnectionState.CREATE);
        try {
            controller.createGame(messageListener1, "Rodri", 2);
        } catch (NumberPlayerWrongException | GameFullException | NicknameInvalidException | NicknameAlreadyInUseException |
                 GameAlreadyCreatedException e) {
            e.printStackTrace();
        }
        assertEquals(controller.getGameInfo(), ConnectionState.CONNECT);
        try {
            controller.connect(messageListener2, "Matti");
        } catch (NicknameInvalidException | NicknameAlreadyInUseException | GameFullException e) {
            e.printStackTrace();
        }

        controller = null;
        controller = new Controller();

        assertEquals(controller.getGameInfo(), ConnectionState.LOAD);
        try {
            controller.reconnect(messageListener1, "Rodri");
        } catch (GameFullException | NicknameInvalidException | NicknameAlreadyInUseException |
                 GameAlreadyCreatedException e) {
            e.printStackTrace();
        }
        assertEquals(controller.getGameInfo(), ConnectionState.LOADING);
        try {
            controller.reconnect(null, "Tommy");
        } catch (GameFullException | NicknameInvalidException | NicknameAlreadyInUseException |
                 GameAlreadyCreatedException e) {
            assertInstanceOf(NicknameInvalidException.class, e);
        }
        try {
            controller.reconnect(messageListener2, "Matti");
        } catch (GameFullException | NicknameInvalidException | NicknameAlreadyInUseException |
                 GameAlreadyCreatedException e) {
            e.printStackTrace();
        }
    }


    @Test
    void setupCurrentPlayerTest() {
        try {
            controller.createGame(messageListener1, "Rodri", 2);
            controller.connect(messageListener2, "Matti");
        } catch (NumberPlayerWrongException | GameFullException | NicknameInvalidException |
                 NicknameAlreadyInUseException | GameAlreadyCreatedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 2; i++) {
            int id = players.get(i).getHand().getFirst().getId();
            List<PlayersColor> colors = null;
            if(i == 0){
                try {
                    colors = controller.placeStarting(players.getLast().getNickname(), players.get(i).getHand().getFirst().getFront());
                } catch (WrongTurnException e) {
                    assertInstanceOf(WrongTurnException.class, e);
                }
            }

            try {
                colors = controller.placeStarting(players.get(i).getNickname(), players.get(i).getHand().getFirst().getFront());
            } catch (WrongTurnException e) {
                e.printStackTrace();
            }
            List<PlayersColor> copyColors = new ArrayList<>(colors);
            assertNotNull(colors);
            assertEquals(change.getLastPlacedFace().getId(), id);
            List<GoalCard> goals = null;
            if(i == 0){
                try {
                    goals = controller.chooseColor(players.getLast().getNickname(), colors.getFirst());
                } catch (WrongTurnException e) {
                    assertInstanceOf(WrongTurnException.class, e);
                }
            }
            try {
                goals = controller.chooseColor(players.get(i).getNickname(), colors.getFirst());
            } catch (WrongTurnException e) {
                e.printStackTrace();
            }
            assertNotNull(goals);
            if(i == 0){
                try {
                    controller.chooseGoal(players.getLast().getNickname(), goals.getFirst());
                } catch (WrongTurnException e) {
                    assertInstanceOf(WrongTurnException.class, e);
                }
            }
            try {
                controller.chooseGoal(players.get(i).getNickname(), goals.getFirst());
            } catch (WrongTurnException e) {
                e.printStackTrace();
            }

            for (Player player : players) {
                if (player.getNickname().equals(change.getCurrentPlayer())) {
                    assertEquals(player.getColor(), copyColors.getFirst());
                    assertEquals(player.getPersonalGoal().getId(), goals.getFirst().getId());
                }
            }
        }

        int points = 20;


        try{
            for(int i = 0; i < 4; i++) {
                int j = i % 2;

                String player = players.get(j).getNickname();
                if(i == 0){
                    try {
                        controller.getAvailablePositions(players.getLast().getNickname());
                    } catch (WrongTurnException e) {
                        assertInstanceOf(WrongTurnException.class, e);
                    }
                }
                Set<Position> availablePos = controller.getAvailablePositions(player);
                assertNotNull(availablePos);

                Front f = players.get(j).getHand().getLast().getFront();



                Front front = new Front(f.getSrcImage(), f.getCorners(), Color.CYAN, new HashMap<>(), new EvaluatorPoints(points++));
                front.setId(150);

                if(i == 0){
                    try {
                        controller.place(players.getLast().getNickname(), front, availablePos.iterator().next());
                    } catch (WrongTurnException e) {
                        assertInstanceOf(WrongTurnException.class, e);
                    }
                }
                controller.place(player, front, availablePos.iterator().next());
                assertEquals(change.getLastPlacedFace().getId(), 150);
                int id2 = change.getFirstGoldCard().getId();
                if(!change.isTurnFinal()){
                    if(i == 0){
                        try {
                            controller.pick(players.getLast().getNickname(), change.getFirstGoldCard());
                        } catch (WrongTurnException e) {
                            assertInstanceOf(WrongTurnException.class, e);
                        }
                    }
                    controller.pick(player, change.getFirstGoldCard());
                    boolean ok = false;
                    for(PlayableCard card : change.getHand())
                        if (card.getId() == id2) {
                            ok = true;
                            break;
                        }
                    assertTrue(ok);
                }
                if(i == 0){
                    try {
                        controller.getWinner();
                    } catch (WrongTurnException e) {
                        assertInstanceOf(WrongTurnException.class, e);
                    }
                }
                if(i == 2)
                    assertTrue(change.isTurnFinal());
            }

            assertEquals(controller.getWinner().size(), 1);
            assertEquals(controller.getWinner().getFirst().getNickname(), players.getLast().getNickname());
        } catch (RequirementsNotMetException e) {
            e.printStackTrace();
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sendMessageTest(){
        try {
            controller.createGame(messageListener1, "Rodri", 2);
            controller.connect(messageListener2, "Matti");
        } catch (NumberPlayerWrongException | GameFullException | NicknameInvalidException |
                 NicknameAlreadyInUseException | GameAlreadyCreatedException e) {
            e.printStackTrace();
        }

        ChatMessage chatMessage = new ChatMessage("Hello", "Matti", "Rodri");
        controller.sendChatMessage(chatMessage);
        assertEquals("Hello", message.getMessage());
        assertEquals("Matti", message.getSender());
        assertEquals("Rodri", message.getReceiver());
        chatMessage = new ChatMessage("Hello", "Rodri");
        controller.sendChatMessage(chatMessage);
        assertEquals("Hello", message.getMessage());
        assertEquals("Rodri", message.getSender());
        assertEquals("all", message.getReceiver());
    }
}
