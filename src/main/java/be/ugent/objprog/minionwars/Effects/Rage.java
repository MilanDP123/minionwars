package be.ugent.objprog.minionwars.Effects;

import be.ugent.objprog.minionwars.Minions.Minion;

public class Rage extends Effect {
    public Rage(int duration, String name, int value) {
        super(duration, name, value);
    }

    @Override
    public void handleEffect(Minion minion) {
        minion.setAttack(minion.getAttack() + value);
        super.handleEffect(minion);
    }

    @Override
    public boolean isFriendly() {
        return true;
    }

    @Override
    public Effect clone() {
        return new Rage(duration, name, value);
    }
}
