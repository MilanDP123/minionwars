package be.ugent.objprog.minionwars.Powers;

import be.ugent.objprog.minionwars.Minions.Minion;
import be.ugent.objprog.minionwars.Status;

public class Fireball extends Power {

    @Override
    public Status getStatus() {
        return Status.POWER_FIREBALL;
    }

    @Override
    public String getType() {
        return "fireball";
    }

    @Override
    public void handle(Minion target) {
        target.setDefence(target.getDefence() - value);
        super.handle(target);
    }
}
