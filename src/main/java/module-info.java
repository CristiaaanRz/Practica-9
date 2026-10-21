module org.uabc.ic.practica9 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.uabc.ic.practica9 to javafx.fxml;
    exports org.uabc.ic.practica9;
}