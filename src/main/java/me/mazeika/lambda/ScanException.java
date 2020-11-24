package me.mazeika.lambda;

final class ScanException extends RuntimeException {

    ScanException(int line, char c) {
        super("Unknown character '" + c + "' at line " + line);
    }
}
