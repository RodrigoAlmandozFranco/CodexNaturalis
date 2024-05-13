package it.polimi.ingsw.am42.network.tcp.client;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.clientToServer.*;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;



public class ClientTCP extends Client {

    private static String ip = "";
    private static int port;
    private ServerHandler serverHandler;
    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientTCP(String ip, int port) throws IOException {
        ClientTCP.ip = ip;
        ClientTCP.port = port;
        socket = new Socket(ip, port);


        Thread thread = new Thread(() -> {
                try {
                    startClient();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        );
        thread.start();
    }

    public void startClient() throws IOException {
        serverHandler = new ServerHandler(socket, this);

        try {
            //System.out.println("ClientTCP ready!");
            new Thread(serverHandler).start();
            //while (true) {}
        } catch (final NoSuchElementException e) {
            //input.close();
            output.close();
        }
}

    private void sendMessage(Message message) {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(message);
            output.flush();
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public ConnectionState getGameInfo() {
        return null;
    }

    @Override
    public int createGame(MessageListener l, String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException {
        Message message = new FirstConnectionMessage(nickname, numPlayers);
        sendMessage(message);
        Message answer = serverHandler.getMessage();
        return switch (answer) {
            case NumberPlayersWrongErrorMessage numberPlayersWrongErrorMessage ->
                    throw new NumberPlayerWrongException("Number Player Wrong");
            case NicknameInvalidErrorMessage nicknameInvalidErrorMessage ->
                    throw new NicknameInvalidException("Nickname is invalid");
            case NicknameAlreadyInUseErrorMessage nicknameAlreadyInUseErrorMessage ->
                    throw new NicknameAlreadyInUseException("Nickname is already in use");
            default -> 1;
        };
    }

    @Override
    public boolean connect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        Message message = new ConnectMessage(gameId, nickname);
        sendMessage(message);
        Message answer = serverHandler.getMessage();
        return switch (answer) {
            case GameFullErrorMessage gameFullErrorMessage -> throw new GameFullException("Game is full");
            case NicknameInvalidErrorMessage nicknameInvalidErrorMessage ->
                    throw new NicknameInvalidException("Nickname is invalid");
            case NicknameAlreadyInUseErrorMessage nicknameAlreadyInUseErrorMessage ->
                    throw new NicknameAlreadyInUseException("Nickname is already in use");
            default -> true;
        };
    }

    @Override
    public boolean reconnect(MessageListener l, String nickname, int gameId) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        Message message = new ReconnectMessage(gameId, nickname);
        sendMessage(message);
        Message answer = serverHandler.getMessage();
        return switch (answer) {
            case GameFullErrorMessage gameFullErrorMessage -> throw new GameFullException("Game is full");
            case NicknameInvalidErrorMessage nicknameInvalidErrorMessage ->
                    throw new NicknameInvalidException("Nickname is invalid");
            case NicknameAlreadyInUseErrorMessage nicknameAlreadyInUseErrorMessage ->
                    throw new NicknameAlreadyInUseException("Nickname is already in use");
            default -> true;
        };
    }

    @Override
    public Set<Position> getAvailablePositions(String p) {
        Message message = new GetAvailablePositionMessage(p);
        sendMessage(message);
        SendAvailablePositionMessage answer = (SendAvailablePositionMessage) serverHandler.getMessage();
        return answer.getPositions();
    }

    public List<Color> placeStarting(String p, Face face) {
        Message message = new PlaceStartingMessage(p, face);
        sendMessage(message);
        SendAvailableColorsMessage answer = (SendAvailableColorsMessage) serverHandler.getMessage();
        return answer.getColors();
    }

    @Override
    public boolean place(String p, Face face, Position pos) throws RequirementsNotMetException {
        Message message = new PlaceMessage(p, face, pos);
        sendMessage(message);
        Message answer = serverHandler.getMessage();
        if(answer instanceof NoRequirementsErrorMessage)
            throw new RequirementsNotMetException("Requirements are not met");
        return true;
    }

    @Override
    public List<GoalCard> chooseColor(String p, Color color) {
        Message message = new ChosenColorMessage(p, color);
        sendMessage(message);
        SendPossibleGoalsMessage answer = (SendPossibleGoalsMessage) serverHandler.getMessage();
        return answer.getGoals();
    }


    @Override
    public void chooseGoal(String p, GoalCard goal) {
        Message message = new ChosenGoalMessage(p, goal);
        sendMessage(message);
        Message answer = serverHandler.getMessage();
        assert(answer instanceof GoodMessage);
    }

    @Override
    public void pick(String p, PlayableCard card) {
        Message message = new PickMessage(p, card);
        sendMessage(message);
        Message answer = serverHandler.getMessage();
        assert(answer instanceof GoodMessage);
    }

    @Override
    public List<Player> getWinner() {
        Message message = new GetWinnerMessage();
        sendMessage(message);
        SendWinnerMessage answer = (SendWinnerMessage) serverHandler.getMessage();
        return answer.getWinners();
    }

    @Override
    public void update(Change change) {
        view.update(change);
    }

    @Override
    public void sendChatMessage(ChatMessage message) {
        sendMessage(message);
    }

    public void updateMessage (ChatMessage chatMessage){
        view.updateMessage(chatMessage);
    }

    public void updateDisconnection(){
        //view.updateDisconnection();
    }

    public void connectionClosed(){

        try{
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view.connectionClosed();
    }

}



