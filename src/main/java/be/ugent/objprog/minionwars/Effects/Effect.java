package be.ugent.objprog.minionwars.Effects;

import be.ugent.objprog.minionwars.Minions.Minion;

public abstract class Effect {
    protected int duration;
    protected String name;
    protected int value;

    public Effect(int duration, String name, int value) {
        this.duration = duration;
        this.name = name;
        this.value = value;
    }

    public int getDuration() {
        return duration;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void handleEffect(Minion minion) {
        duration -= 1;
    }

    public boolean isFriendly() {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract Effect clone();
}