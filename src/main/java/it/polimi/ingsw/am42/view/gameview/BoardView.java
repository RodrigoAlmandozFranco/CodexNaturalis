package it.polimi.ingsw.am42.view.gameview;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardView {


    private List<Face> faces;

    public BoardView() {
        this.faces = new ArrayList<>();
    }

    public void update(Change diff) {
        this.faces.add(diff.getLastPlacedFace());
    }


    @Override
    public String toString() {
        String to_print = "";
        for (Face f: faces) {
            to_print += "\nPosition: " + f.getPosition() + "\n";
            to_print += f;
        }
        return to_print;
    }
}
