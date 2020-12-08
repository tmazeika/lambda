package me.mazeika.lambda;

final class Environment<T> {

    public String id;
    public T val;
    public Environment<T> rest;

    Environment() {
        //
    }

    Environment(String id, T val, Environment<T> rest) {
        this.rest = rest;
        this.id = id;
        this.val = val;
    }

    Environment<T> define(String id, T val) {
        return new Environment<T>(id, val, this);
    }

    T lookup(String id) {
        if (this.id == null) {
            return null;
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
