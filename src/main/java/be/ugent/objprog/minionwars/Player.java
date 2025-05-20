package be.ugent.objprog.minionwars;

import be.ugent.objprog.minionwars.Minions.Minion;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final String nameOpponent;
    private final MinionWarsModel model;
    private int munten;
    private final ArrayList<Minion> minions;
    private ArrayList<Minion> unhandledMinions;
    private final ArrayList<String> powersUsed;

    public Player(String name, int munten, String nameOpponent, MinionWarsModel model) {
        this.name = name;
        this.munten = munten;
        this.nameOpponent = nameOpponent;
        minions = new ArrayList<>();
        unhandledMinions = new ArrayList<>();
        powersUsed = new ArrayList<>();
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public int getMunten() {
        return munten;
    }

    public void setMunten(int munten) {
        this.munten = munten;
    }

    public void addMinion(Minion minion) {
        minions.add(minion);
    }

    public void removeMinion(Minion minion) {
        minions.remove(minion);
        unhandledMinions.remove(minion);
        if (minions.isEmpty() && ! model.isStartFase()) {
            model.setWinner(nameOpponent);
        }
    }

    public ArrayList<Minion> getMinions() {
        return minions;
    }

    public boolean hasMinion(Minion minion) {
        return minions.contains(minion);
    }

    public void resetUnhandledMinions() {
        unhandledMinions = new ArrayList<> (minions);
        unhandledMinions.forEach(Minion::resetMinion);
    }

    public void removeUnhandledMinion(Minion minion) {
        unhandledMinions.remove(minion);
    }

    public ArrayList<Minion> getUnhandledMinions() {
        return unhandledMinions;
    }

    public int getAmountOfMinions() {
        return minions.size();
    }

    public int getAmountOfHandledMinions() {
        return minions.size() - unhandledMinions.size();
    }

    public boolean allMinionsHandled() {
        return unhandledMinions.isEmpty();
    }

    public void handleEffects() {
        List<Minion> minionsToRemove = new ArrayList<>();
        for (Minion minion: minions) {
            minion.handleEffects();
            if (minion.getDefence() <= 0) {
                minionsToRemove.add(minion);
            }
        }
        minionsToRemove.forEach(minion -> minion.getField().minionDied(minion));
    }

    public void usePower(String power) {
        powersUsed.add(power);
    }

    public boolean canUsePower(String power) {
        return !powersUsed.contains(power) && powersUsed.size() < 2;
    }
}