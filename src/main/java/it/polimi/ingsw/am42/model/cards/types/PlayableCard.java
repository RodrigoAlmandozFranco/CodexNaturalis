package it.polimi.ingsw.am42.model.cards.types;

import it.polimi.ingsw.am42.model.cards.Card;
import it.polimi.ingsw.am42.view.tui.ColorChooser;

/**
 * This class represents a Playable Card
 * It has a front and a back
 * It can be visible or not
 * @see Front
 * @see Back
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class PlayableCard extends Card {
    private boolean visible;
    private Front front;
    private Back back;

    public PlayableCard(int id, Front front, Back back) {
        super(id);
        this.front = front;
        this.back = back;
        this.visible = false;
    }

    /**
     * This method returns the front of the card
     * @return Front
     */
    public Front getFront() {
        return front;
    }
    /**
     * This method returns the back of the card
     * @return Back
     */
    public Back getBack() {
        return back;
    }
    /**
     * This method returns if the front of the card is visible
     * @param visible Boolean true if the front of the card is visible
     */
    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    /**
     * This method returns if the front of the card is visible
     * @return Boolean
     */
    public boolean getVisibility() {
        return visible;
    }

    @Override
    public String toString() {
        String to_print = "";

        to_print += "Front:                      Back:\n";
        to_print += front.firstRow("\t") + back.firstRow("\n");
        to_print += front.secondRow("\t") + back.secondRow("\n");
        to_print += front.thirdRow("\t") + back.thirdRow("\n");
        to_print += front.fourthRow("\t") + back.fourthRow("\n");
        to_print += front.fifthRow("\t") + back.fifthRow("\n");
        to_print += front.sixthRow("\t") + back.sixthRow("\n");
        to_print += front.seventhRow("\t") + back.seventhRow("\n");
        to_print += front.eighthRow("\t") + back.eighthRow("\n");
        to_print += front.ninthRow("\t") + back.ninthRow("\n");

        to_print += ColorChooser.RESET;
        return to_print;
    }

}

