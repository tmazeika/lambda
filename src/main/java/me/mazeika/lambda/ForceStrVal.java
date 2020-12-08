package me.mazeika.lambda;

final class ForceStrVal implements Val.Visitor<Val.Str> {

    @Override
    public Val.Str visitLambda(Val.Lambda lambda) {
        throw new EvalException("Expected a string.");
    }

    @Override
    public Val.Str visitStr(Val.Str s) {
        return s;
    }

    @Override
    public Val.Str visitInt(Val.Int i) {
        throw new EvalException("Expected a string.");
    }
}
