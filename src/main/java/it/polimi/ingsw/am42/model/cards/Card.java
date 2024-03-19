package it.polimi.ingsw.am42.model.cards;

/**
 * This class represents a generic card
 * It has an id
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public abstract class Card {
    private final int id;

    public Card(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
