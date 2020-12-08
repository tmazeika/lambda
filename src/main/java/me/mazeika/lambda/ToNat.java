package me.mazeika.lambda;

final class ToNat implements Val.Visitor<Val.Int> {

    @Override
    public Val.Int visitLambda(Val.Lambda lambda) {
        String f = lambda.param;
        Expr innerBody = lambda.body;
        Expr innerBody2 = innerBody.accept(new ForceLambdaExpr(), null).body;
        String x = innerBody.accept(new ForceLambdaExpr(), null).param;
    }

    @Override
    public Val.Int visitStr(Val.Str str) {
        throw new EvalException("Expected a lambda.");
    }

    @Override
    public Val.Int visitInt(Val.Int i) {
        throw new EvalException("Expected a lambda.");
    }
}
