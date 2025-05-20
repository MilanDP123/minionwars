package be.ugent.objprog.minionwars.Effects;

import be.ugent.objprog.minionwars.Minions.Minion;

public class Heal extends Effect{
    public Heal(int duration, String name, int value) {
        super(duration, name, value);
    }

    @Override
    public void handleEffect(Minion minion) {
        minion.setDefence(minion.getDefence() + value);
        super.handleEffect(minion);
    }

    @Override
    public boolean isFriendly() {
        return true;
    }

    @Override
    public Effect clone() {
        return new Heal(duration, name, value);
    }
}
