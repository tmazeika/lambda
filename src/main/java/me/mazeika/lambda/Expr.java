package me.mazeika.lambda;

import java.util.List;

abstract class Expr {

    public abstract Object accept(Expr.Visitor visitor);

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

        @Override
        public Object accept(Visitor visitor) {
            return visitor.visitIdentifierExpr(this);
        }
    }

    static class Define extends Expr {
        Define(Identifier id, Expr expr) {
            this.id = id;
            this.expr = expr;
        }

        final Identifier id;
        final Expr expr;

        @Override
        public Object accept(Visitor visitor) {
            return visitor.visitDefineExpr(this);
        }
    }

    static class Lambda extends Expr implements Callable {
        Lambda(List<Identifier> params, Expr body) {
            this.params = params;
            this.body = body;
        }

        final List<Identifier> params;
        final Expr body;

        @Override
        public Object accept(Visitor visitor) {
            return visitor.visitLambdaExpr(this);
        }

      @Override
      public Object call(Evaluator eval, List<Object> arguments) {
        return null;
      }
    }

    static class Application extends Expr {
        Application(List<Expr> args) {
            this.args = args;
        }

        final List<Expr> args;

        @Override
        public Object accept(Visitor visitor) {
            return visitor.visitApplicationExpr(this);
        }
    }

    // Represents an object that can visit Expr objects
    interface Visitor<T> {
        Object visitIdentifierExpr(Identifier expr);
        Object visitDefineExpr(Define expr);
        Object visitLambdaExpr(Lambda expr);
        Object visitApplicationExpr(Application expr);
    }
}
