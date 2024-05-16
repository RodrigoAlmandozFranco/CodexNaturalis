module it.polimi.ingsw.am42 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.rmi;
    requires com.google.gson;
    requires java.desktop;

    opens it.polimi.ingsw.am42.view.gui.controller to javafx.fxml;
    //exports it.polimi.ingsw.am42;
    opens it.polimi.ingsw.am42.view to javafx.fxml;
    exports it.polimi.ingsw.am42.network.rmi;
    exports it.polimi.ingsw.am42.view.gui.controller;
    exports it.polimi.ingsw.am42.network;
    exports it.polimi.ingsw.am42.view.gui;
    opens it.polimi.ingsw.am42.view.gui to javafx.fxml;
    exports it.polimi.ingsw.am42.view.gui.utils.points;
    opens it.polimi.ingsw.am42.view.gui.utils.points to javafx.fxml;
    exports it.polimi.ingsw.am42.view.gui.utils;
    opens it.polimi.ingsw.am42.view.gui.utils to javafx.fxml;
}