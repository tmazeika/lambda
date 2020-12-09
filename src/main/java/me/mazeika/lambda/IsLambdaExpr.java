package me.mazeika.lambda;

final class IsLambdaExpr implements Expr.Visitor<Boolean, Val> {

    @Override
    public Boolean visitIdentifier(Expr.Identifier expr, Environment<Val> env) {
        return false;
    }

    @Override
    public Boolean visitDefine(Expr.Define expr, Environment<Val> env) {
        return false;
    }

    @Override
    public Boolean visitLambda(Expr.Lambda expr, Environment<Val> env) {
        return true;
    }

    @Override
    public Boolean visitApplication(Expr.Application expr, Environment<Val> env) {
        return false;
    }
}
