package me.mazeika.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public final class Lambda {

    public static void main(String[] args) throws IOException {
        new Lambda().startRepl();
    }

    private void startRepl() throws IOException {
        final Reader in = new InputStreamReader(System.in);
        final BufferedReader reader = new BufferedReader(in);
        final Environment env = new Environment(null);

        while (true) {
            System.out.print("> ");
            final String line = reader.readLine();
            if (line == null) {
                return;
            }
            try {
                final List<Token> tokens = new Scanner(line).scanTokens();
                final Expr expr = new Parser(tokens).parse();
                System.out.println(new Evaluator().evaluate(expr, env));
            } catch (ScanException | ParseException ex) {
                ex.printStackTrace();
            }
        }
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
