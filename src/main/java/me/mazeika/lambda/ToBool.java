package me.mazeika.lambda;

final class ToBool implements Val.Visitor<Val.Str> {

    @Override
    public Val.Str visitLambda(Val.Lambda lambda) {
        final String x = lambda.param;
        final Expr.Lambda innerLambda =
                lambda.body.accept(new ForceLambdaExpr(), null);
        final String y = innerLambda.accept(new ForceLambdaExpr(), null).param;

        final String innerId = innerLambda.body.accept(new ForceIdentifierExpr(), null).name;

        if (innerId.equals(x)) {
            return new Val.Str("true");
        }
        if (innerId.equals(y)) {
            return new Val.Str("false");
        }
        throw new EvalException("Not a true/false value.");
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
