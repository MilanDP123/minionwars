package be.ugent.objprog.minionwars.Effects;

public abstract class AbstractEffectFactory implements EffectFactory {
    protected int duration;
    protected String name;
    protected int value;

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }
}
