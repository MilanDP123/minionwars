package be.ugent.objprog.minionwars;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EindScherm {

    private final Stage stage;
    private final MinionWarsModel model;

    public EindScherm(Stage stage, MinionWarsModel model) {
        this.stage = stage;
        this.model = model;
    }

    public void start() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EindScherm.class.getResource("eindscherm.fxml"));
        fxmlLoader.setControllerFactory(k -> new EindSchermController(stage, model));
        Scene startScene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Minion Wars");
        stage.setScene(startScene);
        stage.setResizable(false);
    }
}
