package me.mazeika.lambda;

final class InnerToNat implements Expr.Visitor<Val.Int, Val> {

    private final String f;
    private final String x;

    InnerToNat(String f, String x) {
        this.f = f;
        this.x = x;
    }

    @Override
    public Val.Int visitIdentifier(Expr.Identifier expr, Environment<Val> env) {
        if (!expr.name.equals(this.x)) {
            throw new EvalException("Not a church-numeral.");
        }
        return new Val.Int(0);
    }

    @Override
    public Val.Int visitDefine(Expr.Define expr, Environment<Val> env) {
        throw new EvalException("Expected identifier or application.");
    }

    @Override
    public Val.Int visitLambda(Expr.Lambda expr, Environment<Val> env) {
        throw new EvalException("Expected identifier or application.");
    }

    @Override
    public Val.Int visitApplication(Expr.Application expr, Environment<Val> env) {
        if (!expr.callee.accept(new ForceIdentifierExpr(), null).name.equals(
                this.f)) {
            throw new EvalException("Not a church-numeral.");
        }
        return new Val.Int(1 + expr.arg.accept(this, env).val);
    }
}
