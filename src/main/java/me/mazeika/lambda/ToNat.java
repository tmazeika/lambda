package me.mazeika.lambda;

final class ToNat implements Val.Visitor<Val.Int> {

    @Override
    public Val.Int visitLambda(Val.Lambda lambda) {
        final String f = lambda.param;
        final Expr.Lambda innerLambda =
                lambda.body.accept(new ForceLambdaExpr(), null);
        final String x = innerLambda.accept(new ForceLambdaExpr(), null).param;

        return innerLambda.body.accept(new InnerToNat(f, x), lambda.env);
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
