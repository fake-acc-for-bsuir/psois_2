package tech.wcobalt.lab_impl.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class PortableApplication extends Application {
    private static UI ui;

    public PortableApplication() { }

    public PortableApplication(UI ui) {
        this.ui = ui;
    }

    @Override
    public void start(Stage stage) throws Exception {
        ui.init();
    }

    public void run() {
        Application.launch();
    }
}