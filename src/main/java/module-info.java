module birzeit.cs.astarpathfinder {
    requires javafx.controls;
    requires javafx.fxml;


    opens birzeit.cs.astarpathfinder to javafx.fxml;
    exports birzeit.cs.astarpathfinder;
}