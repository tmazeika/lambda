package me.mazeika.lambda;

/**
 * Represents a body capable of evaluating abstract syntax trees and
 * identifiers
 */
public class Definer implements Expr.Visitor<Environment> {

    Environment define(Expr expr, Environment env) {
        return expr.accept(this, env);
    }

    @Override
    public Environment visitIdentifier(Expr.Identifier expr, Environment env) {
        return env;
    }

    @Override
    public Environment visitDefine(Expr.Define expr, Environment env) {
        return env.define(expr.id, new Evaluator().evaluate(expr.body, env));
    }

    @Override
    public Environment visitLambda(Expr.Lambda expr, Environment env) {
        return env;
    }

    @Override
    public Environment visitApplication(Expr.Application expr,
                                        Environment env) {
        return env;
    }
}
