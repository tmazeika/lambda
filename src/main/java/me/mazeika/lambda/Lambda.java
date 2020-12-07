package me.mazeika.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class Lambda {
    private static final Evaluator eval = new Evaluator();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            Lambda.repl();
        } else {
            Lambda.exec(args[0]);
        }
    }

    private static void repl() throws IOException {
        final Reader in = new InputStreamReader(System.in);
        final BufferedReader reader = new BufferedReader(in);
        while (true) {
            System.out.print("> ");
            final String line = reader.readLine();
            if (line == null) {
                return;
            }
            try {
                List<Token> tokens = new Scanner(line).scanTokens();
                Expr expr = new Parser(tokens).parse();
                eval.interpret(expr);
            } catch (ScanException | Parser.ParseError ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void exec(String filename) throws IOException {
        final String source = Files.readString(Paths.get(filename));
        List<Token> tokens = new Scanner(source).scanTokens();
        Expr expr = new Parser(tokens).parse();
        eval.interpret(expr);

        if (hadRuntimeError) System.exit(70);
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

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.getLine() + "]");
        hadRuntimeError = true;
    }
}
