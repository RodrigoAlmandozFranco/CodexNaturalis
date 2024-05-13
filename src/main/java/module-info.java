module it.polimi.ingsw.am42 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.rmi;
    requires com.google.gson;

    opens it.polimi.ingsw.am42.view.gui.controller to javafx.fxml;
    exports it.polimi.ingsw.am42;
    opens it.polimi.ingsw.am42.view.gui to javafx.fxml;
}