package it.polimi.ingsw.am42.model.enumeration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayersColorTest {

    @Test
    void testToString() {
        for (PlayersColor c: PlayersColor.values()) {
            System.out.println(c.toString(true));
            System.out.println(c.toString(false));
        }
    }
}