package me.mazeika.lambda;

import java.util.Objects;

abstract class Expr {

    abstract <R, E> R accept(Expr.Visitor<R, E> visitor, Environment<E> env);

    /*
    <EXPR> ::= <id>                                  -- Identifier
             | (define <id> <EXPR>)                  -- Define
             | (lambda (<id> ...) <EXPR> <EXPR> ...) -- Lambda
             | (<EXPR> <EXPR> <EXPR> ...)            -- Application
    */

    static class Identifier extends Expr {

        final String name;

        Identifier(String name) {
            this.name = name;
        }

        @Override
        public <R, E> R accept(Visitor<R, E> visitor, Environment<E> env) {
            return visitor.visitIdentifier(this, env);
        }

        @Override
        public String toString() {
            return this.name;
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
            return this.name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name);
        }
    }

    static class Define extends Expr {

        final String id;
        final Expr body;

        Define(String id, Expr expr) {
            this.id = id;
            this.body = expr;
        }

        @Override
        public <R, E> R accept(Visitor<R, E> visitor, Environment<E> env) {
            return visitor.visitDefine(this, env);
        }

        @Override
        public String toString() {
            return String.format("%s := %s", this.id, this.body);
        }
    }

    static class Lambda extends Expr {

        final String param;
        final Expr body;

        Lambda(String param, Expr body) {
            this.param = param;
            this.body = body;
        }

        @Override
        public <R, E> R accept(Visitor<R, E> visitor, Environment<E> env) {
            return visitor.visitLambda(this, env);
        }

        @Override
        public String toString() {
            return String.format("(λ (%s) %s)", this.param, this.body);
        }
    }

    static class Application extends Expr {

        final Expr callee;
        final Expr arg;

        Application(Expr callee, Expr arg) {
            this.callee = callee;
            this.arg = arg;
        }

        @Override
        public <R, E> R accept(Visitor<R, E> visitor, Environment<E> env) {
            return visitor.visitApplication(this, env);
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", this.callee, this.arg);
        }
    }

    interface Visitor<T, E> {
        T visitIdentifier(Identifier expr, Environment<E> env);

        T visitDefine(Define expr, Environment<E> env);

        T visitLambda(Lambda expr, Environment<E> env);

        T visitApplication(Application expr, Environment<E> env);
    }
}
