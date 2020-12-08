package me.mazeika.lambda;

final class ScanException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    ScanException(int line, char c) {
        super("Unknown character '" + c + "' at line " + line);
    }
}
