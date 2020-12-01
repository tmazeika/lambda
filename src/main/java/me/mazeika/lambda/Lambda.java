package me.mazeika.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Lambda {
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            Lambda.repl();
        } else {
            Lambda.exec(args[0]);
        }
    }

    private static void repl() throws IOException {
        final var in = new InputStreamReader(System.in);
        final var reader = new BufferedReader(in);
        while (true) {
            System.out.print("> ");
            final String line = reader.readLine();
            if (line == null) {
                return;
            }
            try {
                List<Token> tokens = new Scanner(line).scanTokens();
                Expr expr = new Parser(tokens).parse();
                System.out.println(expr);
            } catch (ScanException | Parser.ParseError ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void exec(String filename) throws IOException {
        final var source = Files.readString(Path.of(filename));
        System.out.println(new Scanner(source).scanTokens());
    }

    private static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void error(Token token, String message) {
        if (token.getType() == Token.Type.EOF) {
            report(token.getLine(), " at end", message);
        } else {
            report(token.getLine(), " at '" + token.getLexeme() + "'", message);
        }
    }
}
