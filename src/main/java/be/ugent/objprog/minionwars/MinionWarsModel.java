package be.ugent.objprog.minionwars;

import be.ugent.objprog.minionwars.Effects.Effect;
import be.ugent.objprog.minionwars.Effects.EffectFactory;
import be.ugent.objprog.minionwars.Fields.Field;
import be.ugent.objprog.minionwars.Powers.Power;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.ugent.objprog.minionwars.Minions.MinionButton;
import be.ugent.objprog.minionwars.Minions.Minion;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;

public class MinionWarsModel implements Observable {
    private final String xmlPath;
    private final ArrayList<InvalidationListener> listeners;

    private Field[][] fields;
    private ArrayList<Field> fieldsList;

    private Stage stage;

    private Player player1;
    private Player player2;

    private String winner;

    private int playingPlayer;
    private final SimpleBooleanProperty startFase;

    private MinionButton selectedButton;
    private Minion selectedMinion;
    private final SimpleObjectProperty<Power> selectedPower;
    private final SimpleObjectProperty<Field> selectedField;
    private final SimpleObjectProperty<Status> status;
    private Map<String, EffectFactory> effectFactoriesMap;

    public MinionWarsModel(String xmlPath) {
        this.xmlPath = xmlPath;
        this.player1 = null;
        this.player2 = null;
        this.playingPlayer = 1;
        this.startFase = new SimpleBooleanProperty(true);

        this.selectedButton = null;
        this.selectedMinion = null;
        selectedField = new SimpleObjectProperty<>(null);
        status = new SimpleObjectProperty<>(Status.NONE);
        selectedPower = new SimpleObjectProperty<>(null);
        this.winner = null;

        listeners = new ArrayList<>();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public XMLReader getXMLReader() {
        return new XMLReader(xmlPath, this);
    }

    public void setFields(ArrayList<Field> fieldArrayList, int maxX, int maxY) {
        fieldsList = fieldArrayList;
        fields = new Field[maxY][maxX];
        for (Field f: fieldArrayList) {
            fields[f.getY()][f.getX()] = f;
        }
    }

    public void setEffectFactoriesMap(Map<String, EffectFactory> effectFactoriesMap) {
        this.effectFactoriesMap = effectFactoriesMap;
    }

    public Map<String, EffectFactory> getEffectFactoriesMap() {
        return effectFactoriesMap;
    }

    public Effect getNewEffect(String type, int value) {
        if (effectFactoriesMap.containsKey(type)) {
            return effectFactoriesMap.get(type).createEffect(value);
        }
        return null;
    }

    public Effect getNewEffect(String type) {
        if (effectFactoriesMap.containsKey(type)) {
            return effectFactoriesMap.get(type).createEffect();
        }
        return null;
    }

    private void fireInvalidationEvent() {
        for (InvalidationListener listener : listeners) {
            listener.invalidated(this);
        }
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        listeners.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        listeners.remove(invalidationListener);
    }

    public void setMinion(Minion minion, int x, int y) {
        fields[y][x].setMinion(minion);
        getCurrentPlayer().addMinion(minion);
        fireInvalidationEvent();
    }

    public void nextPlayer() {
        playingPlayer = 2 - (playingPlayer + 1) % 2;
        if (isStartFase() && playingPlayer == 1) {
            endStartFase();
        }
        setSelectedButton(null);
        setSelectedField(null);
        getCurrentPlayer().resetUnhandledMinions();
        getCurrentPlayer().handleEffects();
        fireInvalidationEvent();
    }

    public int getPlayingPlayer() {
        return playingPlayer;
    }

    public Player getPlayer(int i) {
        return i == 1 ? player1 : player2;
    }

    public Player getCurrentPlayer() {
        return getPlayer(playingPlayer);
    }

    public void removeUnhandledMinion(Minion minion) {
        getCurrentPlayer().removeUnhandledMinion(minion);
        fireInvalidationEvent();
    }

    public void setSelectedButton(MinionButton minionButton) {
        if (this.selectedButton != null) {
            this.selectedButton.getStyleClass().remove("selected-button");
        }
        this.selectedButton = minionButton;
        this.selectedMinion = null;
        if (this.selectedButton != null) {
            this.selectedButton.getStyleClass().add("selected-button");
        }
        fireInvalidationEvent();
    }

    public void setSelectedField(Field field) {
        selectedField.set(field);
        if (field != null && field.hasMinion()) {
            setStatus(Status.BEWEGEN);
        }
    }

    public void endStartFase() {
        startFase.set(false);
        fireInvalidationEvent();
    }

    public SimpleBooleanProperty startFaseProperty() {
        return startFase;
    }

    public boolean isStartFase() {
        return startFase.get();
    }

    public void setSelectedMinion(Minion minion) {
        if (this.selectedButton != null) {
            this.selectedButton.getStyleClass().remove("selected-button");
        }
        this.selectedMinion = minion;
        this.selectedButton = null;
        fireInvalidationEvent();
    }

    public Minion getSelectedMinion() {
        return selectedMinion;
    }

    public List<Field> getNeighbours(Field field) {
        int[][] richtingen = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {-2, 0}, {2, 0}};
        List<Field> neighbours = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if ((field.getX() + richtingen[i][0]) >= 0 && (field.getX() + richtingen[i][0]) < fields[0].length
                && (field.getY() + richtingen[i][1]) >= 0 && (field.getY() + richtingen[i][1]) < fields.length) {
                Field f = fields[field.getY() + richtingen[i][1]][field.getX() + richtingen[i][0]];
                if (f != null && f.isWalkable()) {
                    neighbours.add(f);
                }
            }
        }
        return neighbours;

    }

    public Power getSelectedPower() {
        return selectedPower.get();
    }

    public void setSelectedPower(Power power) {
        selectedPower.set(power);
    }

    public SimpleObjectProperty<Power> selectedPowerProperty() {
        return selectedPower;
    }

    public void colorFieldsInRange(Field field, int minRange, int maxRange, String color, boolean friendly) {
        for (Field f: fieldsList) {
            int distance = field.distanceTo(f);
            if (minRange <= distance && distance <= maxRange) {
                if (!f.hasMinion() || (
                        (friendly && getCurrentPlayer().hasMinion(f.getMinion()))
                            || (!friendly && !getCurrentPlayer().hasMinion(f.getMinion())))
                ) {
                    f.colorOverlay(color);
                }
            }
        }
    }

    public void placeMinion(Field field) {
        if (selectedButton != null
                && field.getHomeBase() == playingPlayer
                && !selectedButton.isDisable()) {
            Minion minion = selectedButton.createMinion(field.getX(), field.getY());
            minion.setField(field);
            setMinion(minion, field.getX(), field.getY());
            getCurrentPlayer().setMunten(getCurrentPlayer().getMunten() - selectedButton.getCost());
            fireInvalidationEvent();
        }
    }

    public void removeSelectedMinion() {
        if (selectedMinion != null) {
            fields[selectedMinion.getY()][selectedMinion.getX()].setMinion(null);
            if (startFase.get()) {
                getCurrentPlayer().setMunten(getCurrentPlayer().getMunten() + selectedMinion.getCost());
            }
            getCurrentPlayer().removeMinion(selectedMinion);
            selectedMinion = null;
            fireInvalidationEvent();
        }
    }

    public void removeMinion(Minion minion) {
        if (player1.hasMinion(minion)) {
            player1.removeMinion(minion);
            fireInvalidationEvent();
        } else if (player2.hasMinion(minion)) {
            player2.removeMinion(minion);
            fireInvalidationEvent();
        }
    }

    public void setWinner(String winner) {
        this.winner = winner;
        try {
            EindScherm eindScherm = new EindScherm(stage, this);
            eindScherm.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getWinner() {
        return winner;
    }

    public Field getSelectedField() {
        return selectedField.get();
    }

    public SimpleObjectProperty<Field> selectedFieldProperty() {
        return selectedField;
    }

    public Status getStatus() {
        return status.get();
    }

    public void setStatus(Status status) {
        this.status.set(status);
    }

    public SimpleObjectProperty<Status> statusProperty() {
        return status;
    }

    public void resetFieldColors() {
        for (Field f: fieldsList) {
            f.clearOverlayColors();
        }
        fireInvalidationEvent();
    }

    public void rust() {
        getSelectedField().getMinion().rust();
    }

    public void heal(Minion minion) {
        minion.heal();
        fireInvalidationEvent();
    }

    public boolean hasOpponentsInRange(int minRange, int maxRange, Field field) {
        for (Field f: fieldsList) {
            int distance = field.distanceTo(f);
            if (
                minRange <= distance
                && distance <= maxRange
                && f.hasMinion()
                && !getCurrentPlayer().hasMinion(f.getMinion())
            ) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFriendliesInRange(int minRange, int maxRange, Field field) {
        for (Field f: fieldsList) {
            int distance = field.distanceTo(f);
            if (
                    minRange <= distance
                            && distance <= maxRange
                            && f.hasMinion()
                            && getCurrentPlayer().hasMinion(f.getMinion())
            ) {
                return true;
            }
        }
        return false;
    }

    public void handlePower() {
        getCurrentPlayer().usePower(getSelectedPower().toString());
        for (Field f: fieldsList) {
            if (f.getOverlay().getStyleClass().contains("overlay-blue") && f.hasMinion()) {
                getSelectedPower().handle(f.getMinion());
            }
        }

        setSelectedPower(null);
        setStatus(Status.NONE);
    }
}
