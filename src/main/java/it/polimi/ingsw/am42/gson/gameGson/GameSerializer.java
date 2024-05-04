package it.polimi.ingsw.am42.gson.gameGson;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.am42.controller.state.State;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.structure.Board;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is used to serialize a Game object into a json file.
 * If the game has not started yet, the entire game is serialized (method writeEntireGame).
 * When the game has started, only the changes made by
 * the current player are serialized in the same position as before (method writeChangesGame).
 * It is the core component for persistence purposes.
 * The write method, with support of the private methods, serializes the game object into a json file.
 * It receives a JsonWriter object and a Game object as parameters.
 * The private methods are used to write the different attributes of the game object.
 *
 * All the changes made by the current player are saved in the object JsonObject.
 * In the GameDB class, the object will be transformed to a string and passed to the client
 * Afterward, the client can easily get that string to JsonObject format by the following line of code:
 *                          JsonObject object = JsonParser.parseString(string).getAsJsonObject();
 *
 * @author Rodrigo Almandoz Franco
 */

public class GameSerializer extends TypeAdapter<Game> {

    private String path = "src/main/resources/it/polimi/ingsw/am42/gamePersistence/game.json";
    private boolean gameStarted;
    private JsonObject object;
    private State state;

    public GameSerializer(boolean gs, JsonObject object, State state) {
        this.gameStarted = gs;
        this.object = object;
        this.state = state;
    }


    public void write(JsonWriter out, Game game) throws IOException {

        if (!gameStarted) {
            writeEntireGame(out, game);
        } else {
            writeChangesGame(game);
        }
    }


    /**
     * This method writes the changes made by the current player into
     * the same json file where the game was saved before the game start.
     * @param game the Game object to be serialized, which contains the changes made by the current player.
     * @throws IOException if there is an error writing the json file.
     */
    private void writeChangesGame(Game game) throws IOException {
        try {
            FileReader reader = new FileReader(path);
            JsonReader jsonReader = new JsonReader(reader);
            JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();

            writeChangesPlayer(game, jsonObject.get("players").getAsJsonArray());

            writeChangesPlayableDeck(game, jsonObject.get("resourceDeck").getAsJsonArray(), "resourceDeck");
            writeChangesPlayableDeck(game, jsonObject.get("goldDeck").getAsJsonArray(), "goldDeck");

            writeChangesPickable(game, jsonObject.get("pickableResourceCards").getAsJsonArray(), "pickableResourceCards");
            writeChangesPickable(game, jsonObject.get("pickableGoldCards").getAsJsonArray(), "pickableGoldCards");

            jsonObject.addProperty("currentPlayer", game.getNextPlayer().getNickname());

            jsonObject.addProperty("currentState", state.toString());
            object.addProperty("currentState", state.toString());


            jsonReader.close();
            FileWriter writer = new FileWriter(path);
            writer.write(jsonObject.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * This method writes the changes of the pickableCards (resource or gold pickable cards).
     * @param game the Game object to be serialized, which contains the pickable cards information.
     * @param jsonArray the JsonArray object that contains the pickable cards information saved before.
     * @param str the string that indicates if the pickable cards are resource cards or gold cards.
     */
    private void writeChangesPickable(Game game, JsonArray jsonArray, String str) {
        List<Integer> picks;
        if(str.equals("pickableResourceCards")) picks = game.getPickableResourceCards();
        else picks = game.getPickableGoldCards();

        JsonArray pickable = new JsonArray();

        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            if(jsonObject.get("id").getAsInt() != picks.get(i)) {
                jsonObject.addProperty("id", picks.get(i));
            }
            JsonObject idcard = new JsonObject();
            idcard.addProperty("id", picks.get(i));
            pickable.add(idcard);
        }

        object.add(str, pickable);
    }


    /**
     * This method writes the changes of the playableDeck (resource or gold playable deck).
     * @param game the Game object to be serialized, which contains the playable deck information.
     * @param jsonArray the JsonArray object that contains the playable deck information saved before.
     * @param str the string that indicates if the playable deck is the resource deck or the gold deck.
     */
    private void writeChangesPlayableDeck(Game game, JsonArray jsonArray, String str) {
        List<Integer> ids;
        if(str.equals("resourceDeck")) ids = game.getResourceDeck();
        else ids = game.getGoldDeck();


        int id = ids.getFirst();
        if(jsonArray.get(0).getAsJsonObject().get("id").getAsInt() != id) {
            jsonArray.remove(0);
        }
        object.addProperty(str.concat("IdFirstCard"), ids.getFirst());
    }


    /**
     * This method writes the changes made by the current player into the json file.
     * It takes into account the player's points, hand, board and number of goals achieved.
     * @param game the Game object to be serialized, which contains the changes made by the current player.
     * @param jsonArray the JsonArray object that contains the players' information saved before.
     */
    private void writeChangesPlayer(Game game, JsonArray jsonArray) {
        JsonArray ja = new JsonArray();
        JsonObject points = new JsonObject();
        JsonObject numberGoals = new JsonObject();

        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject json = jsonArray.get(i).getAsJsonObject();
            if(game.getCurrentPlayer().getNickname().equals(json.get("nickname").getAsString())) {
                json.addProperty("points", game.getCurrentPlayer().getPoints());
                points.addProperty("points", game.getCurrentPlayer().getPoints());
                ja.add(points);
                writeChangesHand(json.get("hand").getAsJsonArray(), game, ja);
                writeChangesBoard(json.get("board").getAsJsonArray(), game, ja);
                json.addProperty("numberGoalsAchieved", game.getCurrentPlayer().getGoalsAchieved());
                numberGoals.addProperty("numberGoalsAchieved", game.getCurrentPlayer().getGoalsAchieved());
                ja.add(numberGoals);
            }
        }
        object.add(game.getCurrentPlayer().getNickname(), ja);
        object.addProperty("futurePlayer", game.getNextPlayer().getNickname());
    }


    /**
     * This method writes the hand's changes made by the current player into the json file.
     * @param jsonArray the JsonArray object that contains the hand's information saved before.
     * @param game the Game object to be serialized, which contains the hand's information.
     */
    private void writeChangesHand(JsonArray jsonArray, Game game, JsonArray ja) {
        JsonArray hand = new JsonArray();

        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject json = jsonArray.get(i).getAsJsonObject();

            if(game.getCurrentPlayer().getHand().size() <= i) {
                jsonArray.remove(i);
                continue;
            }

            if (json.get("id").getAsInt() != game.getCurrentPlayer().getHand().get(i).getId()) {
                json.addProperty("id", game.getCurrentPlayer().getHand().get(i).getId());
            }

            JsonObject cardId = new JsonObject();
            cardId.addProperty("id", game.getCurrentPlayer().getHand().get(i).getId());
            hand.add(cardId);
        }

        for(int i = jsonArray.size(); i < game.getCurrentPlayer().getHand().size(); i++){
            JsonObject json = new JsonObject();
            json.addProperty("id", game.getCurrentPlayer().getHand().get(i).getId());
            jsonArray.add(json);
            JsonObject cardId = new JsonObject();
            cardId.addProperty("id", game.getCurrentPlayer().getHand().get(i).getId());
            hand.add(cardId);
        }


        JsonObject handObject = new JsonObject();
        handObject.add("hand", hand);
        ja.add(handObject);
    }


    /**
     * This method writes the board's changes made by the current player into the json file.
     * @param jsonArray the JsonArray object that contains the board's information saved before.
     * @param game the Game object to be serialized, which contains the board's information.
     */
    private void writeChangesBoard(JsonArray jsonArray, Game game, JsonArray ja) {
        JsonArray board = new JsonArray();

        Face face = game.getCurrentPlayer().getBoard().getLastPlacedFace();
        JsonObject jsonObject = new JsonObject();

        if(face.getSrcImage() != null)
            jsonObject.addProperty("srcImage", face.getSrcImage());
        else jsonObject.addProperty("srcImage", "-1");
        JsonObject src = new JsonObject();
        src.addProperty("srcImage", face.getSrcImage());
        board.add(src);

        JsonObject type = new JsonObject();

        if(face instanceof Front) {
            jsonObject.addProperty("type", "front");
            type.addProperty("type", "front");
        } else {
            jsonObject.addProperty("type", "back");
            type.addProperty("type", "back");
        }
        board.add(type);

        JsonObject x = new JsonObject();
        x.addProperty("x", face.getPosition().getX());
        board.add(x);

        JsonObject y = new JsonObject();
        y.addProperty("y", face.getPosition().getY());
        board.add(y);

        jsonObject.addProperty("x", face.getPosition().getX());
        jsonObject.addProperty("y", face.getPosition().getY());

        jsonArray.add(jsonObject);

        JsonObject boardObject = new JsonObject();
        boardObject.add("board", board);
        ja.add(boardObject);
    }


    /**
     * This method writes the entire game into the json file.
     * @param out the JsonWriter object, which is used to write the json file.
     * @param game the Game object to be serialized, which contains the entire game information.
     * @throws IOException if there is an error writing the json file.
     */
    private void writeEntireGame(JsonWriter out, Game game) throws IOException {
        out.beginObject();
        writePlayers(out, game);
        writeGlobalGoals(out, game);
        writePlayableDeck(out, game, "resourceDeck");
        writePlayableDeck(out, game, "goldDeck");
        writePlayableDeck(out, game, "startingDeck");
        writeGoalDeck(out, game);
        writePickableCards(out, game, "pickableResourceCards");
        writePickableCards(out, game, "pickableGoldCards");
        writeCurrentPlayer(out, game);
        writeNumberPlayers(out, game);
        out.name("currentState").value(state.toString());
        out.endObject();
    }


    /**
     * This method writes the current player of the game into the json file.
     * @param out the JsonWriter object, which is used to write the json file.
     * @param game the Game object to be serialized, which contains the currentPlayer information.
     * @throws IOException if there is an error writing the json file.
     */
    private void writeCurrentPlayer(JsonWriter out, Game game) throws IOException {
        out.name("currentPlayer").value(game.getNextPlayer().getNickname());
    }


    /**
     * This method writes the number of players of the game into the json file.
     * @param out the JsonWriter object, which is used to write the json file.
     * @param game the Game object to be serialized, which contains the number of players information.
     * @throws IOException if there is an error writing the json file.
     */
    private void writeNumberPlayers(JsonWriter out, Game game) throws IOException {
        out.name("numberPlayers").value(game.getPlayers().size());
    }


    /**
     * This method writes the pickable cards of the game into the json file.
     * @param out the JsonWriter object, which is used to write the json file.
     * @param game the Game object to be serialized, which contains the pickable cards information.
     * @param str the string that indicates if the pickable cards are resource cards or gold cards.
     * @throws IOException if there is an error writing the json file.
     */
    private void writePickableCards(JsonWriter out, Game game, String str) throws IOException {
        List<Integer> picks;
        if(str.equals("pickableResourceCards")) picks = game.getPickableResourceCards();
        else picks = game.getPickableGoldCards();

        out.name(str);
        out.beginArray();
        for (int i : picks) {
            out.beginObject();
            out.name("id").value(i);
            out.endObject();
        }
        out.endArray();
    }


    /**
     * This method writes the goal deck of the game into the json file.
     * @param out the JsonWriter object, which is used to write the json file.
     * @param game the Game object to be serialized, which contains the goal deck information.
     * @throws IOException if there is an error writing the json file.
     */
    private void writeGoalDeck(JsonWriter out, Game game) throws IOException {
        out.name("goalDeck");
        out.beginArray();
        for (int i: game.getGoalDeck()) {
            out.beginObject();
            out.name("id").value(i);
            out.endObject();
        }
        out.endArray();
    }


    /**
     * This method writes the playable deck of the game into the json file.
     * @param out the JsonWriter object, which is used to write the json file.
     * @param game the Game object to be serialized, which contains the playable deck information.
     * @param str the string that indicates if the playable deck contains the resource, gold or starting cards.
     * @throws IOException
     */
    private void writePlayableDeck(JsonWriter out, Game game, String str) throws IOException {
        List<Integer> ids;

        if(str.equals("resourceDeck")) ids = game.getResourceDeck();
        else if(str.equals("goldDeck")) ids = game.getGoldDeck();
        else ids = game.getStartingDeck();

        out.name(str);
        out.beginArray();
        for (int i : ids) {
            out.beginObject();
            out.name("id").value(i);
            out.endObject();
        }
        out.endArray();
    }


    /**
     * This method writes the global goals of the game into the json file.
     * @param out the JsonWriter object, which is used to write the json file.
     * @param game the Game object to be serialized, which contains the global goals' information.
     * @throws IOException if there is an error writing the json file.
     */
    private void writeGlobalGoals(JsonWriter out, Game game) throws IOException {
        out.name("globalGoals");
        out.beginArray();
        for (GoalCard c : game.getGoals()) {
            out.beginObject();
            out.name("id").value(c.getId());
            out.endObject();
        }
        out.endArray();
    }


    /**
     * This method writes all the information about the players of the game into the json file.
     * @param out the JsonWriter object, which is used to write the json file.
     * @param game the Game object to be serialized, which contains the players' information.
     * @throws IOException if there is an error writing the json file.
     */
    private void writePlayers(JsonWriter out, Game game) throws IOException {
        out.name("players");
        out.beginArray();

        for (Player player : game.getPlayers()) {
            out.beginObject();
            out.name("nickname").value(player.getNickname());
            out.name("points").value(player.getPoints());
            out.name("hand");
            writeHand(out, player.getHand());
            if(player.getColor() != null)
                out.name("color").value(player.getColor().toString());
            else out.name("color").value("-1");
            out.name("board");
            writeBoard(out, player.getBoard());
            if(player.getPersonalGoal() != null) {
                out.name("personalGoal").value(player.getPersonalGoal().getId());
            } else {
                out.name("personalGoal").value(-1);
            }
            out.name("numberGoalsAchieved").value(player.getGoalsAchieved());
            out.endObject();
        }
        out.endArray();
    }


    /**
     * This method writes the board of a player into the json file. The writePlayers method calls writeBoard.
     * @param out the JsonWriter object, which is used to write the json file.
     * @param board the Board object to be serialized, which contains the board's information.
     * @throws IOException if there is an error writing the json file.
     */
    private void writeBoard(JsonWriter out, Board board) throws IOException {

        out.beginArray();


        for (Face face: board.getFaces()) {
            out.beginObject();
            out.name("srcImage").value(face.getSrcImage());
            if(face instanceof Front){
                out.name("type").value("front");
            } else {
                out.name("type").value("back");
            }
            out.name("x").value(face.getPosition().getX());
            out.name("y").value(face.getPosition().getY());
            out.endObject();
        }

        out.endArray();

    }


    /**
     * This method writes the hand of a player into the json file. The writePlayers method calls writeHand.
     * @param out the JsonWriter object, which is used to write the json file.
     * @param hand the List of PlayableCard objects to be serialized, which contains the hand's information.
     * @throws IOException if there is an error writing the json file.
     */
    private void writeHand(JsonWriter out, List<PlayableCard> hand) throws IOException {
        out.beginArray();

        for (PlayableCard card : hand) {
            out.beginObject();
            out.name("id").value(card.getId());
            out.endObject();
        }

        out.endArray();
    }


    /**
     * This method is not used to serialize a Game object into a json file.
     * @see GameDeserializer
     * @param in
     * @return null
     */
    @Override
    public Game read(JsonReader in) {
        System.out.println("Use GameDeserializer instead of GameSerializer to read a game from a json file.");
        return null;
    }
}



