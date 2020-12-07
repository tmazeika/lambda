package me.mazeika.lambda;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a body capable of evaluating abstract syntax trees and identifiers
 */
public class Evaluator implements Expr.Visitor<Object> {
  private Environment environment = new Environment();


  void interpret(Expr expression) {
    try {
      Object value = evaluate(expression);
      System.out.println(stringify(value));
    } catch (RuntimeError e) {
      Lambda.runtimeError(e);
    }
  }

  protected Object evaluate(Expr expr) {
    return expr.accept(this);
  }

  private String stringify(Object object) {
    return object.toString();
  }

  @Override
  public Object visitIdentifierExpr(Expr.Identifier expr) {
    return expr.id;
  }

  @Override
  public Object visitDefineExpr(Expr.Define expr) {
    // Now we must convert a literal syntax tree (an expression) into a runtime value
    Object value = evaluate(expr.expr);
    environment.define(expr.id.id.getLexeme(), value);
    return null;
  }

  @Override
  public Object visitLambdaExpr(Expr.Lambda expr) {
    Object body = this.evaluate(expr.body);

    return null;
  }

  // This one I'm not certain about
  @Override
  public Object visitApplicationExpr(Expr.Application expr) {
    List<Object> arguments = new ArrayList<>();

    // Called function as first argument from application
    Object callee = evaluate(expr.args.get(0));
    if (!(callee instanceof Expr.Lambda)) {
      // Throw runtime error--but don't have a token here

    }
    Expr.Lambda function = (Expr.Lambda)expr.args.get(0);

    if (arguments.size() - 1 != function.params.size()) {
      // If wrong number of args are given--should actually throw error
      System.out.println("Bad number of args");
    }
    for (int i = 1; i < expr.args.size(); i++) {
      arguments.add(evaluate(expr.args.get(i)));
    }

    return function.call(this, arguments);
  }
}
