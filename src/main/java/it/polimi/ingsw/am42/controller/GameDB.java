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


    public void saveGame() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Game.class, new GameSerializer())
                .create();

        String json = gson.toJson(game);

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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



