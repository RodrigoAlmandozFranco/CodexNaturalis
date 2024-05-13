package it.polimi.ingsw.am42.model.cards.types;

import it.polimi.ingsw.am42.model.cards.Card;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;

/**
 * This class represents a Goal Card
 * It can be private or common goal
 * It has an evaluator, which will give points to the players
 * @author Rodrigo Almandoz Franco
 */

public class GoalCard extends Card {
    private Evaluator evaluator;
    private String srcImage;

    public GoalCard(int id, String srcImage, Evaluator evaluator) {
        super(id);
        this.srcImage = srcImage;
        this.evaluator = evaluator;
    }

    /**
     * This method returns the evaluator of the card
     * @return Evaluator
     */
    public Evaluator getEvaluator() {
        return evaluator;
    }

    public String getSrcImage() {
        return srcImage;
    }

}
