package be.ugent.objprog.minionwars;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartScherm {

    private final Stage stage;
    private final MinionWarsModel model;

    public StartScherm(Stage stage, MinionWarsModel model) {
        this.stage = stage;
        this.model = model;
    }

    public void start() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartScherm.class.getResource("startscherm.fxml"));
        fxmlLoader.setControllerFactory(k -> new StartSchermController(stage, model));
        Scene startScene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Minion Wars");
        stage.setScene(startScene);
        stage.show();
        stage.setResizable(false);
    }
}
