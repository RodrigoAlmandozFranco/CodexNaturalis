package it.polimi.ingsw.am42.controller.gameDB;

import com.google.gson.*;
import it.polimi.ingsw.am42.controller.state.State;
import it.polimi.ingsw.am42.gson.gameGson.GameDeserializer;
import it.polimi.ingsw.am42.gson.gameGson.GameSerializer;
import it.polimi.ingsw.am42.model.Game;

import java.io.*;


/**
 * This class is responsible for saving and loading the game state.
 * It implements the Persistence advance functionality.
 * It uses GSON to serialize and deserialize the game state.
 *
 * @author Rodrigo Almandoz Franco
 */

public class GameDB {
    private Game game;
    private static final String path = "src/main/resources/it/polimi/ingsw/am42/gamePersistence/game.json";

    public GameDB() {}

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * This method saves the game in the json file located at path. The file is always updated.
     * If the gameStarted boolean is false, it returns as a string the entire json.
     * If the gameStarted boolean is true, it returns as a string only the changes made by the current player.
     * @param gameStarted boolean that points out if the game has started or not.
     * @return the string which contains all the game or only the changes.
     */

    public String saveGame(boolean gameStarted, State state) {

        JsonObject object = new JsonObject();

        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Game.class, new GameSerializer(gameStarted, object, state))
                        .create();


        String json = gson.toJson(game);
        String changes = object.toString();

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameStarted ? changes: json;

    }


    /**
     * This method returns the json file as a string.
     * @return the json file as a string.
     */
    public String jsonToString() {
        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(path));
            return jsonElement.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean jsonExists() {
        File file = new File(path);
        return file.exists();
    }

    public boolean jsonCreate() {
        File file = new File(path);
        try {
            return file.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    public boolean jsonDelete() {
        File file = new File(path);
        if(file.exists()) {
            return file.delete();
        }
        return false;
    }


    /**
     * This method loads the Game contained in the json file path.
     * @return the game contained in the json file already loaded.
     */
    public Game loadGame() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Game.class, new GameDeserializer())
                .create();
        try {
            FileReader reader = new FileReader(path);
            setGame(gson.fromJson(reader, Game.class));
            return this.game;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}



