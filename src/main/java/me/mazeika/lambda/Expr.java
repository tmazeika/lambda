package me.mazeika.lambda;

import java.util.List;

abstract class Expr {

  /*
  <Expr> ::= <id>                    -- Identifier
           | (define <id> <Expr>)    -- Define
           | (lambda (<id>+) <Expr>) -- Lambda
           | (<Expr> <Expr>+)               -- Application
  */

    static class Identifier extends Expr {
        Identifier(Token id) {
            this.id = id;
        }

        final Token id;
    }

    static class Define extends Expr {
        Define(Identifier id, Expr expr) {
            this.id = id;
            this.expr = expr;
        }

        final Identifier id;
        final Expr expr;
    }

    static class Lambda extends Expr {
        Lambda(List<Identifier> params, Expr body) {
            this.params = params;
            this.body = body;
        }

        final List<Identifier> params;
        final Expr body;
    }

    static class Application extends Expr {
        Application(List<Expr> args) {
            this.args = args;
        }

        final List<Expr> args;
    }
}
