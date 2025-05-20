package be.ugent.objprog.minionwars;

import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EindSchermController {
    private final Stage stage;
    private final MinionWarsModel model;

    public Label winnerLabel;

    public EindSchermController(Stage stage, MinionWarsModel model) {
        this.stage = stage;
        this.model = model;
    }

    public void initialize() {
        winnerLabel.setText("Het spel werd gewonnen door " + model.getWinner() + "!");

    }

    public void quit() {
        stage.close();
    }
}
