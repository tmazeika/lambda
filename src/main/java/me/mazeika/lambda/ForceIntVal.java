package me.mazeika.lambda;

final class ForceIntVal implements Val.Visitor<Val.Int> {

    @Override
    public Val.Int visitLambda(Val.Lambda lambda) {
        throw new EvalException("Expected an integer.");
    }

    @Override
    public Val.Int visitStr(Val.Str s) {
        throw new EvalException("Expected an integer.");
    }

    @Override
    public Val.Int visitInt(Val.Int i) {
        return i;
    }
}
