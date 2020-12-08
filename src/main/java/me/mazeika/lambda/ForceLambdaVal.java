package me.mazeika.lambda;

final class ForceLambdaVal implements Val.Visitor<Val.Lambda> {

    @Override
    public Val.Lambda visitLambda(Val.Lambda lambda) {
        return lambda;
    }

    @Override
    public Val.Lambda visitStr(Val.Str s) {
        throw new EvalException("Expected a lambda.");
    }

    @Override
    public Val.Lambda visitInt(Val.Int i) {
        throw new EvalException("Expected a lambda.");
    }
}
