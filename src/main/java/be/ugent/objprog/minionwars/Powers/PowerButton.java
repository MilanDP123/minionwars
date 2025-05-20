package be.ugent.objprog.minionwars.Powers;

import be.ugent.objprog.minionwars.Effects.Effect;
import be.ugent.objprog.minionwars.MinionWarsModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class PowerButton extends HBox implements InvalidationListener {
    private String type;
    private String name;
    private int radius;
    private int value;
    private Effect effect;
    private MinionWarsModel model;

    private final Circle circle;
    private final Label nameLabel;
    private final Label effectLabel;
    private final VBox infoBox;

    private final Map<String, Supplier<Power>> powerMap = Map.of(
            "fireball", Fireball::new,
            "heal", HealPower::new,
            "lightning", Lightning::new
    );

    public PowerButton() {
        super();
        setPrefSize(300, 100);
        setAlignment(Pos.CENTER);

        circle = new Circle();
        circle.setRadius(30);

        getChildren().add(circle);
        setMargin(circle, new Insets(5, 2, 5, 5));

        VBox labelsBox = new VBox();
        labelsBox.setPrefSize(200, 60);
        labelsBox.setAlignment(Pos.CENTER);
        nameLabel = new Label();
        nameLabel.setPrefSize(200, 30);
        effectLabel = new Label();
        effectLabel.setPrefSize(200, 30);
        labelsBox.getChildren().addAll(nameLabel, effectLabel);
        getChildren().add(labelsBox);

        infoBox = new VBox();
        infoBox.setPrefSize(60, 100);

        getChildren().add(infoBox);
        setOnMousePressed(this::selectThis);
    }

    public void setModel(MinionWarsModel model) {
        this.model = model;
        model.selectedPowerProperty().addListener(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Map<String, String> typeToImageName = Map.of(
                "fireball", "fireball.png",
                "heal", "healing.png",
                "lightning", "lightning.png"
        );
        circle.setFill(new ImagePattern(new Image(Objects.requireNonNull(PowerButton.class.getResourceAsStream("/be/ugent/objprog/minionwars/images/powers/" + typeToImageName.get(type))))));
        nameLabel.setText(name);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        HBox hBox = new HBox();
        hBox.setPrefSize(60, 33);
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(PowerButton.class.getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/range-119533.png"))));

        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        Label label = new Label("" + radius);
        label.getStyleClass().add("groenetext");
        hBox.getChildren().addAll(imageView, label);
        infoBox.getChildren().add(hBox);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        HBox hBox = new HBox();
        hBox.setPrefSize(60, 33);
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(PowerButton.class.getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/attack-D60000.png"))));
        if ("heal".equals(type)) {
            imageView.setImage(new Image(Objects.requireNonNull(PowerButton.class.getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/heal-D60000.png"))));

        }
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        Label label = new Label("" + value);
        label.getStyleClass().add("rodeText");
        hBox.getChildren().addAll(imageView, label);
        infoBox.getChildren().add(hBox);
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
        effectLabel.setText("Effect: " + effect.getName());
        HBox hBox = new HBox();
        hBox.setPrefSize(60, 33);
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(PowerButton.class.getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/duration-0073FF.png"))));
        imageView.setFitHeight(20);
        imageView.setPreserveRatio(true);
        Label label = new Label("" + effect.getDuration());
        label.getStyleClass().add("blauwetext");
        hBox.getChildren().addAll(imageView, label);
        infoBox.getChildren().add(hBox);
    }

    public Power getPower() {
        Power power = powerMap.get(type).get();
        power.setName(name);
        power.setRadius(radius);
        power.setValue(value);
        power.setEffect(effect);
        return power;
    }

    private void selectThis(MouseEvent mouseEvent) {
        Power power = getPower();
        model.setSelectedPower(power);
        model.setStatus(power.getStatus());
    }

    @Override
    public void invalidated(Observable observable) {
        if (model.getSelectedPower() != null && Objects.equals(model.getSelectedPower().getType(), this.type)) {
            getStyleClass().add("selected-power-button");
        } else {
            getStyleClass().removeAll("selected-power-button");
        }
    }

    @Override
    public String toString(){
        return type;
    }
}
