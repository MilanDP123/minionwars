package be.ugent.objprog.minionwars.Effects;

public interface EffectFactory {
    void setDuration(int duration);
    void setName(String name);
    void setValue(int value);
    Effect createEffect();
    Effect createEffect(int value);
}
