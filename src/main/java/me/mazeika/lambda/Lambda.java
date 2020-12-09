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

        Environment<Expr> env = new Environment<>();

        for (String line : Files.readAllLines(
                Path.of(this.getClass().getResource("/stdlib.txt").toURI()))) {
            if (!line.isEmpty()) {
                env = new Definer().define(this.lineToExpr(line), env);
            }
        }

        while (true) {
            System.out.print("> ");
            final String line = reader.readLine();
            if (line == null) {
                return;
            }
            Expr expr;
            try {
                expr = this.lineToExpr(line);
            } catch (ScanException | ParseException ex) {
                ex.printStackTrace();
                continue;
            }
            env = new Definer().define(expr, env);
            try {
                System.out.println(expr);
                expr = new BReduce().betaReduce(expr, env);
                final Val val = new Evaluator().evaluate(expr, null);
                if (val != null) {
                    System.out.println("==>");
                    System.out.println(val);
                    System.out.println("==>");
                    System.out.println(val.accept(new ToBool()));
                }
            } catch (EvalException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Expr lineToExpr(String line) {
        final List<Token> tokens = new Scanner(line).scanTokens();
        return new Parser(tokens).parse();
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
