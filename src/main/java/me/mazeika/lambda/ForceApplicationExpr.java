package me.mazeika.lambda;

final class ForceApplicationExpr implements Expr.Visitor<Expr.Application, Val> {

    @Override
    public Expr.Application visitIdentifier(Expr.Identifier expr, Environment<Val> env) {
        throw new EvalException("Expected an application.");
    }

    @Override
    public Expr.Application visitDefine(Expr.Define expr, Environment<Val> env) {
        throw new EvalException("Expected an application.");
    }

    @Override
    public Expr.Application visitLambda(Expr.Lambda expr, Environment<Val> env) {
        throw new EvalException("Expected an application.");
    }

    @Override
    public Expr.Application visitApplication(Expr.Application expr, Environment<Val> env) {
        return expr;
    }
}
