module it.polimi.ingsw.am42 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.rmi;

    opens it.polimi.ingsw.am42 to javafx.fxml;

    exports it.polimi.ingsw.am42;
}


