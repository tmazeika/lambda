package me.mazeika.lambda;

final class ForceApplicationExpr implements Expr.Visitor<Expr.Application> {

    @Override
    public Expr.Application visitIdentifier(Expr.Identifier expr, Environment env) {
        throw new EvalException("Expected an application.");
    }

    @Override
    public Expr.Application visitDefine(Expr.Define expr, Environment env) {
        throw new EvalException("Expected an application.");
    }

    @Override
    public Expr.Application visitLambda(Expr.Lambda expr, Environment env) {
        throw new EvalException("Expected an application.");
    }

    @Override
    public Expr.Application visitApplication(Expr.Application expr, Environment env) {
        return expr;
    }
}
