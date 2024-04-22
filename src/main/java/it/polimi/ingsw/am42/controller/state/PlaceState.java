package it.polimi.ingsw.am42.controller.state;

import it.polimi.ingsw.am42.controller.Hub;
import it.polimi.ingsw.am42.controller.state.State;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.structure.Position;
import org.controlsfx.control.action.Action;

import java.util.List;
import java.util.Set;

public class PlaceState implements State {
    private Game g;
    private Hub hub;
    private Action a;

    public PlaceState(Game g, Hub hub){
        this.g = g;
        this.hub = hub;
    }

    @Override
    public void handleAction() throws RuntimeException {
        boolean validCard = false;
        Set<Position> availablePosition = g.getCurrentPlayer().getBoard().getPossiblePositions();
        Face chosenFace;
        Position chosenPosition;
        //TODO I send this Set of Possible Position to the view of the current Player

        while (!validCard) {
            do {
                try {
                    a = hub.askAction();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } while (!(a.getNickname() == g.getCurrentPlayer().getNickname()));
            chosenFace = a.getChosenFace();
            chosenPosition = a.getChosenPosition();
            if (g.getCurrentPlayer().checkRequirements(chosenFace) && availablePosition.contains(chosenPosition)) {
                g.getCurrentPlayer().placeCard(chosenPosition, chosenFace);
                int points = g.getCurrentPlayer().calculatePoint(chosenFace);
                g.getCurrentPlayer().addPoints(points);
                g.getCurrentPlayer().removeCardFromHand(chosenFace);

                validCard = true;
            } else {
                //TODO I must inform the client's view that he has to choose another face and position
            }
        }
    }
}