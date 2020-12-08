package me.mazeika.lambda;

final class ToBool implements Val.Visitor<Val.Str> {

    @Override
    public Val.Str visitLambda(Val.Lambda lambda) {
        final Evaluator evaluator = new Evaluator();
        final Val.Lambda inner = evaluator
                .evaluate(lambda.body,
                        lambda.env.define(lambda.param, new Val.Str("true")))
                .accept(new ForceLambdaVal());
        return evaluator
                .evaluate(inner.body,
                        inner.env.define(inner.param, new Val.Str("false")))
                .accept(new ForceStrVal());
    }

    @Override
    public Val.Str visitStr(Val.Str str) {
        throw new EvalException("Expected a lambda.");
    }

    @Override
    public Val.Str visitInt(Val.Int i) {
        throw new EvalException("Expected a lambda.");
    }
}
