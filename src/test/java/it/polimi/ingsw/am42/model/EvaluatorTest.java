package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.*;
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

    @Test
    void EvaluatorPointsPerCorner() {

        int numPoints = 2;
        Evaluator e = new EvaluatorPointsPerCorner(numPoints);

        Board b = new Board();
        assertEquals(0, e.getPoints(b));
        List<Corner> corners = new ArrayList<>();
        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.UPRIGHT));
        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.DOWNRIGHT));
        corners.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, Direction.DOWNLEFT));

        List<Resource> lst = new ArrayList<>();
        Face f = new Back("", corners, null, lst);
        f.setPosition(new Position(0, 0));
        b.addFace(f);
        Face f2 = new Back("", corners, null, lst);
        f2.setPosition(new Position(1, 0));
        b.addFace(f2);
        assertEquals(numPoints, e.getPoints(b));
        Face f3 = new Back("", corners, null, lst);
        f3.setPosition(new Position(0, 1));
        b.addFace(f3);
        assertEquals(numPoints, e.getPoints(b));
        Face f4 = new Back("", corners, null, lst);
        f4.setPosition(new Position(1, 1));
        b.addFace(f4);
        assertEquals(2 * numPoints, e.getPoints(b));


    }

    @Test
    void EvaluatorPointsPerResource() {

        int numPoints = 2;

        List<Resource> lst = new ArrayList<>();

        lst.add(Resource.ANIMALKINGDOM);

        Board b = new Board();

        List<Corner> corners = new ArrayList<>();
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.UPLEFT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.UPRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNRIGHT));
        corners.add(new Corner(Resource.QUILLOBJECT, CornerState.OPEN, Direction.DOWNLEFT));

        Face f = new Back("", corners, null, lst);
        f.setPosition(new Position(0, 0));
        b.addFace(f);
        Face f2 = new Back("", corners, null, lst);
        f2.setPosition(new Position(1, 0));
        b.addFace(f2);
        Face f3 = new Back("", corners, null, lst);
        f3.setPosition(new Position(0, 1));
        b.addFace(f3);
        Face f4 = new Back("", corners, null, lst);
        f4.setPosition(new Position(1, 1));
        b.addFace(f4);


        Evaluator e = new EvaluatorPointsPerResource(numPoints, null);
        assertEquals(0, e.getPoints(b));
        e = new EvaluatorPointsPerResource(numPoints, new HashMap<>());
        assertEquals(0, e.getPoints(b));
        Map<Resource, Integer> map = new HashMap<>();
        map.put(Resource.ANIMALKINGDOM, 1);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(4 * numPoints, e.getPoints(b));
        map.put(Resource.ANIMALKINGDOM, 2);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(2 * numPoints, e.getPoints(b));
        map.put(Resource.ANIMALKINGDOM, 3);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(numPoints, e.getPoints(b));
        map.put(Resource.ANIMALKINGDOM, 4);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(numPoints, e.getPoints(b));
        map.put(Resource.ANIMALKINGDOM, 5);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(0, e.getPoints(b));

        List<Resource> lst1 = new ArrayList<>();

        lst1.add(Resource.FUNGIKINGDOM);



        Face f5 = new Back("", corners, null, lst1);
        f5.setPosition(new Position(5, 5));
        b.addFace(f5);
        map.put(Resource.FUNGIKINGDOM, 1);
        map.put(Resource.ANIMALKINGDOM, 1);
        e = new EvaluatorPointsPerResource(numPoints, map);
        assertEquals(numPoints, e.getPoints(b));
        Face f6 = new Back("", corners, null, lst1);
        f6.setPosition(new Position(10, 10));
        b.addFace(f6);
        assertEquals(2 * numPoints, e.getPoints(b));
    }


    @Test
    void EvaluatorPointsPerStair() {

        Board b = new Board();

        int numPoints = 2;
        Color color = Color.GREEN;
        Direction direction = Direction.UPRIGHT;

        Evaluator e = new EvaluatorPointsPerStair(numPoints, color, direction);

        List<Resource> lst = new ArrayList<>();

        lst.add(Resource.ANIMALKINGDOM);

        List<Corner> cornerList = new ArrayList<Corner>();
        for (Direction d : Direction.values()) {
            cornerList.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, d));
        }


        assertEquals(0, e.getPoints(b));

        for (int i = 0; i < 4; i++) {

            Face f = new Back("default", cornerList, color, lst);
            f.setPosition(new Position(i, 0));

            b.addFace(f);
        }

        assertEquals(numPoints, e.getPoints(b));

        Face f = new Back("default", cornerList, color, lst);
        f.setPosition(new Position(4, 0));
        b.addFace(f);

        f = new Back("default", cornerList, color, lst);
        f.setPosition(new Position(-1, 0));

        b.addFace(f);

        assertEquals(2 * numPoints, e.getPoints(b));
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

        List<Resource> lst = new ArrayList<>();

        lst.add(Resource.ANIMALKINGDOM);


        for (Direction d : Direction.values()) {
            cornerList.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, d));
        }

        assertEquals(0, e.getPoints(b));

        Face f = new Back("default", cornerList, color1, lst);
        f.setPosition(new Position(1, 1));
        b.addFace(f);


        f = new Back("default", cornerList, color1, lst);
        f.setPosition(new Position(0, 0));
        b.addFace(f);


        f = new Back("default", cornerList, color2, lst);
        f.setPosition(new Position(1, 0));
        b.addFace(f);


        f = new Back("default", cornerList, color2, lst);
        f.setPosition(new Position(2, 1));
        b.addFace(f);

        assertEquals(numPoints, e.getPoints(b));

        f = new Back("default", cornerList, color2, lst);
        f.setPosition(new Position(3, 2));
        b.addFace(f);


        f = new Back("default", cornerList, color1, lst);
        f.setPosition(new Position(3, 3));
        b.addFace(f);


        f = new Back("default", cornerList, color1, lst);
        f.setPosition(new Position(2, 2));
        b.addFace(f);

        assertEquals(numPoints, e.getPoints(b));


        f = new Back("default", cornerList, color2, lst);
        f.setPosition(new Position(4, 3));
        b.addFace(f);

        assertEquals(2*numPoints, e.getPoints(b));

    }
}