module com.example.last_but_not_least {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.last_but_not_least to javafx.fxml;
    exports com.example.last_but_not_least;
}