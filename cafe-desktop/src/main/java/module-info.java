module com.cafe.cafedesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.cafe.cafedesktop to javafx.fxml;
    exports com.cafe.cafedesktop;
}