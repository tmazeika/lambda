package me.mazeika.lambda;

import java.util.List;

interface Callable {
  Object call(Evaluator eval, List<Object> arguments);
}
