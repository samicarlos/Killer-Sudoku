module com.example.sudokudemo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sudokudemo to javafx.fxml;
    exports com.example.sudokudemo;
}