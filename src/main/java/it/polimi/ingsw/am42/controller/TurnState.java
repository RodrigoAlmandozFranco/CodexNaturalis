package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.structure.Position;

import java.util.List;
import java.util.Set;

public class TurnState implements State{
    private Game g;

    public TurnState(Game g){
        this.g = g;
    }

    @Override
    public void handleAction() {
        boolean validCard = false;
        Set<Position> availablePosition = g.getCurrentPlayer().getBoard().getPossiblePositions();
        //TODO I send this Set of Possible Position to the view of the current Player

        while(!validCard){
            //TODO I wait for the position and face
            //TODO I expect to receive back the tuple with the x,y and the Face
            if(g.getCurrentPlayer().checkRequirements( choosenFace)&&availablePosition.contains(choosenPosition)){
                g.getCurrentPlayer().placeCard(choosenPosition, choosenFace);
                int points = g.getCurrentPlayer().calculatePoint( choosenFace);
                g.getCurrentPlayer().addPoints(points);
                g.getCurrentPlayer().removeCardFromHand(choosenFace);

                validCard = true;
            }
            else{
                //TODO I must inform the client's view that he has to choose another face and position
            }
        }
        List<PlayableCard> pickable = g.getPickableCards();
        //TODO I send them to the client view and I wait for the choosen card
        g.chosenCardToAddInHand(choosenCard);
    }
}
