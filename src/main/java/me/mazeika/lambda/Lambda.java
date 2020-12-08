package me.mazeika.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Lambda {

    public static void main(String[] args)
            throws IOException, URISyntaxException {
        new Lambda().startRepl();
    }

    private void startRepl() throws IOException, URISyntaxException {
        final Reader in = new InputStreamReader(System.in);
        final BufferedReader reader = new BufferedReader(in);
        final Environment env = new Environment(null);

        Files
                .readAllLines(Path.of(this
                        .getClass()
                        .getResource("/stdlib.txt")
                        .toURI()))
                .stream()
                .filter(s -> !s.isEmpty())
                .forEach(s -> this.lineToExpr(s, env));

        while (true) {
            System.out.print("> ");
            final String line = reader.readLine();
            if (line == null) {
                return;
            }
            try {
                System.out.println(this.lineToExpr(line, env).toString());
            } catch (ScanException | ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Expr lineToExpr(String line, Environment env) {
        final List<Token> tokens = new Scanner(line).scanTokens();
        final Expr expr = new Parser(tokens).parse();
        return new Evaluator().evaluate(expr, env);
    }

    static void error(Token token, String message) {
        if (token.getType() == Token.Type.EOF) {
            report(token.getLine(), "at end", message);
        } else {
            report(token.getLine(), "at '" + token.getLexeme() + "'", message);
        }
    }

    private static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error " + where + ": " + message);
    }
}
