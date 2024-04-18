package it.polimi.ingsw.am42;

import it.polimi.ingsw.am42.controller.*;
import it.polimi.ingsw.am42.controller.state.*;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
        Controller gameController = new Controller(g);

        gameController.executionOfState();
        gameController.setState(new SetHandsState(g));
        gameController.executionOfState();
        while (!(g.getTurnFinal() && g.getCurrentPlayer()==g.getPlayers().getFirst())){
            gameController.setState(new TurnState(g));
            gameController.executionOfState();
            gameController.setState(new CheckState(g));
            gameController.executionOfState();
        }
        for(Player p : g.getPlayers()){
            gameController.setState(new LastTurnState(g));
            gameController.executionOfState();
        }
        gameController.setState(new FinalState(g));
        gameController.executionOfState();

    }
}