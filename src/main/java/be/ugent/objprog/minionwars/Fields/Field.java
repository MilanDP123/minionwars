package be.ugent.objprog.minionwars.Fields;

import be.ugent.objprog.minionwars.MinionWarsModel;
import be.ugent.objprog.minionwars.Minions.Minion;
import be.ugent.objprog.minionwars.Status;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.StrokeType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Field extends Hexagon implements InvalidationListener {
    protected final MinionWarsModel model;
    protected final int homeBase;
    protected static final String imagesPath = "/be/ugent/objprog/minionwars/images/";
    protected String imageName = "tiles/void.png";
    protected Image image = new Image(imagesPath + imageName);
    protected Color borderColor = Color.BLACK;
    protected Overlay overlay;
    protected SimpleObjectProperty<Minion> minion;
    private final Map<Status, Runnable> statusMap = Map.of(
            Status.NONE, this::selectThis,
            Status.AANVALLEN, this::selectThis,
            Status.POWER, this::selectThis,
            Status.BEWEGEN, this::beweegMinion,
            Status.AANVALLEN_BASISAANVAL, this::handleBasisAanval,
            Status.AANVALLEN_SPECIALAANVAL, this:: handleSpecialeAanval,
            Status.POWER_FIREBALL, this::handlePower,
            Status.POWER_LIGHTNING, this::handlePower,
            Status.POWER_HEAL, this::handlePower
    );

    private final Map<Status, Runnable> statusToRadiusMethod = Map.of(
            Status.BEWEGEN, this::showBeweegRadius,
            Status.AANVALLEN, this::showAttackRadius,
            Status.AANVALLEN_BASISAANVAL, this::showAttackRadius,
            Status.AANVALLEN_SPECIALAANVAL, this::showAttackRadius
    );

    private final Status[] powerStatussen = {Status.POWER_LIGHTNING, Status.POWER_HEAL, Status.POWER_FIREBALL};

    public Field(int x, int y, MinionWarsModel model, int homeBase, Overlay overlay) {
        super(x, y);
        this.homeBase = homeBase;
        this.model = model;
        this.overlay = overlay;
        this.minion = new SimpleObjectProperty<>(null);

        model.addListener(this);
        model.startFaseProperty().addListener(this::endStartFase);
        model.selectedFieldProperty().addListener(this::selectedFieldChangend);
        minion.addListener((ignored) -> setImage());
        model.statusProperty().addListener(this::selectedFieldChangend);
    }

    private void setImage(Image image) {
        ImagePattern imagePattern = new ImagePattern(image);
        setFill(imagePattern);
        setStrokeType(StrokeType.INSIDE);
        setStroke(borderColor);
        setStrokeWidth(4.0);
    }

    public void setImage() {
        setImage(getImageName());
    }

    public void hidePane() {
        setImage(image);
    }

    public int getHomeBase() {
        return homeBase;
    }

    public boolean isWalkable() {
        return ! hasMinion();
    }

    public boolean canAttack() {
        return true;
    }

    public void colorReachableNeighbours(int range) {
        if (range <= 0) {
            return;
        }
        range -= 1;
        List<Field> neighbours = model.getNeighbours(this);
        for (Field neighbour: neighbours) {
            if (!Objects.equals(neighbour.getType(), "forest") || range >= 1) {
                neighbour.colorOverlay("green");
                neighbour.colorReachableNeighbours(range);
            }
        }
    }

    public int distanceTo(Field field) {
        int dx = Math.abs(this.getX() - field.getX());
        int dy = Math.abs(this.getY() - field.getY());

        return dy + Math.max((dx - dy)/2, 0);
    }

    public Image getImageName() {
        return hasMinion() ? getMinion().getImage() : image;
    }

    public void setMinion(Minion minion) {
        this.minion.set(minion);
        model.setSelectedField(null);
    }

    public Minion getMinion() {
        return minion.get();
    }

    public boolean hasMinion() {
        return getMinion() != null;
    }

    public void beweegMinion() {
        if (overlay.getStyleClass().contains("overlay-green")){
            model.getSelectedField().getMinion().moveTo(this);
            model.resetFieldColors();
        } else {
            selectThis();
        }
    }

    public void handlePower() {
        model.handlePower();
    }

    public void handleBasisAanval() {
        if (overlay.getStyleClass().contains("overlay-red") && hasMinion()) {
            model.getSelectedField().getMinion().attack(getMinion());
            model.resetFieldColors();
        } else {
            selectThis();
        }
    }

    public void handleSpecialeAanval() {
        if (overlay.getStyleClass().contains("overlay-red") && hasMinion()) {
            model.getSelectedField().getMinion().specialAttack(getMinion());
            model.resetFieldColors();
        } else {
            selectThis();
        }
    }

    protected void mouseClicked(MouseEvent ignored) {
        if (model.isStartFase()) {
            if (hasMinion()) {
                model.setSelectedMinion(getMinion());
            } else {
                model.placeMinion(this);
            }
        } else {
            if (model.getSelectedField() == this) {
                model.setSelectedField(null);
                model.resetFieldColors();
            } else if (statusMap.containsKey(model.getStatus())) {
                statusMap.get(model.getStatus()).run();
            }
        }
    }

    private void selectThis() {
        model.setSelectedField(this);
    }

    public void invalidated(Observable o) {
       if (model.isStartFase()) {
           overlay.clearBorderColor();
           setImage();
           if (model.getSelectedMinion() == getMinion()) {
               overlay.setBorderColor("darkgreen");
           }
           if (model.getPlayingPlayer() != homeBase) {
               hidePane();
               overlay.color("red");
           } else if (getMinion() != null) {
               overlay.clearColors();
           } else if (model.getPlayingPlayer() == homeBase) {
               overlay.color("yellow");

           }
       } else {
           overlay.clearColors();
       }
    }

    public void colorBorder() {
        if (!model.isStartFase() && ! model.getCurrentPlayer().hasMinion(getMinion())) {
            overlay.setBorderColor("red");
        } else if (!model.isStartFase() && model.getCurrentPlayer().getUnhandledMinions().contains(getMinion())) {
            overlay.setBorderColor("aqua");
        }
    }

    public void endStartFase(Observable o) {
        overlay.clearColors();
        setImage();
    }

    public void minionDied(Minion minion) {
        model.removeMinion(minion);
        setMinion(null);
    }

    public void showActionRadius() {
        if (this.hasMinion() && model.getCurrentPlayer().hasMinion(getMinion())) {
            if (statusToRadiusMethod.containsKey(model.getStatus())) {
                statusToRadiusMethod.get(model.getStatus()).run();
            }
        }
    }

    public void showBeweegRadius() {
        if (getMinion().canMove()) {
            colorReachableNeighbours(getMinion().getMovement());
        }
    }

    public void showAttackRadius() {
        if (getMinion().canAttack()) {
            model.colorFieldsInRange(this, getMinion().getMinRange(), getMinion().getMaxRange(), "red", false);
        }
    }

    public void selectedFieldChangend(Observable o) {
        if (model.getSelectedField() == this) {
            model.resetFieldColors();
            overlay.setBorderColor("darkgreen");
            showActionRadius();
        }
    }

    public void clearOverlayColors() {
        overlay.clearColors();
    }

    public void colorOverlay (String color) {
        overlay.color(color);
    }

    public void setBorderColor(String color) {
        overlay.setBorderColor(color);
    }

    public abstract String getType();

    public void mouseEntered(MouseEvent ignored) {
        if (Arrays.asList(powerStatussen).contains(model.getStatus())) {
            model.resetFieldColors();
            if (!model.getSelectedPower().isFriendly() && model.hasOpponentsInRange(0, model.getSelectedPower().getRadius(), this)
            || model.getSelectedPower().isFriendly() && model.hasFriendliesInRange(0, model.getSelectedPower().getRadius(), this)) {
                model.colorFieldsInRange(this, 0, model.getSelectedPower().getRadius(), "blue", model.getSelectedPower().isFriendly());
            } else {
                model.colorFieldsInRange(this, 0, model.getSelectedPower().getRadius(), "red", model.getSelectedPower().isFriendly());
            }

        }
    }

    public Overlay getOverlay() {
        return overlay;
    }
}
