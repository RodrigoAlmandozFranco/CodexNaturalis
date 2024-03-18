package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FrontTest {

    @Test
    void getEvaluator() {
        String src = "C:/.../face1";

        List<Corner> corners = new ArrayList<Corner>();

        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(Resource.PLANTKINGDOM, CornerState.CLOSED, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT));

        Evaluator e = new EvaluatorPoints(0);

        Face face = new Front(src, corners, Color.CYAN, null, e);

        assertEquals(e, face.getEvaluator());


    }

    @Test
    void getRequirements() {
        String src = "C:/.../face1";

        List<Corner> corners = new ArrayList<Corner>();

        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(Resource.PLANTKINGDOM, CornerState.CLOSED, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT));


        Map<Resource, Integer> requirements = new HashMap<Resource, Integer>();
        requirements.put(Resource.ANIMALKINGDOM, 5);



        Face face = new Front(src, corners, Color.CYAN, requirements, null);

        assertEquals(requirements, face.getRequirements());
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
        resources.put(Resource.PLANTKINGDOM, 1);
        resources.put(Resource.QUILLOBJECT, 2);


        Face face = new Front(src, corners, Color.CYAN, null, null);

        assertEquals(resources, face.getRequirements());
    }
}