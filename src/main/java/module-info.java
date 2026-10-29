module org.uabc.ic {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.uabc.ic to javafx.fxml;
    exports org.uabc.ic;
}