package it.polimi.ingsw.am42.view.App;

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

class TUIApplicationTest {

    @Test
    void placeStarting() {
    }

    @Test
    void chooseColor() {
    }

    @Test
    void chooseGoal() {
    }

    @Test
    void seeBoard() {

        TUIApplication tui = new TUIApplication();

        List<Face> faces = new ArrayList<>();

        List<Corner> cornerList = new ArrayList<Corner>();
        for (Direction d : Direction.values()) {
            cornerList.add(new Corner(Resource.ANIMALKINGDOM, CornerState.OPEN, d));
        }

        List<Resource> res = new ArrayList<>();
        res.add(Resource.ANIMALKINGDOM);

        // Faces
        Face f1 = new Back(null, cornerList, Color.PURPLE, res);
        f1.setPosition(new Position(0, 0));
        Face f2 = new Back(null, cornerList, Color.CYAN, res);
        f2.setPosition(new Position(0, 1));
        Face f3 = new Back(null, cornerList, Color.WHITE, res);
        f3.setPosition(new Position(1, 0));

        faces.add(f1);
        faces.add(f2);
        faces.add(f3);

        System.out.println("Only faces");
        tui.seeBoard(faces);

        System.out.println();
        System.out.println();
        System.out.println();

        // AvailablePositions
        Face f4 = new Back(null, cornerList, null, res);
        f4.setPosition(new Position(0, 2));
        Face f5 = new Back(null, cornerList, null, res);
        f5.setPosition(new Position(1, 1));
        Face f6 = new Back(null, cornerList, null, res);
        f6.setPosition(new Position(-1, 1));
        Face f7 = new Back(null, cornerList, null, res);
        f7.setPosition(new Position(-1, 0));


        faces.clear();
        faces.add(f4);
        faces.add(f5);
        faces.add(f6);
        faces.add(f7);

        faces.add(f1);
        faces.add(f2);
        faces.add(f3);

        System.out.println("With positions");
        tui.seeBoard(faces);

    }

    @Test
    void seeCards() {
    }

    @Test
    void seeHand() {
    }

    @Test
    void seeStandings() {
    }

    @Test
    void seeGoals() {
    }

    @Test
    void place() {
    }

    @Test
    void seePickableCards() {
    }

    @Test
    void pick() {
    }

    @Test
    void seeChat() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void disconnect() {
    }

    @Test
    void selectChoice() {
    }
}