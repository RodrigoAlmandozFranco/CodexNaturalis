package it.polimi.ingsw.am42.controller;

import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import javafx.beans.InvalidationListener;
import org.controlsfx.control.action.Action;

import java.rmi.Remote;


/**
 * This class is the hub of all the messages, every message, from or to the client, passes from here
 * Implements RMISpeaker interface, such that it can be used with RMI connection
 * Extends Observable interface, such that it can notify clients
 * @author Tommaso Crippa
 */
public class Hub extends Observable implements RMISpeaker {

    private Action action;
    private Controller controller;

    public Hub() {
        this.action = null;
        this.controller = null;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Hub(Controller controller) {
        this.action = null;
        this.controller = controller;
    }

    //TODO
    private boolean checkRequirements(Action a) {
        return true;
    }

    public void addAction(Action a) {
        if (checkRequirements(a))
            action = a;
        else {
            notify(new ErrorMessage(a), a.getId());
        }
    }

    public Action askAction() throws InterruptedException {
        while (action != null)
            wait(1000);
        return action;
    }
}
