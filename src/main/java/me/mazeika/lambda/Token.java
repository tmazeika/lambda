package me.mazeika.lambda;

import java.util.Objects;

public final class Token {

    private final Type type;
    private final String lexeme;
    private final int line;

    Token(Type type, String lexeme, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
    }

    public Type getType() {
        return this.type;
    }

    public String getLexeme() {
        return this.lexeme;
    }

    public int getLine() {
        return this.line;
    }

    @Override
    public String toString() {
        return this.type.toString() + ":" + this.lexeme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final var token = (Token) o;
        return this.type == token.type && this.lexeme.equals(token.lexeme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.lexeme);
    }

    public enum Type {
        EOF, IDENTIFIER, LEFT_PAREN, RIGHT_PAREN,
    }
}
