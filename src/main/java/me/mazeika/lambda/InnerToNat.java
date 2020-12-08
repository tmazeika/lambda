package me.mazeika.lambda;

final class InnerToNat implements Val.Visitor<Val.Int> {

    @Override
    public Val.Int visitLambda(Val.Lambda lambda) {
        if (innerBody2.accept(new IsIdentifierExpr(), null)) {
            if (innerBody2.accept(new ForceIdentifierExpr(),
                    null).name.equals(x)) {
                return new Val.Int(i);
            }
            throw new EvalException("Not a church-numeral.");
        } else if (innerBody2.accept(new IsApplicationExpr(), null)) {
            Expr.Application app =
                    innerBody2.accept(new ForceApplicationExpr(), null);
            if (!app.callee.accept(new IsIdentifierExpr(), null) ||
                !app.callee.accept(new ForceIdentifierExpr(),
                        null).name.equals(f)) {
                throw new EvalException("Not a church-numeral.");
            }
            i++;
            innerBody2 =
                    new Evaluator().evaluate(app.arg, lambda.env).accept(new ForceLambdaVal()).body;
        } else {
            throw new EvalException("Not a church-numeral.");
        }
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
