package it.polimi.ingsw.am42.controller;

import com.google.gson.*;
import it.polimi.ingsw.am42.gson.gameGson.GameDeserializer;
import it.polimi.ingsw.am42.gson.gameGson.GameSerializer;
import it.polimi.ingsw.am42.model.Game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * This class is responsible for saving and loading the game state.
 * It implements the Persistence advance functionality.
 * It uses GSON to serialize and deserialize the game state.
 *
 * @author Rodrigo Almandoz Franco
 */

public class GameDB {
    private Game game;
    private String path;

    public GameDB(Game g) {
        this.game = g;
        path = "src/main/resources/it/polimi/ingsw/am42/gamePersistence/game.json";
    }

    /**
     * This method saves the game in the json file located at path. The file is always updated.
     * If the gameStarted boolean is false, it returns as a string the entire json.
     * If the gameStarted boolean is true, it returns as a string only the changes made by the current player.
     * @param gameStarted boolean that points out if the game has started or not.
     * @return the string which contains all the game or only the changes.
     */

    public String saveGame(boolean gameStarted) {

        JsonObject jo = new JsonObject();

        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Game.class, new GameSerializer(gameStarted, jo))
                        .create();


        String json = gson.toJson(game);
        String changes = jo.toString();

        if(gameStarted) {
            return changes;
        }

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            return gson.fromJson(reader, Game.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}



