package it.polimi.ingsw.am42.model.cards;

import java.io.Serializable;

/**
 * This class represents a generic card
 * It has an id
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public abstract class Card implements Serializable {
    private final int id;

    public Card(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
