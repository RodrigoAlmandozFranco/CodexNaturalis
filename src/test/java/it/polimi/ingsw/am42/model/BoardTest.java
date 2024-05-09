package it.polimi.ingsw.am42.model;

import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.model.structure.Position;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void getFaces() {
        Board b = new Board();

        assertTrue(b.getFaces().isEmpty());

        List<Corner> cornerList = new ArrayList<Corner>();
        for (Direction d : Direction.values()) {
            cornerList.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, d));
        }

        List<Resource> lst = new ArrayList<>();

        lst.add(Resource.FUNGIKINGDOM);


        Face f = new Back("default", cornerList, Color.PURPLE, lst);
        Face f1 = new Back("default", cornerList, Color.PURPLE, lst);


        f.setPosition(new Position(0,0) ) ;
        f1.setPosition(new Position(1,0) ) ;

        b.addFace(f);
        b.addFace(f1);

        assertTrue(b.getFaces().contains(f));
        assertTrue(b.getFaces().contains(f1));
        assertEquals(2, b.getFaces().size());

    }

    @Test
    void getTotalResources() {
        Board b = new Board();

        for (Resource r : Resource.values())
            assertEquals(0, b.getTotalResources().get(r));

        List<Corner> cornerList = new ArrayList<Corner>();

        for (Direction d : Direction.values()) {
            cornerList.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, d));
        }

        Face f = new Front("default", cornerList, Color.PURPLE, new HashMap<Resource, Integer>(), null);
        f.setPosition(new Position(0,0) ) ;

        b.addFace(f);

        for (Resource r : Resource.values())
            if(r.equals(Resource.ANIMALKINGDOM))
                assertEquals(4, b.getTotalResources().get(r));
            else
                assertEquals(0, b.getTotalResources().get(r));

        List<Corner> cornerList1 = new ArrayList<Corner>();

        for (Direction d : Direction.values()) {
            cornerList1.add(new Corner(Resource.FUNGIKINGDOM, CornerState.OPEN, d));
        }

        Face f1 = new Front("default", cornerList1, Color.PURPLE, new HashMap<Resource, Integer>(), null);
        f1.setPosition(new Position(1,0) ) ;
        b.addFace(f1);

        for (Resource r : Resource.values())
            if(r.equals(Resource.ANIMALKINGDOM))
                assertEquals(3, b.getTotalResources().get(r));
            else if(r.equals(Resource.FUNGIKINGDOM))
                assertEquals(4, b.getTotalResources().get(r));
            else
                assertEquals(0, b.getTotalResources().get(r));
    }

    @Test
    void getLastPlacedFace() {
        Board b = new Board();
        List<Corner> cornerList = new ArrayList<Corner>();


        List<Resource> lst = new ArrayList<>();

        lst.add(Resource.FUNGIKINGDOM);

        Face f = new Back("default", cornerList, Color.PURPLE, lst);

        f.setPosition(new Position(0,0) ) ;

        b.addFace(f);

        assertEquals(f, b.getLastPlacedFace());
    }

    @Test
    void getPossiblePositions() {
        Board b = new Board();
        Set<Position> expectPos = new HashSet<Position>();

        Position p = new Position(0,0);
        assertTrue(b.getPossiblePositions().contains(p));
        assertEquals(1, b.getPossiblePositions().size());

        List<Face> faceList = new ArrayList<Face>();

        for (int i = 0; i <= 2; i++){
            List<Corner> cornerList = new ArrayList<Corner>();
            for (Direction d : Direction.values()) {
                if (d.equals(Direction.DOWNLEFT)) cornerList.add(new Corner(Resource.ANIMALKINGDOM, CornerState.CLOSED, d));
                else cornerList.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, d));
            }

            List<Resource> lst = new ArrayList<>();

            lst.add(Resource.ANIMALKINGDOM);
            faceList.add(new Back("default", cornerList, Color.PURPLE, lst));
            faceList.get(i).setPosition(new Position(i > 0 ? 1:0 ,i/2) ) ;

            b.addFace(faceList.get(i));
        }

        Set<Position> posPos = b.getPossiblePositions();

        expectPos.add(new Position(0,-1));
        expectPos.add(new Position(1,-1));
        expectPos.add(new Position(1,2));
        expectPos.add(new Position(2,0));
        expectPos.add(new Position(2,1));

        assertTrue(expectPos.containsAll(posPos));
        assertEquals(expectPos.size(), posPos.size());
    }

    @Test
    void getNearbyFaces() {
        Board b = new Board();

        List<Face> faceList = new ArrayList<Face>();

        for (int i = 0; i <= 2; i++){
            List<Corner> cornerList = new ArrayList<Corner>();
            for (Direction d : Direction.values()) {
                cornerList.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, d));
            }

            List<Resource> lst = new ArrayList<>();

            lst.add(Resource.ANIMALKINGDOM);

            faceList.add(new Back("default", cornerList, Color.PURPLE, lst));
            faceList.get(i).setPosition(new Position(i%2,i/2) ) ;

            b.addFace(faceList.get(i));
        }

        Map<Direction, Face> nfaces = b.getNearbyFaces(faceList.getFirst().getPosition());

        assertEquals(faceList.get(1), nfaces.get(Direction.UPRIGHT));
        assertEquals(faceList.get(2), nfaces.get(Direction.UPLEFT));
        assertEquals(2, nfaces.size());
    }

    @Test
    void addFace() {
        Board b = new Board();
        List<Corner> cornerList = new ArrayList<Corner>();

        List<Resource> lst = new ArrayList<>();

        lst.add(Resource.FUNGIKINGDOM);
        Face f = new Back("default", cornerList, Color.PURPLE, lst);
        f.setPosition(new Position(0,0) ) ;

        b.addFace(f);

        assertTrue(b.getFaces().contains(f));
        assertEquals(f, b.getLastPlacedFace());
    }
}