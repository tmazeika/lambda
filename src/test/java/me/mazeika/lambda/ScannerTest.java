package me.mazeika.lambda;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public final class ScannerTest {

    private static Stream<Arguments> scanTokensProvider() {
        // @formatter:off
        return Stream.of(
                args("", ""),
                args(";(", ""),
                args("~(`",
                        "~", "(", "`", ""),
                args("(hello (world)) ; hi there\r\n\n",
                        "(", "hello", "(", "world", ")", ")", ""),
                args("\n\rh\n  i there!! what's$ up? ));()\n(",
                        "h", "i", "there!!", "what's$", "up?", ")", ")", "(",
                        ""));
        // @formatter:on
    }

    private static Arguments args(String source, String... lexemes) {
        return Arguments.of(source,
                Arrays.stream(lexemes).map(lexeme -> switch (lexeme) {
                    case "(" -> new Token(Token.Type.LEFT_PAREN, lexeme, 0);
                    case ")" -> new Token(Token.Type.RIGHT_PAREN, lexeme, 0);
                    case "" -> new Token(Token.Type.EOF, lexeme, 0);
                    default -> new Token(Token.Type.IDENTIFIER, lexeme, 0);
                }).collect(Collectors.toUnmodifiableList()));
    }

    @ParameterizedTest
    @MethodSource("scanTokensProvider")
    public void testScanTokens(String source, List<Token> expected) {
        final var tokens = new Scanner(source).scanTokens();
        assertIterableEquals(expected, tokens);
    }
}
