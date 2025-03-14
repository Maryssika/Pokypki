module com.example.dao1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.dao1 to javafx.fxml;
    exports com.example.dao1;
}