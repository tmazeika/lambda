package me.mazeika.lambda;

import java.util.List;

interface Callable {
    Expr call(Evaluator evaluator, List<Expr> arguments, Environment env);
}
