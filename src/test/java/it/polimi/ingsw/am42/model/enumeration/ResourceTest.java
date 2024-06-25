package it.polimi.ingsw.am42.model.enumeration;

import it.polimi.ingsw.am42.model.structure.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {

    @Test
    void testToString() {
        for (Resource r: Resource.values()) {
            System.out.println(r.toString());
            System.out.println(r.fullName());
        }
    }

    @Test
    void resourceToColor() {
        for (Resource r: Resource.values()) {
            if (r.equals(Resource.PLANTKINGDOM))
                assertEquals(Color.GREEN, r.resourceToColor());
            else if (r.equals(Resource.ANIMALKINGDOM))
                assertEquals(Color.CYAN, r.resourceToColor());
            else if (r.equals(Resource.FUNGIKINGDOM))
                assertEquals(Color.RED, r.resourceToColor());
            else if (r.equals(Resource.INSECTKINGDOM))
                assertEquals(Color.PURPLE, r.resourceToColor());
            else
                assertEquals(Color.WHITE, r.resourceToColor());
        }
    }
}