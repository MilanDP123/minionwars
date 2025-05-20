package be.ugent.objprog.minionwars.Effects;

public class BlindnessFactory extends AbstractEffectFactory {

    @Override
    public Effect createEffect() {
        return new Blindness(duration, name, value);
    }

    @Override
    public Effect createEffect(int value) {
        return new Blindness(duration, name, value);
    }
}
