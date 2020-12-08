package me.mazeika.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

final class Parser {

    private final List<Token> tokens;

    private int current;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parse() {
        return expression();
    }

    private Expr expression() {
        final Expr expr;
        if (this.match(Token.Type.LEFT_PAREN)) {
            final String lexeme = this.peek().getLexeme();
            switch (lexeme) {
                case "define" -> {
                    this.advance();
                    expr = this.define();
                }
                case "lambda" -> {
                    this.advance();
                    expr = this.lambda();
                }
                default -> expr = this.application();
            }
            this.consume(Token.Type.RIGHT_PAREN,
                    "Expected ')' after expression.");
        } else {
            expr = this.identifier();
        }
        return expr;
    }

    private Expr.Identifier identifier() {
        return new Expr.Identifier(this.advance().getLexeme());
    }

    private Expr.Define define() {
        if (this.peek().getType() != Token.Type.IDENTIFIER) {
            throw this.error(this.peek(), "Expected identifier.");
        }
        return new Expr.Define(this.identifier().name, this.expression());
    }

    private Expr.Lambda lambda() {
        this.consume(Token.Type.LEFT_PAREN, "Expected '(' after lambda.");
        final List<String> params = new ArrayList<>();
        int count = 0;
        while (!this.isAtEnd() &&
               this.peek().getType() != Token.Type.RIGHT_PAREN) {
            count++;
            params.add(this.identifier().name);
        }
        if (count < 1) {
            throw this.error(this.peek(),
                    "Expected one or more parameters for lambda.");
        } else if (this.isAtEnd()) {
            throw this.eofError();
        }
        this.consume(Token.Type.RIGHT_PAREN, "Expected ')' after parameters.");
        return IntStream
                .iterate(params.size() - 2, i -> i >= 0, i -> i - 1)
                .boxed()
                .reduce(new Expr.Lambda(params.get(params.size() - 1),
                                this.expression()),
                        (inner, i) -> new Expr.Lambda(params.get(i), inner),
                        (a, b) -> a);
    }

    private Expr.Application application() {
        final List<Expr> exprs = new ArrayList<>();
        int count = 0;
        while (!this.isAtEnd() &&
               this.peek().getType() != Token.Type.RIGHT_PAREN) {
            count++;
            exprs.add(this.expression());
        }
        if (count < 2) {
            throw this.error(this.peek(),
                    "Expected two or more expressions for application.");
        } else if (this.isAtEnd()) {
            throw this.eofError();
        }
        return IntStream
                .range(2, exprs.size())
                .boxed()
                .reduce(new Expr.Application(exprs.get(0), exprs.get(1)),
                        (left, i) -> new Expr.Application(left, exprs.get(i)),
                        (a, b) -> a);
    }

    private void consume(Token.Type type, String message) {
        if (this.check(type)) {
            this.advance();
            return;
        }
        throw this.error(this.peek(), message);
    }

    private boolean match(Token.Type... types) {
        for (Token.Type type : types) {
            if (this.check(type)) {
                this.advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(Token.Type type) {
        if (this.isAtEnd()) {
            return false;
        }
        return this.peek().getType() == type;
    }

    private Token advance() {
        if (!this.isAtEnd()) {
            this.current++;
        }
        return this.previous();
    }

    private boolean isAtEnd() {
        return this.peek().getType() == Token.Type.EOF;
    }

    private Token peek() {
        return this.tokens.get(this.current);
    }

    private Token previous() {
        return this.tokens.get(this.current - 1);
    }

    private ParseException error(Token token, String message) {
        Lambda.error(token, message);
        return new ParseException();
    }

    private ParseException eofError() {
        return this.error(this.peek(), "Unexpected EOF.");
    }
}
