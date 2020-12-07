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
        return env.get(expr);
    }

    @Override
    public Expr visitDefine(Expr.Define expr, Environment env) {
        env.define(expr.id, expr.body);
        return null;
    }

    @Override
    public Expr visitLambda(Expr.Lambda expr, Environment env) {
        return expr;
    }

    @Override
    public Expr visitApplication(Expr.Application expr, Environment env) {
        final Expr callee = expr.args.get(0).accept(this, env);
        if (!(callee instanceof Callable)) {
            return expr;
        }
        return ((Callable) callee).call(this,
                expr.args.subList(1, expr.args.size()), env);
    }

//  @Override
//  public Object visitIdentifierExpr(Expr.Identifier expr) {
//    return expr.id;
//  }
//
//  @Override
//  public Object visitDefineExpr(Expr.Define expr) {
//    // Now we must convert a literal syntax tree (an expression) into a
//    runtime value
//    Object value = evaluate(expr.expr);
//    environment.define(expr.id.id.getLexeme(), value);
//    return null;
//  }
//
//  @Override
//  public Object visitLambdaExpr(Expr.Lambda expr) {
//    Object body = this.evaluate(expr.body);
//
//    return null;
//  }
//
//  // This one I'm not certain about
//  @Override
//  public Object visitApplicationExpr(Expr.Application expr) {
//    List<Object> arguments = new ArrayList<>();
//
//    // Called function as first argument from application
//    Object callee = evaluate(expr.args.get(0));
//    if (!(callee instanceof Expr.Lambda)) {
//      // Throw runtime error--but don't have a token here
//
//    }
//    Expr.Lambda function = (Expr.Lambda)expr.args.get(0);
//
//    // If wrong number of args are given--should actually throw error
//    System.out.println("Bad number of args");
//    for (int i = 1; i < expr.args.size(); i++) {
//      arguments.add(evaluate(expr.args.get(i)));
//    }
//
//    return function.call(this, arguments);
//  }
}
