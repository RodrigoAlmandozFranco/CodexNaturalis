package it.polimi.ingsw.am42.network.tcp.server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Messages;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.clientToServer.*;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.SendAvailableColorsMessage;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.SendAvailablePositionMessage;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.SendPossibleGoalsMessage;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.SendWinnerMessage;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Controller controller;
    private Messages messages;

    public ClientHandler(Socket socket, Controller controller, Game game){
        this.socket = socket;
        this.controller = controller;
        messages = new Messages(controller, game);
    }

    public void run() {
        try {

            Scanner in = new Scanner(socket.getInputStream());
            final PrintWriter out = new PrintWriter(socket.getOutputStream());

            while(true) {
                if(in.hasNextLine()) {
                    String message = in.nextLine();
                    JsonObject object = JsonParser.parseString(message).getAsJsonObject();

                    String result = switch (object.get("type").getAsString()) {

                        case "FirstConnectionMessage" -> new FirstConnectionMessage(object).execute();
                        case "ConnectMessage" -> new ConnectMessage(object).execute();
                        case "ReconnectMessage" -> new ReconnectMessage(object).execute();
                        case "GetColorsMessage" -> new SendAvailableColorsMessage(object).execute();
                        case "ChosenColorMessage" -> new ChosenColorMessage(object).execute();
                        case "GetGoalsMessage" -> new SendPossibleGoalsMessage(object).execute();
                        case "ChosenGoalMessage" -> new ChosenGoalMessage(object).execute();
                        case "GetAvailablePositionMessage" -> new SendAvailablePositionMessage(object).execute();
                        case "PlaceMessage" -> new PlaceMessage(object).execute();
                        case "PickMessage" -> new PickMessage(object).execute();
                        case "GetWinnerMessage" -> new SendWinnerMessage(object).execute();
                        default -> "Error";
                    };

                    out.println(result);
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
