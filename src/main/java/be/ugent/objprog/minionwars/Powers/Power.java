package be.ugent.objprog.minionwars.Powers;

import be.ugent.objprog.minionwars.Effects.Effect;
import be.ugent.objprog.minionwars.Minions.Minion;
import be.ugent.objprog.minionwars.Status;

public abstract class Power {
    protected String name;
    protected int radius;
    protected int value;
    protected Effect effect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public abstract Status getStatus();

    public boolean isFriendly() {
        return false;
    }

    public abstract String getType();

    public void handle(Minion target) {
        if (effect != null) {
            target.addEffect(effect.clone());
        }
    }

    public String toString() {
        return getType();
    }
}
