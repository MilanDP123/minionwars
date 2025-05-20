package be.ugent.objprog.minionwars;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StartSchermController {

    public TextField speler1Naam;
    public TextField speler2Naam;
    public TextField munten;
    public Label speler1ErrorLabel;
    public Label speler2ErrorLabel;
    public Label muntenErrorLabel;

    private final Stage stage;
    private final MinionWarsModel model;

    public StartSchermController(Stage stage, MinionWarsModel model) {
        this.stage = stage;
        this.model = model;
    }

    public void startSpel() throws IOException {
        checkNaam(speler1Naam, speler1ErrorLabel);
        checkNaam(speler2Naam, speler2ErrorLabel);
        checkMuntenAantal(munten, muntenErrorLabel);

        if (!speler1ErrorLabel.isVisible() && !speler2ErrorLabel.isVisible() && !muntenErrorLabel.isVisible()) {
            Player player1 = new Player(speler1Naam.getText().trim(), Integer.parseInt(munten.getText().trim()), speler2Naam.getText().trim(), model);
            Player player2 = new Player(speler2Naam.getText().trim(), Integer.parseInt(munten.getText().trim()), speler1Naam.getText().trim(), model);
            model.setPlayers(player1, player2);
            GameScherm gameScherm = new GameScherm(stage, model);
            gameScherm.start();
        }
    }

    private void checkNaam(TextField naam, Label errorLabel) {
        errorLabel.setVisible(naam.getText().trim().isEmpty());
    }

    private void checkMuntenAantal(TextField munten, Label errorLabel) {
        if ( ! munten.getText().trim().isEmpty() && munten.getText().trim().matches("\\d+") ) {
            int aantal = Integer.parseInt(munten.getText().trim());
            errorLabel.setVisible(aantal < 10 || aantal > 50);
        } else {
            errorLabel.setVisible(true);
        }
    }
}