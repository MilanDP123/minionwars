package be.ugent.objprog.minionwars.Powers;

import be.ugent.objprog.minionwars.Minions.Minion;
import be.ugent.objprog.minionwars.Status;

public class HealPower extends Power {

    @Override
    public Status getStatus() {
        return Status.POWER_HEAL;
    }

    @Override
    public boolean isFriendly() {
        return true;
    }

    @Override
    public String getType() {
        return "heal";
    }

    @Override
    public void handle(Minion target) {
        target.setDefence(target.getDefence() + value);
        super.handle(target);
    }
}
