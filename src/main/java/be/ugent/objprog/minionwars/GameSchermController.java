package be.ugent.objprog.minionwars;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.Objects;

public class GameSchermController implements InvalidationListener {

    private final MinionWarsModel model;

    private final Image minionsImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/icons/minions-0073FF.png")));

    public ScrollPane gamePane;
    public ScrollPane minionPane;
    public Pane menuPane;
    public Label currentName;
    public ImageView topIcon;
    public Label topText;
    public HBox buttonsBox;
    public Button eindeBeurtButton;
    public Button rustButton;


    public GameSchermController(MinionWarsModel model) {
        this.model = model;

        model.addListener(this);
        model.selectedFieldProperty().addListener(this);
    }

    public void initialize() {
        setUpGamePane();
        setUpMenuPane();
        setLabels();
        addEffectFactories();
        model.startFaseProperty().addListener((observable, oldValue, newValue) -> eindeStartFase());
        eindeBeurtButton.setDisable(model.getCurrentPlayer().getMinions().isEmpty());
    }

    private void setLabels() {
        if (model.isStartFase()) {
            topText.setText("" + model.getCurrentPlayer().getMunten());
        }
        currentName.setText(model.getCurrentPlayer().getName());
    }

    private void setUpGamePane() {
        AnchorPane pane = new AnchorPane();
        gamePane.setContent(pane);
        addHexagons(pane);
    }

    private void setUpMenuPane() {
        VBox pane = new VBox();
        minionPane = new ScrollPane();
        minionPane.setContent(pane);
        minionPane.setHmax(0);
        minionPane.setPrefHeight(700);
        minionPane.setPrefWidth(300);
        minionPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        minionPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        menuPane.getChildren().add(minionPane);
        addMinionButtons(pane);
    }

    private void addHexagons(AnchorPane pane) {
        try {
            model.getXMLReader().makeFields(model.getXMLReader().getFields(), pane);
        } catch (IOException e) {
            System.out.println("Error reading fields: IOException");
        } catch (JDOMException e) {
            System.out.println("Error reading fields: JDOMException");
        }
    }

    private void addMinionButtons(VBox pane) {
        try {
            model.getXMLReader().makeMinionButtons(model.getXMLReader().getMinions(), pane);
        } catch (IOException e) {
            System.out.println("Error reading fields: IOException");
        } catch (JDOMException e) {
            System.out.println("Error reading fields: JDOMException");
        }
    }

    private void addEffectFactories() {
        try {
            model.setEffectFactoriesMap(model.getXMLReader().getEffectFactoriesMap(model.getXMLReader().getEffectElements()));
        } catch (IOException e) {
            System.out.println("Error reading fields: IOException");
        } catch (JDOMException e) {
            System.out.println("Error reading fields: JDOMException");
        }
    }

    public void eindeStartFase() {
        menuPane.getChildren().remove(minionPane);
        minionPane = null;

        topIcon.setImage(minionsImage);
        topText.setTextFill(Color.BLUE);

        try {
            MinionViewer minionViewer = new MinionViewer(model, model.selectedFieldProperty());
            menuPane.getChildren().add(minionViewer);
        } catch (IOException e) {
           e.printStackTrace();
        }

        rustButton = new Button("Rust");
        rustButton.setAlignment(Pos.CENTER);
        rustButton.setContentDisplay(ContentDisplay.CENTER);
        rustButton.setOnAction(this::rust);
        buttonsBox.getChildren().addFirst(rustButton);
        eindeBeurtButton.setDisable(true);
    }

    public void rust(ActionEvent actionEvent) {
        model.rust();
    }

    public void eindeBeurt() {
        model.nextPlayer();
        setLabels();
    }

    @Override
    public void invalidated(Observable observable) {
        if (model.isStartFase()) {
            topText.setText("" + model.getCurrentPlayer().getMunten());
            eindeBeurtButton.setDisable(model.getCurrentPlayer().getMinions().isEmpty());
        }
        else {
            topText.setText(model.getCurrentPlayer().getAmountOfHandledMinions() + "/" + model.getCurrentPlayer().getAmountOfMinions());
            eindeBeurtButton.setDisable(! model.getCurrentPlayer().allMinionsHandled());
            rustButton.setDisable( !(
                            model.getSelectedField() != null
                            && model.getSelectedField().hasMinion()
                            && model.getCurrentPlayer().hasMinion(model.getSelectedField().getMinion())
                            && model.getSelectedField().getMinion().canRust())
            );
        }
    }
}