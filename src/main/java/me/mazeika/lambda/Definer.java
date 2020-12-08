package me.mazeika.lambda;

/**
 * Represents a body capable of evaluating abstract syntax trees and
 * identifiers
 */
public class Definer implements Expr.Visitor<Environment<Expr>, Expr> {

    Environment<Expr> define(Expr expr, Environment<Expr> env) {
        return expr.accept(this, env);
    }

    @Override
    public Environment<Expr> visitIdentifier(Expr.Identifier expr, Environment<Expr> env) {
        return env;
    }

    @Override
    public Environment<Expr> visitDefine(Expr.Define expr, Environment<Expr> env) {
        return env.define(expr.id, expr.body);
    }

    @Override
    public Environment<Expr> visitLambda(Expr.Lambda expr, Environment<Expr> env) {
        return env;
    }

    @Override
    public Environment<Expr> visitApplication(Expr.Application expr,
                                        Environment<Expr> env) {
        return env;
    }
}
