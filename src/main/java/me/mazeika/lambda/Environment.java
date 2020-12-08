package me.mazeika.lambda;

final class Environment {

    private String id;
    private Val val;
    private Environment rest;

    Environment() {
        //
    }

    Environment(String id, Val val, Environment rest) {
        this.rest = rest;
        this.id = id;
        this.val = val;
    }

    Environment define(String id, Val val) {
        return new Environment(id, val, this);
    }

    Val lookup(String id) {
        if (this.id == null) {
            throw new EvalException(
                    "Cannot reference an identifier before its definition.");
        }
        if (id.equals(this.id)) {
            return this.val;
        }
        return this.rest.lookup(id);
    }

    @Override
    public String toString() {
        if (this.id == null) {
            return "";
        }
        return this.id + " := " + this.val + "\n" + this.rest.toString();
    }
}
