package be.ugent.objprog.minionwars.Minions;

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
import javafx.scene.text.TextAlignment;

import java.util.Objects;

public class MinionButton extends HBox implements InvalidationListener {
    private final String name;
    private final String type;
    private final int cost;
    private final int movement;
    private final int attack;
    private final int defence;
    private final int minRange;
    private final int maxRange;
    private final String effectType;
    private final Integer effectValue;

    private final MinionWarsModel model;

    public MinionButton(String name, String type, int cost, int movement, int attack, int defence, int minRange, int maxRange, String effectType, Integer effectValue, MinionWarsModel model) {
        this.name = name;
        this.type = type;
        String imageName = "minions/" + type + ".png";
        this.cost = cost;
        this.movement = movement;
        this.attack = attack;
        this.defence = defence;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.effectType = effectType;
        this.effectValue = effectValue;

        this.model = model;
        setMaxSize(250, 100);
        setAlignment(Pos.CENTER);

        Circle circle = new Circle();
        circle.setFill(new ImagePattern(new Image(Objects.requireNonNull(MinionButton.class.getResourceAsStream("/be/ugent/objprog/minionwars/images/" + imageName)))));
        circle.setRadius(30);

        getChildren().add(circle);
        setMargin(circle, new Insets(5, 2, 5, 5));

        Label nameLabel = new Label();
        nameLabel.setText(name);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setWrapText(true);
        nameLabel.setMinSize(140, 100);
        getChildren().add(nameLabel);

        VBox vBox = new VBox();
        vBox.setMinSize(100, 100);

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setMinSize(100, 33);

        Label costLabel = new Label(cost + "");
        costLabel.setAlignment(Pos.CENTER);
        costLabel.setMinSize(20, 33);
        costLabel.getStyleClass().add("geleText");
        ImageView costImageView = new ImageView(new Image(Objects.requireNonNull(MinionButton.class.getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/coin-FFB900.png"))));
        costImageView.setFitHeight(33);
        costImageView.setFitWidth(20);
        costImageView.setPreserveRatio(true);
        box.getChildren().addAll(costImageView, costLabel);


        vBox.getChildren().add(box);

        HBox box2 = new HBox();
        box2.setAlignment(Pos.CENTER);
        box2.setMinSize(100, 33);

        Label attackLabel = new Label(attack + "");
        attackLabel.setAlignment(Pos.CENTER);
        attackLabel.setMinSize(20, 33);
        attackLabel.getStyleClass().add("rodeText");
        ImageView attackImageView = new ImageView(new Image(Objects.requireNonNull(MinionButton.class.getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/attack-D60000.png"))));
        attackImageView.setFitHeight(33);
        attackImageView.setFitWidth(20);
        attackImageView.setPreserveRatio(true);
        box2.getChildren().addAll(attackImageView, attackLabel);

        vBox.getChildren().add(box2);

        HBox box3 = new HBox();
        box3.setAlignment(Pos.CENTER);
        box3.setMinSize(100, 33);

        Label defenceLabel = new Label(defence + "");
        defenceLabel.setAlignment(Pos.CENTER);
        defenceLabel.setMinSize(20, 33);
        defenceLabel.getStyleClass().add("rodeText");
        ImageView defenceImageView = new ImageView(new Image(Objects.requireNonNull(MinionButton.class.getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/health-D60000.png"))));
        defenceImageView.setFitHeight(33);
        defenceImageView.setFitWidth(20);
        defenceImageView.setPreserveRatio(true);
        box3.getChildren().addAll(defenceImageView, defenceLabel);

        vBox.getChildren().add(box3);
        getChildren().add(vBox);

        model.addListener(this);
        setOnMousePressed(this::selectThis);
    }

    public Minion createMinion(int x, int y) {
        return new Minion(model, name, type, cost, movement, attack, defence, minRange, maxRange, effectType, effectValue, x, y);
    }

    public int getCost() {
        return cost;
    }

    public boolean canBeBought() {
        return ! isDisable();
    }

    public String getName() {
        return name;
    }

    private void selectThis(MouseEvent event) {
        model.setSelectedButton(this);
    }

    @Override
    public void invalidated(Observable observable) {
        setDisable(model.getCurrentPlayer().getMunten() < cost);
        if (! canBeBought()) {
            getStyleClass().add("disabled");
        } else {
            getStyleClass().removeAll("disabled");
        }
    }
}
