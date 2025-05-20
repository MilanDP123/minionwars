package be.ugent.objprog.minionwars.Minions;

import be.ugent.objprog.minionwars.Effects.Effect;
import be.ugent.objprog.minionwars.Fields.Field;
import be.ugent.objprog.minionwars.MinionWarsModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Minion implements InvalidationListener {
    private int x;
    private int y;
    private final String name;
    private final Image image;
    private final String type;
    private final int cost;
    private final int movementDefault;
    private int movement;
    private final int attackDefault;
    private int attack;
    private final SimpleObjectProperty<Integer> defence;
    private final int maxDefence;
    private final int minRange;
    private final int maxRangeDefault;
    private int maxRange;
    private final String effectType;
    private final Integer effectValue;
    private final ArrayList<Effect> currentEffects;
    private boolean handlingEffects;

    private final SimpleBooleanProperty hasMoved;
    private final SimpleBooleanProperty hasAttacked;
    private boolean hasHealed;
    private int healsLeft;

    private boolean hasSpecialAttack;
    private int timesRested;

    private Field field;
    private final MinionWarsModel model;

    public Minion(MinionWarsModel model, String name, String type, int cost, int movement, int attack, int defence, int minRange, int maxRange, String effectType, Integer effectValue, int x, int y) {
        this.model = model;
        model.addListener(this);
        model.startFaseProperty().addListener(ignored -> model.removeListener(this));
        this.name = name;
        this.type = type;
        String imageName = "minions/" + type + ".png";
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/ugent/objprog/minionwars/images/" + imageName)));
        this.cost = cost;
        this.movementDefault = movement;
        this.movement = movement;
        this.attackDefault = attack;
        this.attack = attack;
        this.defence = new SimpleObjectProperty<>(defence);
        this.defence.addListener(ignored -> defenceChanged());
        this.maxDefence = defence;
        this.minRange = minRange;
        this.maxRangeDefault = maxRange;
        this.maxRange = maxRange;
        this.effectType = effectType;
        this.effectValue = effectValue;
        this.currentEffects = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.field = null;
        hasMoved = new SimpleBooleanProperty(false);
        hasAttacked = new SimpleBooleanProperty(false);
        hasHealed = false;
        hasMoved.addListener(ignored -> checkIfHandled());
        hasAttacked.addListener(ignored -> checkIfHandled());

        this.healsLeft = 2;
        hasSpecialAttack = true;
        timesRested = 0;
        handlingEffects = false;
    }

    private void defenceChanged() {
        if (getDefence() <= 0 && !handlingEffects) {
            field.minionDied(this);
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Image getImage() {
        return image;
    }

    public int getCost() {
        return cost;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public int getMaxDefence() {
        return maxDefence;
    }

    public int getDefence() {
        return defence.get();
    }

    public void setDefence(int defence) {
        this.defence.set(Math.min(defence, maxDefence));
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void attack(Minion target) {
        target.setDefence(target.getDefence() - attack);
        hasAttacked.set(true);
        hasHealed = true;
    }

    public void specialAttack(Minion target) {
        if (effectType != null) {
            Effect effect = (effectValue != null)
                    ? model.getNewEffect(effectType, effectValue)
                    : model.getNewEffect(effectType);
            if (effect.isFriendly()) {
                this.addEffect(effect);
            } else {
                target.addEffect(effect);
            }
            hasSpecialAttack = false;
        }
        attack(target);
    }

    public void handleEffects() {
        Iterator<Effect> it = currentEffects.iterator();
        handlingEffects = true;
        while (it.hasNext()) {
            Effect effect = it.next();
            if (effect.getDuration() > 0) {
                effect.handleEffect(this);
            }
            if (effect.getDuration() <= 0) {
                it.remove();
            }
        }
        handlingEffects = false;
    }

    public void addEffect(Effect effect) {
        currentEffects.add(effect);
    }

    public ArrayList<Effect> getCurrentEffects() {
        return currentEffects;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getAttack() {
        return attack;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void blijfStaan() {
        hasMoved.set(true);
        model.resetFieldColors();
    }

    public void moveTo(Field target) {
        field.setMinion(null);
        setField(target);
        setCoordinates(target.getX(), target.getY());
        target.setMinion(this);
        hasMoved.set(true);
        if (!canHeal() && ! model.hasOpponentsInRange(getMinRange(), getMaxRange(), field)) {
            hasAttacked.set(true);
            hasHealed = true;
        }
        model.setSelectedField(target);
    }

    public void heal() {
        if (canHeal()) {
            setDefence(defence.get() + 2);
            healsLeft--;
            hasAttacked.set(true);
            hasHealed = true;
        }
    }

    public boolean canHeal() {
        return healsLeft > 0 && !hasHealed;
    }

    public void resetMinion() {
        hasMoved.set(false);
        hasAttacked.set(false);
        hasHealed = false;
        maxRange = maxRangeDefault;
        attack = attackDefault;
        movement = movementDefault;
    }

    public boolean canAttack() {
        return !hasAttacked.get() && field.canAttack();
    }

    public void setAttacked(boolean attacked) {
        hasAttacked.set(attacked);
    }

    public void setMoved(boolean moved) {
        hasMoved.set(moved);
    }

    public boolean canMove() {
        return !hasMoved.get();
    }

    public void checkIfHandled() {
        if (hasMoved.get() && hasAttacked.get()) {
            model.removeUnhandledMinion(this);
        }
    }

    public boolean canRust() {
        return canMove() && canAttack();
    }

    public void rust() {
        hasMoved.set(true);
        hasAttacked.set(true);
        timesRested++;
        if (timesRested >= 2 && !hasSpecialAttack) {
            timesRested = 0;
            hasSpecialAttack = true;
        }
    }

    public boolean canSpecialAttack() {
        return canAttack() && hasSpecialAttack && effectType != null;
    }

    @Override
    public void invalidated(Observable observable) {
        if (model.getSelectedMinion() == this) {
            field.setBorderColor("darkgreen");
        }
    }
}