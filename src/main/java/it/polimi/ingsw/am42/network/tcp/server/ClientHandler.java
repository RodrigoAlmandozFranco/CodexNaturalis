package it.polimi.ingsw.am42.network.tcp.server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.network.MessageListener;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Message;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.clientToServer.*;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.SendAvailableColorsMessage;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.SendAvailablePositionMessage;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.SendPossibleGoalsMessage;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.SendWinnerMessage;

public class ClientHandler implements Runnable, MessageListener {
    private Socket socket;
    private Controller controller;
    private Message messages;
    private PrintWriter out;


    public ClientHandler(Socket socket, Controller controller, Game game) throws IOException {
        this.socket = socket;
        this.controller = controller;
        messages = new Message(controller, game);
        out = new PrintWriter(socket.getOutputStream());
    }

    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            final PrintWriter out = new PrintWriter(socket.getOutputStream());
            while(true) {
                if(in.hasNextLine()) {
                    String message = in.nextLine();
                    JsonObject object = JsonParser.parseString(message).getAsJsonObject();

                    String result;
                    switch (object.get("type").getAsString()) {

                        case "FirstConnectionMessage":
                            result = new FirstConnectionMessage(object, this).execute();
                            break;
                        case "ConnectMessage":
                            result = new ConnectMessage(object, this).execute();
                            break;
                        case "ReconnectMessage":
                            result = new ReconnectMessage(object, this).execute();
                            break;
                        case "GetColorsMessage":
                            result = new SendAvailableColorsMessage(object).execute();
                            this.update(result);
                            break;
                        case "ChosenColorMessage":
                            result = new ChosenColorMessage(object).execute();
                            break;
                        case "GetGoalsMessage":
                            result = new SendPossibleGoalsMessage(object).execute();
                            this.update(result);
                            break;
                        case "ChosenGoalMessage":
                            result = new ChosenGoalMessage(object).execute();
                            break;
                        case "GetAvailablePositionMessage":
                            result = new SendAvailablePositionMessage(object).execute();
                            this.update(result);
                            break;
                        case "PlaceMessage":
                            result = new PlaceMessage(object).execute();
                            break;
                        case "PickMessage":
                            result = new PickMessage(object).execute();
                            break;
                        case "GetWinnerMessage":
                            result = new SendWinnerMessage(object).execute();
                            this.update(result);
                            break;
                        default:
                            result = "Error";
                            break;
                    };

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Message message){
        //TODO serialize the message
        //JsonObject result = new JsonObject();

        //out.println(result);
        //out.flush();
    }

    public void update (String result){
        out.println(result);
        out.flush();
    }
    public String getId(){
        return null;
    }

}
