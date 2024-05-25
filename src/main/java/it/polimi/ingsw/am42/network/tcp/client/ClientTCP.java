package it.polimi.ingsw.am42.network.tcp.client;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.exceptions.*;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.PingMessage;
import it.polimi.ingsw.am42.network.tcp.messages.clientToServer.*;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.*;
import it.polimi.ingsw.am42.view.gameview.GameView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;



public class ClientTCP implements Client {

    private static String ip = "";
    private static int port;
    private ServerHandler serverHandler;
    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private GameView view;
    private boolean isRunning = true;

    public ClientTCP(String ip, int port) throws IOException {
        ClientTCP.ip = ip;
        ClientTCP.port = port;
        socket = new Socket(ip, port);

        output = new ObjectOutputStream(socket.getOutputStream());
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
            new Thread(serverHandler).start();
            new Thread(this::checkServerStatus).start();
            while (isRunning) {}
        } catch (final NoSuchElementException e) {
            serverDown();
        }
    }

    public void sendMessage(Message message) {
        if(isRunning) {
            try {
                synchronized (output) {
                    output.writeObject(message);
                    output.flush();
                }
            } catch (final IOException e) {
                serverDown();
            }
        }
    }


    @Override
    public ConnectionState getGameInfo() {
        Message message = new GetGameInfoMessage();
        sendMessage(message);
        ConnectionStateMessage answer;
        try {
            answer = (ConnectionStateMessage) serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return answer.getConnectionState();
    }

    @Override
    public int createGame(String nickname, int numPlayers) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException, NumberPlayerWrongException {
        Message message = new FirstConnectionMessage(nickname, numPlayers);
        sendMessage(message);


        Message answer;
        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return switch (answer) {
            case NumberPlayersWrongErrorMessage numberPlayersWrongErrorMessage ->
                    throw new NumberPlayerWrongException(numberPlayersWrongErrorMessage.getMessage());
            case NicknameInvalidErrorMessage nicknameInvalidErrorMessage ->
                    throw new NicknameInvalidException(nicknameInvalidErrorMessage.getMessage());
            case NicknameAlreadyInUseErrorMessage nicknameAlreadyInUseErrorMessage ->
                    throw new NicknameAlreadyInUseException(nicknameAlreadyInUseErrorMessage.getMessage());
            default -> 1;
        };
    }

    @Override
    public boolean connect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        Message message = new ConnectMessage(nickname);
        sendMessage(message);
        Message answer;

        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return switch (answer) {
            case GameFullErrorMessage gameFullErrorMessage -> throw new GameFullException(gameFullErrorMessage.getMessage());
            case NicknameInvalidErrorMessage nicknameInvalidErrorMessage ->
                    throw new NicknameInvalidException(nicknameInvalidErrorMessage.getMessage());
            case NicknameAlreadyInUseErrorMessage nicknameAlreadyInUseErrorMessage ->
                    throw new NicknameAlreadyInUseException(nicknameAlreadyInUseErrorMessage.getMessage());
            default -> true;
        };
    }

    @Override
    public boolean reconnect(String nickname) throws GameFullException, NicknameInvalidException, NicknameAlreadyInUseException {
        Message message = new ReconnectMessage(nickname);
        sendMessage(message);

        Message answer;

        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return switch (answer) {
            case GameFullErrorMessage gameFullErrorMessage -> throw new GameFullException(gameFullErrorMessage.getMessage());
            case NicknameInvalidErrorMessage nicknameInvalidErrorMessage ->
                    throw new NicknameInvalidException(nicknameInvalidErrorMessage.getMessage());
            case NicknameAlreadyInUseErrorMessage nicknameAlreadyInUseErrorMessage ->
                    throw new NicknameAlreadyInUseException(nicknameAlreadyInUseErrorMessage.getMessage());
            default -> true;
        };
    }


    @Override
    public Set<Position> getAvailablePositions(String p) {
        Message message = new GetAvailablePositionMessage(p);
        sendMessage(message);
        SendAvailablePositionMessage answer;

        try {
            answer = (SendAvailablePositionMessage) serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return answer.getPositions();
    }

    public List<PlayersColor> placeStarting(String p, Face face) {
        Message message = new PlaceStartingMessage(p, face);
        sendMessage(message);

        SendAvailableColorsMessage answer;
        try {
            answer = (SendAvailableColorsMessage) serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return answer.getColors();
    }

    @Override
    public boolean place(String p, Face face, Position pos) throws RequirementsNotMetException {
        Message message = new PlaceMessage(p, face, pos);
        sendMessage(message);

        Message answer;

        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(answer instanceof NoRequirementsErrorMessage)
            throw new RequirementsNotMetException("Requirements are not met");


        return true;
    }

    @Override
    public List<GoalCard> chooseColor(String p, PlayersColor color) {
        Message message = new ChosenColorMessage(p, color);
        sendMessage(message);
        SendPossibleGoalsMessage answer;
        try {
            answer = (SendPossibleGoalsMessage) serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return answer.getGoals();
    }


    @Override
    public void chooseGoal(String p, GoalCard goal) {
        Message message = new ChosenGoalMessage(p, goal);
        sendMessage(message);

        Message answer;

        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void pick(String p, PlayableCard card) {
        Message message = new PickMessage(p, card);
        sendMessage(message);

        Message answer;

        try {
            answer = serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<Player> getWinner() {
        Message message = new GetWinnerMessage();
        sendMessage(message);

        SendWinnerMessage answer;

        try {
            answer = (SendWinnerMessage) serverHandler.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return answer.getWinners();
    }

    public void update(Change change) {
        view.update(change);
    }

    @Override
    public void sendChatMessage(ChatMessage message) {
        sendMessage(message);
    }

    public void receiveMessage(Message message){
        view.updateMessage(message);
    }

    public void updateDisconnection(){
        view.setGameAborted(true);
    }

    public void playerDisconnected() {
        sendMessage(new PlayerDisconnectedMessage());
        updateDisconnection();
    }

    public void checkServerStatus() {
        while (isRunning) {
            try {
               sendMessage(new PingMessage());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                serverDown();
            }
        }
    }

    public void serverDown() {
        view.setServerDown(true);
        isRunning = false;
        serverHandler.closeAll();
        try {
            output.close();
            socket.close();
        } catch (IOException e) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
                System.exit(0);
            }
        }
    }



    public void setView(GameView view) {
        this.view = view;
    }

    public GameView getView() {
        return view;
    }
}