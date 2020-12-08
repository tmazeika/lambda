package me.mazeika.lambda;

final class ForceLambdaExpr implements Expr.Visitor<Expr.Lambda> {

    @Override
    public Expr.Lambda visitIdentifier(Expr.Identifier expr, Environment env) {
        throw new EvalException("Expected a lambda.");
    }

    @Override
    public Expr.Lambda visitDefine(Expr.Define expr, Environment env) {
        throw new EvalException("Expected a lambda.");
    }

    @Override
    public Expr.Lambda visitLambda(Expr.Lambda expr, Environment env) {
        return expr;
    }

    @Override
    public Expr.Lambda visitApplication(Expr.Application expr, Environment env) {
        throw new EvalException("Expected a lambda.");
    }
}
