package it.polimi.ingsw.am42.model.evaluator;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.model.structure.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EvaluatorTest {

    @Test
    void EvaluatorPoints() {
        Evaluator e = new EvaluatorPoints(0);
        assertEquals(0, e.getPoints(null));

        e = new EvaluatorPoints(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, e.getPoints(null));
    }

    void EvaluatorPointsPerCorner() {

        int numPoints = 2;
        Evaluator e = new EvaluatorPointsPerCorner(numPoints);

        Board b = new Board();
        assertEquals(0, e.getPoints(b));
        Face f = new Back("", null, null, null);
        f.setPosition(new Position(0, 0));
        b.addFace(f);
        Face f2 = new Back("", null, null, null);
        f2.setPosition(new Position(1, 0));
        b.addFace(f2);
        assertEquals(numPoints, e.getPoints(b));
        Face f3 = new Back("", null, null, null);
        f3.setPosition(new Position(0, 1));
        b.addFace(f3);
        assertEquals(numPoints, e.getPoints(b));
        Face f4 = new Back("", null, null, null);
        f4.setPosition(new Position(1, 1));
        b.addFace(f4);
        assertEquals(2*numPoints, e.getPoints(b));


    }

    void EvaluatorPointsPerResource() {

        int numPoints = 2;

        Board b = new Board();
        Face f = new Back("", null, null, Resource.ANIMALKINGDOM);
        f.setPosition(new Position(0, 0));
        b.addFace(f);
        Face f2 = new Back("", null, null, Resource.ANIMALKINGDOM);
        f2.setPosition(new Position(1, 0));
        b.addFace(f2);
        Face f3 = new Back("", null, null, Resource.ANIMALKINGDOM);
        f3.setPosition(new Position(0, 1));
        b.addFace(f3);
        Face f4 = new Back("", null, null, Resource.ANIMALKINGDOM);
        f4.setPosition(new Position(1, 1));
        b.addFace(f4);


        Evaluator e = new EvaluatorPointsPerResource(numPoints, null);
        assertEquals(0, e.getPoints(b));
        e = new EvaluatorPointsPerResource(numPoints, new HashMap<>());
        assertEquals(0, e.getPoints(b));
        Map<Resource, Integer> map = new HashMap<>();
        map.put(Resource.ANIMALKINGDOM, 1);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(4*numPoints, e.getPoints(b));
        map.put(Resource.ANIMALKINGDOM, 2);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(2*numPoints, e.getPoints(b));
        map.put(Resource.ANIMALKINGDOM, 3);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(numPoints, e.getPoints(b));
        map.put(Resource.ANIMALKINGDOM, 4);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(numPoints, e.getPoints(b));
        map.put(Resource.ANIMALKINGDOM, 5);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(0, e.getPoints(b));

        Face f5 = new Back("", null, null, Resource.FUNGIKINGDOM);
        f5.setPosition(new Position(1, 1));
        b.addFace(f5);
        map.put(Resource.FUNGIKINGDOM, 1);
        map.put(Resource.ANIMALKINGDOM, 1);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(numPoints, e.getPoints(b));
        Face f6 = new Back("", null, null, Resource.FUNGIKINGDOM);
        f6.setPosition(new Position(1, 1));
        b.addFace(f6);
        assertEquals(2*numPoints, e.getPoints(b));
    }

    @Test
    void EvaluatorPointsPerChair() {
        Board b = new Board();

        int numPoints = 2;
        Color color1 = Color.GREEN;
        Color color2 = Color.RED;
        Direction direction = Direction.UPRIGHT;

        Evaluator e = new EvaluatorPointsPerChair(numPoints, color1, color2, direction);

        List<Corner> cornerList = new ArrayList<Corner>();

        for (Direction d : Direction.values()) {
            cornerList.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, d));
        }

        assertEquals(0, e.getPoints(b));

        Face f = new Back("default", cornerList, color1, Resource.ANIMALKINGDOM);
        f.setPosition(new Position(1, 1));
        b.addFace(f);


        f = new Back("default", cornerList, color1, Resource.ANIMALKINGDOM);
        f.setPosition(new Position(0, 0));
        b.addFace(f);


        f = new Back("default", cornerList, color2, Resource.ANIMALKINGDOM);
        f.setPosition(new Position(1, 0));
        b.addFace(f);


        f = new Back("default", cornerList, color2, Resource.ANIMALKINGDOM);
        f.setPosition(new Position(2, 1));
        b.addFace(f);

        assertEquals(numPoints, e.getPoints(b));

        f = new Back("default", cornerList, color2, Resource.ANIMALKINGDOM);
        f.setPosition(new Position(3, 2));
        b.addFace(f);


        f = new Back("default", cornerList, color1, Resource.ANIMALKINGDOM);
        f.setPosition(new Position(3, 3));
        b.addFace(f);


        f = new Back("default", cornerList, color1, Resource.ANIMALKINGDOM);
        f.setPosition(new Position(2, 2));
        b.addFace(f);

        assertEquals(numPoints, e.getPoints(b));


        f = new Back("default", cornerList, color2, Resource.ANIMALKINGDOM);
        f.setPosition(new Position(4, 3));
        b.addFace(f);

        assertEquals(2*numPoints, e.getPoints(b));

    }
}