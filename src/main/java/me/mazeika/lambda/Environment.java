package me.mazeika.lambda;

import java.util.HashMap;
import java.util.Map;

final class Environment {

    private final Environment parent;
    private final Map<Expr.Identifier, Expr> values = new HashMap<>();

    Environment(Environment parent) {
        this.parent = parent;
    }

    void define(Expr.Identifier id, Expr value) {
        this.values.put(id, value);
    }

    Expr get(Expr.Identifier id) {
        final Expr expr = this.values.get(id);
        if (expr == null) {
            if (this.parent == null) {
                return id;
            }
            return this.parent.get(id);
        }
        return expr;
    }

    @Override
    public String toString() {
        String str = "";
        if (this.parent != null) {
            str = this.parent.toString();
        }
        return str + this.values
                .entrySet()
                .stream()
                .reduce("", (s, entry) -> s + entry.getKey() + ": " +
                                          entry.getValue() + "\n",
                        (a, b) -> a + b);
    }
}
