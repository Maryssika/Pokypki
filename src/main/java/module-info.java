module com.example.dao1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires io.github.cdimascio.dotenv.java;


    opens com.example.dao1 to javafx.fxml;
    exports com.example.dao1;
}