package be.ugent.objprog.minionwars;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameScherm {
	private final Stage stage;

	private final MinionWarsModel model;

	public GameScherm(Stage stage, MinionWarsModel model) {
		this.stage = stage;
		this.model = model;
	}

	public void start() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(GameScherm.class.getResource("gamescherm.fxml"));
		fxmlLoader.setControllerFactory(k -> new GameSchermController(model));
		Scene gameScene = new Scene(fxmlLoader.load());
		gameScene.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.DELETE) {
				model.removeSelectedMinion();
			}
		});
		gameScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("gamescherm.css")).toExternalForm());
		stage.setScene(gameScene);
		stage.setResizable(true);
	}

}
