package it.polimi.ingsw.am42.model.structure;

import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents the gaming board of a player, in which he can place cards.
 * @author Tommaso Crippa
 */
public class Board implements Serializable {
    private final List<Face> faces;
    private final Map<Resource, Integer> totalResources;
    private Face lastPlacedFace;

    public Board() {
        this.faces = new ArrayList<Face>();
        this.totalResources = new HashMap<Resource, Integer>();
        for (Resource r : Resource.values())
            this.totalResources.put(r, 0);
        this.lastPlacedFace = null;
    }

    /**
     * Returns for each resource the number of occurrences in the board
     *
     * @author Alessandro Di Maria
     * @return a list that contains the faces put in the board
     */
    public List<Face> getFaces() {
        return faces;
    }

    /**
     * Returns for each resource the number of occurrences in the board
     *
     * @author Alessandro Di Maria
     * @return a map, where the keys are the resources and the values are the number of occurrences
     */
    public Map<Resource, Integer> getTotalResources() {
        return totalResources;
    }

    /**
     * Returns the LastPlacedFace
     *
     * @author Alessandro Di Maria
     * @return the LastPlacedFace
     */
    public Face getLastPlacedFace() {
        return lastPlacedFace;
    }

    /**
     * Returns the possible positions where you can insert a card
     *
     * @author Alessandro Di Maria, Tommaso Crippa
     * @return availablePositions, a set with all the possible placeable positions
     */
    public Set<Position> getPossiblePositions(){
        Set<Position> availablePositions = new HashSet<Position>();
        Map<Direction, Face> futureNearby;
        boolean check;

        if (faces.isEmpty()){
            availablePositions.add(new Position(0,0));
            return availablePositions;
        }

        for(Face f: faces){
            Map<Direction, Face>nearbyFaces = getNearbyFaces(f.getPosition());
            if(nearbyFaces.size() < 4){
                for(Direction d: Direction.values())
                    if(!nearbyFaces.containsKey(d)) {
                        Position possiblePos = d.getPosition(f.getPosition());
                        futureNearby = getNearbyFaces(possiblePos);
                        check = true;
                        for (Direction possible: futureNearby.keySet()){
                            if(futureNearby.get(possible).getCorner(possible.opposite()).getState() == CornerState.CLOSED)
                                check = false;
                        }
                        if(check) availablePositions.add(possiblePos);
                    }
            }
        }
        return availablePositions;
    }

    /**
     * Returns the list of the faces nearby the one given in input
     *
     * @author Alessandro Di Maria
     * @param pos the position of the face that will be analysed
     */
    public Map<Direction, Face> getNearbyFaces(Position pos){
        int xLastFace = pos.getX();
        int yLastFace = pos.getY();
        Map<Direction, Face> nearbyFaces = new HashMap<Direction, Face>();

        for(Face f : getFaces()){
            if(xLastFace == f.getPosition().getX() &&  (yLastFace - f.getPosition().getY()) == -1)
                nearbyFaces.put(Direction.UPLEFT, f);
            if(xLastFace == f.getPosition().getX() &&  (yLastFace - f.getPosition().getY()) == 1)
                nearbyFaces.put(Direction.DOWNRIGHT, f);
            if(yLastFace == f.getPosition().getY() &&  (xLastFace - f.getPosition().getX()) == -1)
                nearbyFaces.put(Direction.UPRIGHT, f);
            if(yLastFace == f.getPosition().getY() &&  (xLastFace - f.getPosition().getX()) == 1)
                nearbyFaces.put(Direction.DOWNLEFT, f);
        }
        return nearbyFaces;
    }

    /**
     * Adds this face to the list of faces and updates the lastPlacedCard
     * then it calls the updateResources() method
     *
     * @author Alessandro Di Maria
     * @param face the face chosen to be placed
     */
    public void addFace(Face face){
        faces.add(face);
        lastPlacedFace = face;
        updateResources();

    }

    /**
     * Updates totalResources map by adding the resources contained in the lastPlacedCard
     * and removing the ones contained in the corners covered by the lastPlacedCard
     *
     * @author Alessandro Di Maria
     */
    private void updateResources(){
        Map<Resource, Integer> totResLast = lastPlacedFace.getResources();
        for(Resource r: totResLast.keySet())
            totalResources.put(r, totalResources.get(r)+totResLast.get(r));

        Map<Direction, Face> nearbyFaces = getNearbyFaces(lastPlacedFace.getPosition());
        for(Direction d: nearbyFaces.keySet()){
            Corner coveredCorner = nearbyFaces.get(d).getCorner(d.opposite());
            Resource res = coveredCorner.getResource();
            if(res != null)
                totalResources.put(res, totalResources.get(res)- 1);
            coveredCorner.closeCorner();
        }

    }
}
