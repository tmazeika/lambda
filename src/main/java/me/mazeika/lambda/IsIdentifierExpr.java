package me.mazeika.lambda;

final class IsIdentifierExpr implements Expr.Visitor<Boolean> {

    @Override
    public Boolean visitIdentifier(Expr.Identifier expr, Environment env) {
        return true;
    }

    @Override
    public Boolean visitDefine(Expr.Define expr, Environment env) {
        return false;
    }

    @Override
    public Boolean visitLambda(Expr.Lambda expr, Environment env) {
        return false;
    }

    @Override
    public Boolean visitApplication(Expr.Application expr, Environment env) {
        return false;
    }
}
