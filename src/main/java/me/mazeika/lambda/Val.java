package me.mazeika.lambda;

abstract class Val {

    abstract <R> R accept(Val.Visitor<R> visitor);

    static class Lambda extends Val {

        final String param;
        final Expr body;
        final Environment env;

        Lambda(String param, Expr body, Environment env) {
            this.param = param;
            this.body = body;
            this.env = env;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLambda(this);
        }

        @Override
        public String toString() {
            return String.format("(λ (%s) %s)", this.param, this.body);
        }
    }

    static class Str extends Val {

        final String val;

        Str(String val) {
            this.val = val;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitStr(this);
        }

        @Override
        public String toString() {
            return this.val;
        }
    }

    static class Int extends Val {

        final int val;

        Int(int val) {
            this.val = val;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitInt(this);
        }

        @Override
        public String toString() {
            return String.valueOf(this.val);
        }
    }

    interface Visitor<R> {

        R visitLambda(Lambda lambda);

        R visitStr(Str s);

        R visitInt(Int i);
    }
}
