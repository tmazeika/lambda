package me.mazeika.lambda;

/**
 * Represents a body capable of evaluating abstract syntax trees and
 * identifiers
 */
public class Evaluator implements Expr.Visitor<Expr> {

    Expr evaluate(Expr expr, Environment env) {
        return expr.accept(this, env);
    }

    @Override
    public Expr visitIdentifier(Expr.Identifier expr, Environment env) {
        final Expr resolved = env.get(expr);
        if (resolved == null) {
            return expr;
        }
        return this.evaluate(resolved, env);
    }

    @Override
    public Expr visitDefine(Expr.Define expr, Environment env) {
        env.define(expr.id, expr.body);
        return expr;
    }

    @Override
    public Expr visitLambda(Expr.Lambda expr, Environment env) {
        return expr;
    }

    @Override
    public Expr visitApplication(Expr.Application expr, Environment env) {
        final Expr callee = this.evaluate(expr.callee, env);
        if (callee instanceof Expr.Plus1) {
            return new Expr.Integer(((Expr.Integer) expr.arg).val + 1);
        }
        if (!(callee instanceof Expr.Lambda)) {
            return new Expr.Application(callee, expr.arg);
        }
        final Expr.Lambda lambda = (Expr.Lambda) callee;
        final Environment callEnv = new Environment(env);
        callEnv.define(lambda.param, this.evaluate(expr.arg, env));
        return this.evaluate(lambda.body, callEnv);
    }
}
