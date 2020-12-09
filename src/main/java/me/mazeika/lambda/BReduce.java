package me.mazeika.lambda;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Represents a body capable of evaluating abstract syntax trees and identifiers
 */
public class BReduce implements Expr.Visitor<Expr, Expr> {

    Expr betaReduce(Expr expr, Environment<Expr> env) {
        while (env.id != null) {
            expr = this.subst(env.id, env.val, expr);
            env = env.rest;
        }
        Expr oldExpr;
        do {
            System.out.println(expr.toString());
            oldExpr = expr;
            expr = expr.accept(this, null);
        } while (expr != null);
        return oldExpr;
    }

    @Override
    public Expr visitIdentifier(Expr.Identifier expr, Environment<Expr> env) {
        return null;
    }

    @Override
    public Expr visitDefine(Expr.Define expr, Environment<Expr> env) {
        return null;
    }

    @Override
    public Expr visitLambda(Expr.Lambda expr, Environment<Expr> env) {
        Expr body = expr.body.accept(this, null);
        if (body == null) {
            return null;
        } else {
            System.out.println(body.toString());
            return new Expr.Lambda(expr.param, body);
        }
    }

    @Override
    public Expr visitApplication(Expr.Application expr, Environment<Expr> env) {
        if (expr.callee.accept(new IsLambdaExpr(), null)) {
            return this.reduce(expr);
        } else {
            Expr appCallee = expr.callee.accept(this, null);
            if (appCallee == null) {
                Expr appArg = expr.arg.accept(this, null);
                if (appArg == null) {
                    return null;
                }
                return new Expr.Application(expr.callee, appArg);
            }
            return new Expr.Application(appCallee, expr.arg);
        }
    }

    private Expr reduce(Expr.Application expr) {
        Expr.Lambda lam = expr.callee.accept(new ForceLambdaExpr(), null);
        return this.subst(lam.param, expr.arg, lam.body);
    }

    private Expr subst(String param, Expr arg, Expr body) {
        if (body.accept(new IsIdentifierExpr(), null)) {
            Expr.Identifier iden = body.accept(new ForceIdentifierExpr(), null);
            String v = iden.name;
            return param.equals(v) ? arg : body;
        } else if (body.accept(new IsLambdaExpr(), null)) {
            Expr.Lambda lam = body.accept(new ForceLambdaExpr(), null);
            String v = lam.param;
            if (param.equals(v)) {
                return body;
            } else {
                ArrayList<Expr> exprList = new ArrayList<>(List.of(
                    new Expr.Identifier(param),
                    arg,
                    lam.body
                ));
                String newParam = this.makeNewParam(lam.param, exprList);
                Expr s1 = this.subst(lam.param, new Expr.Identifier(newParam), lam.body);
                Expr s2 = this.subst(param, arg, s1);
                if (s1 == null || s2 == null) {
                    return null;
                }
                return new Expr.Lambda(newParam, s2);
            }
        } else if (body.accept(new IsApplicationExpr(), null)) {
            Expr.Application app = body.accept(new ForceApplicationExpr(), null);
            Expr appCallee = this.subst(param, arg, app.callee);
            Expr appArg = this.subst(param, arg, app.arg);
            if (appCallee == null || appArg == null) {
                return null;
            }
            return new Expr.Application(appCallee, appArg);
        }
        return null;
    }

    private String makeNewParam(String param, List<Expr> exprList) {
        HashSet<String> freeVars = new HashSet<String>();
        for (Expr expr : exprList) {
            freeVars.addAll(findFreeVars(expr));
        }
        if (!freeVars.contains(param)) {
            return param;
        } else {
            String newParam = param;
            while (newParam.equals(param) && !freeVars.contains(newParam)) {
                newParam += '\'';
            }
            return newParam;
        }
    }

    private HashSet<String> findFreeVars(Expr expr) {
        HashSet<String> freeVars = new HashSet<String>();
        if (expr.accept(new IsIdentifierExpr(), null)) {
            Expr.Identifier iden = expr.accept(new ForceIdentifierExpr(), null);
            freeVars.add(iden.name);
        } else if (expr.accept(new IsLambdaExpr(), null)) {
            Expr.Lambda lam = expr.accept(new ForceLambdaExpr(), null);
            freeVars.addAll(findFreeVars(lam.body));
            freeVars.remove(lam.param);
        } else if (expr.accept(new IsApplicationExpr(), null)) {
            Expr.Application app = expr.accept(new ForceApplicationExpr(), null);
            freeVars.addAll(findFreeVars(app.callee));
            freeVars.addAll(findFreeVars(app.arg));
        }
        return freeVars;
    }
}
