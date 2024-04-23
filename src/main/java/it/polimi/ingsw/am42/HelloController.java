package it.polimi.ingsw.am42;

import it.polimi.ingsw.am42.controller.*;
import it.polimi.ingsw.am42.controller.state.*;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import it.polimi.ingsw.am42.model.Game;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public static void main(String[] args) {
        //TODO ask to the first Player to tell the number of Players
        int numberPlayers = 0;
        Game g;
        try {
            g = new Game(numberPlayers);
        } catch (it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException e) {
            throw new RuntimeException(e);
        }
        Hub hub = new Hub();
        Controller gameController = new Controller(g);
        hub.setController(gameController);

        gameController.setState(new InitialState(g));
        try{
            gameController.executionOfState();
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        gameController.setState(new SetHandsState(g, hub));
        try{
            gameController.executionOfState();
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        gameController.setState(new StartingCardState(g, hub));
        try{
            gameController.executionOfState();
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        while (!(g.getTurnFinal() && g.getCurrentPlayer()==g.getPlayers().getFirst())){

            gameController.setState(new PlaceState(g, hub));
            try{
                gameController.executionOfState();
            }catch (RuntimeException e) {
                throw new RuntimeException(e);
            }

            gameController.setState(new PickState(g, hub));
            try{
                gameController.executionOfState();
            }catch (RuntimeException e) {
                throw new RuntimeException(e);
            }

            gameController.setState(new CheckState(g, hub));
            try{
                gameController.executionOfState();
            }catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }

        for(Player p : g.getPlayers()){
            gameController.setState(new LastTurnState(g));
            try{
                gameController.executionOfState();
            }catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }

        gameController.setState(new FinalState(g));
        try{
            gameController.executionOfState();
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}