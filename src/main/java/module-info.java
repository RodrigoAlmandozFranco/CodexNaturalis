module it.polimi.ingsw.am42 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.rmi;
    requires com.google.gson;
    requires java.desktop;

    opens it.polimi.ingsw.am42.view.gui.controller to javafx.fxml;
    opens it.polimi.ingsw.am42.model.structure to javafx.fxml;
    exports it.polimi.ingsw.am42;
    exports it.polimi.ingsw.am42.model;
    exports it.polimi.ingsw.am42.model.enumeration;
    exports it.polimi.ingsw.am42.model.cards.types;
    exports it.polimi.ingsw.am42.model.cards.types.playables;
    exports it.polimi.ingsw.am42.network.rmi;

    exports it.polimi.ingsw.am42.view.gui.controller;
    exports it.polimi.ingsw.am42.network;
    exports it.polimi.ingsw.am42.view.gui;
    opens it.polimi.ingsw.am42.view.gui to javafx.fxml;
    exports it.polimi.ingsw.am42.view.gui.utils.points;
    opens it.polimi.ingsw.am42.view.gui.utils.points to javafx.fxml;
    exports it.polimi.ingsw.am42.view.gui.utils;
    opens it.polimi.ingsw.am42.view.gui.utils to javafx.fxml;
    opens it.polimi.ingsw.am42 to javafx.fxml;

    requires java.base;
    exports it.polimi.ingsw.am42.model.structure;

    exports it.polimi.ingsw.am42.view.gameview;
    exports it.polimi.ingsw.am42.controller.gameDB;
    exports it.polimi.ingsw.am42.controller;
}
