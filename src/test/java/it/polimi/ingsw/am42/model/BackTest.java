package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.EvaluatorPoints;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BackTest {

    @Test
    void getEvaluator() {
        String src = "C:/.../face1";

        List<Corner> corners = new ArrayList<Corner>();

        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(Resource.PLANTKINGDOM, CornerState.CLOSED, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT));

        Face face = new Back(src, corners, Color.CYAN, Resource.ANIMALKINGDOM);

        assertEquals(new EvaluatorPoints(0), face.getEvaluator());
    }

    @Test
    void getRequirements() {
        String src = "C:/.../face1";

        List<Corner> corners = new ArrayList<Corner>();

        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(Resource.PLANTKINGDOM, CornerState.CLOSED, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT));



        Face face = new Back(src, corners, Color.CYAN, null);

        assertEquals(new HashMap<Resource, Integer>(), face.getRequirements());
    }

    @Test
    void getResources() {
        String src = "C:/.../face1";

        List<Corner> corners = new ArrayList<Corner>();

        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(Resource.PLANTKINGDOM, CornerState.CLOSED, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT));

        Map<Resource, Integer> resources = new HashMap<Resource, Integer>();
        resources.put(Resource.ANIMALKINGDOM, 1);

        Face face = new Back(src, corners, Color.CYAN, Resource.ANIMALKINGDOM);

        assertEquals(resources, face.getResources());
    }
}