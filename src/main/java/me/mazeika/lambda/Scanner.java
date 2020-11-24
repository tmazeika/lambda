package me.mazeika.lambda;

import java.util.ArrayList;
import java.util.List;

final class Scanner {

    private final String source;

    private List<Token> tokens;
    private int start, current, line = 1;

    Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        if (this.tokens != null) {
            return List.copyOf(this.tokens);
        }
        this.tokens = new ArrayList<>();

        while (!this.isAtEnd()) {
            this.start = this.current;
            this.scanToken();
        }
        this.tokens.add(new Token(Token.Type.EOF, "", this.line));
        return List.copyOf(this.tokens);
    }

    private void scanToken() {
        final char c = this.advance();
        switch (c) {
            case '(':
                this.addToken(Token.Type.LEFT_PAREN);
                break;
            case ')':
                this.addToken(Token.Type.RIGHT_PAREN);
                break;
            case ';':
                while (this.peek() != '\n' && !this.isAtEnd()) {
                    this.advance();
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                this.line++;
                break;
            default:
                if (this.isId(c)) {
                    this.consumeIdentifier();
                } else {
                    throw new ScanException(this.line, c);
                }
        }
    }

    private void consumeIdentifier() {
        while (this.isId(this.peek())) {
            this.advance();
        }
        this.addToken(Token.Type.IDENTIFIER);
    }

    private boolean isId(char c) {
        return c != '(' && c != ')' && c != ';' && c != ' ' && c != '\r' &&
               c != '\t' && c != '\n' && c != '\0';
    }

    private char advance() {
        return this.source.charAt(this.current++);
    }

    private char peek() {
        if (this.isAtEnd()) {
            return '\0';
        }
        return this.source.charAt(this.current);
    }

    private void addToken(Token.Type type) {
        final String lexeme = this.source.substring(this.start, this.current);
        this.tokens.add(new Token(type, lexeme, line));
    }

    private boolean isAtEnd() {
        return this.current >= this.source.length();
    }
}
