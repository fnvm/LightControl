module org.github.fnvm.lightcontrol {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.github.fnvm.lightcontrol to javafx.fxml;
    exports org.github.fnvm.lightcontrol;
    exports org.github.fnvm.lightcontrol.model;
    opens org.github.fnvm.lightcontrol.model to javafx.fxml;
    exports org.github.fnvm.lightcontrol.view;
    opens org.github.fnvm.lightcontrol.view to javafx.fxml;
    exports org.github.fnvm.lightcontrol.viewmodel;
    opens org.github.fnvm.lightcontrol.viewmodel to javafx.fxml;
}