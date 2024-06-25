package it.polimi.ingsw.am42.model.enumeration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void testToString() {
        for (Color c : Color.values()) {
            System.out.println(c.toString(true));
            System.out.println(c.toString(false));
        }
    }

    @Test
    void colorToResource() {
        for (Color c: Color.values()) {
            if (c.equals(Color.RED))
                    assertEquals(Resource.FUNGIKINGDOM, c.colorToResource());
            if (c.equals(Color.CYAN))
                assertEquals(Resource.ANIMALKINGDOM, c.colorToResource());
            if (c.equals(Color.PURPLE))
                assertEquals(Resource.INSECTKINGDOM, c.colorToResource());
            if (c.equals(Color.GREEN))
                assertEquals(Resource.PLANTKINGDOM, c.colorToResource());
        }
    }
}