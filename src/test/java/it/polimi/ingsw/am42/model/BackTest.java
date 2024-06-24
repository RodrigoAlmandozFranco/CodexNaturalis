package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;
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

        List<Resource> lst = new ArrayList<>();

        lst.add(Resource.ANIMALKINGDOM);


        Face face = new Back(src, corners, Color.CYAN, lst);

        Evaluator other = new EvaluatorPoints(0);
        assertEquals(other.getPoints(null), face.getEvaluator().getPoints(null));
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
        resources.put(Resource.ANIMALKINGDOM, 2);
        resources.put(Resource.QUILLOBJECT, 2);

        List<Resource> lst = new ArrayList<>();

        lst.add(Resource.ANIMALKINGDOM);

        Face face = new Back(src, corners, Color.CYAN, lst);

        assertEquals(resources, face.getResources());
    }
}