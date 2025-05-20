package be.ugent.objprog.minionwars.Effects;

public class ParalysisFactory extends AbstractEffectFactory {

    @Override
    public Effect createEffect() {
        return new Paralysis(duration, name, value);
    }

    @Override
    public Effect createEffect(int value) {
        return new Paralysis(duration, name, value);
    }
}
