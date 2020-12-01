package me.mazeika.lambda;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parse() {
        return expression();
    }

    private Expr expression() {
        Expr expr;
        if (this.match(Token.Type.LEFT_PAREN)) {
            String lexeme = this.peek().getLexeme();
            if (lexeme.equals("define")) {
                this.advance();
                expr = define();
            } else if (lexeme.equals("lambda")) {
                this.advance();
                expr = this.lambda();
            } else {
                expr = this.application();
            }
            this.consume(Token.Type.RIGHT_PAREN, "Expect ')' after expression");
        } else {
            expr = this.identifier();
        }
        return expr;
    }

    private Expr.Identifier identifier() {
        return new Expr.Identifier(this.advance());
    }

    private Expr define() {
        if (this.peek().getType() != Token.Type.IDENTIFIER) {
            throw this.error(this.peek(), "Expected identifier");
        }
        return new Expr.Define(this.identifier(), this.expression());
    }

    private Expr lambda() {
        this.consume(Token.Type.LEFT_PAREN, "Expect '(' after lambda");
        List<Expr.Identifier> params = new ArrayList<Expr.Identifier>();
        int count = 0;
        while (!this.isAtEnd() &&
               this.peek().getType() != Token.Type.RIGHT_PAREN) {
            count++;
            params.add(identifier());
        }
        if (count < 1) {
            throw this.error(this.peek(),
                    "Expect one or more parameters for lambda");
        } else if (this.isAtEnd()) {
            throw this.eofError();
        }
        this.consume(Token.Type.RIGHT_PAREN, "Expect ')' after params");
        return new Expr.Lambda(params, expression());
    }

    private Expr application() {
        List<Expr> exprs = new ArrayList<Expr>();
        int count = 0;
        while (!this.isAtEnd() &&
               this.peek().getType() != Token.Type.RIGHT_PAREN) {
            count++;
            exprs.add(expression());
        }
        if (count < 2) {
            throw this.error(this.peek(),
                    "Expect two or more expressions for application");
        } else if (this.isAtEnd()) {
            throw this.eofError();
        }
        return new Expr.Application(exprs);
    }

    private Token consume(Token.Type type, String message) {
        if (check(type)) {
            return advance();
        }

        throw error(peek(), message);
    }

    private boolean match(Token.Type... types) {
        for (Token.Type type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(Token.Type type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == Token.Type.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError eofError() {
        return this.error(this.peek(), "Unexpected eof");
    }

    private ParseError error(Token token, String message) {
        Lambda.error(token, message);
        return new ParseError();
    }
}
