package me.mazeika.lambda;

final class ForceLambdaExpr implements Expr.Visitor<Expr.Lambda, Val> {

    @Override
    public Expr.Lambda visitIdentifier(Expr.Identifier expr, Environment<Val> env) {
        throw new EvalException("Expected a lambda.");
    }

    @Override
    public Expr.Lambda visitDefine(Expr.Define expr, Environment<Val> env) {
        throw new EvalException("Expected a lambda.");
    }

    @Override
    public Expr.Lambda visitLambda(Expr.Lambda expr, Environment<Val> env) {
        return expr;
    }

    @Override
    public Expr.Lambda visitApplication(Expr.Application expr, Environment<Val> env) {
        throw new EvalException("Expected a lambda.");
    }
}
