package be.ugent.objprog.minionwars;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MinionWars extends Application {

	private MinionWarsModel model;

	@Override
	public void init() {
		List<String> arguments = getParameters().getRaw();
		if (arguments.size() != 1) {
			System.out.println("Arguments not valid");
			Platform.exit();
		}

		model = new MinionWarsModel(arguments.getFirst());
	}

	@Override
	public void start(Stage stage) throws IOException {
		model.setStage(stage);
		StartScherm startScherm = new StartScherm(stage, model);
		startScherm.start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
