package me.mazeika.lambda;

/**
 * Represents a body capable of evaluating abstract syntax trees and
 * identifiers
 */
public class BReducer implements Expr.Visitor<Expr> {

    Expr reduce(Expr expr, Environment env) {
        return expr.accept(this, env);
    }

    @Override
    public Expr visitIdentifier(Expr.Identifier expr, Environment env) {
        final Val val = env.lookup(expr.name);
        if (val == null) {
            return expr;
        }
        final Val.Lambda lambda = val.accept(new ForceLambdaVal());
        return new Expr.Lambda(lambda.param, lambda.body);
    }

    @Override
    public Expr visitDefine(Expr.Define expr, Environment env) {
        return expr;
    }

    @Override
    public Expr visitLambda(Expr.Lambda expr, Environment env) {
        return expr;
    }

    @Override
    public Expr visitApplication(Expr.Application expr, Environment env) {
        expr.callee.accept(new BReducer(), env.define())
//        final Val calleeVal = this.evaluate(expr.callee, env);
//        final Val.Lambda callee = calleeVal.accept(new ForceLambdaVal());
//        final Val arg = this.evaluate(expr.arg, env);
//        return this.evaluate(callee.body, callee.env.define(callee.param, arg));
    }
}
