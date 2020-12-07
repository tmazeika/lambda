package me.mazeika.lambda;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

abstract class Expr {

    abstract <R> R accept(Expr.Visitor<R> visitor, Environment env);

    /*
    <EXPR> ::= <id>                                  -- Identifier
             | (define <id> <EXPR>)                  -- Define
             | (lambda (<id> ...) <EXPR> <EXPR> ...) -- Lambda
             | (<EXPR> <EXPR> <EXPR> ...)            -- Application
    */

    static class Identifier extends Expr {

        final Token id;

        Identifier(Token id) {
            this.id = id;
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Environment env) {
            return visitor.visitIdentifier(this, env);
        }

        @Override
        public String toString() {
            return this.id.getLexeme();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Identifier that = (Identifier) o;
            return this.id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.id);
        }
    }

    static class Define extends Expr {

        final Identifier id;
        final Expr body;

        Define(Identifier id, Expr expr) {
            this.id = id;
            this.body = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Environment env) {
            return visitor.visitDefine(this, env);
        }

        @Override
        public String toString() {
            return String.format("(define %s %s)", this.id, this.body);
        }
    }

    static class Lambda extends Expr implements Callable {

        final List<Identifier> params;
        final Expr body;

        Lambda(List<Identifier> params, Expr body) {
            this.params = List.copyOf(params);
            this.body = body;
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Environment env) {
            return visitor.visitLambda(this, env);
        }

        @Override
        public Expr call(Evaluator evaluator, List<Expr> arguments,
                         Environment env) {
            final Environment fnEnv = new Environment(env);
            for (int i = 0; i < this.params.size(); i++) {
                fnEnv.define(this.params.get(i),
                        evaluator.evaluate(arguments.get(i), env));
            }
            return evaluator.evaluate(this.body, fnEnv);
        }

        @Override
        public String toString() {
            return String.format("(lambda (%s) %s)", this.params
                    .stream()
                    .map(Identifier::toString)
                    .collect(Collectors.joining(" ")), this.body.toString());
        }
    }

    static class Application extends Expr {

        final List<Expr> args;

        Application(List<Expr> args) {
            this.args = List.copyOf(args);
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Environment env) {
            return visitor.visitApplication(this, env);
        }

        @Override
        public String toString() {
            return String.format("(%s)", this.args
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" ")));
        }
    }

    interface Visitor<T> {
        T visitIdentifier(Identifier expr, Environment env);

        T visitDefine(Define expr, Environment env);

        T visitLambda(Lambda expr, Environment env);

        T visitApplication(Application expr, Environment env);
    }
}
