package it.polimi.ingsw.am42.controller.gameDB;

import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.GameInterface;

import java.io.*;


/**
 * This class is responsible for saving and loading the game state.
 * It implements the Persistence advance functionality.
 * It uses ObjectOutputStream and ObjectInputStream to save and load the game state.
 *
 * @author Rodrigo Almandoz Franco
 */
public class GameDB {
    protected GameInterface game;
    private static final String path = "src/main/resources/it/polimi/ingsw/am42/gamePersistence/game.dat";

    public GameDB() {}

    public void setGame(GameInterface game) {
        this.game = game;
    }


    /**
     * This method saves the game in the file located at path. The file is always updated.
     * If the gameStarted boolean is false, it returns null.
     * If the gameStarted boolean is true, it returns a Change class which contains the
     * changes made by the current player.
     *
     * @param gs boolean that points out if the game has started or not.
     * @return the string which contains all the game or only the changes.
     */
    public Change saveGame(boolean gs) {

        try {
            if (fileExists()) fileDelete();

            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));

            outputStream.writeObject(game);

            outputStream.close();

            return new Change(game, gs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChangeAfterLoad afterLoad() {
        return new ChangeAfterLoad(game);
    }


    /**
     * This method checks if the file exists.
     * @return true if the file exists, false otherwise.
     */
    public boolean fileExists() {
        File file = new File(path);
        return file.exists();
    }


    /**
     * This method deletes the file located at path.
     * @return true if the file was deleted, false otherwise.
     */
    private boolean fileDelete() {
        File file = new File(path);
        if(file.exists()) {
            return file.delete();
        }
        return false;
    }


    /**
     * This method loads the Game contained in the file path.
     * @return the game contained in the file already loaded.
     */
    public Game loadGame() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));

            Game game = (Game) inputStream.readObject();
            this.setGame(game);

            inputStream.close();
            return game;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}



