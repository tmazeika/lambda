package me.mazeika.lambda;

final class ForceIdentifierExpr implements Expr.Visitor<Expr.Identifier, Val> {

    @Override
    public Expr.Identifier visitIdentifier(Expr.Identifier expr, Environment<Val> env) {
        return expr;
    }

    @Override
    public Expr.Identifier visitDefine(Expr.Define expr, Environment<Val> env) {
        throw new EvalException("Expected an identifier.");
    }

    @Override
    public Expr.Identifier visitLambda(Expr.Lambda expr, Environment<Val> env) {
        throw new EvalException("Expected an identifier.");
    }

    @Override
    public Expr.Identifier visitApplication(Expr.Application expr, Environment<Val> env) {
        throw new EvalException("Expected an identifier.");
    }
}
