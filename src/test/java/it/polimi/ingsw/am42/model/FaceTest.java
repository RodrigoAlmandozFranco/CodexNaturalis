package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.structure.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FaceTest {

    @Test
    void getCorners() {
        String src = "C:/.../face1";

        List<Corner> corners = new ArrayList<Corner>();

        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(Resource.PLANTKINGDOM, CornerState.CLOSED, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT));


        Face face = new Back(src, corners, Color.CYAN, null);


        assertEquals(corners, face.getCorners());
    }

    @Test
    void getCorner() {

        String src = "C:/.../face1";

        List<Corner> corners = new ArrayList<Corner>();

        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(Resource.PLANTKINGDOM, CornerState.CLOSED, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));
        Corner c = new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT);
        corners.add(c);

        Face face = new Back(src, corners, Color.CYAN, null);


        assertEquals(c, face.getCorner(c.getDirection()));
    }

    @Test
    void getPosition() {
        String src = "C:/.../face1";

        List<Corner> corners = new ArrayList<Corner>();

        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(Resource.PLANTKINGDOM, CornerState.CLOSED, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT));

        Face face = new Back(src, corners, Color.CYAN, null);

        face.setPosition(new Position(1, 0));

        assertEquals(new Position(1, 0), face.getPosition());

        face.setPosition(1, 0);

        assertEquals(new Position(1, 0), face.getPosition());

    }

    @Test
    void getColor() {
        String src = "C:/.../face1";

        List<Corner> corners = new ArrayList<Corner>();

        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(null, CornerState.CLOSED, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT));


        Face face = new Back(src, corners, Color.CYAN, null);


        assertEquals(Color.CYAN, face.getColor());
    }

    @Test
    void getSrcImage() {
        String src = "C:/.../face1";

        List<Corner> corners = new ArrayList<Corner>();

        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(null, CornerState.CLOSED, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT));


        Face face = new Back(src, corners, Color.CYAN, null);


        assertEquals(src, face.getSrcImage());
    }
}