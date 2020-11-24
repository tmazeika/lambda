package me.mazeika.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Lambda {

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
                System.out.println(new Scanner(line).scanTokens());
            } catch (ScanException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void exec(String filename) throws IOException {
        final var source = Files.readString(Path.of(filename));
        System.out.println(new Scanner(source).scanTokens());
    }
}
