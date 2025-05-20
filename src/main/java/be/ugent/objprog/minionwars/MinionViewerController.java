package be.ugent.objprog.minionwars;

import be.ugent.objprog.minionwars.Fields.Field;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class MinionViewerController implements InvalidationListener {
    private static final Map<String, Status> statusMap = Map.of(
            "Bewegen", Status.BEWEGEN,
            "Aanvallen", Status.AANVALLEN,
            "Bonus", Status.POWER
    );

    private static final Map<String, String> fieldNederlands = Map.of(
            "dirt", "Aarde",
            "water", "Water",
            "mountains", "Berg",
            "forest", "Bos"
    );

    private static final Status[] tabStatus = {Status.BEWEGEN, Status.AANVALLEN, Status.POWER};

    public HBox topBox;
    public Circle icon;
    public Label nameLabel;
    public Label tileLabel;
    public Label damageLabel;
    public ImageView damageIcon;
    public Label defenceLabel;
    public ImageView defenceIcon;
    public TabPane tabPane;
    public VBox effectsInfoBox;
    public Button blijfStaanButton;
    public Button basisAanvalButton;
    public Button specialAanvalButton;
    public Button healButton;
    public VBox bonusBox;

    private final MinionWarsModel model;
    private final SimpleObjectProperty<Field> field;

    public MinionViewerController(MinionWarsModel model, SimpleObjectProperty<Field> field) {
        this.model = model;
        this.field = field;
        model.addListener(this);
        field.addListener(this);
    }

    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(this::selectionChanged);
        try {
            model.getXMLReader().addPowerButtons(
                    model.getXMLReader().getPowerElements(),
                    model.getEffectFactoriesMap(),
                    bonusBox
            );

        } catch (IOException | JDOMException e) {
            throw new RuntimeException(e);
        }
    }

    public void blijfStaan() {
        field.get().getMinion().blijfStaan();
    }

    public void basisAanval() {
        model.setStatus(Status.AANVALLEN_BASISAANVAL);

    }

    public void specialeAanval() {
        model.setStatus(Status.AANVALLEN_SPECIALAANVAL);
    }

    public void healMinion() {
        model.heal(model.getSelectedField().getMinion());
    }

    public void selectionChanged(Observable observable) {
        model.setStatus(statusMap.get(tabPane.getSelectionModel().getSelectedItem().getText()));
        if (model.getStatus() != Status.POWER) {
            model.setSelectedPower(null);
        }
    }

    @Override
    public void invalidated(Observable observable) {
        Field field = this.field.get();

        topBox.setVisible(field != null);
        tabPane.setVisible(field != null);
        effectsInfoBox.setVisible(field != null);
        if (field != null) {
            if (field.hasMinion()) {
                effectsInfoBox.getChildren().clear();
                field.getMinion().getCurrentEffects().forEach(
                        effect -> {
                            if (effect.getDuration() > 0) {
                                HBox effectBox = new HBox();
                                effectBox.setPrefWidth(300);
                                Label label = new Label(effect.getName());
                                label.setPrefSize(200, 20);
                                effectBox.getChildren().add(label);
                                Label label2 = new Label("nog " + effect.getDuration() + " beurt" + (effect.getDuration() > 1? "en" : ""));
                                label2.setPrefSize(100, 20);
                                label2.setTextAlignment(TextAlignment.RIGHT);
                                effectBox.getChildren().add(label2);
                                effectsInfoBox.getChildren().add(effectBox);
                            }
                        });
                effectsInfoBox.setPrefHeight(effectsInfoBox.getChildren().size() * 20);
                if (Arrays.asList(tabStatus).contains(model.getStatus())) {
                    tabPane.getSelectionModel().select(Arrays.asList(tabStatus).indexOf(model.getStatus()));
                }
                nameLabel.setText(field.getMinion().getName());
                defenceLabel.setText(field.getMinion().getDefence() + "/" + field.getMinion().getMaxDefence());
                damageLabel.setText("" + field.getMinion().getAttack());
                blijfStaanButton.setDisable(! field.getMinion().canMove());
                basisAanvalButton.setDisable(! field.getMinion().canAttack() || !model.hasOpponentsInRange(field.getMinion().getMinRange(), field.getMinion().getMaxRange(), field));
                specialAanvalButton.setDisable(! field.getMinion().canSpecialAttack() || !model.hasOpponentsInRange(field.getMinion().getMinRange(), field.getMinion().getMaxRange(), field));
                healButton.setDisable(! field.getMinion().canHeal());

                for(Node node: bonusBox.getChildren()) {
                    node.setDisable(!model.getCurrentPlayer().canUsePower(node.toString()));
                }
            }
            tabPane.setVisible(field.hasMinion() && model.getCurrentPlayer().hasMinion(field.getMinion()));
            nameLabel.setVisible(field.hasMinion());
            defenceLabel.setVisible(field.hasMinion());
            defenceIcon.setVisible(field.hasMinion());
            damageLabel.setVisible(field.hasMinion());
            damageIcon.setVisible(field.hasMinion());
            effectsInfoBox.setVisible(field.hasMinion());
            icon.setFill(new ImagePattern(field.getImageName()));
            tileLabel.setText("Ondergrond: " + fieldNederlands.get(field.getType()));
        }
    }
}
