module tech.wcobalt.lab_impl {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;
    requires javafx.swing;

    exports tech.wcobalt.lab_impl.ui to javafx.base, javafx.graphics, javafx.fxml, javafx.controls;
}