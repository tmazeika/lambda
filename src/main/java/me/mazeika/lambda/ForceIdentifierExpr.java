package me.mazeika.lambda;

final class ForceIdentifierExpr implements Expr.Visitor<Expr.Identifier> {

    @Override
    public Expr.Identifier visitIdentifier(Expr.Identifier expr, Environment env) {
        return expr;
    }

    @Override
    public Expr.Identifier visitDefine(Expr.Define expr, Environment env) {
        throw new EvalException("Expected an identifier.");
    }

    @Override
    public Expr.Identifier visitLambda(Expr.Lambda expr, Environment env) {
        throw new EvalException("Expected an identifier.");
    }

    @Override
    public Expr.Identifier visitApplication(Expr.Application expr, Environment env) {
        throw new EvalException("Expected an identifier.");
    }
}
