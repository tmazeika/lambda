package me.mazeika.lambda;

/**
 * Represents a body capable of evaluating abstract syntax trees and
 * identifiers
 */
public class Evaluator implements Expr.Visitor<Val> {

    Val evaluate(Expr expr, Environment env) {
        return expr.accept(this, env);
    }

    @Override
    public Val visitIdentifier(Expr.Identifier expr, Environment env) {
        return env.lookup(expr.name);
    }

    @Override
    public Val visitDefine(Expr.Define expr, Environment env) {
        return null;
    }

    @Override
    public Val visitLambda(Expr.Lambda expr, Environment env) {
        return new Val.Lambda(expr.param, expr.body, env);
    }

    @Override
    public Val visitApplication(Expr.Application expr, Environment env) {
        final Val calleeVal = this.evaluate(expr.callee, env);
        final Val.Lambda callee = calleeVal.accept(new ForceLambdaVal());
        final Val arg = this.evaluate(expr.arg, env);
        return this.evaluate(callee.body, callee.env.define(callee.param, arg));
    }
}
